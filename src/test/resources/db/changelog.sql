CREATE TABLE payment
(
    id      uuid,
    amount  VARCHAR(12) NULL,
    data_id uuid        NULL,
    CONSTRAINT PK_PAYMENT PRIMARY KEY (id)
);

CREATE TABLE data
(
    id          uuid         NOT NULL,
    information VARCHAR(255) NOT NULL,
    CONSTRAINT PK_DATA PRIMARY KEY (id)
);

ALTER TABLE payment
    ADD CONSTRAINT fk_data FOREIGN KEY (data_id) REFERENCES data (id) ON UPDATE RESTRICT ON DELETE CASCADE;