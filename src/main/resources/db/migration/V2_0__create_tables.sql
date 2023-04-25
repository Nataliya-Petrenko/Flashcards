CREATE TABLE IF NOT EXISTS person (
    id VARCHAR PRIMARY KEY NOT NULL,
    password VARCHAR,
    email VARCHAR,
    first_name VARCHAR,
    last_name VARCHAR,
    enable BOOLEAN,
    role VARCHAR
);

CREATE TABLE IF NOT EXISTS folder (
    id VARCHAR PRIMARY KEY NOT NULL,
    name TEXT,
    description TEXT,
    time_of_creation TIMESTAMP,
    person_id VARCHAR REFERENCES person(id)
);

CREATE TABLE IF NOT EXISTS set_of_cards (
    id VARCHAR PRIMARY KEY NOT NULL,
    name TEXT,
    description TEXT,
    time_of_creation TIMESTAMP,
    folder_id VARCHAR REFERENCES folder(id)
);

CREATE TABLE IF NOT EXISTS card (
    id VARCHAR PRIMARY KEY NOT NULL,
    question TEXT,
    short_answer TEXT,
    long_answer TEXT,
    time_of_creation TIMESTAMP,
    set_of_cards_id VARCHAR REFERENCES set_of_cards(id)
);