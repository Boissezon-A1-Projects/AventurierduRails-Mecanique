package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.CarteTransport;
import fr.umontpellier.iut.rails.data.Couleur;
import fr.umontpellier.iut.rails.data.TypeCarteTransport;
import fr.umontpellier.iut.rails.data.Ville;

import java.util.ArrayList;
import java.util.List;

public class RouteMaritime extends Route {
    public RouteMaritime(Ville ville1, Ville ville2, Couleur couleur, int longueur) {
        super(ville1, ville2, couleur, longueur);
    }

    /**
     * Vérifie si, à partir de son jeu, le joueur peut payer pour construire la route maritime passée en paramètre
     * @param joueur
     * @return si le joueur peut payer la route
     */
    @Override
    public boolean peutPayerRoute(Joueur joueur){
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
        if(joueur.getNbPionsBateau()<this.getLongueur()){ return false;}

        Couleur couleurRoute = this.getCouleur();
        int tailleRoute = this.getLongueur();
        Couleur[] couleurs = {Couleur.BLANC, Couleur.JAUNE, Couleur.VERT, Couleur.ROUGE, Couleur.VIOLET, Couleur.NOIR};

        boolean estBateau;
        boolean estBateauDouble;
        boolean estJoker;
        boolean valide = false;

        int compteurCartesValides = 0;

        if(joueur.getNbPionsBateau() >= tailleRoute) {
            if (couleurRoute.equals(Couleur.GRIS)) {
                for (Couleur couleur : couleurs) {
                    for (CarteTransport carte : joueur.getCartesTransport()) {
                        estJoker = carte.getType().equals(TypeCarteTransport.JOKER);
                        estBateau = carte.getType().equals(TypeCarteTransport.BATEAU);
                        estBateauDouble = carte.estDouble();
                        if (estJoker || (estBateau && carte.getCouleur().equals(couleur))) {
                            if(estBateauDouble){
                                compteurCartesValides++;
                            }
                            compteurCartesValides++;
                        }
                    }
                    if ((compteurCartesValides >= tailleRoute)){
                        valide = true;
                    }
                    compteurCartesValides = 0;
                }
            } else {
                for (CarteTransport carte : joueur.getCartesTransport()) {
                    estJoker = carte.getType().equals(TypeCarteTransport.JOKER);
                    estBateau = carte.getType().equals(TypeCarteTransport.BATEAU);
                    estBateauDouble = carte.estDouble();
                    if (estJoker || (estBateau && carte.getCouleur().equals(couleurRoute))) {
                        if(estBateauDouble){
                            compteurCartesValides++;
                        }
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
        boolean estValide = false;
        String nomCarteChoisie;
        CarteTransport carteChoisie;
        ArrayList<String> listeChoixPossibles = new ArrayList<>();
        List<CarteTransport> carteTransportsPosees = joueur.getCartesTransportPosees();

        ArrayList<Couleur> couleursPossiblesGrise = new ArrayList<>();
        boolean routeEstGrise = this.getCouleur().equals(Couleur.GRIS);
        boolean aChoisiUneCouleur = false;
        Couleur couleurChoisieRouteGrise = Couleur.GRIS;
        if(routeEstGrise){
            couleursPossiblesGrise = couleursPossiblesRouteMaritimeGrise(joueur);
        }



        int nbSimplesPoses = 0;
        int nbJokersPoses = 0;

        for(CarteTransport carte: joueur.getCartesTransport()){
            listeChoixPossibles.add(carte.getNom());
        }

        int valeurPosee = 0;
        while(valeurPosee < this.getLongueur()){
            while (!estValide) {
                nomCarteChoisie = joueur.choisir("Choisissez vos cartes à utiliser pour construire la route :", listeChoixPossibles, null, false);
                carteChoisie = joueur.carteTransportNomVersCarte(nomCarteChoisie);
                boolean estJoker = carteChoisie.getType().equals(TypeCarteTransport.JOKER);
                boolean estBateau = carteChoisie.getType().equals(TypeCarteTransport.BATEAU);
                Couleur couleurCarteChoisie = carteChoisie.getCouleur();
                boolean longueurPaire = this.getLongueur() % 2 == 0;


                int nbJokers = 0;
                int nbSimples = 0;

                for(CarteTransport carte : joueur.getCartesTransport()){
                    if (carte.getType().equals(TypeCarteTransport.BATEAU)) {
                        if(routeEstGrise && carte.getCouleur().equals(couleurCarteChoisie) && !estJoker){
                            if (!carte.estDouble()) {
                                nbSimples++;
                            }
                        }
                        else if (carte.getCouleur().equals(this.getCouleur())) {
                            if (!carte.estDouble()) {
                                nbSimples++;
                            }
                        }
                    }
                    else if (!carte.getType().equals(TypeCarteTransport.WAGON)) {
                        nbJokers++;
                    }
                }


                // Si on a un nombre pair de simples poses et qu'on veut en poser un sur une route de longueur paire, il faut qu'on en ait un deuxieme ou un joker
                if((couleurCarteChoisie.equals(this.getCouleur()) && estBateau) || estJoker
                        || (routeEstGrise && !aChoisiUneCouleur && couleursPossiblesGrise.contains(couleurCarteChoisie))
                        || (routeEstGrise && aChoisiUneCouleur && couleurCarteChoisie.equals(couleurChoisieRouteGrise))){
                    if(!carteChoisie.estDouble()){
                        if(nbSimplesPoses + nbJokersPoses % 2 == 0 && longueurPaire){
                            if(nbSimples + nbJokers >= 2){
                                if(estJoker){
                                    nbJokersPoses++;
                                }
                                else {
                                    if(!aChoisiUneCouleur){
                                        aChoisiUneCouleur = true;
                                        couleurChoisieRouteGrise = couleurCarteChoisie;
                                    }
                                    nbSimplesPoses++;
                                }
                                estValide = true;
                                valeurPosee++;
                            }
                        }
                        // Si on a un nombre impair de simples poses et qu'on veut en poser un sur une route de longueur impaire, il faut qu'on en ait un deuxieme ou un joker
                        else if(nbSimplesPoses + nbJokersPoses % 2 == 1 && !longueurPaire){
                            if(nbSimples + nbJokers >= 2){
                                if(estJoker){
                                    nbJokersPoses++;
                                }
                                else {
                                    if(!aChoisiUneCouleur){
                                        aChoisiUneCouleur = true;
                                        couleurChoisieRouteGrise = couleurCarteChoisie;
                                    }
                                    nbSimplesPoses++;
                                }
                                estValide = true;
                                valeurPosee++;
                            }
                        }
                        else{
                            estValide = true;
                            if(estJoker){
                                nbJokersPoses++;
                            }
                            else {
                                if(!aChoisiUneCouleur){
                                    aChoisiUneCouleur = true;
                                    couleurChoisieRouteGrise = couleurCarteChoisie;
                                }
                                nbSimplesPoses++;
                            }
                            valeurPosee++;
                        }
                    }
                    else{
                        if((nbSimplesPoses + nbJokersPoses >= 1 && (nbSimples + nbJokers >0) && (valeurPosee + 1 == this.getLongueur()))){
                            estValide = false;
                        }
                        else if(routeEstGrise && aChoisiUneCouleur && couleurCarteChoisie.equals(couleurChoisieRouteGrise)){
                            estValide = true;
                            valeurPosee += 2;
                        }
                        else if(routeEstGrise && !aChoisiUneCouleur && couleursPossiblesGrise.contains(couleurCarteChoisie)){
                            estValide = true;
                            valeurPosee += 2;
                            aChoisiUneCouleur = true;
                            couleurChoisieRouteGrise = couleurCarteChoisie;
                        }
                        else{

                            estValide = true;
                            valeurPosee += 2;
                        }
                    }
                }
                if(estValide){
                    joueur.ajouteCarteTransportPosee(carteChoisie);
                    joueur.retireCarteTransport(carteChoisie);
                }
            }
            estValide = false;
        }
        joueur.ajouteRoute(this);
        joueur.retireRoutesDeslibres(this);
        joueur.defausserCarteDansBonPaquet(joueur.getCartesTransportPosees());
        joueur.ajouteAuScore(this.getScore());
        joueur.retirePionsBateau(this.getLongueur());
        this.setProprio(joueur);
        if(joueur.getNbJoueursJeu()==2 || joueur.getNbJoueursJeu()==3){
            if(this.getRouteParallele()!=null){
                joueur.retireRoutesDeslibres(this.getRouteParallele());
            }
        }
    }

    public ArrayList<Couleur> couleursPossiblesRouteMaritimeGrise(Joueur joueur){
        int tailleRoute = this.getLongueur();
        Couleur[] couleurs = {Couleur.BLANC, Couleur.JAUNE, Couleur.VERT, Couleur.ROUGE, Couleur.VIOLET, Couleur.NOIR};

        boolean estBateau;
        boolean estBateauDouble;
        boolean estJoker;

        ArrayList<Couleur> couleursPossibles = new ArrayList<>();

        int compteurCartesValides = 0;

        for (Couleur couleur : couleurs) {
            for (CarteTransport carte : joueur.getCartesTransport()) {
                estJoker = carte.getType().equals(TypeCarteTransport.JOKER);
                estBateau = carte.getType().equals(TypeCarteTransport.BATEAU);
                estBateauDouble = carte.estDouble();
                if (estJoker || (estBateau && carte.getCouleur().equals(couleur))) {
                    if(estBateauDouble){
                        compteurCartesValides++;
                    }
                    compteurCartesValides++;
                }
            }
            if ((compteurCartesValides >= tailleRoute)){
                couleursPossibles.add(couleur);
            }
            compteurCartesValides = 0;
        }
        return couleursPossibles;
    }
}
