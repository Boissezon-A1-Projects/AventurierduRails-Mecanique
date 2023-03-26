package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.CarteTransport;
import fr.umontpellier.iut.rails.data.Couleur;
import fr.umontpellier.iut.rails.data.TypeCarteTransport;
import fr.umontpellier.iut.rails.data.Ville;

import java.util.ArrayList;
import java.util.List;

public class RoutePaire extends Route {
    public RoutePaire(Ville ville1, Ville ville2, int longueur) {
        super(ville1, ville2, Couleur.GRIS, longueur);
    }
    /**
     * Vérifie si, à partir de son jeu, le joueur peut payer pour construire la route paire passée en paramètre
     * @param joueur courant
     * @return si le joueur peut payer la route
     */
    @Override
    public boolean peutPayerRoute(Joueur joueur){
        //s'il n'a pas assez de pions
        if(joueur.getNbPionsWagon()<this.getLongueur()){ return false;}
        int[] nbParCouleur = {0, 0, 0, 0, 0, 0};
        Couleur[] couleurs = {Couleur.BLANC, Couleur.JAUNE, Couleur.VERT, Couleur.ROUGE, Couleur.VIOLET, Couleur.NOIR};
        int nbPairesPossibles = 0;
        int nbJokers = 0;
        boolean peutPayer = false;
        int longueurRoute = this.getLongueur();


        // Compte combien de wagons de chaque couleur le joueur a en main, ainsi que le nombre de jokers
        for(CarteTransport carte : joueur.getCartesTransport()){
            if(carte.getType().equals(TypeCarteTransport.JOKER)){
                nbJokers++;
            } else if (carte.getType().equals(TypeCarteTransport.WAGON)) {
                for(int i = 0; i < couleurs.length; i++){
                    if(carte.getCouleur().equals(couleurs[i])){
                        nbParCouleur[i]++;
                    }   }   }   }
        // Compte le nombre de paires que le joueur peut faire avec sa main
        // D'abord on fait des paires sans joker, puis si besoin on en ajoute un pour faire une paire
        for(int nb : nbParCouleur){
            while(nb >= 1){
                while(nb > 1) {
                    nb -= 2;
                    nbPairesPossibles++;
                }
                if(nb == 1 && nbJokers != 0){
                    nb--;
                    nbJokers--;
                    nbPairesPossibles++;
                }
                else{
                    break;
                }
            }
        }

        return nbPairesPossibles >= longueurRoute;
    }

    @Override
    public void payerRoute(Joueur joueur) {
        int tailleRoute = this.getLongueur();
        ArrayList<String> cartesEnMain = new ArrayList<>();
        boolean estValide = false;
        int nbJokers = 0;
        int nbJokersReserves = 0;
        List<Couleur> couleurCarteJouee = new ArrayList<>();
        List<Couleur> couleursCarteSeule = couleurCarteSeule(joueur);
        List<CarteTransport> cartesSeules=  new ArrayList<>();
        String nomCarteChoisie="";
        CarteTransport carteChoisie=null;
        int nbPaires =0;
        for (CarteTransport carte : joueur.getCartesTransport()) {
            if (carte.getType().equals(TypeCarteTransport.JOKER)) {
                nbJokers++;
            }
            cartesEnMain.add(carte.getNom());
        }

        boolean skip = false;
        while (joueur.getCartesTransportPosees().size() < tailleRoute * 2) {
            estValide = false;
            skip = false;
            while (!estValide) {
                nomCarteChoisie = joueur.choisir("Choisissez vos cartes à utiliser pour construire la route :", cartesEnMain, null, false);
                carteChoisie = joueur.carteTransportNomVersCarte(nomCarteChoisie);
                boolean estJoker = carteChoisie.getType().equals(TypeCarteTransport.JOKER);
                boolean estWagon = carteChoisie.getType().equals(TypeCarteTransport.WAGON);
                Couleur couleurCarteChoisie = carteChoisie.getCouleur();
                //si la carte est seule et qu'il y a pas de joker alors on peut pas la mettre
                if(couleursCarteSeule.contains(couleurCarteChoisie) && nbJokers==0){
                    break;
                }
                //ça c'est pour check si on a déjà joué une carte de la meme couleur
                if(couleurCarteJouee.contains(couleurCarteChoisie) ){
                    estValide = true; // on met valide à true et on enleve la couleur de carteJouee( qui est une liste avec les couleurs des cartes qui ont deja ete jouee)
                    couleurCarteJouee.remove(carteChoisie.getCouleur());
                    skip=true; // pour ne pas passer la grosse boucle

                }
                if(estJoker){
                    if(cartesSeules.size()>=1){
                        estValide = true; // on met valide a true et on enleve la carte qui est seule et on eneleve sa couleur des cartes jouees et des cartes seules
                        CarteTransport carteCoupleJoker = cartesSeules.remove(0);
                        couleurCarteJouee.remove(carteCoupleJoker.getCouleur());
                        couleursCarteSeule.remove(couleurCarteChoisie);
                    }
                    else{
                        estValide = true;
                        skip = true;
                    }
                }
                // si y a une carte seule qui attend un joker
                if(cartesSeules.size()>=1 && estJoker){


                }
                //si y a un joker qui a été joué et qu'il veut mtn poser une carte seule
                if(couleurCarteJouee.contains(Couleur.GRIS) && couleursCarteSeule.contains(couleurCarteChoisie)){
                    estValide = true; // on met valide a true et on enleve la couleur grise des couleurs des cartes jouees
                    couleurCarteJouee.remove(Couleur.GRIS);
                    nbPaires++;
                    skip = true; // on passe la boucle d'apres
                }

                if (nbPaires < tailleRoute && !skip) {
                    //Parcourt le jeu pour voir si le joueur peut completer la carte qu'il a choisie
                    for (CarteTransport carte : joueur.getCartesTransport()) {
                        if (carte.getType().equals(TypeCarteTransport.JOKER) && nbJokersReserves < nbJokers) { // si la carte des cartes du joueur est un joker
                            // et que le nombre de joker utilisé n'est pas depasser
                            if (estWagon && couleursCarteSeule.contains(couleurCarteChoisie)) { // si la carte choisie est un wagon et qu'elle est seule
                                cartesSeules.add(carteChoisie); // on l'ajoute aux cartes seules (à qui il manque un joker)
                                nbPaires++; // et on augmente le  nbPair parce qu'on sait qu'elle va aps pouvoir etre finie
                            }
                            if (estJoker) { // si la carte choisie est un joker on met la couleur grise dans les cartes qui sont jouees
                                couleurCarteJouee.add(Couleur.GRIS);
                            }

                            nbJokersReserves++; // on augmente le nbdeJoker qui est use
                            estValide = true;

                            couleurCarteJouee.add(couleurCarteChoisie); // on ajoute la carte dans les cartesJouees
                            break;
                        } else if (estWagon
                                && carte.getCouleur().equals(couleurCarteChoisie)
                                && !carte.equals(carteChoisie)) { // si la carte choisie est un wagon, que la carte parcourue à la meme couleur mais que c'est pas la meme carte
                            estValide = true;
                            nbPaires++; // on ajoute le nbPaire car on sait que ça va etre complete

                            couleurCarteJouee.add(couleurCarteChoisie); // on l'ajoute au carte jouée
                            break;
                        }
                    }

                }
                if(estValide){ // si on peut l'ajoute alros on l'a suppr des cartes transport et on l'enleve des cartes Transports posees
                    joueur.ajouteCarteTransportPosee(carteChoisie);
                    joueur.retireCarteTransport(carteChoisie);
                }
            }

        }
        joueur.ajouteRoute(this);
        joueur.retireRoutesDeslibres(this);
        joueur.defausserCarteDansBonPaquet(joueur.getCartesTransportPosees());
        joueur.ajouteAuScore(this.getScore());
        joueur.retirePionsWagons(this.getLongueur());
        this.setProprio(joueur);

    }

    public List<Couleur> couleurCarteSeule(Joueur joueur){
        int[] nbParCouleur= {0,0,0,0,0,0};
        List<Couleur> couleurCarteSeule = new ArrayList<>();
        Couleur[] couleurs = {Couleur.BLANC, Couleur.JAUNE, Couleur.VERT, Couleur.ROUGE, Couleur.VIOLET, Couleur.NOIR};
        for (CarteTransport carte : joueur.getCartesTransportPosees()) {
            if(carte.getType().equals(TypeCarteTransport.WAGON)){
                for(int i = 0; i < couleurs.length; i++){
                    if(carte.getCouleur().equals(couleurs[i])){
                        nbParCouleur[i]++;
                    }
                }
            }
        }
        for (int i = 0; i < nbParCouleur.length; i++) {
            if(nbParCouleur[i]==1){
                couleurCarteSeule.add(couleurs[i]);
            }
        }
        return couleurCarteSeule;
    }
  }




