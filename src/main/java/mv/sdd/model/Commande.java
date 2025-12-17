package mv.sdd.model;

import java.util.ArrayList;
import java.util.List;
import mv.sdd.utils.Constantes;


public class Commande {
    private int id;
    private static int nbCmd = 0;
    private final Client client;
    private EtatCommande etat = EtatCommande.EN_ATTENTE;
    private int tempsRestant; // en minutes simulées
    // TODO : ajouter l'attribut plats et son getter avec le bon type et le choix de la SdD adéquat
    // private final <Votre structure de choix adéquat> plats
    private final List<MenuPlat> plats;

    // TODO : Ajout du ou des constructeur(s) nécessaires ou compléter au besoin
    public Commande(Client client, MenuPlat plat) {
        id = ++nbCmd;
        this.client = client;
        // À compléter
        this.tempsRestant = 0;
        this.plats = new ArrayList<>();

        if (plat != null) {
            this.plats.add(plat);
        }
    }

    public int getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public EtatCommande getEtat() {
        return etat;
    }

    public int getTempsRestant() {
        return tempsRestant;
    }

    public void setEtat(EtatCommande etat) {
        this.etat = etat;
    }

    // TODO : Ajoutez la méthode ajouterPlat À
    public List<MenuPlat> getPlats() {
        return plats;
    }

    public void ajouterPlat(MenuPlat plat) {
        if (plat == null) {
            return;
        }
        plats.add(plat);
    }

    // TODO : Ajoutez la méthode demarrerPreparation
    public void demarrerPreparation() {
        if (etat != EtatCommande.EN_ATTENTE) {
            return;
        }

        etat = EtatCommande.EN_PREPARATION;

        int total = calculerTempsPreparationTotal();
        tempsRestant = total;
    }

    // TODO : Ajoutez la méthode decrementerTempsRestant
    public void decrementerTempsRestant() {
        if (etat != EtatCommande.EN_PREPARATION) {
            return;
        }

        if (tempsRestant > 0) {
            tempsRestant = tempsRestant - 1;
        }

        if (tempsRestant == 0) {
            etat = EtatCommande.PRETE;
        }
    }

    // TODO : Ajoutez la méthode estTermineeParTemps
    public boolean estTermineeParTemps() {
        if (etat == EtatCommande.PRETE) {
            return true;
        }
        return false;
    }

    // TODO : Ajoutez la méthode calculerTempsPreparationTotal
    public int calculerTempsPreparationTotal() {
        int tempsTotal = 0;

            for (MenuPlat plat : plats) {
                int tempsPlat = Constantes.MENU.get(plat).getTempsPreparation();
                tempsTotal = tempsTotal + tempsPlat;
            }
        return tempsTotal;
    }

    // TODO : Ajoutez la méthode calculerMontant
    public double calculerMontant() {
        double montantTotal = 0.0;

        if (plats != null) {
            for (MenuPlat plat : plats) {
                double prixPlat = Constantes.MENU.get(plat).getPrix();
                montantTotal = montantTotal + prixPlat;
            }
        }

        return montantTotal;
    }

}
