DROP TABLE IF EXISTS DrugsInfo;
DROP TABLE IF EXISTS Recipient;

CREATE TABLE DrugsInfo
(
    id              INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name            varchar(255) NOT NULL,
    expiration_date date         NOT NULL,
    form            varchar(255),
    category        varchar(255),
    md5             varchar(32)  NOT NULL
);


CREATE TABLE Recipient
(
    id           INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    first_name   varchar(255) NOT NULL,
    last_name    varchar(255) NOT NULL,
    phone_number varchar(32)  NOT NULL
);