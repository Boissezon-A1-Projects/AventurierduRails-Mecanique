package fr.umontpellier.iut.rails;

public record Bouton(
        String label,
        String valeur) {

    public Bouton(String valeur) {
        this(valeur, valeur);
    }

    public String toPrompt() {
        if (label.equals(valeur)) {
            return valeur;
        } else {
            return label + " (" + valeur + ")";
        }
    }
}