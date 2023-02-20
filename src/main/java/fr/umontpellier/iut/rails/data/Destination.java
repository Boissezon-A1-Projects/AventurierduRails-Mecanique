package fr.umontpellier.iut.rails.data;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Destination {
    /**
     * Liste des villes à relier
     */
    private final List<String> villes;
    /**
     * Score obtenu si les villes de la destination sont connectées en fin de partie
     */
    private final int valeurSimple;
    /**
     * Score obtenu si les villes de la destination sont connectées dans le bon ordre en fin de partie
     * (égal à valeurSimple pour les destinations simples, différent uniquement pour les itinéraires)
     */
    private final int valeurMax;
    /**
     * Nombre de points à retirer en fin de partie si les villes de la destination ne sont pas connectées
     * (égal à valeurSimples pour les destinations simples, différent uniquement pour les itinéraires)
     */
    private final int penalite;
    /**
     * Nom de la destination automatiquement assigné à la création en fonction du compteur statique.
     * Le nom d'une destination est "Dxx" (où xx est le numéro de la destination)
     */
    private final String nom;
    /**
     * Compteur du nombre de destinations instanciées (utilisé pour donner
     * automatiquement un id unique à chaque destination)
     * Vous ne devez pas toucher à cet attribut qui est utilisé pour les interfaces
     * (console et web)
     */
    private static int compteur = 1;

    public Destination(List<String> villes, int valeurSimple, int valeurMax, int penalite) {
        this.villes = villes;
        this.valeurSimple = valeurSimple;
        this.valeurMax = valeurMax;
        this.penalite = penalite;
        this.nom = "D" + compteur++;
    }

    public Destination(String ville1, String ville2, int valeur) {
        this(new ArrayList<>(List.of(ville1, ville2)), valeur, valeur, valeur);
    }


    public String toString() {
        StringJoiner sj = new StringJoiner(" - ");
        for (String ville : villes) {
            sj.add(ville);
        }
        if (valeurSimple == valeurMax) {
            return String.format("%s (%d)", sj, valeurSimple);
        } else {
            return String.format("%s (%d, %d, %d)", sj, valeurSimple, valeurMax, penalite);
        }
    }

    /**
     * @return une liste contenant toutes les destinations du jeu
     */
    public static ArrayList<Destination> makeDestinationsMonde() {
        ArrayList<Destination> destinations = new ArrayList<>();
        destinations.add(new Destination("Mumbai", "Beijing", 6)); // D1
        destinations.add(new Destination("Bangkok", "Tokyo", 6)); // D2
        destinations.add(new Destination("Lima", "Jakarta", 14)); // D3
        destinations.add(new Destination("Moskva", "Hong Kong", 13)); // D4
        destinations.add(new Destination("Marseille", "Beijing", 14)); // D5
        destinations.add(new Destination("Buenos Aires", "Sydney", 13)); // D6
        destinations.add(new Destination("Buenos Aires", "Marseille", 18)); // D7
        destinations.add(new Destination("Vancouver", "Miami", 9)); // D8
        destinations.add(new Destination("Djibouti", "Lahore", 7)); // D9
        destinations.add(new Destination("Mexico", "Mumbai", 15)); // D10
        destinations.add(new Destination("Mexico", "New York", 11)); // D11
        destinations.add(new Destination("Rio de Janeiro", "Hamburg", 18)); // D12
        destinations.add(new Destination("New York", "Cape Town", 19)); // D13
        destinations.add(new Destination("Edinburgh", "Sydney", 25)); // D14
        destinations.add(new Destination("Edinburgh", "Tokyo", 22)); // D15
        destinations.add(new Destination("Winnipeg", "Perth", 14)); // D16
        destinations.add(new Destination("Jakarta", "Sydney", 7)); // D17
        destinations.add(new Destination("Caracas", "Al-Zahira", 13)); // D18
        destinations.add(new Destination("Hong Kong", "Jakarta", 5)); // D19
        destinations.add(new Destination("Casablanca", "Vakutsk", 16)); // D20
        destinations.add(new Destination("Athina", "Manila", 14)); // D21
        destinations.add(new Destination("Cape Town", "Jakarta", 13)); // D22
        destinations.add(new Destination("Marseille", "Al-Zahira", 5)); // D23
        destinations.add(new Destination("Moskva", "Petropavlovsk", 15)); // D24
        destinations.add(new Destination("Hamburg", "Beijing", 13)); // D25
        destinations.add(new Destination("Moskva", "Toamasina", 11)); // D26
        destinations.add(new Destination("Lagos", "Tehran", 10)); // D27
        destinations.add(new Destination("Reykjavik", "Mumbai", 13)); // D28
        destinations.add(new Destination("Hamburg", "Dar Es Salaam", 8)); // D29
        destinations.add(new Destination("Caracas", "Athina", 12)); // D30
        destinations.add(new Destination("Tokyo", "Sydney", 11)); // D31
        destinations.add(new Destination("Al-Zahira", "Sydney", 19)); // D32
        destinations.add(new Destination("Los Angeles", "Jakarta", 11)); // D33
        destinations.add(new Destination("Marseille", "Jakarta", 18)); // D34
        destinations.add(new Destination("Miami", "Buenos Aires", 9)); // D35
        destinations.add(new Destination("Rio de Janeiro", "Perth", 17)); // D36
        destinations.add(new Destination("Valparaiso", "Rio de Janeiro", 6)); // D37
        destinations.add(new Destination("Los Angeles", "Hamburg", 14)); // D38
        destinations.add(new Destination("Edinburgh", "Hong Kong", 17)); // D39
        destinations.add(new Destination("New York", "Marseille", 10)); // D40
        destinations.add(new Destination("Rio de Janeiro", "Tokyo", 20)); // D41
        destinations.add(new Destination("New York", "Sydney", 17)); // D42
        destinations.add(new Destination("Los Angeles", "Rio de Janeiro", 15)); // D43
        destinations.add(new Destination("Casablanca", "Honolulu", 16)); // D44
        destinations.add(new Destination("Miami", "Moskva", 13)); // D45
        destinations.add(new Destination("Buenos Aires", "Manila", 17)); // D46
        destinations.add(new Destination("Los Angeles", "Dar Es Salaam", 17)); // D47
        destinations.add(new Destination("Dar Es Salaam", "Tokyo", 15)); // D48
        destinations.add(new Destination("New York", "Tokyo", 15)); // D49
        destinations.add(new Destination("New York", "Mumbai", 19)); // D50
        destinations.add(new Destination("Novosibirsk", "Darwin", 13)); // D51
        destinations.add(new Destination("Rio de Janeiro", "Dar Es Salaam", 11)); // D52
        destinations.add(new Destination("Mexico", "Beijing", 13)); // D53
        destinations.add(new Destination("Edinburgh", "Luanda", 10)); // D54
        destinations.add(new Destination("Marseille", "Christchurch", 23)); // D55
        destinations.add(new Destination("Vancouver", "Edinburgh", 13)); // D56
        destinations.add(new Destination("Lagos", "Hong Kong", 14)); // D57
        destinations.add(new Destination(List.of("Manila", "Honolulu", "Port Moresby", "Darwin"), 9, 13, 19)); // D58
        destinations.add(new Destination(List.of("Anchorage", "Cambridge Bay", "Reykjavik", "Murmansk", "Tiksi"), 23, 34, 40)); // D59
        destinations.add(new Destination(List.of("Anchorage", "Vancouver", "Winnipeg", "Cambridge Bay"), 12, 18, 24)); // D60
        destinations.add(new Destination(List.of("Casablanca", "Al-Qahira", "Tehran"), 6, 9, 15)); // D61
        destinations.add(new Destination(List.of("Mexico", "Caracas", "Lima", "Valparaiso"), 10, 15, 21)); // D62
        destinations.add(new Destination(List.of("Lagos", "Luanda", "Dar Es Salaam", "Djibouti"), 6, 9, 15)); // D63
        destinations.add(new Destination(List.of("Tehran", "Lahore", "Mumbai", "Bangkok"), 9, 13, 19)); // D64
        destinations.add(new Destination(List.of("Murmansk", "Tiksi", "Novosibirsk", "Yakutsk", "Petropavlovsk"), 20, 30, 36)); // D65
        return destinations;
    }
}