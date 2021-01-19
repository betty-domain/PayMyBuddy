use `paymybuddy`;

INSERT INTO `user`(`email`,`firstname`,`lastname`,`password`)
VALUES ('betty.domain@free.fr','betty','domain','$2a$12$yc2A75xOtp72Yl8sWplcReJs8Ku0zg6Im7U7W35y5eo5xagomBAOa');  /*id=1*/

INSERT INTO `user`(`email`,`firstname`,`lastname`,`password`,`balance`)
VALUES ('harry.potter@gmail.com','Harry','Potter','$2a$12$NlA3EC9Te8deetirGNY2reVJnc7o8rluYGgUocVvc8L0gxPXToo1y','639.5'); /*id=2*/

INSERT INTO `user`(`email`,`firstname`,`lastname`,`password`,`balance`)
VALUES ('hermione.granger@gmail.com','Hermione','Granger','$2a$12$G9AYvFCmxcNaUu2TaK0roeylMGogVBVwIVM1tW5x42nDBYajZaDFS','550'); /*id=3*/

INSERT INTO `user`(`email`,`firstname`,`lastname`,`password`,`balance`)
VALUES ('ron.weasley@gmail.com','Ron','Weasley','$2a$12$tc8qg.uvMUOxo7tssDnU5eSSL2qAIK4V1jsY7z3Agt46u1QJfbsIa','100'); /*id=4*/

/*betty.domain@free.fr, mdp:myPassword
Harry potter, mdp:potter
Hermione Granger, mdp:granger
Ron Weasley, mdp:weasley*/

/* insertion des comptes bancaires */

INSERT INTO `bank_account`(`iban`, `description`, `is_actif`, `user_id`)
VALUES ('Iban To Delete','BankAccountToDelete',true,1); /*id=1*/

INSERT INTO `bank_account`(`iban`, `description`, `is_actif`, `user_id`)
VALUES ('FR12345','Potter iban fr12345',true,2); /*id=2*/

INSERT INTO `bank_account`(`iban`, `description`, `is_actif`, `user_id`)
VALUES ('FR12345','Granger iban ',true,3); /*id=3*/

INSERT INTO `bank_account`(`iban`, `description`, `is_actif`, `user_id`)
VALUES ('FR12345','Weasley iban ',true,4); /*id=4*/

INSERT INTO `bank_account`(`iban`, `description`, `is_actif`, `user_id`)
VALUES ('EN 78 945 12','Potter iban EN 78945',false,2); /*id=5*/

INSERT INTO `bank_account`(`iban`, `description`, `is_actif`, `user_id`)
VALUES ('DE 65 4321','Potter iban DE 654321',true,2); /*id=6*/

/* insertion des transferts bancaires*/

INSERT INTO `bank_transfer`(`amount`, `date`, `transfer_order`, `user_id`, `bank_account_id`)
VALUES (   250,    '2010-10-15 10:10:00',    1,    2,    2);

INSERT INTO `bank_transfer`(`amount`, `date`, `transfer_order`, `user_id`, `bank_account_id`)
VALUES (   500,    '2010-10-25 11:10:00',    1,    2,    2);

INSERT INTO `bank_transfer`(`amount`, `date`, `transfer_order`, `user_id`, `bank_account_id`)
VALUES (   100,    '2010-10-28 10:10:00',    0,    2,    2);

INSERT INTO `bank_transfer`(`amount`, `date`, `transfer_order`, `user_id`, `bank_account_id`)
VALUES (   100,    '2010-10-28 11:00:00',    1,    2,    5);

INSERT INTO `bank_transfer`(`amount`, `date`, `transfer_order`, `user_id`, `bank_account_id`)
VALUES (   10,    '2010-11-5 14:00:00',    0,    2,    5);

INSERT INTO `bank_transfer`(`amount`, `date`, `transfer_order`, `user_id`, `bank_account_id`)
VALUES (   500,    '2010-10-28 9:30:00',    1,    3,    3);

INSERT INTO `bank_transfer`(`amount`, `date`, `transfer_order`, `user_id`, `bank_account_id`)
VALUES (   500,    '2010-10-28 9:50:10',    1,    4,    4);
/*``*/


/*Insertion des liens d'amitiés */
INSERT INTO `user_friends` (`user_friend_id`, `user_id`, `is_actif`)
VALUES ( 3 , 1, true);

INSERT INTO `user_friends` (`user_friend_id`, `user_id`, `is_actif`)
VALUES ( 4 , 1, true);

INSERT INTO `user_friends` (`user_friend_id`, `user_id`, `is_actif`)
VALUES ( 1 , 3, false);

INSERT INTO `user_friends` (`user_friend_id`, `user_id`, `is_actif`)
VALUES ( 2 , 3, true);

INSERT INTO `user_friends` (`user_friend_id`, `user_id`, `is_actif`)
VALUES ( 4 , 3, true);

/* insertions de transactions entre amis  et taxe associées */
INSERT INTO `transaction`(`description`, `amount`, `date`, `beneficiary_user_id`, `payer_user_id`)
VALUES ('achat de livres à Ron',   50,    '2020-12-5 10:00:00',    4,    2); /* id =1*/

INSERT INTO `fee`(`amount`, `percentage100`, `date`, `user_id`, `transaction_id`)
VALUES ( 0.25  , 0.5, '2020-12-5 10:00:00' ,   2 ,   1);

INSERT INTO `transaction`(`description`, `amount`, `date`, `beneficiary_user_id`, `payer_user_id`)
VALUES ('achat de livres à Hermione',   50,    '2020-12-15 9:00:00',    3,    2) /* id=2 */;

INSERT INTO `fee`(`amount`, `percentage100`, `date`, `user_id`, `transaction_id`)
VALUES ( 0.25  , 0.5, '2020-12-15 9:00:00' ,   2 ,   2);