# Bibliemprunt

Logiciel de contrôle de borne d'emprunt de bibliothèque.

Ce projet constitue le projet final du cours de Modélisation et Conception Orienté Objet.

## Prérequis

- Java >= 21
- SQLite 3

## Assembler

1. `./gradlew build` ou `gradlew.bat build` sur Windows.
2. Les fichiers exécutables se trouveront dans `app/build/distributions/Bibliemprunt.zip` et `app/build/distributions/Bibliemprunt.tar`

## Exécuter

1. `./gradlew build run` ou `gradlew.bat build run` sur Windows.

OU

1. Dézippez le dossier dans `app/build/distributions/Bibliemprunt.zip` ou `app/build/distributions/Bibliemprunt.tar`
2. exécutez `bin/bibliemprunt` ou `bin/bibliemprunt.bat` sur Windows.

## Utiliser

Vous serez d'abord face à l'écran de connection suivant :

```shell
=== BIBLIEMPRUNT | SYSTÈME D'EMPRUNT DE LIVRES ===
Bienvenue au système d'emprunt de livres bibliemprunt
--- Authentification ---

Veuillez vous authentifier.

Votre nom d'utilisateur :
Votre NIP :
```

Entrez un nom d'utilisateur et un NIP parmis les suivants :

| numeroCompte | NIP  |
| :----------: | :--: |
|   jean123    | 1234 |
|  jeanne456   | 4321 |
|  martin456   | 5678 |
|  gagnon789   | 9012 |
|    roy101    | 3456 |
|  leblanc202  | 7890 |
| bertrand303  | 2345 |
|   morin404   | 6789 |
|  dubois505   | 1235 |
| pelletier606 | 4567 |
| bouchard707  | 8901 |
|  girard808   | 3457 |

Vous pouvez toujours ajouter un utilisateur en modifiant le fichier `src/resources/clients.json`

Une fois connect, appuyez sur entrer vous ferez face à l'écran suivant :

```shell
=== BIBLIEMPRUNT | SYSTÈME D'EMPRUNT DE LIVRES ===

--- Emprunts ---

1. Emprunter un livre
2. Voir les emprunts en cours
3. Voir tous vos emprunts actifs
4. Voir les livres disponibles
5. Annuler un emprunt
6. Annuler la transaction
7. Terminer la transaction

>
```

Si vous choisissez l'option _Emprunter un livre_, vous aurez le choix d'emprunter l'un des livres suivants :

|  RFID  | Titre                         |
| :----: | :---------------------------- |
| 123456 | La peste                      |
| 789012 | Les misérables                |
| 234567 | L'Étranger                    |
| 345678 | Le Petit Prince               |
| 456789 | Germinal                      |
| 567890 | Madame Bovary                 |
| 678901 | Notre-Dame de Paris           |
| 890123 | Le Comte de Monte-Cristo      |
| 901234 | Les Trois Mousquetaires       |
| 012345 | Candide                       |
| 112233 | Le Rouge et le Noir           |
| 223344 | À la recherche du temps perdu |
| 334455 | Les Fleurs du mal             |
| 445566 | Voyage au bout de la nuit     |
| 556677 | Le Père Goriot                |
| 667788 | Bel-Ami                       |
| 778899 | La Chartreuse de Parme        |
| 889900 | Thérèse Raquin                |
| 990011 | Les Liaisons dangereuses      |
| 101112 | Eugénie Grandet               |
| 121314 | La Condition humaine          |
| 131415 | Bonjour tristesse             |
| 141516 | Le Grand Meaulnes             |
| 151617 | Nana                          |
| 161718 | Zazie dans le métro           |
| 171819 | Le Horla                      |
| 181920 | Poil de Carotte               |
| 192021 | Le Diable au corps            |
| 202122 | La Nausée                     |
| 212223 | Rhinocéros                    |

Vous pouvez aussi ajouter un livre en modifiant le fichier `src/resources/livres.json`

Il est important de noter qu'une fois le programme démarré une fois, la base de donnée vivera dans un fichier SQLite nommé `bibliemprunt.db` et que la seule façon de modifier les entrée sera de directement modifier ce fichier. Pour réinitialiser la base de donnée, simplement effacer ce fichier.

## Présentation du projet

_TODO_

