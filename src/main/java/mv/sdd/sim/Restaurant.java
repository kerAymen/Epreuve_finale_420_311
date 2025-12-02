package mv.sdd.sim;

import mv.sdd.io.Action;
import mv.sdd.utils.Logger;

public class Restaurant {
    private final Logger logger;
    // TODO : Ajouter les attributs nécessaires ainsi que les getters et les setters

    // TODO : Ajouter le(s) constructeur(s)
    public Restaurant(Logger logger) {
        this.logger = logger;
    }

    // TODO : implémenter les méthodes suivantes
    // Méthode appelée depuis App pour chaque action
    public void executerAction(Action action){
        // Votre code ici.
    }

    public void demarrerService(int dureeMax, int nbCuisiniers) {
        // Votre code ici.
    }

    public void avancerTemps(int minutes) {
        // Votre code ici.
    }

    public void arreterService(){
        // Votre code ici.
    }

    // TODO : Déclarer et implémenter les méthodes suivantes
    // tick()
    // afficherEtat()
    // afficherStatistiques()
    // Client ajouterClient(int id, String nom, int patienceInitiale)
    // Commande passerCommande(int idClient, MenuPlat codePlat)
    // retirerProchaineCommande(): Commande
    // marquerCommandeTerminee(Commande commande)
    // Client creerClient(String nom, int patienceInitiale)
    // Commande creerCommandePourClient(Client client)

    // TODO : implémenter d'autres sous-méthodes qui seront appelées par les méthodes principales
    //  pour améliorer la lisibilité des méthodes en les découpant au besoin (éviter les trés longues méthodes)
    //  exemple : on peut avoir une méthode diminuerPatienceClients()
    //  qui permet de diminuer la patience des clients (appelée par tick())
}
