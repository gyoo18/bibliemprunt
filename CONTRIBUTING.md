# Bibliemprunt, manuel du contributeur

Ce projet repose sur :

- **Java 21** pour le code principal
- **Gradle 9.2** sur **Groovy 4.0** pour le système d'assemblage
- **JUnit 4.13** pour les tests unitaires
- **Java Swing** pour l'interface graphique
- ~~**SQLite** pour la gestion de base de donnée~~ *À venir.*

Voici une décomposition sommaire du répertoire :

```python
src
├── build.gradle    # fichier de spécification de l'assemblage
├── main
│   └── bibliemprunt
│       └── ...         # code
├──test
│  └── bibliemprunt
│      └── ...          # tests
├── resources
│   └── ...             # ressources importantes au fonctionnement du code
└── build               # dossier généré après l'assemblage
    ├── reports
    │   └── ...         # contient des fichiers html faisant état des rapports de compilation
    ├── distributions
    │   └── ...         # contient les exécutables finaux
    ...
README
└── ...     # Toutes les ressources présentes dans le README.md
README.md
CONTRIBUTING.md     # le présent fichier
gradlew             # scritps d'interface avec Gradle
gradlew.bat
settings.gradle     # paramètres du système Gradle. À ne pas confondre avec les build.gradle, 
                    # qui sont les fichiers de spécification de l'assemblage
modelio
└── bibliemprunt
    └── ...         # contient l'espace de travail modelio
rapport             # Tous les fichiers liés au rapport/à la paperasse du devoir
├── diagrammes
│   └── ...         # Tous les diagrammes liés au rapport/à la paperasse du devoir
... 

1081 directories, 400 files
```

Voici une décomposition sommaire des branches git :

1. **master** La branche principale, sur laquelle doit se trouver la dernière version stable. Cette branche est bloqué et il nécessite deux approbations afin de fusionner avec une autre branche.
2. **dev** Cette branche sert à combiner les morceaux en cours de développement. Une fois jugée stable, elle serat fusionnée avec *master*.
3. **rapport** Cette branche sert à conserver les modifications au rapport
4. **modelio** Tout travail effectué sur modelio et à la modélisation doit être confiné à cette branche.
5. **feature/Ajout-des-classes-de-bases** Branche d'ajout des classes de bases.

Voici une décomposition de l'architecture du code en tant que tel. Veuillez vous référer aux diagrammes UML
présents dans rapport/diagrammes pour plus d'information.

```python
src/main/bibliemprunt
├── Bibliothèque.java           # Main
├── Borne.java                  # Contrôleur de la borne d'emprunt de livres
├── Paramètre.java              # Paramètres globaux de l'application
├── données                     # Divers gestionnaires de données
│   ├── BanqueClient.java
│   ├── BanqueEmprunts.java
│   ├── BanqueLivres.java
│   └── SQLInterface.java
├── gui                         # Système d'interface utilisateur de la borne
│   ├── GUI.java
│   ├── Page.java
│   └── UsinePages.java
└── models                      # Éléments conceptuels pour la manipulation dans Java
    ├── CompteClient.java
    ├── Emprunt.java
    └── Livre.java
```
