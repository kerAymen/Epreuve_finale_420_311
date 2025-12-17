Simulation de restaurant 

Ce projet est une application Java en mode console qui simule le service dans un petit restaurant lors d’un rush de midi. Ce projet est une application Java qui simule le service d'un petit restaurant lors d’un rush de midi.  

## Auteur: Aymen

## 1. Objectif
L’objectif est de développer une application **Java (console)** qui simule le service dans un petit restaurant pendant un “rush” de midi :

- des clients arrivent, passent commande, attendent leurs plats 🍕 ;
- un cuisinier prépare les commandes dans un **thread séparé** ;
- les clients sont soit servis 😋, soit repartent fâchés 😡 si leur patience tombe à 0 ;
- toutes les actions sont lues depuis un **fichier texte** ;
- toute la sortie est écrite dans un **fichier de logs**.



## 2. Prérequis

- **Java** : version 21 et plus (recommandé 21).
- **Maven** installé et accessible avec la commande `mvn`
- IDE recommandé : **IntelliJ IDEA**

---


## 3. Structure du projet

Le projet suit la structure standard Maven :
```text
.
├── pom.xml
└── src
    └── main
        └── java
            └── mv
                └── sdd
                    ├── App.java          # Point d'entrée (main)
                    ├── model/            # Entités métier (Client, Commande, MenuPlat, Stats, ...)
                    ├── sim/              # Simulation (Restaurant, Horloge, ...)
                    │   └── thread/       # Threads (Cuisinier, ...)
                    ├── io/               # Lecture d'actions, Logger
                    └── utils/            # Constantes, Formatter, outils divers
```
## 4. Scénarios d’exemple

Un fichier de scénario est un simple fichier texte où chaque ligne décrit une action.
Le dossier data contient deux fichiers exemples.

## 5. Compilation et exécution
À la racine du projet :
```bash
mvn clean package
```

### 5.1 Exécuter l’application

L’application attend deux arguments :
1. le chemin du fichier de scénario (entrée),
2. le chemin du fichier de sortie (logs).
```bash
mvn exec:java -Dexec.args="data/scenario_1.txt data/sortie_1.txt"
```


## 7. Fonctionnalités principales

Gestion des clients et de leur patience
Gestion des commandes et de leurs états
Utilisation de structures de données adaptées
Gestion du temps simulé via la méthode
Utilisation de synchronized, wait() et notifyAll() pour le thread du cuisinier
Calcul et affichage des statistiques finales.

