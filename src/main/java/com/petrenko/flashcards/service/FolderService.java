package com.petrenko.flashcards.service;

import com.petrenko.flashcards.dto.*;
import com.petrenko.flashcards.model.Folder;
import com.petrenko.flashcards.repository.FolderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FolderService {
    private final static Logger LOGGER = LoggerFactory.getLogger(FolderService.class);
    private final FolderRepository folderRepository;
    private final SetOfCardsService setOfCardsService;
    private final CardService cardService;
    private final PersonService personService;

    @Autowired
    public FolderService(final FolderRepository folderRepository,
                         final SetOfCardsService setOfCardsService,
                         final CardService cardService,
                         final PersonService personService) {
        this.folderRepository = folderRepository;
        this.setOfCardsService = setOfCardsService;
        this.cardService = cardService;
        this.personService = personService;
    }

    public List<FolderIdNameDto> getFoldersIdNameDtoByPersonId(final String userId) {
        final List<FolderIdNameDto> folderIdNameDto = folderRepository.getFoldersIdNameDtoByPersonId(userId);
        LOGGER.info("folderNameIdDto '{}' for userId '{}'", folderIdNameDto, userId);
        return folderIdNameDto;
    }

    @Transactional
    public Folder saveFolderCreateDtoToFolder(final String userId, final FolderCreateDto folderCreateDto) {
        LOGGER.trace("invoked for userId '{}' and folderCreateDto '{}'", userId, folderCreateDto);

        final Folder folder = getFolderWithNameOrNew(userId, folderCreateDto.getName());
        LOGGER.info("getFolderWithNameOrNew {}", folder);

        folder.setDescription(folderCreateDto.getDescription());

        final Folder savedFolder = folderRepository.save(folder);
        LOGGER.info("savedFolder {}", savedFolder);

        return savedFolder;
    }


    public Folder getFolderWithNameOrNew(final String userId, final String folderName) {
        LOGGER.trace("invoked for userId '{}' and folderName '{}'", userId, folderName);
        final Folder folder = folderRepository.findByUserIdAndName(userId, folderName)
                .orElseGet(() -> {
                    Folder newFolder = new Folder();
                    newFolder.setName(folderName);
                    newFolder.setPerson(personService.getById(userId));
                    return folderRepository.save(newFolder);
                });
        LOGGER.info("finalFolder {}", folder);
        return folder;
    }

    @Transactional
    public FolderByIdDto getFolderByIdDto(final String userId, final String folderId) {
        LOGGER.trace("invoked for userId '{}' and folderId '{}'", userId, folderId);

        final FolderIdNameDescriptionDto folderIdNameDescriptionDto = folderRepository
                .getFolderIdNameDescriptionDto(folderId).orElseThrow(() ->
                        new IllegalArgumentException("Folder not found for ID: " + folderId));

        final FolderByIdDto folderByIdDto = new FolderByIdDto();

        folderByIdDto.setId(folderIdNameDescriptionDto.getId());
        folderByIdDto.setName(folderIdNameDescriptionDto.getName());
        folderByIdDto.setDescription(folderIdNameDescriptionDto.getDescription());

        folderByIdDto.setPreviousOrLastFolderId(getPreviousOrLastFolderId(userId, folderId));
        folderByIdDto.setNextOrFirstFolderId(getNextOrFirstFolderId(userId, folderId));

        LOGGER.info("folderByIdDto {}", folderByIdDto);
        return folderByIdDto;
    }

    private String getPreviousOrLastFolderId(final String userId, final String folderId) {
        LOGGER.trace("invoked for userId '{}' and folderId '{}'", userId, folderId);
        final String previousOrLastFolderId = folderRepository.getPreviousId(userId, folderId)
                .orElse(folderRepository.getLastId(userId)
                        .orElseThrow(() -> new IllegalArgumentException("Unable to find any previous or last folder " +
                                "for ID: '" + folderId + "' in the user '" + userId + "'")));
        LOGGER.info("nextOrFirstFolderId {}", previousOrLastFolderId);
        return previousOrLastFolderId;
    }

    private String getNextOrFirstFolderId(final String userId, final String folderId) {
        LOGGER.trace("invoked for userId '{}' and folderId '{}'", userId, folderId);
        final String nextOrFirstFolderId = folderRepository.getNextId(userId, folderId)
                .orElse(folderRepository.getFirstId(userId)
                        .orElseThrow(() -> new IllegalArgumentException("Unable to find any next or first folder " +
                                "for ID: '" + folderId + "' in the user '" + userId + "'")));
        LOGGER.info("nextOrFirstFolderId {}", nextOrFirstFolderId);
        return nextOrFirstFolderId;
    }

    public FolderIdNameDescriptionDto getFolderIdNameDescriptionDto(final String folderId) {
        LOGGER.trace("invoked for folderId '{}'", folderId);
        final FolderIdNameDescriptionDto folderIdNameDescriptionDto = folderRepository
                .getFolderIdNameDescriptionDto(folderId)
                .orElseThrow(() -> new IllegalArgumentException("Folder information not found. Folder ID: " + folderId));
        LOGGER.info("folderIdNameDescriptionDto {}", folderIdNameDescriptionDto);
        return folderIdNameDescriptionDto;
    }

    @Transactional
    public Folder updateFolderByFolderIdNameDescriptionDto(final String userId,
                                                           final FolderIdNameDescriptionDto folderIdNameDescriptionDto) {
        LOGGER.trace("invoked for userId '{}' and folderIdNameDescriptionDto '{}'", userId, folderIdNameDescriptionDto);

        final String newName = folderIdNameDescriptionDto.getName();

        final Optional<String> idByUserIdAndName = folderRepository.findIdByUserIdAndName(userId, newName);
        if (idByUserIdAndName.isPresent() && !idByUserIdAndName.get().equals(folderIdNameDescriptionDto.getId())) {
            throw new IllegalArgumentException("Folder with this newName already exist: " + newName);
        }

        final String newDescription = folderIdNameDescriptionDto.getDescription();

        folderRepository.update(userId, newName, newDescription);

        final Folder updatedFolder = folderRepository.findById(folderIdNameDescriptionDto.getId())
                .orElseThrow(IllegalArgumentException::new);

        LOGGER.info("updatedFolder {}", updatedFolder);
        return updatedFolder;
    }

    @Transactional
    public void deleteAllByFolderId(final String folderId) {
        LOGGER.trace("invoked for folderId '{}'", folderId);
        final List<String> setsId = folderRepository.getSetsIdByFolderId(folderId);
        setsId.forEach(s -> {
            cardService.deleteBySetId(s);
            setOfCardsService.deleteById(s);
        });
        folderRepository.deleteById(folderId);
        LOGGER.info("deleted folder with folderId '{}'", folderId);
    }

    public String getNameById(final String folderId) {
        final String folderName = folderRepository.findNameById(folderId).orElseThrow(() ->
                new IllegalArgumentException("Folder name not found. Folder ID: " + folderId));
        LOGGER.info("folderName '{}' for folderId '{}'", folderName, folderId);
        return folderName;
    }

}
