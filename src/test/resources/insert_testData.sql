use `paymybuddy_test`;

INSERT INTO `user`(`email`,`firstname`,`lastname`,`password`,`balance`)
VALUES ('updateUser@free.fr','firstname','latname','$2a$12$yc2A75xOtp72Yl8sWplcReJs8Ku0zg6Im7U7W35y5eo5xagomBAOa','25');

INSERT INTO `user`(`email`,`firstname`,`lastname`,`password`,`balance`)
VALUES ('harry.potter@gmail.com','Harry','Potter','$2a$12$NlA3EC9Te8deetirGNY2reVJnc7o8rluYGgUocVvc8L0gxPXToo1y','500');

INSERT INTO `user`(`email`,`firstname`,`lastname`,`password`,`balance`)
VALUES ('hermione.granger@gmail.com','Hermione','Granger','$2a$12$G9AYvFCmxcNaUu2TaK0roeylMGogVBVwIVM1tW5x42nDBYajZaDFS','450');

INSERT INTO `user`(`email`,`firstname`,`lastname`,`password`,`balance`)
VALUES ('ron.weasley@gmail.com','Ron','Weasley','$2a$12$tc8qg.uvMUOxo7tssDnU5eSSL2qAIK4V1jsY7z3Agt46u1QJfbsIa','53.26');

INSERT INTO `bank_account`(`iban`, `description`, `is_actif`, `user_id`)
VALUES ('Iban To Delete','BankAccountToDelete',true,1);

INSERT INTO `bank_account`(`iban`, `description`, `is_actif`, `user_id`)
VALUES ('FR12345','Potter iban fr12345',true,2);

INSERT INTO `bank_account`(`iban`, `description`, `is_actif`, `user_id`)
VALUES ('FR12345','Granger iban ',true,3);

INSERT INTO `bank_account`(`iban`, `description`, `is_actif`, `user_id`)
VALUES ('FR12345','Weasley iban ',true,4);

/*``*/