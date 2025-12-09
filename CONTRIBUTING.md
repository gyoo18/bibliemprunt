# Bibliemprunt, manuel du contributeur

Ce projet repose sur Java 21 et utilise Gradle sur Groovy comme système d'assemblage.

Voici une décomposition sommaire du répertoire :

```python
app
├── build.gradle    # fichier de spécification de l'assemblage
├── src
│   ├── main
│   │   ├── java
│   │   │   └── bibliemprunt
│   │   │       └── ...         # code
│   │   └── resources           # ressources importantes au fonctionnement du code
│   └── test
│       ├── java
│       │   └── bibliemprunt
│       │       └── ...         # tests
│       └── resources           # ressources pour les tests
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
