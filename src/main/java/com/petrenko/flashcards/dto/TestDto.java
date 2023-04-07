package com.petrenko.flashcards.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestDto {
    public TestDto(String id) {
        this.id = id;
    }

    String id;
}
