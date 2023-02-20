package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.Couleur;
import fr.umontpellier.iut.rails.data.Ville;

public class RoutePaire extends Route {
    public RoutePaire(Ville ville1, Ville ville2, int longueur) {
        super(ville1, ville2, Couleur.GRIS, longueur);
    }
}
