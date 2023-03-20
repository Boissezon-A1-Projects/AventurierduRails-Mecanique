package fr.umontpellier.iut.rails;

import com.google.gson.Gson;
import fr.umontpellier.iut.gui.GameServer;
import fr.umontpellier.iut.rails.data.*;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Jeu implements Runnable {
    /**
     * Liste des joueurs
     */
    private final List<Joueur> joueurs;
    /**
     * Le joueur dont c'est le tour
     */
    private Joueur joueurCourant;
    /**
     * Liste des villes disponibles sur le plateau de jeu
     */
    private final List<Ville> portsLibres;
    /**
     * Liste des routes disponibles sur le plateau de jeu
     */
    private final List<Route> routesLibres;
    /**
     * Pile de pioche et défausse des cartes wagon
     */
    private final PilesCartesTransport pilesDeCartesWagon;
    /**
     * Pile de pioche et défausse des cartes bateau
     */
    private final PilesCartesTransport pilesDeCartesBateau;
    /**
     * Cartes de la pioche face visible (normalement il y a 6 cartes face visible)
     */
    private final List<CarteTransport> cartesTransportVisibles;
    /**
     * Pile des cartes "Destination"
     */
    private final List<Destination> pileDestinations;
    /**
     * File d'attente des instructions recues par le serveur
     */
    private final BlockingQueue<String> inputQueue;
    /**
     * Messages d'information du jeu
     */
    private final List<String> log;

    private String instruction;
    private Collection<Bouton> boutons;

    public Jeu(String[] nomJoueurs) {
        // initialisation des entrées/sorties
        inputQueue = new LinkedBlockingQueue<>();
        log = new ArrayList<>();

        // création des villes et des routes
        Plateau plateau = Plateau.makePlateauMonde();
        portsLibres = plateau.getPorts();
        routesLibres = plateau.getRoutes();

        // création des piles de pioche et défausses des cartes Transport (wagon et
        // bateau)
        ArrayList<CarteTransport> cartesWagon = new ArrayList<>();
        ArrayList<CarteTransport> cartesBateau = new ArrayList<>();
        for (Couleur c : Couleur.values()) {
            if (c == Couleur.GRIS) {
                continue;
            }
            for (int i = 0; i < 4; i++) {
                // Cartes wagon simples avec une ancre
                cartesWagon.add(new CarteTransport(TypeCarteTransport.WAGON, c, false, true));
            }
            for (int i = 0; i < 7; i++) {
                // Cartes wagon simples sans ancre
                cartesWagon.add(new CarteTransport(TypeCarteTransport.WAGON, c, false, false));
            }
            for (int i = 0; i < 4; i++) {
                // Cartes bateau simples (toutes avec une ancre)
                cartesBateau.add(new CarteTransport(TypeCarteTransport.BATEAU, c, false, true));
            }
            for (int i = 0; i < 6; i++) {
                // Cartes bateau doubles (toutes sans ancre)
                cartesBateau.add(new CarteTransport(TypeCarteTransport.BATEAU, c, true, false));
            }
        }
        for (int i = 0; i < 14; i++) {
            // Cartes wagon joker
            cartesWagon.add(new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true));
        }
        pilesDeCartesWagon = new PilesCartesTransport(cartesWagon);
        pilesDeCartesBateau = new PilesCartesTransport(cartesBateau);

        // création de la liste pile de cartes transport visibles
        // (les cartes seront retournées plus tard, au début de la partie dans run())
        cartesTransportVisibles = new ArrayList<>();

        // création des destinations
        pileDestinations = Destination.makeDestinationsMonde();
        Collections.shuffle(pileDestinations);

        // création des joueurs
        ArrayList<Joueur.CouleurJouer> couleurs = new ArrayList<>(Arrays.asList(Joueur.CouleurJouer.values()));
        Collections.shuffle(couleurs);
        joueurs = new ArrayList<>();
        for (String nomJoueur : nomJoueurs) {
            joueurs.add(new Joueur(nomJoueur, this, couleurs.remove(0)));
        }
        this.joueurCourant = joueurs.get(0);
    }

    public List<Joueur> getJoueurs() {
        return joueurs;
    }

    public List<Ville> getPortsLibres() {
        return new ArrayList<>(portsLibres);
    }

    public List<Route> getRoutesLibres() {
        return new ArrayList<>(routesLibres);
    }

    public List<CarteTransport> getCartesTransportVisibles() {
        return new ArrayList<>(cartesTransportVisibles);
    }

    /** faite par nous*/
    public List<Destination> getPileDestinations() { return pileDestinations; }

    /**
     * Exécute la partie
     * C'est cette méthode qui est appelée pour démarrer la partie. Elle doit intialiser le jeu
     * (retourner les cartes transport visibles, puis demander à chaque joueur de choisir ses destinations initiales
     * et le nombre de pions wagon qu'il souhaite prendre) puis exécuter les tours des joueurs en appelant la
     * méthode Joueur.jouerTour() jusqu'à ce que la condition de fin de partie soit réalisée.
     */
    public void run() {
        // IMPORTANT : Le corps de cette fonction est à réécrire entièrement
        // Un exemple très simple est donné pour illustrer l'utilisation de certaines méthodes
        // Donne la main de départ à tous les joueurs (bateaux et wagons)
        distribuerMainDepart();

        // Retourne 3 cartes de chaque paquet sur la table face visible
        for(int i = 0; i < 3; i++) {
            cartesTransportVisibles.add(piocherCarteWagon());
            cartesTransportVisibles.add(piocherCarteBateau());
        }


        for (Joueur j : joueurs) {
            joueurCourant = j;
            distribuerDestionations(j);
            donnerPions(j);
        }
        // Corps du jeu, on joue jusqu'à qu'un joueur ait moins de 6 pions
        boolean finDuJeu = false;
        int indiceJoueurDeclencheurFinDuJeu = 0;
        while(!finDuJeu) {
            for(int i = 0; i < joueurs.size(); i++){
                joueurCourant = joueurs.get(i);
                joueurCourant.jouerTour();
                finDuJeu = verfierReserve(joueurCourant);
                // On garde l'indice du déclencheur en mémoire
                if(finDuJeu){
                    indiceJoueurDeclencheurFinDuJeu = i;
                    break;
                }
            }
        }

        // On fait passer deux tours après le déclenchement de la fin
        // à partir du joueur suivant le déclencheur
        int compteurToursPasses = 0;
        while(compteurToursPasses != 2) {
            for (int i = indiceJoueurDeclencheurFinDuJeu + 1; i < joueurs.size(); i++) {
                joueurCourant = joueurs.get(i);
                joueurCourant.jouerTour();
                if(i == indiceJoueurDeclencheurFinDuJeu){
                    compteurToursPasses ++;
                    if (compteurToursPasses == 2) {
                        break;
                    }
                }
            }
        }







        // Fin de la partie
        prompt("Fin de la partie.", new ArrayList<>(), true);
    }

    /**
     * (Fonction faite par nous)
     * Distribue la main de départ de chacun des joueurs (7 Bateaux et 3 Wagons)
     * @return rien
     */
    public void distribuerMainDepart(){
        for (Joueur joueurCourant : joueurs) {
            for(int i = 0; i < 7; i++) {
                joueurCourant.ajouterCarteEnMain(piocherCarteBateau());
            }
            for(int i = 0; i < 3; i++) {
                joueurCourant.ajouterCarteEnMain(piocherCarteWagon());
            }
        }
    }


    public Ville nomVilleToVille(String nomVille){
        for(Ville ville: portsLibres){
            if(ville.toString().equals(nomVille)){
                return ville;
            }
        }
        return null;
    }

    public boolean verfierReserve(Joueur joueur){
        if(joueur.getNbPionsBateau() + joueur.getNbPionsWagon() <= 6){
            return true;
        }
        return false;
    }

    public void distribuerDestionations(Joueur joueurCourant){
        //Distribue les 5 cartes au joueur
        int compteur = 0;
        boolean passer = false;
        ArrayList<String> listeChoixNom = new ArrayList<>();
        String nomCarteChoisie;
        for (int i = 0; i < 5; i++) {
            Destination destinationAjoutee = piocherDestination();
            listeChoixNom.add(destinationAjoutee.getNom());
            joueurCourant.ajouterDestination(destinationAjoutee);
        }
        while(compteur < 2 && !passer) {
            if(compteur == 0) {
                nomCarteChoisie = joueurCourant.choisir("Choisissez un nom de carte à défausser, ou passez.\n" +
                        "Choix :" + listeChoixNom.get(0) + " " + listeChoixNom.get(1) + " " + listeChoixNom.get(2) + " " + listeChoixNom.get(3) + " " + listeChoixNom.get(4), listeChoixNom, null, true);
            }
            else{
                nomCarteChoisie = joueurCourant.choisir("Choisissez un nom de carte à défausser, ou passez.\n" +
                        "Choix :" + listeChoixNom.get(0) + " " + listeChoixNom.get(1) + " " + listeChoixNom.get(2) + " " + listeChoixNom.get(3), listeChoixNom, null, true);
            }
            if (!nomCarteChoisie.equals("")) {
                // Rajoute la carte défaussée à la pile de destinations
                for(int i = 0; i < joueurCourant.getDestinations().size(); i++){
                    if(joueurCourant.getDestinations().get(i).getNom() == nomCarteChoisie){
                        pileDestinations.add(pileDestinations.size(), joueurCourant.getDestinations().get(i));
                    }
                }
                joueurCourant.enleverDestinationId(nomCarteChoisie);
                listeChoixNom.remove(nomCarteChoisie);
                compteur++;
            }
            else{
                passer = true;
            }
        }
    }

    public void donnerPions(Joueur joueur){
        ArrayList<String> reponsesPossibles = new ArrayList<>();
        for(int i = 10; i <= 25; i ++){
            reponsesPossibles.add(String.valueOf(i));
        }
        String nombreWagons;
        nombreWagons = joueur.choisir("Combien voulez vous de pions wagon ? (Maximum 25)\n " +
                                      "Vous pouvez avoir 60 pions wagon ou bateau en tout.",
                                      reponsesPossibles, null, false);
        joueur.updatePions(Integer.parseInt(nombreWagons));
        System.out.println(nombreWagons);
    }

    // Fonction ratée qui fonctionne mais ne passe pas les tests, on va quand
    // même pas la supprimmer après le travail fourni
    /*

    public void distribuerDestinations(){
        // Donne les cartes destinations à chacun
        ArrayList<Destination> listeDesPropositions = new ArrayList<>();
        ArrayList<Bouton> differentsChoix = new ArrayList<>();
        for (Joueur joueurCourant : joueurs) {
            // Créée la liste des 5 propositions pour le joueur et de la liste de boutons
            listeDesPropositions.clear();
            differentsChoix.clear();
            for (int i = 1; i <= 5; i++) {
                Bouton boutonVariable = new Bouton("" + (i));
                differentsChoix.add(boutonVariable);
                listeDesPropositions.add(piocherDestination());
            }

            // Fait faire choisir les 3 premières cartes au joueur (obligatoire) puis un choix entre les 2 dernières (facultatif)
            for (int nbChoixFaits = 0; nbChoixFaits < 5; nbChoixFaits++) {
                String valeurCarteChoisie;
                if (nbChoixFaits < 3) {
                    valeurCarteChoisie = joueurCourant.choisir("Joueur " + joueurCourant.getNom() + " : Choisissez une carte à garder (encore " + (3 - nbChoixFaits) + ")", null, differentsChoix, false);
                } else {
                    if (nbChoixFaits == 3) {
                        Bouton BoutonPasser = new Bouton("Passer");
                        differentsChoix.add(BoutonPasser);
                    }
                    valeurCarteChoisie = joueurCourant.choisir("Joueur " + joueurCourant.getNom() + " :Vous pouvez choisir une carte en plus, ou passer", null, differentsChoix, false);
                }


                if (valeurCarteChoisie.equals("Passer")) {
                    break;
                }
                else {
                    int indice = enleverBoutonDeListeAvecValeur(differentsChoix, valeurCarteChoisie);
                    joueurCourant.ajouterDestination(listeDesPropositions.get(indice));
                    listeDesPropositions.remove(indice);
                }
            }
        }
    }
    */

     /**
     * (Fonction faite par nous)
     * Enleve un bouton d'une liste de boutons dont la valeur est passée en paramètre
     * @return l'indice du bouton retiré
     */
    public int enleverBoutonDeListeAvecValeur(ArrayList<Bouton> listeBoutons, String valeur){
        int tailleListe = listeBoutons.size();
        for(int i = 0; i < tailleListe; i++){
            if(listeBoutons.get(i).label().equals(valeur)){
                listeBoutons.remove(i);
                return i;
            }
        }
        return -1;
    }



    /**
     * (Fonction faite par nous)
     * Renvoie une carte destination aléatoire et la retire de la pile.
     * @return la destination choisie aléatoirement
     */
    public Destination piocherDestination(){
        Destination desti =pileDestinations.get(0);
        pileDestinations.remove(0);
        return desti;
    }

    public void defausserDestination(Destination desti){
        pileDestinations.add(desti);
    }

    /**
     * Pioche une carte de la pile de pioche des cartes wagon.
     *
     * @return la carte qui a été piochée (ou null si aucune carte disponible)
     */
    public CarteTransport piocherCarteWagon() {
        return pilesDeCartesWagon.piocher();
    }

    public boolean piocheWagonEstVide() {
        return pilesDeCartesWagon.estVide();
    }

    public void defausserCarteWagon(CarteTransport carte){ pilesDeCartesWagon.defausser(carte);}

    /**
     * Pioche une carte de la pile de pioche des cartes bateau.
     *
     * @return la carte qui a été piochée (ou null si aucune carte disponible)
     */
    public CarteTransport piocherCarteBateau() {
        return pilesDeCartesBateau.piocher();
    }

    public boolean piocheBateauEstVide() {
        return pilesDeCartesBateau.estVide();
    }

    public void defausserCarteBateau(CarteTransport carte){pilesDeCartesBateau.defausser(carte);   }


    public void ajoutCartePaquetRandomDansCarteVisible(){
        //fonction pour ajouter dans les cartes visibles une carte piochée d'un des deux paquets aléatoirement
        Random random = new Random();
        int nbalea = random.nextInt(2);
        if(nbalea==0){
            cartesTransportVisibles.add(piocherCarteWagon());
        }
        else{
            cartesTransportVisibles.add(piocherCarteBateau());
        }
    }

    public void ajoutCarteDePaquetDemandéDansCarteVisible(String paquet){
        if(paquet.equals("WAGON")){
            cartesTransportVisibles.add(piocherCarteWagon());
        }
        else{
            cartesTransportVisibles.add(piocherCarteBateau());
        }
    }

    public void retireCarteVisible(CarteTransport carte){
        cartesTransportVisibles.remove(carte);
    }

    //return s'il y a 3 joker ou plus dans les cartes visibles
    public boolean verifieCompteJokerCarteVisible(){
        boolean aTropDeJokers = false;
        int compteJoker=0;
        for (CarteTransport carte: cartesTransportVisibles ) {
            if(carte.getType().equals(TypeCarteTransport.JOKER)){
                compteJoker++;
            }
        }
        if(compteJoker>=3){ aTropDeJokers=true;}
        return aTropDeJokers;
    }

    //ajoute 3 cartes de chaque paquet dans carteVisible
    public void ajout3CarteWagon3CarteBateauCarteVisible(String paquet){
        if(paquet.equals("WAGON")){
            for (int i = 0; i < 3; i++) {

                cartesTransportVisibles.add(piocherCarteWagon());
            }
        }
        else{
            for (int i = 0; i < 3; i++) {
                cartesTransportVisibles.add(piocherCarteBateau());

            }
        }

    }

    public void retireCarteVisibleEtDefausseDansBonPaquet(){
        List<CarteTransport> cartevisiblesadefausser = new ArrayList<>();
        for (CarteTransport carte: cartesTransportVisibles ) {
            cartevisiblesadefausser.add(carte);
        }
        for (CarteTransport carte: cartevisiblesadefausser) {
            retireCarteVisible(carte);
            if(carte.getType().equals(TypeCarteTransport.WAGON)||carte.getType().equals(TypeCarteTransport.JOKER)){
                defausserCarteWagon(carte);
            }
            if(carte.getType().equals(TypeCarteTransport.BATEAU)){
                defausserCarteBateau(carte);
            }
        }
    }

    public void changeCarteVisibleSiTropJoker(){
        if(!piocheWagonEstVide() && !piocheBateauEstVide()){
            while(verifieCompteJokerCarteVisible()){
                retireCarteVisibleEtDefausseDansBonPaquet();
                ajout3CarteWagon3CarteBateauCarteVisible("WAGON");
                ajout3CarteWagon3CarteBateauCarteVisible("BATEAU");
            }
        } else if (!(piocheBateauEstVide() && piocheWagonEstVide())) {
            if (piocheWagonEstVide()) {
                while (verifieCompteJokerCarteVisible()) {
                    retireCarteVisibleEtDefausseDansBonPaquet();
                    ajout3CarteWagon3CarteBateauCarteVisible("BATEAU");
                    ajout3CarteWagon3CarteBateauCarteVisible("BATEAU");
                }
            } else if (piocheBateauEstVide()) {
                while (verifieCompteJokerCarteVisible()) {
                    retireCarteVisibleEtDefausseDansBonPaquet();
                    ajout3CarteWagon3CarteBateauCarteVisible("WAGON");
                    ajout3CarteWagon3CarteBateauCarteVisible("WAGON");
                }
            }
        }




    }


    /**
     * Ajoute un message au log du jeu
     */
    public void log(String message) {
        log.add(message);
    }

    /**
     * Ajoute un message à la file d'entrées
     */
    public void addInput(String message) {
        inputQueue.add(message);
    }

    /**
     * Lit une ligne de l'entrée standard
     * C'est cette méthode qui doit être appelée à chaque fois qu'on veut lire
     * l'entrée clavier de l'utilisateur (par exemple dans {@code Player.choisir})
     *
     * @return une chaîne de caractères correspondant à l'entrée suivante dans la
     * file
     */
    public String lireLigne() {
        try {
            return inputQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Envoie l'état de la partie pour affichage aux joueurs avant de faire un choix
     *
     * @param instruction l'instruction qui est donnée au joueur
     * @param boutons     labels des choix proposés s'il y en a
     * @param peutPasser  indique si le joueur peut passer sans faire de choix
     */
    public void prompt(String instruction, Collection<Bouton> boutons, boolean peutPasser) {
        this.instruction = instruction;
        this.boutons = boutons;

        System.out.println();
        System.out.println(this);
        if (boutons.isEmpty()) {
            System.out.printf(">>> %s: %s <<<\n", joueurCourant.getNom(), instruction);
        } else {
            StringJoiner joiner = new StringJoiner(" / ");
            for (Bouton bouton : boutons) {
                joiner.add(bouton.toPrompt());
            }
            System.out.printf(">>> %s: %s [%s] <<<\n", joueurCourant.getNom(), instruction, joiner);
        }
        GameServer.setEtatJeu(new Gson().toJson(dataMap()));
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n");
        for (Joueur j : joueurs) {
            joiner.add(j.toString());
        }
        return joiner.toString();
    }

    public Map<String, Object> dataMap() {
        return Map.ofEntries(
                Map.entry("joueurs", joueurs.stream().map(Joueur::dataMap).toList()),
                Map.entry("joueurCourant", joueurs.indexOf(joueurCourant)),
                Map.entry("piocheWagon", pilesDeCartesWagon.dataMap()),
                Map.entry("piocheBateau", pilesDeCartesBateau.dataMap()),
                Map.entry("cartesTransportVisibles", cartesTransportVisibles),
                Map.entry("nbDestinations", pileDestinations.size()),
                Map.entry("instruction", instruction),
                Map.entry("boutons", boutons),
                Map.entry("log", log));
    }
}
