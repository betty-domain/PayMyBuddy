###### PayMyBuddy

PayMyBuddy est une application permettant de réaliser facilement des transferts d'argent entre amis.
Le projet actuellement sous GitHub est une application web basée sur les éléments suiants :
- Spring Boot
- Spring Security
- Base de données MySql

Afin de pouvoir interagir avec n'importe quel front, les fonctionnalités sont disponibles sous forme d'une API avec différents endpoints

###### Mise en place de l'environnement
L'ensemble des éléments de configuration ci-dessous sont présents dans le fichier src/main/resources/application.properties, vous pouvez les modifier si vous le souhaitez.

L'application est déployée sur le serveur Tomcat embarqué, sur le port **9020**.
L'application fonction avec une base de données **MySQL**.

Avant de lancer l'application, vous devez exécuter les scripts SQL suivants, disponibles dans le répertoire src/main/resources/static :
- schema.sql : qui permet de créer le schéma et d'initialiser la structure de la base de données
- insert_data.sql : qui permet d'initialiser certaines données en base

###### Mise en place de l'environnement pour les tests
L'ensemble des éléments de configuration ci-dessous sont présents dans le fichier src/test/resources/application.properties, vous pouvez les modifier si vous le souhaitez.

L'application est déployée sur le serveur Tomcat embarqué, sur le port **9021**.
L'application fonction avec une base de données **MySQL**.

Avant de lancer les tests de l'application, vous devez exécuter les scripts SQL suivants, disponibles dans le répertoire src/test/resources/static :
- schemaTest.sql : qui permet de créer le schéma et d'initialiser la structure de la base de données

###### Modélisation du domaine métier :
![Diagramme de classes](src/main/resources/static/DiagrammeClasse.png?raw=true)

![Diagramme de cas d'utilisation](src/main/resources/static/DiagrammeCasUtilisation.png?raw=true)

![SchemaBDD](src/main/resources/static/SchemaBDD.PNG?raw=true)

###### Liste des fonctionnalités présentes :

Voici la liste des endpoints disponibles et leur description :

- login 
    - method GET: permet de se connecter à l'application et d'être reconnu (vous pouvez utiliser le compte de test suivant pour le démarrage : _**email :**_ harry.potter@gmail.com, **_mdp :_** potter)
- user 
    - method POST: permet d'ajouter un utilisateur dans l'application
    - method PUT : permet de mettre à jour les informations d'un utilisateur
- users
    - method GET : permet de récupérer l'ensemble des utilisateurs de l'application
- bankAccount
    - method POST : permet d'ajouter un compte bancaire pour un utilisateur
    - method DELETE : permet de supprimer un compte bancaire pour un utilisateur
- bankAccounts
    - method GET : permet de récupérer la liste des comptes bancaires d'un utilisateur
- transferFromBank 
    - method POST : permet de réaliser un transfert d'argent depuis un compte bancaire enregistré dans l'application PayMyBuddy, afin d'alimenter le solde de l'utilisateur détenteur du compte
- transferToBank :
    - method POST : permet de réaliser un transfert d'argent vers un compte bancaire enregistré dans l'application PayMyBuddy
- transfers :
    - method GET : permet de liste l'ensemble des transferts réalisés pour un utilisateur donné
- friendship :
    - method POST : permet d'ajouter un utilisateur en tant qu'ami
    - method DELETE : permet de retirer un utilisateur en tant qu'ami
    - method GET : permet de lister les amis d'un utilisateur
- transaction :
    - method POST : permet de réaliser un virement d'argent vers un ami
    - method GET : permet de lister tous les virements d'argent réalisés vers des amis
- fee : 
    - method GET : permet de lister toutes les taxes appliquées sur les virement pour un utilisateur donné
    




