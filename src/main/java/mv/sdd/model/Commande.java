package mv.sdd.model;

public class Commande {
    private int id;
    private static int nbCmd = 0;
    private final Client client;
    private EtatCommande etat = EtatCommande.EN_ATTENTE;
    private int tempsRestant; // en minutes simulées
    // TODO : ajouter l'attribut plats et son getter avec le bon type et le choix de la SdD adéquat
    // private final <Votre structure de choix adéquat> plats

    // TODO : Ajout du ou des constructeur(s) nécessaires ou compléter au besoin
    public Commande(Client client, MenuPlat plat) {
        id = ++nbCmd;
        this.client = client;
        // À compléter
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

    // TODO : Ajoutez la méthode ajouterPlat

    // TODO : Ajoutez la méthode demarrerPreparation

    // TODO : Ajoutez la méthode decrementerTempsRestant

    // TODO : Ajoutez la méthode estTermineeParTemps

    // TODO : Ajoutez la méthode calculerTempsPreparationTotal

    // TODO : Ajoutez la méthode calculerMontant
}
