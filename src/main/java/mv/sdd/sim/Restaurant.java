package mv.sdd.sim;

import mv.sdd.io.Action;
import mv.sdd.io.ActionType;
import mv.sdd.model.*;
import mv.sdd.sim.thread.Cuisinier;
import mv.sdd.utils.Constantes;
import mv.sdd.utils.Logger;
import mv.sdd.utils.Formatter;


import java.util.*;

public class Restaurant {
    private final Logger logger;
    // TODO : Ajouter les attributs nécessaires ainsi que les getters et les setters
    private boolean serviceActif;
    private int dureeMaxService;
    private int nbCuisiniers;
    private Horloge horloge;
    private Stats stats;
    private Map<Integer, Client> clients;
    private Deque<Commande> attenteCommande;
    private List<Commande> commandesEnPrep;
    private Map<MenuPlat, Plat> menu;
    private Thread threadCuisinier;
    private Cuisinier cuisinier;


    // TODO : Ajouter le(s) constructeur(s)
    public Restaurant(Logger logger) {this.logger = logger;
        this.serviceActif = false;
        this.dureeMaxService = 0;
        this.nbCuisiniers = 0;
        this.horloge = new Horloge();
        this.stats = new Stats(horloge);
        this.clients = new HashMap<>();
        this.attenteCommande = new ArrayDeque<>();
        this.commandesEnPrep = new ArrayList<>();
        this.menu = Constantes.MENU;
    }

    // TODO : implémenter les méthodes suivantes
    // Méthode appelée depuis App pour chaque action
    public void executerAction(Action action){
        // Votre code ici.
        ActionType type = action.getType();

        if (type == ActionType.DEMARRER_SERVICE) {
            demarrerService(action.getParam1(), action.getParam2());
            return;
        }

        if (type == ActionType.AVANCER_TEMPS) {
            logger.logLine(Constantes.AVANCER_TEMPS + action.getParam1());
            avancerTemps(action.getParam1());
            return;
        }

        if (type == ActionType.AJOUTER_CLIENT) {
            int id = action.getParam1();
            int patience = action.getParam2();
            String nom = action.getParam3();

            ajouterClient(id, nom, patience);
            return;
        }

        if (type == ActionType.PASSER_COMMANDE) {
            MenuPlat code = MenuPlat.valueOf(action.getParam3());
            passerCommande(action.getParam1(), code);
            return;
        }

        if (type == ActionType.AFFICHER_ETAT) {
            afficherEtat();
            return;
        }
        if (type == ActionType.AFFICHER_STATS) {
            logger.logLine(Constantes.HEADER_AFFICHER_STATS);
            afficherStatistiques();
            return;
        }

        if (type == ActionType.QUITTER) {
            arreterService();
            return;
        }
    }

    public void demarrerService(int dureeMax, int nbCuisiniers) {
        // Votre code ici.
        serviceActif = true;
        this.dureeMaxService = dureeMax;
        this.nbCuisiniers = nbCuisiniers;

        logger.logLine(String.format(
                Constantes.DEMARRER_SERVICE, dureeMaxService, this.nbCuisiniers
        ));

        cuisinier = new Cuisinier(this);
        threadCuisinier = new Thread(cuisinier, "Cuisinier");
        threadCuisinier.start();
    }

    public void avancerTemps(int minutes) {
        if (minutes <= 0) {
            return;
        }

        int i = 0;
        while (i < minutes) {
            tick();
            i = i + 1;
        }
    }

    public synchronized void arreterService(){
        // Votre code ici.
        serviceActif = false;

        if (cuisinier != null) {
            cuisinier.arreter();
        }
        notifyAll();

        if (threadCuisinier != null) {
            try {
                threadCuisinier.join(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // TODO : Déclarer et implémenter les méthodes suivantes
    // tick()
    private void tick() {
        avancerPreparations();
        terminerCommandes();
        diminuerPatienceClients();
        avancerHorloge();
    }

    // afficherEtat()
    private void afficherEtat() {
        int nombreClients = 0;
        int nombreServis = 0;
        int nombreFaches = 0;

        for (Client client : clients.values()) {
            EtatClient etat = client.getEtat();

            if (etat != EtatClient.PARTI_FACHE) {
                nombreClients = nombreClients + 1;
            }
            if (etat == EtatClient.SERVI) {
                nombreServis = nombreServis + 1;
            }
            if (etat == EtatClient.PARTI_FACHE) {
                nombreFaches = nombreFaches + 1;
            }
        }

        int nombreAttente  = attenteCommande.size();
        int nombreEnPreparation = commandesEnPrep.size();

        logger.logLine(
                Formatter.resumeEtat(horloge.getTempsSimule(), nombreClients, nombreServis, nombreFaches, nombreAttente, nombreEnPreparation)
        );

        List<Client> liste = new ArrayList<>(clients.values());
        Collections.sort(liste, new Comparator<Client>() {
            public int compare(Client a, Client b) {
                return Integer.compare(a.getId(), b.getId());
            }
        });

        for (Client client : liste) {
            logger.logLine("    " + Formatter.clientLine(client, null));
        }
    }
    // afficherStatistiques()
    private void afficherStatistiques() {
        logger.logLine(stats.toString());
    }

    // Client ajouterClient(int id, String nom, int patienceInitiale)
    private Client ajouterClient(int id, String nom, int patienceInitiale) {
        Client clientExistant = clients.get(id);
        if (clientExistant != null) {
            return clientExistant;
        }

        Client client = new Client(id, nom, patienceInitiale);
        clients.put(id, client);

        stats.incrementerTotalClients();
        logger.logLine(Formatter.eventArriveeClient(horloge.getTempsSimule(), client));

        return client;
    }

    // Commande passerCommande(int idClient, MenuPlat codePlat)
    private Commande passerCommande(int idClient, MenuPlat codePlat) {
        Client client = clients.get(idClient);
        if (client == null) {
            return null;
        }
        Commande commande = client.getCommande();
        if (commande == null) {
            commande = new Commande(client, codePlat);
            client.setCommande(commande);
            attenteCommande.addLast(commande);

        }
        else {
            commande.ajouterPlat(codePlat);
        }

        logger.logLine(Formatter.eventCommandeCree(horloge.getTempsSimule(), commande.getId(), client, codePlat)
        );
        return commande;


    }


    // retirerProchaineCommande(): Commande
    private Commande retirerProchaineCommande() {
        if (attenteCommande.isEmpty()) {
            return null;
        }
        return attenteCommande.pollFirst();
    }

    // marquerCommandeTerminee(Commande commande)
    private void marquerCommandeTerminee(Commande commande) {
        if (commande == null) {
            return;
        }
        if (commande.getEtat() != EtatCommande.PRETE) {
            return;
        }

        commande.setEtat(EtatCommande.LIVREE);

        Client client = commande.getClient();
        client.setEtat(EtatClient.SERVI);

        stats.incrementerNbServis();
        double montant = commande.calculerMontant();
        stats.incrementerChiffreAffaires(montant);

        for (MenuPlat code : commande.getPlats()) {
            stats.incrementerVentesParPlat(code);
        }

        commandesEnPrep.remove(commande);
        logger.logLine(Formatter.eventCommandeTerminee(horloge.getTempsSimule(), commande.getId(), client)
        );
    }

    // Client creerClient(String nom, int patienceInitiale)
    private Client creerClient(String nom, int patienceInitiale) {
        int id = 1;
        while (clients.containsKey(id)) {
            id = id + 1;
        }
        return ajouterClient(id, nom, patienceInitiale);
    }

    // Commande creerCommandePourClient(Client client)
    private Commande creerCommandePourClient(Client client) {
        if (client == null) {
            return null;
        }

        Commande commande = client.getCommande();
        if (commande != null) {
            return commande;
        }

        commande = new Commande(client, null);
        client.setCommande(commande);
        return commande;
    }

    // TODO : implémenter d'autres sous-méthodes qui seront appelées par les méthodes principales
    //  pour améliorer la lisibilité des méthodes en les découpant au besoin (éviter les trés longues méthodes)
    //  exemple : on peut avoir une méthode diminuerPatienceClients()
    //  qui permet de diminuer la patience des clients (appelée par tick())
    private void diminuerPatienceClients() {
        for (Client client : clients.values()) {

            if (client.getEtat() == EtatClient.EN_ATTENTE) {

                Commande commande = client.getCommande();

                if (commande == null) {
                    client.diminuerPatience(1);
                }
                else {
                    if (commande.getEtat() == EtatCommande.EN_PREPARATION) {
                        if (commande.getTempsRestant() > 1) {
                            client.diminuerPatience(1);
                        }
                    }
                    else {
                        client.diminuerPatience(1);
                    }
                }

                if (client.getEtat() == EtatClient.PARTI_FACHE) {
                    stats.incrementerNbFaches();
                    logger.logLine(Formatter.eventClientFache(horloge.getTempsSimule(), client));
                }
            }
        }
    }


    public synchronized void demarrerPreparations() {
        boolean attendre = true;

        while (attendre) {

            if (serviceActif == false) {
                return;
            }

            if (attenteCommande.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            else {
                attendre = false;
            }
        }

            Commande commande = retirerProchaineCommande();
            if (commande == null) {
                return;
            }

            commande.demarrerPreparation();
            commandesEnPrep.add(commande);

            logger.logLine(Formatter.eventCommandeDebut(horloge.getTempsSimule(), commande.getId(), commande.getTempsRestant())
            );
    }
    private void avancerPreparations() {
        for (Commande commande : commandesEnPrep) {
            commande.decrementerTempsRestant();
        }
    }
    private void terminerCommandes() {
        List<Commande> commandesTermine = new ArrayList<>();

        for (Commande commande : commandesEnPrep) {
            if (commande.estTermineeParTemps()) {
                commandesTermine.add(commande);
            }
        }

        for (Commande commande : commandesTermine) {
            marquerCommandeTerminee(commande);
        }
    }
    private synchronized void avancerHorloge() {
        horloge.avancerTempsSimule(1);
         notifyAll();

        if (serviceActif) {
            int temps = horloge.getTempsSimule();
            if (temps >= dureeMaxService) {
                arreterService();
            }
        }
    }
}
