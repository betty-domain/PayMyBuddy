DROP SCHEMA if exists `paymybuddy_test`;

CREATE SCHEMA if not exists `paymybuddy_test`;

use `paymybuddy_test`;
CREATE TABLE user (
                      user_id INT AUTO_INCREMENT NOT NULL,
                      email VARCHAR(100) NOT NULL,
                      firstname VARCHAR(30) NOT NULL,
                      lastname VARCHAR(60) NOT NULL,
                      password VARCHAR(80) NOT NULL,
                      balance DECIMAL(12,2) DEFAULT 0 NOT NULL,
                      PRIMARY KEY (user_id)
);


CREATE UNIQUE INDEX user_email_idx
 ON user
 ( email );

CREATE TABLE bank_account (
                              bank_account_id INT AUTO_INCREMENT NOT NULL,
                              iban VARCHAR(34) NOT NULL,
                              description VARCHAR(50) NOT NULL,
                              is_actif BOOLEAN NOT NULL,
                              user_id INT NOT NULL,
                              PRIMARY KEY (bank_account_id)
);


CREATE TABLE bank_transfer (
                               bank_transfer_id INT AUTO_INCREMENT NOT NULL,
                               amount DECIMAL(12,2) NOT NULL,
                               date DATE NOT NULL,
                               transfer_order INT NOT NULL,
                               user_id INT NOT NULL,
                               bank_account_id INT NOT NULL,
                               PRIMARY KEY (bank_transfer_id)
);


CREATE TABLE user_friends (
                              user_friend_id INT NOT NULL,
                              user_id INT NOT NULL,
                              is_actif BOOLEAN NOT NULL,
                              PRIMARY KEY (user_friend_id, user_id)
);


CREATE TABLE transaction (
                             transaction_id INT AUTO_INCREMENT NOT NULL,
                             description VARCHAR(200) NOT NULL,
                             amount DECIMAL(12,2) NOT NULL,
                             date DATETIME NOT NULL,
                             beneficiary_user_id INT NOT NULL,
                             payer_user_id INT NOT NULL,
                             PRIMARY KEY (transaction_id)
);


CREATE TABLE fee (
                     fee_id INT AUTO_INCREMENT NOT NULL,
                     amount DECIMAL(12,2) NOT NULL,
                     percentage100 DECIMAL(4,2) NOT NULL,
                     date DATE NOT NULL,
                     user_id INT NOT NULL,
                     transaction_id INT NOT NULL,
                     PRIMARY KEY (fee_id)
);


ALTER TABLE transaction ADD CONSTRAINT user_transaction_fk
    FOREIGN KEY (beneficiary_user_id)
        REFERENCES user (user_id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION;

ALTER TABLE transaction ADD CONSTRAINT user_transaction_fk1
    FOREIGN KEY (payer_user_id)
        REFERENCES user (user_id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION;

ALTER TABLE user_friends ADD CONSTRAINT user_user_amis_fk
    FOREIGN KEY (user_id)
        REFERENCES user (user_id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION;

ALTER TABLE user_friends ADD CONSTRAINT user_user_amis_fk1
    FOREIGN KEY (user_friend_id)
        REFERENCES user (user_id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION;

ALTER TABLE bank_account ADD CONSTRAINT user_bank_account_fk
    FOREIGN KEY (user_id)
        REFERENCES user (user_id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION;

ALTER TABLE bank_transfer ADD CONSTRAINT user_bank_transfer_fk
    FOREIGN KEY (user_id)
        REFERENCES user (user_id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION;

ALTER TABLE fee ADD CONSTRAINT user_fee_fk
    FOREIGN KEY (user_id)
        REFERENCES user (user_id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION;

ALTER TABLE bank_transfer ADD CONSTRAINT bank_account_bank_transfer_fk
    FOREIGN KEY (bank_account_id)
        REFERENCES bank_account (bank_account_id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION;

ALTER TABLE fee ADD CONSTRAINT transaction_fee_fk
    FOREIGN KEY (transaction_id)
        REFERENCES transaction (transaction_id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION;
