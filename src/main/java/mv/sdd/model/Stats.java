package mv.sdd.model;

import mv.sdd.utils.Constantes;

import java.util.HashMap;
import java.util.Map;

public class Stats {
    private Horloge horloge;
    private int totalClients = 0;
    private int nbServis = 0;
    private int nbFaches = 0;
    private double chiffreAffaires = 0;
    // TODO : remplacer Object par le bon type et initilaliser l'attribut avec la bonne valeur
    //  et ajuster les getters et les setters
    private Map<MenuPlat, Integer> ventesParPlat;

    // TODO: au besoin ajuster le constructeur et/ou ajouter d'autres
    public Stats(Horloge horloge) {
        this.horloge = horloge;
        // TODO : compléter le code manquant
        ventesParPlat = new HashMap<>();
        ventesParPlat.put(MenuPlat.PIZZA, 0);
        ventesParPlat.put(MenuPlat.BURGER, 0);
        ventesParPlat.put(MenuPlat.FRITES, 0);
    }

    public void incrementerTotalClients() {
        totalClients++;
    }

    public void incrementerNbServis() {
        nbServis++;
    }

    public void incrementerNbFaches() {
        nbFaches++;
    }

    public void incrementerChiffreAffaires(double montant) {
        this.chiffreAffaires += montant;
    }

    public static String statsPlatLine(MenuPlat codePlat, int quantite) {
        return "\n" + "\t\t" + codePlat + " : " + quantite;
    }

    // TODO : ajouter incrementerVentesParPlat(MenuPlat codePlat) et autres méthodes au besoin
    public void incrementerVentesParPlat(MenuPlat codePlat) {
        Integer nombreVentes = ventesParPlat.get(codePlat);

        if (nombreVentes == null) {
            nombreVentes = 0;
        }
        ventesParPlat.put(codePlat, nombreVentes + 1);
    }

    public int getVentes(MenuPlat codePlat) {
        Integer quantite = ventesParPlat.get(codePlat);

        if (quantite == null) {
            return 0;
        }

        return quantite;
    }

    @Override
    public String toString() {
        String chaine = String.format(
                Constantes.STATS_GENERAL,
                horloge.getTempsSimule(),
                totalClients,
                nbServis,
                nbFaches,
                chiffreAffaires
        );

        // TODO : ajouter le code pour concaténer avec statsPlatLines les lignes des quantités vendus par plat (à l'aide de ventesParPlat),
        //  sachant que la méthode statsPlatLine sert à formater une ligne et retourne une chaine
        chaine = chaine + statsPlatLine(MenuPlat.FRITES, getVentes(MenuPlat.FRITES));
        chaine = chaine + statsPlatLine(MenuPlat.BURGER, getVentes(MenuPlat.BURGER));
        chaine = chaine + statsPlatLine(MenuPlat.PIZZA,  getVentes(MenuPlat.PIZZA));

        return chaine;
    }
}
