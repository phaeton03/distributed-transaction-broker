--liquibase formatted sql

--changeset nikolskiy:2022-02-02--0010-create-tables
DROP TABLE IF EXISTS CLIENT CASCADE;
--changeset nikolskiy:2022-02-02--0011-create-tables
CREATE TABLE CLIENT
(
    id   bigint GENERATED BY DEFAULT AS IDENTITY,
    name varchar(255),
    city varchar(255),
    PRIMARY KEY (id)
);
--changeset nikolskiy:2022-02-02--0012-create-tables
DROP TABLE IF EXISTS ACCOUNT CASCADE;
--changeset nikolskiy:2022-02-02--0013-create-tables
CREATE TABLE ACCOUNT
(
    id   bigint GENERATED BY DEFAULT AS IDENTITY,
    type varchar(255),
    amount decimal DEFAULT '0.0',
    currency varchar(255) DEFAULT 'RUB',
    client_id bigint,
    is_block boolean DEFAULT false,
    PRIMARY KEY (id),
    FOREIGN KEY (client_id) REFERENCES CLIENT (id)
);
--changeset nikolskiy:2022-02-02--0014-create-tables
DROP TABLE IF EXISTS TRANSACTIONS CASCADE;
--changeset nikolskiy:2022-02-02--0015-create-tables
CREATE TABLE TRANSACTIONS
(
    id   bigint GENERATED BY DEFAULT AS IDENTITY,
    tracking_number uuid,
    type varchar(255),
    is_distributed boolean,
    status varchar(255),
    amount decimal,
    create_dt timestamp,
    currency varchar(255) DEFAULT 'RUB',
    client_id bigint,
    account_id bigint,
    third_party_account_id bigint,
    PRIMARY KEY (id),
    FOREIGN KEY (client_id) REFERENCES CLIENT (id),
    FOREIGN KEY (account_id) REFERENCES ACCOUNT (id)
);