package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.CarteTransport;
import fr.umontpellier.iut.rails.data.Couleur;
import fr.umontpellier.iut.rails.data.TypeCarteTransport;
import fr.umontpellier.iut.rails.data.Ville;

import java.util.ArrayList;
import java.util.List;

public class RouteTerrestre extends Route {
    public RouteTerrestre(Ville ville1, Ville ville2, Couleur couleur, int longueur) {
        super(ville1, ville2, couleur, longueur);
    }

    /** Vérifie si, à partir de son jeu, le joueur peut payer pour construire la route terrestre passée en paramètre
     * @param joueur
     * @return si le joueur peut payer la route
     */

    @Override
    public boolean peutPayerRoute(Joueur joueur) {
        //s'il y a plus de 4 joueurs et la route est double et qu'il a déjà l'autre on l'accepte pas
        if(joueur.getNbJoueursJeu() >=4) {
            if (this.getProprio()==null) {
                if(this.getRouteParallele()!=null && this.getRouteParallele().getProprio()!=null) {
                    if (this.getRouteParallele().getProprio().equals(joueur)) {
                        return false;
                    }
                }
            }
        }
        //s'il n'a pas assez de pions
        if(joueur.getNbPionsWagon()<this.getLongueur()){ return false;}
        Couleur couleurRoute = this.getCouleur();
        int tailleRoute = this.getLongueur();
        Couleur[] couleurs = {Couleur.BLANC, Couleur.JAUNE, Couleur.VERT, Couleur.ROUGE, Couleur.VIOLET, Couleur.NOIR};

        boolean estWagon;
        boolean estJoker;
        boolean valide = false;

        int compteurCartesValides = 0;



        // Vérifie si le joueur a assez de pions pour capturer la route
        if(joueur.getNbPionsWagon() >= tailleRoute) {
            // Si la route est une grise non paire on vérifie si le joueur peut la capturer avec au moins une de ses couleurs + jokers
            if (couleurRoute.equals(Couleur.GRIS) ) {
                for (Couleur couleur : couleurs) {
                    for (CarteTransport carte : joueur.getCartesTransport()) {
                        estJoker = carte.getType().equals(TypeCarteTransport.JOKER);
                        estWagon = carte.getType().equals(TypeCarteTransport.WAGON);
                        if (estJoker || (estWagon && carte.getCouleur().equals(couleur))) {
                            compteurCartesValides++;
                        }
                    }
                    if ((compteurCartesValides >= tailleRoute)) {
                        valide = true;
                    }
                    compteurCartesValides = 0;
                }
            }
            // Si la route a une couleur, vérifie si a assez de wagons de la couleur / jokers pour le capturer
            else {
                for (CarteTransport carte : joueur.getCartesTransport()) {
                    estJoker = carte.getType().equals(TypeCarteTransport.JOKER);
                    estWagon = carte.getType().equals(TypeCarteTransport.WAGON);
                    if (estJoker || (estWagon && carte.getCouleur().equals(couleurRoute))) {
                        compteurCartesValides++;
                    }
                }
                if (compteurCartesValides >= tailleRoute) {
                    valide = true;
                }
            }
        }
        return valide;
    }

    @Override
    public void payerRoute(Joueur joueur) {
        if(this.getCouleur().equals(Couleur.GRIS)){
            ArrayList<Couleur> couleursPossible = couleursPossiblesRouteGrise(joueur);
            int tailleRoute = this.getLongueur();
            List<String> carteEnMain = new ArrayList<>();
            Couleur nvlCouleurRoute = null;
            for (CarteTransport carte : joueur.getCartesTransport()) {
                carteEnMain.add(carte.getNom());
            }
            String choix = "";
            CarteTransport carteChoisie = null;
            boolean estBonWagon= false;

            while(joueur.getCartesTransportPosees().size() <tailleRoute && !estBonWagon){
                choix = joueur.choisir("Choisir une carte à utiliser: ", carteEnMain, null, false);
                carteChoisie = joueur.carteTransportNomVersCarte(choix);
                boolean estJoker = carteChoisie.getType().equals(TypeCarteTransport.JOKER);
                if(estJoker){
                    joueur.ajouteCarteTransportPosee(carteChoisie);
                    joueur.retireCarteTransport(carteChoisie);
                }
                else if (carteChoisie.getType().equals(TypeCarteTransport.WAGON)){
                    if(couleursPossible.contains(carteChoisie.getCouleur())) {
                        estBonWagon = true;
                        nvlCouleurRoute = carteChoisie.getCouleur();
                        joueur.ajouteCarteTransportPosee(carteChoisie);
                        joueur.retireCarteTransport(carteChoisie);
                    }
                }
            }
            while(joueur.getCartesTransportPosees().size() <tailleRoute){
                choix = joueur.choisir("Choisir une carte à utiliser: ", carteEnMain, null, false);
                carteChoisie = joueur.carteTransportNomVersCarte(choix);
                boolean estJoker = carteChoisie.getType().equals(TypeCarteTransport.JOKER);
                if(estJoker){
                    joueur.ajouteCarteTransportPosee(carteChoisie);
                    joueur.retireCarteTransport(carteChoisie);
                }
                else if(carteChoisie.getType().equals(TypeCarteTransport.WAGON) && carteChoisie.getCouleur().equals(nvlCouleurRoute)){
                    joueur.ajouteCarteTransportPosee(carteChoisie);
                    joueur.retireCarteTransport(carteChoisie);
                }
            }
        }
        else {

            int tailleRoute = this.getLongueur();
            List<String> carteEnMain = new ArrayList<>();
            for (CarteTransport carte : joueur.getCartesTransport()) {
                carteEnMain.add(carte.getNom());
            }


            while (joueur.getCartesTransportPosees().size() < tailleRoute) {
                String choix = joueur.choisir("Choisir une carte à utiliser: ", carteEnMain, null, false);
                CarteTransport carteChoisie = joueur.carteTransportNomVersCarte(choix);
                boolean estJoker = carteChoisie.getType().equals(TypeCarteTransport.JOKER);
                if (estJoker) {
                    joueur.ajouteCarteTransportPosee(carteChoisie);
                    joueur.retireCarteTransport(carteChoisie);
                } else {
                    if (carteChoisie.getType().equals(TypeCarteTransport.WAGON) && carteChoisie.getCouleur().equals(this.getCouleur())) {
                        joueur.ajouteCarteTransportPosee(carteChoisie);
                        joueur.retireCarteTransport(carteChoisie);
                    }
                }
            }
        }
        joueur.ajouteRoute(this);
        joueur.retireRoutesDeslibres(this);
        joueur.defausserCarteDansBonPaquet(joueur.getCartesTransportPosees());
        joueur.ajouteAuScore(this.getScore());
        joueur.retirePionsWagons(this.getLongueur());
        this.setProprio(joueur);
        if(joueur.getNbJoueursJeu()==2 || joueur.getNbJoueursJeu()==3){
            if(this.getRouteParallele()!=null){
                joueur.retireRoutesDeslibres(this.getRouteParallele());
            }
        }

    }

    ArrayList<Couleur> couleursPossiblesRouteGrise(Joueur joueur){
        int tailleRoute = this.getLongueur();
        ArrayList<Couleur> couleursPossibles = new ArrayList<>();
        int[] nbParCouleur = {0, 0, 0, 0, 0, 0};
        Couleur[] couleurs = {Couleur.BLANC, Couleur.JAUNE, Couleur.VERT, Couleur.ROUGE, Couleur.VIOLET, Couleur.NOIR};
        int nbJokers = 0;
        for(CarteTransport carte : joueur.getCartesTransport()){
            if(carte.getType().equals(TypeCarteTransport.JOKER)){
                nbJokers++;
            } else if (carte.getType().equals(TypeCarteTransport.WAGON)) {
                for(int i = 0; i < couleurs.length; i++){
                    if(carte.getCouleur().equals(couleurs[i])){
                        nbParCouleur[i]++;
                    }
                }
            }
        }
        for (int i = 0; i < nbParCouleur.length; i++) {
            if(nbParCouleur[i]+nbJokers>=tailleRoute){
                couleursPossibles.add(couleurs[i]);
            }
        }
        return couleursPossibles;
    }




}
