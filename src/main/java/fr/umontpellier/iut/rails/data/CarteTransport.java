package fr.umontpellier.iut.rails.data;

public final class CarteTransport implements Comparable<CarteTransport> {

    /**
     * Compteur du nombre de routes instanciées (utilisé pour donner automatiquement
     * un id unique à chaque route)
     */

    private static int compteur = 1;
    private final TypeCarteTransport type;
    private final Couleur couleur;
    private final boolean estDouble;
    private final boolean ancre;
    private final String nom;

    public CarteTransport(TypeCarteTransport type, Couleur couleur, boolean estDouble, boolean ancre) {
        this.type = type;
        this.couleur = couleur;
        this.estDouble = estDouble;
        this.ancre = ancre;
        this.nom = "C" + compteur++;
    }

    public TypeCarteTransport getType() {
        return type;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public boolean estDouble() {
        return estDouble;
    }

    public boolean getAncre() {
        return ancre;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public String toString() {
        String label;
        if (type == TypeCarteTransport.JOKER) {
            label = "Joker";
        } else if (type == TypeCarteTransport.BATEAU) {
            if (estDouble) {
                label = "Double";
            } else {
                label = "Bateau";
            }
        } else {
            label = "Wagon";
        }
        return String.format("%s %s (%s)", label, couleur.name(), nom);
    }

    public String toLog() {
        String label;
        if (type == TypeCarteTransport.JOKER) {
            label = "Joker";
        } else if (type == TypeCarteTransport.BATEAU) {
            if (estDouble) {
                label = "Double";
            } else {
                label = "Bateau";
            }
        } else {
            label = "Wagon";
        }

        return String.format(
                "<img class=\"couleur\" src=\"images/symbole-%s.png\"><span class=\"nom-carte %s %s\">%s</span>",
                couleur.name(), type.name().toLowerCase(), couleur.name().toLowerCase(), label);
    }

    @Override
    public int compareTo(CarteTransport carte) {
        if (getType() != carte.getType()) {
            return getType().compareTo(carte.getType());
        }
        if (getCouleur() != carte.getCouleur()) {
            return getCouleur().compareTo(carte.getCouleur());
        }
        if (estDouble() != carte.estDouble()) {
            return Boolean.compare(estDouble(), carte.estDouble());
        }
        if (getAncre() != carte.getAncre()) {
            return Boolean.compare(getAncre(), carte.getAncre());
        }
        return getNom().compareTo(carte.getNom());
    }
}
