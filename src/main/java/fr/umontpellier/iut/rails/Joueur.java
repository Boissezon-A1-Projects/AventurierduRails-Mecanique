package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.*;

import java.sql.SQLOutput;
import java.util.*;

import static java.lang.Character.isDigit;

public class Joueur {
    public enum CouleurJouer {
        JAUNE, ROUGE, BLEU, VERT, ROSE;
    }

    /**
     * Jeu auquel le joueur est rattaché
     */
    private final Jeu jeu;
    /**
     * Nom du joueur
     */
    private final String nom;
    /**
     * CouleurJouer du joueur (pour représentation sur le plateau)
     */
    private final CouleurJouer couleur;
    /**
     * Liste des villes sur lesquelles le joueur a construit un port
     */
    private final List<Ville> ports;
    /**
     * Liste des routes capturées par le joueur
     */
    private final List<Route> routes;
    /**
     * Nombre de pions wagons que le joueur peut encore poser sur le plateau
     */
    private int nbPionsWagon;
    /**
     * Nombre de pions wagons que le joueur a dans sa réserve (dans la boîte)
     */
    private int nbPionsWagonEnReserve;
    /**
     * Nombre de pions bateaux que le joueur peut encore poser sur le plateau
     */
    private int nbPionsBateau;
    /**
     * Nombre de pions bateaux que le joueur a dans sa réserve (dans la boîte)
     */
    private int nbPionsBateauEnReserve;
    /**
     * Liste des destinations à réaliser pendant la partie
     */
    private final List<Destination> destinations;
    /**
     * Liste des cartes que le joueur a en main
     */
    private final List<CarteTransport> cartesTransport;
    /**
     * Liste temporaire de cartes transport que le joueur est en train de jouer pour
     * payer la capture d'une route ou la construction d'un port
     */
    private final List<CarteTransport> cartesTransportPosees;
    /**
     * Score courant du joueur (somme des valeurs des routes capturées, et points
     * perdus lors des échanges de pions)
     */
    private int score;

    public Joueur(String nom, Jeu jeu, CouleurJouer couleur) {
        this.nom = nom;
        this.jeu = jeu;
        this.couleur = couleur;
        this.ports = new ArrayList<>();
        this.routes = new ArrayList<>();
        this.nbPionsWagon = 0;
        this.nbPionsWagonEnReserve = 25;
        this.nbPionsBateau = 0;
        this.nbPionsBateauEnReserve = 50;
        this.cartesTransport = new ArrayList<>();
        this.cartesTransportPosees = new ArrayList<>();
        this.destinations = new ArrayList<>();
        this.score = 0;
    }

    public String getNom() {
        return nom;
    }

    public List<Destination> getDestinations(){
        return destinations;
    }

    public int getNbPionsWagon() {
        return nbPionsWagon;
    }

    public int getNbPionsBateau() {
        return nbPionsBateau;
    }

    public void setCartesTransport(List<CarteTransport> carteAAdd){
        for (CarteTransport carte:carteAAdd) {
            cartesTransport.add(carte);
        }
    }

    public List<CarteTransport> getCartesTransport() {
        return cartesTransport;
    }

    /**
     * Cette méthode est appelée à tour de rôle pour chacun des joueurs de la partie.
     * Elle doit réaliser un tour de jeu, pendant lequel le joueur a le choix entre 5 actions possibles :
     *  - piocher des cartes transport (visibles ou dans la pioche)
     *  - échanger des pions wagons ou bateau
     *  - prendre de nouvelles destinations
     *  - capturer une route
     *  - construire un port
     */
    void jouerTour() {
        while(jeu.verifieCompteJokerCarteVisible()){
            jeu.retireCarteVisibleEtDefausseDansBonPaquet();
            jeu.ajout3CarteWagon3CarteBateauCarteVisible();
        }
        List<String> listeChoixPossible= new ArrayList<>();
        //Piocher une carte transport // si les deux pioches sont vides il peut pas choisir piocher cartes transports A FAIRE
        List<CarteTransport> carteVisible = jeu.getCartesTransportVisibles();
        if(carteVisible.size()>=1) {
            for (CarteTransport carte : carteVisible) {
                listeChoixPossible.add(carte.getNom());
            }
        }
        if(!jeu.piocheWagonEstVide()){
        listeChoixPossible.add("WAGON");}
        if(!jeu.piocheBateauEstVide()){
        listeChoixPossible.add("BATEAU");}        //capturer une route

        //nouvelles destination
        listeChoixPossible.add("DESTINATION");
        //Construction port // EST CE QUON VERIFIE AUSSI SI SA LISTE DE PORTS EST PAS COMPLETE ?
        if(verificationCarteConstruirePort(cartesTransport)){ // seulement s'il a les cartes pour construire un port
           for (Ville ville: jeu.getPortsLibres()) { // parcours les villes où le joueur a une route où elle est
                listeChoixPossible.add(ville.toString());
           }
        }
        //echanger pions
        listeChoixPossible.add("PIONS WAGON"); listeChoixPossible.add("PIONS BATEAU");

        String choix = choisir("Que voulez-vous faire ?", listeChoixPossible, null, true);



        if (choix.equals("WAGON") || choix.equals("BATEAU") || (choix.charAt(0)=='C' && isDigit(choix.charAt(1)))) {
            /** PIOCHER CARTE TRANSPORT*/
            log(String.format("%s carte transp", toLog()));
            boolean possible = true;


            while(possible) {
                if (choix.equals("WAGON") || choix.equals("BATEAU")) {
                    piocherCarteDunPaquet(choix);
                    //deuxiemeTour
                    possible = deuxiemeTourPiocherCarteTransport();
                }
                else { // carteVisible
                    List<String> choixPaquetARemplacer = null;
                    if (jeu.piocheWagonEstVide()) {
                       choixPaquetARemplacer  = Arrays.asList("BATEAU");
                    } else if (jeu.piocheBateauEstVide()) {
                        choixPaquetARemplacer = Arrays.asList("WAGON");
                    } else if (jeu.piocheWagonEstVide() && jeu.piocheBateauEstVide()) {
                        possible = false;
                    } else {
                        choixPaquetARemplacer = Arrays.asList("WAGON", "BATEAU");
                    }
                    String paquetChoisi = choisir("Cliquer sur le paquet par lequel vous voulez remplacer la carte prise",choixPaquetARemplacer,null,false );
                    CarteTransport carteChoisie = prendreCarteVisible(choix,paquetChoisi);
                    while(jeu.verifieCompteJokerCarteVisible()){
                        jeu.retireCarteVisibleEtDefausseDansBonPaquet();
                        jeu.ajout3CarteWagon3CarteBateauCarteVisible();
                    }
                    if(carteChoisie.getType().equals(TypeCarteTransport.JOKER)){
                        possible = false;
                    }
                    else{
                        //deuxieme tour
                        while(jeu.verifieCompteJokerCarteVisible()){
                            jeu.retireCarteVisibleEtDefausseDansBonPaquet();
                            jeu.ajout3CarteWagon3CarteBateauCarteVisible();
                        }
                        possible= deuxiemeTourPiocherCarteTransport();
                    }
                }
            }

        }
        else if(choix.charAt(0)=='R' && isDigit(choix.charAt(1))) {
            //demander au joueur la route qu'il veut prendre via map
            //appeler fonction prendre possession route
            List<String> listRoutes = new ArrayList<String>() ;
            for (Route route: jeu.getRoutesLibres()) {
                listRoutes.add(route.getNom());
            }
            String choixRoute = choisir("ivyuv", listRoutes, null, false);
            log(String.format(choixRoute, toLog()));
            log(String.format("%s route", toLog()));
        }
        else if(choix.equals("DESTINATION")) {
            log(String.format("%s destination", toLog()));
           piocherCarteDestination();
        }
        else if(choix.equals("PIONS WAGON")){
            log(String.format("%s Echanger Pions wagons", toLog()));
            echangerPions(choix);
        } else if (choix.equals("PIONS BATEAU")) {
            log(String.format("%s Echanger Pions bateaux", toLog()));
            echangerPions(choix);
        }
        else {
            /**CONSTRUCTION PORT*/
            log(String.format(choix, toLog()));



            //il faut add les cartes qu'il choisit dans cartesTransportPosees puis apres les defausser
            //GROS BUG je sais po quoi faire pour qu'il donne les bonnes cartes (je sais pas si on considere qu'il est pas con)
            //pour ajouter un port dans la liste des ports du joueur : ports.add(la ville qu'il a choisi donc choix)
            ArrayList<String> listeChoixPossibles = new ArrayList<>();
            CarteTransport carteChoisie;
            String nomCarteChoisie;
            for(CarteTransport carte: cartesTransport){
                listeChoixPossibles.add(carte.getNom());
            }
            int compteur = 0;
            boolean valide = false;


            while(!valide) {

                if(compteur > 1){
                    for(CarteTransport carte : cartesTransportPosees){
                        cartesTransport.add(carte);
                    }
                }


                while (compteur<4) {
                    nomCarteChoisie = choisir("Choisissez une carte à utiliser pour construire un port :", listeChoixPossibles, null, false);
                    carteChoisie = carteTransportNomVersCarte(nomCarteChoisie);
                    listeChoixPossibles.remove(carteChoisie);
                    cartesTransport.remove(carteChoisie);
                    cartesTransportPosees.add(carteChoisie);
                    System.out.println(cartesTransportPosees.toString());
                    compteur++;
                }
                valide = verificationCarteConstruirePort(cartesTransportPosees);
                cartesTransportPosees.clear();
                System.out.println(cartesTransportPosees.toString());
                System.out.println(valide);
            }

            ports.add(jeu.nomVilleToVille(choix));
            System.out.println(ports.toString());
        }

    }

     /**
     * Attend une entrée de la part du joueur (au clavier ou sur la websocket) et
     * renvoie le choix du joueur.
     *
     * Cette méthode lit les entrées du jeu (`Jeu.lireligne()`) jusqu'à ce
     * qu'un choix valide (un élément de `choix` ou de `boutons` ou
     * éventuellement la chaîne vide si l'utilisateur est autorisé à passer) soit
     * reçu.
     * Lorsqu'un choix valide est obtenu, il est renvoyé par la fonction.
     *
     * Exemple d'utilisation pour demander à un joueur de répondre à une question
     * par "oui" ou "non" :
     *
     * ```
     * List<String> choix = Arrays.asList("Oui", "Non");
     * String input = choisir("Voulez-vous faire ceci ?", choix, null, false);
     * ```
     *
     * Si par contre on voulait proposer les réponses à l'aide de boutons, on
     * pourrait utiliser :
     *
     * ```
     * List<Bouton> boutons = Arrays.asList(new Bouton("Un", "1"), new Bouton("Deux", "2"), new Bouton("Trois", "3"));
     * String input = choisir("Choisissez un nombre.", null, boutons, false);
     * ```
     *
     * @param instruction message à afficher à l'écran pour indiquer au joueur la
     *                    nature du choix qui est attendu
     * @param choix       une collection de chaînes de caractères correspondant aux
     *                    choix valides attendus du joueur
     * @param boutons     une collection de `Bouton` représentés par deux String (label,
     *                    valeur) correspondant aux choix valides attendus du joueur
     *                    qui doivent être représentés par des boutons sur
     *                    l'interface graphique (le label est affiché sur le bouton,
     *                    la valeur est ce qui est envoyé au jeu quand le bouton est
     *                    cliqué)
     * @param peutPasser  booléen indiquant si le joueur a le droit de passer sans
     *                    faire de choix. S'il est autorisé à passer, c'est la
     *                    chaîne de caractères vide ("") qui signifie qu'il désire
     *                    passer.
     * @return le choix de l'utilisateur (un élement de `choix`, ou la valeur
     * d'un élément de `boutons` ou la chaîne vide)
     */
    public String choisir(
            String instruction,
            Collection<String> choix,
            Collection<Bouton> boutons,
            boolean peutPasser) {
        if (choix == null)
            choix = new ArrayList<>();
        if (boutons == null)
            boutons = new ArrayList<>();

        HashSet<String> choixDistincts = new HashSet<>(choix);
        choixDistincts.addAll(boutons.stream().map(Bouton::valeur).toList());
        if (peutPasser || choixDistincts.isEmpty()) {
            choixDistincts.add("");
        }

        String entree;
        // Lit l'entrée de l'utilisateur jusqu'à obtenir un choix valide
        while (true) {
            jeu.prompt(instruction, boutons, peutPasser);
            entree = jeu.lireLigne();
            // si une réponse valide est obtenue, elle est renvoyée
            if (choixDistincts.contains(entree)) {
                return entree;
            }
        }
    }

    /**
     * Affiche un message dans le log du jeu (visible sur l'interface graphique)
     *
     * @param message le message à afficher (peut contenir des balises html pour la
     *                mise en forme)
     */
    public void log(String message) {
        jeu.log(message);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add(String.format("=== %s (%d pts) ===", nom, score));
        joiner.add(String.format("  Wagons: %d  Bateaux: %d", nbPionsWagon, nbPionsBateau));
        return joiner.toString();
    }

    /**
     * @return une chaîne de caractères contenant le nom du joueur, avec des balises
     * HTML pour être mis en forme dans le log
     */
    public String toLog() {
        return String.format("<span class=\"joueur\">%s</span>", nom);
    }

    boolean destinationEstComplete(Destination d) {
        /** Cette méthode pour l'instant renvoie false pour que le jeu puisse s'exécuter.*/
        // À vous de modifier le corps de cette fonction pour qu'elle retourne la valeur attendue.
        return false;
    }

    public int calculerScoreFinal() {
        /**à faire*/
        throw new RuntimeException("Méthode pas encore implémentée !");
    }

    public void ajouterDestination(Destination destination){
        this.destinations.add(destination);
    }

    public void enleverDestinationId(String destinationId){
        for (int i = 0; i < destinations.size(); i++) {
            if(destinations.get(i).getNom().equals(destinationId)){
                destinations.remove(destinations.get(i));
            }
        }
    }

    public void ajouterCarteEnMain(CarteTransport carte){
        this.cartesTransport.add(carte);
    }

    public CarteTransport carteTransportNomVersCarte(String nom){

        for (CarteTransport carte: cartesTransport) {
            if(carte.getNom().equals(nom)){
                return carte;
            }
        }
        return null;
    }

    /**methodes pour jouer tour*/

    /*piocher carte transport : peut prendre DEUX cartes soit visibles soit non visible deux wagons ou deux bateaux ou un wagon un bateau
      S'il prend un joker face visible il peut pas prendre d'autres cartes
      S'il prend une carte, il ne peut pas prendre un joker apres
      S'il tombe sur un joker dasn une pioche face cachée bah GG WP et il peut en prendre une deuxieme
      */

    /**FAIT PAR NOUS
     * fonction qui crée les possibilité de choix s'il veut prendre une carte du paquet visible ou wagon ou bateau
     * @Renvoi: String avec le choix*/
    public List<String> choixPiocherPaquetOuCarteVisible(int nbTours){
        List<String> choixCartesPossibles = new ArrayList<String>(); // initialisation choix possibles
        if(!jeu.piocheWagonEstVide()){
            choixCartesPossibles.add("WAGON");
        }
        if(!jeu.piocheBateauEstVide()){
            choixCartesPossibles.add("BATEAU");
        }

        if(nbTours==1) { // au tour 1 il peut prendre n'importe quelle carte
            if(jeu.getCartesTransportVisibles().size()<=1) {
                for (CarteTransport carte : jeu.getCartesTransportVisibles()) {
                    choixCartesPossibles.add(carte.getNom()); // ajout des cartes visibles dans les choix possibles
                }
            }
        }
        else{ // au tour 2 tout sauf les jokers présents
            if(jeu.getCartesTransportVisibles().size()<=1) {
                for (CarteTransport carte : jeu.getCartesTransportVisibles()) {
                    if (carte.getType() != TypeCarteTransport.JOKER) {
                        choixCartesPossibles.add(carte.getNom()); // ajout des cartes visibles dans les choix possibles
                    }
                }
            }
        }
        return choixCartesPossibles;
    }


    /**FAIT PAR NOUS
     * METHODE:ajoute dans la main du joueur la carte qu'il a choisi au préalable,
     * l'enleve des cartes visibles, et ajoute une carte du paquet demandé dans les cartes visibles
     * @Return : carte choisie*/
    public CarteTransport prendreCarteVisible(String nomCarte, String paquetRemplacementVoulu){
        CarteTransport carteChoisie= null;
        for (CarteTransport carte: jeu.getCartesTransportVisibles()) {
            if(carte.getNom().equals(nomCarte)){
                carteChoisie = new CarteTransport(carte.getType(),carte.getCouleur(), carte.estDouble(), carte.getAncre());
                jeu.retireCarteVisible(carte);
                ajouterCarteEnMain(carte);
                jeu.ajoutCarteDePaquetDemandéDansCarteVisible(paquetRemplacementVoulu);
            }
        }
        return carteChoisie;
    }
    /** FAIT PAR NOUS
     * METHODE demande au joueur de quel paquet il veut prendre sa carte
     * @Renvoi : string avec paquet choisi*/

    /**FAIT PAR NOUS
     * METHODE: ajoute dans la main du joueur une carte des pioches WAGON ou BATEAU en fonction de son choix*/
    public void piocherCarteDunPaquet(String paquet){
        if(paquet.equals("WAGON")){
            ajouterCarteEnMain(jeu.piocherCarteWagon());
        }
        else{
            ajouterCarteEnMain(jeu.piocherCarteBateau());
        }
    }

    public boolean deuxiemeTourPiocherCarteTransport(){
        List<String> choixPaquet2 = choixPiocherPaquetOuCarteVisible(2);
        String choixPaquetJoueur2= choisir("Veuillez cliquer sur la carte ou le paquet voulu", choixPaquet2, null, true);
        if(choixPaquetJoueur2.equals("WAGON") || choixPaquetJoueur2.equals("BATEAU")){
            piocherCarteDunPaquet(choixPaquetJoueur2);
            return false;
        }
        else if(choixPaquetJoueur2.equals("")){
            return false;
        }
        else{

            List<String> choixPaquetARemplacer2 = null;
            if (jeu.piocheWagonEstVide()) {
                choixPaquetARemplacer2  = Arrays.asList("BATEAU");
            } else if (jeu.piocheBateauEstVide()) {
                choixPaquetARemplacer2 = Arrays.asList("WAGON");
            } else if (jeu.piocheWagonEstVide() && jeu.piocheBateauEstVide()) {
                return false;
            } else {
                choixPaquetARemplacer2 = Arrays.asList("WAGON", "BATEAU");
            }
            String paquetChoisi2 = choisir("Cliquer sur le paquet par lequel vous voulez remplacer la carte prise",choixPaquetARemplacer2,null,false );
            prendreCarteVisible(choixPaquetJoueur2,paquetChoisi2);
            return false;
        }
    }


    /** FAIT PAR NOUS
    *prendre possession route : pose autant de WAGON ou BATEAU de la couleur de la route choisie
    * toute carte jouée doivent etre du meme type donc route maritime -> bateau ; route terrestre -> wagon
    * ATTENTION : 1.route grise -> n'importe quelle serie de carte (wagon ou bateau) de la meme couleur
    * 2. route paire : doit utiliser deux fois plus de cartes; ex: une route paire à deux wagons peut etre prise avec quatres wagons
    * 3. route double : un joueur peut prendre que l'une des deux routes pas les deux
    * 4. cartes double-bateau : prend pour deux bateaux; ex: une route à 4 bateaux peut prendre 2 doubles bateaux ou encore 2 simple 1 double
    * METHODE : lit quelle route prend le joueur et prend son type, verifie si les cartes qu'il veut mettre sont bonnes,
    * pose les pions dont le joueur a besoin (enleve de la main du joueur en gros),
    * les cartes qu'il a choisit s'enlevent de sa main et sont défaussées (dans la bonne defausse)
    * le score s'ajoute en fonction de la longueur de la route (vf fonction get score de route)
    * pareil je sais pas ce qu'elle doit rnvoyer mais le score ig ou elle fait tout et renvoie rien
    */
    public boolean verfierRoute(){ // verifie carte ET bon nombre de pions
        boolean peutPoserRoute = false;
        List<Integer> compteurCarteParType = compteTypeCarte();
        for (Route route : jeu.getRoutesLibres()) {
            if(route instanceof RouteTerrestre){
                if (route.getLongueur() == 1) {
                    if(compteurCarteParType.get(0)>=1 || compteurCarteParType.get(3)>=1){}
                } else if (route.getLongueur() == 2) {

                }else if (route.getLongueur() == 3) {

                }else if (route.getLongueur() == 4) {

                }else if (route.getLongueur() == 5) {

                }else if (route.getLongueur() == 6) {

                }else if (route.getLongueur() == 7) {

                }else{}
            }
            else if (route instanceof RouteMaritime) {
                if (route.getLongueur() == 1) {
                } else if (route.getLongueur() == 2) {

                }else if (route.getLongueur() == 3) {

                }else if (route.getLongueur() == 4) {

                }else if (route.getLongueur() == 5) {

                }else if (route.getLongueur() == 6) {

                }else if (route.getLongueur() == 7) {

                }else{}
            }
            else if(route instanceof RouteMaritime){
                if (route.getLongueur() == 1) {

                } else if (route.getLongueur() == 2) {

                }else if (route.getLongueur() == 3) {

                }else if (route.getLongueur() == 4) {

                }else if (route.getLongueur() == 5) {

                }else if (route.getLongueur() == 6) {

                }else if (route.getLongueur() == 7) {

                }else{}
            }
        }
        return peutPoserRoute;
    }

    public List<Integer> compteTypeCarte(){
        List<Integer> compteursType = new ArrayList<>();
        int compteurWagon =0; int compteurBateau =0; int compteurDoubleBateau =0;int compteurJoker=0;
        for (CarteTransport carte:cartesTransport) {
            if(carte.getType().equals(TypeCarteTransport.WAGON)){
                compteurWagon++;
            } else if (carte.getType().equals(TypeCarteTransport.BATEAU)) {
                if(carte.estDouble()){
                    compteurBateau++;
                }else{
                    compteurDoubleBateau++;
                }
            } else if (carte.getType().equals(TypeCarteTransport.JOKER)) {
                compteurJoker++;
            }
        }
        compteursType.add(compteurWagon);compteursType.add(compteurBateau);compteursType.add(compteurDoubleBateau); compteursType.add(compteurJoker);
        return compteursType;
    }

    /** FAIT PAS NOUS
     * piocher carte destination: tire 4 destinations de la pioche et en garde au minimum une (et jusqu'à 4)
    * ATTENTION : 1. si moins de quatres cartes dans la pioche il prend ce qui reste
    * cartes conservees sont mises sous la pioche destination
    * METHODE : donne 4 cartes (ou moins) au joueur, 1ere fois lui demande de choisir une carte et l'ajoute dans sa main
    * et reste des fois : choix entre les autres cartes ou passer et si passer met les cartes destinations sous la pioche*/
    public boolean piocherCarteDestination(){
        boolean aPasse = false;
        List<Destination> destinationsPiochee = new ArrayList<Destination>();
        List<String> destinationsPiocheeNom = new ArrayList<String>();


        if(jeu.getPileDestinations().size()>=4){ //initialisation des piles en fonction du nombre dans la pileDestination
            for (int i = 0; i < 4; i++) {
                Destination desti = jeu.piocherDestination();
                destinationsPiochee.add(desti);
                destinationsPiocheeNom.add(desti.getNom());

            }
        }
        else{
            for (int i = 0; i < jeu.getPileDestinations().size(); i++) {
                Destination desti = jeu.piocherDestination();
                destinationsPiochee.add(desti);
                destinationsPiocheeNom.add(desti.getNom());

            }
        }

        String choix;Destination destiChoisie;

        // tant que aPasse est faux ou que la la taille de la liste des choisies n'est pas égale à 0 (parce qu'il faut qu'il en garde une au moins
        while(!aPasse && destinationsPiochee.size()>1){
            String pourChoix = "Veuillez choisir ou non une carte à défausser: ";
            for (String nomDesti: destinationsPiocheeNom) {
                pourChoix += nomDesti +"  ";
            }
            choix = choisir(pourChoix, destinationsPiocheeNom, null, true);

            if (!choix.equals("")) { // si c'est pas un passer on enleve des piles et on la defausse
                destiChoisie= nomDestinationVersCarteDestination(choix,destinationsPiochee);
                jeu.defausserDestination(destiChoisie);
                destinationsPiochee.remove(destiChoisie);
                destinationsPiocheeNom.remove(choix);
                System.out.print(destinationsPiocheeNom.toString());
            }
            else{ //sinon on met à true (et on sort de la boucle normalement)
                aPasse = true;
            }

        }
        for (Destination destinationChoisie: destinationsPiochee ) { // on ajoute toute les cartes restantes dans la main
            ajouterDestination(destinationChoisie);
        }
        return aPasse;
    }


    public Destination nomDestinationVersCarteDestination(String nomCarte,List<Destination> listeDesti){
        Destination resultat = null;
        for (Destination destination: listeDesti) {
            if(destination.getNom().equals(nomCarte)){
                resultat = destination;
            }
        }
        return resultat;
    }

    /** FAIT PAR NOUS
    * construire un port: deux cartes WAGONS deux cartes BATEAUX (ou joker) marquees d'une ancre et de la meme couleur
    * ATTENTION : 4.peut que le faire si le joueur a deja une route qui mene à la ville
    * 2. si ville est un Port (faire un get)
    * 3. si ville n'a pas deja un port
    * 5. les cartes utilisées doivent etre marquées d'une ancre (get) et meme couleur (sauf si joker)
    * 1. si la length de la liste des ports n'est pas égale à 3 (ptet à faire en premier ça)
    * METHODE : verifie toutes les conditions + ajoute un port dans la liste si c'est faisable*/

    
    //retourne la liste des villes (à partir de celle qui sont libres) où il a une route qui relie
    public List<Ville> villeLibreReliéesParRoute(){
        List<Ville> villesPossibles = new ArrayList<>();
        for (Ville villeCourante: jeu.getPortsLibres()) {
            for(Route routeCourante : routes){
                if(villeCourante.equals(routeCourante.getVille1())||villeCourante.equals(routeCourante.getVille2()) && !villesPossibles.contains(villeCourante)){
                    villesPossibles.add(villeCourante);
                }

            }
        }
        return villesPossibles;
    }

    //verifie s'il a les cartes pour pouvoir mettre un port
    public boolean verificationCarteConstruirePort(List<CarteTransport> listeAVerifier){
        boolean peutConstruire =false;
        if(listeAVerifier.size()>=4) {
            List<Couleur> couleurs = Arrays.asList(Couleur.JAUNE, Couleur.NOIR, Couleur.BLANC, Couleur.ROUGE, Couleur.VERT, Couleur.VIOLET);
            int wagonJaune = 0;
            int bateauJaune = 0;
            int wagonNoir = 0;
            int bateauNoir = 0;
            int wagonBlanc = 0;
            int bateauBlanc = 0;
            int wagonRouge = 0;
            int bateauRouge = 0;
            int wagonVert = 0;
            int bateauVert = 0;
            int wagonViolet = 0;
            int bateauViolet = 0;
            int nombreJoker = 0;
            List<Integer> wagonsCouleurs = new ArrayList<>();
            List<Integer> bateauCouleurs = new ArrayList<>();
            for (CarteTransport carte :  listeAVerifier) {
                if(carte.getAncre()) {
                    for (int i = 0; i < couleurs.size(); i++) {
                        Couleur couleurCourante = couleurs.get(i);
                        if (carte.getCouleur().equals(couleurCourante)) {
                            if (carte.getType().equals(TypeCarteTransport.WAGON)) {
                                if (i == 0) {
                                    wagonJaune++;
                                    break;
                                } else if (i == 1) {
                                    wagonNoir++;
                                    break;
                                } else if (i == 2) {
                                    wagonBlanc++;
                                    break;
                                } else if (i == 3) {
                                    wagonRouge++;
                                    break;
                                } else if (i == 4) {
                                    wagonVert++;
                                    break;
                                } else {
                                    wagonViolet++;
                                    break;
                                }
                            }
                            if (carte.getType().equals(TypeCarteTransport.BATEAU)) {
                                if (i == 0) {
                                    bateauJaune++;
                                    break;
                                } else if (i == 1) {
                                    bateauNoir++;
                                    break;
                                } else if (i == 2) {
                                    bateauBlanc++;
                                    break;
                                } else if (i == 3) {
                                    bateauRouge++;
                                    break;
                                } else if (i == 4) {
                                    bateauVert++;
                                    break;
                                } else {
                                    bateauViolet++;
                                    break;
                                }
                            }
                        }

                        if (carte.getType().equals(TypeCarteTransport.JOKER)) {
                            nombreJoker++;
                            break;
                        }
                    }
                }
            }
            wagonsCouleurs.add(wagonJaune);
            wagonsCouleurs.add(wagonNoir);
            wagonsCouleurs.add(wagonBlanc);
            wagonsCouleurs.add(wagonRouge);
            wagonsCouleurs.add(wagonVert);
            wagonsCouleurs.add(wagonViolet);
            bateauCouleurs.add(bateauJaune);
            bateauCouleurs.add(bateauNoir);
            bateauCouleurs.add(bateauBlanc);
            bateauCouleurs.add(bateauRouge);
            bateauCouleurs.add(bateauVert);
            bateauCouleurs.add(bateauViolet);

            for (int i = 0; i < wagonsCouleurs.size(); i++) {
                int wagonCouleurCourante = wagonsCouleurs.get(i);
                int bateauCouleurCourante = bateauCouleurs.get(i);
                if (wagonCouleurCourante >= 2 && bateauCouleurCourante >= 2) {
                    peutConstruire = true;
                    break;
                } else if (wagonCouleurCourante == 1 && bateauCouleurCourante >= 2 && nombreJoker >= 1) {
                    peutConstruire = true;
                    break;
                } else if (wagonCouleurCourante >= 2 && bateauCouleurCourante == 1 && nombreJoker >= 1) {
                    peutConstruire = true;
                    break;
                } else if (wagonCouleurCourante == 1 && bateauCouleurCourante == 1 && nombreJoker >= 2) {
                    peutConstruire = true;
                    break;
                } else if (wagonCouleurCourante == 0 && bateauCouleurCourante >= 2 && nombreJoker >= 2) {
                    peutConstruire = true;
                    break;
                } else if (wagonCouleurCourante >= 2 && bateauCouleurCourante == 0 && nombreJoker >= 2) {
                    peutConstruire = true;
                    break;
                } else if (wagonCouleurCourante == 0 && bateauCouleurCourante == 1 && nombreJoker >= 3) {
                    peutConstruire = true;
                    break;
                } else if (wagonCouleurCourante == 1 && bateauCouleurCourante == 0 && nombreJoker >= 3) {
                    peutConstruire = true;
                    break;
                } else if (wagonCouleurCourante == 0 && bateauCouleurCourante == 0 && nombreJoker >= 4) {
                    peutConstruire = true;
                    break;
                }

            }
        }
        return peutConstruire;
    }



    /**FONCTION FAITE PAR NOUS
    echanger des pions: enchange pions w par b ou b par w
    * score joueur diminue du nombre de pions qu'il a échangé
    * ATTENTION : il faut qu'il reste des pions du type voulu dans la boite
    * METHODE : enleve les pions du type non voulu au joueur et ajoute les pions du type voulu au joueur
    * + diminue son score*/
    public void echangerPions(String type){
        List<String> nombre = new ArrayList<String>(); // créations du liste pour savoir si le choix est correct (chiffre possibles)
        if(type.equals("PIONS WAGON")) {
            for (int i = 1; i <= Math.min(nbPionsBateau, nbPionsWagonEnReserve); i++) { /*pions en reserve ou le nombre de pions de bateau qu'il a ?*/
                nombre.add(String.valueOf(i));
            }
        }
        else{
            for (int i = 1; i <=Math.min(nbPionsWagon,nbPionsBateauEnReserve); i++) { /*pions en reserve ou le nombre de pions de bateau qu'il a ?*/
                nombre.add(String.valueOf(i));
            }
        }

        System.out.println(nombre.toString());
        String choixNombreARecevoir = choisir("Rentrez le nombre de pions à recevoir",nombre,null,false);

        pionsARecevoir(type,Integer.valueOf(choixNombreARecevoir));// appel fonction qui gere l'echange
    }

    public void pionsARecevoir(String type, int nombreEchanges){
        if(type.equals("PIONS WAGON") && nbPionsWagonEnReserve!=0 && nbPionsBateau>=nombreEchanges){
            //Si le joueur demande à recevoir des wagons, on lui enleve des bateaux qu'on ajoute dans la reserve, on lui ajoute des wagons qu'on enleve de la reserve
            nbPionsBateau -= nombreEchanges; nbPionsBateauEnReserve += nombreEchanges;
            nbPionsWagon += nombreEchanges; nbPionsWagonEnReserve -= nombreEchanges;
            score -= nombreEchanges;
        }
        if(type.equals("PIONS BATEAU")&& nbPionsBateauEnReserve!=0 && nbPionsWagon>=nombreEchanges){
            //Si le joueur demande à recevoir des bateaux, on lui enleve des wagons qu'on ajoute dans la reserve, on lui ajoute des bateaux qu'on enleve de la reserve
            nbPionsWagon -= nombreEchanges; nbPionsWagonEnReserve += nombreEchanges;
            nbPionsBateau += nombreEchanges; nbPionsBateauEnReserve -= nombreEchanges;
            score -= nombreEchanges;
        }
    }

    //Fonction utile dans jeu
    public void updatePions(int nbWagons){
        this.nbPionsWagonEnReserve -= nbWagons;
        this.nbPionsWagon += nbWagons;
        this.nbPionsBateauEnReserve -= 60 - nbWagons;
        this.nbPionsBateau += 60 - nbWagons;

    }


    /**
     * Renvoie une représentation du joueur sous la forme d'un dictionnaire de
     * valeurs sérialisables
     * (qui sera converti en JSON pour l'envoyer à l'interface graphique)
     */

    Map<String, Object> dataMap() {
        return Map.ofEntries(
                Map.entry("nom", nom),
                Map.entry("couleur", couleur),
                Map.entry("score", score),
                Map.entry("pionsWagon", nbPionsWagon),
                Map.entry("pionsWagonReserve", nbPionsWagonEnReserve),
                Map.entry("pionsBateau", nbPionsBateau),
                Map.entry("pionsBateauReserve", nbPionsBateauEnReserve),
                Map.entry("destinationsIncompletes",
                        destinations.stream().filter(d -> !destinationEstComplete(d)).toList()),
                Map.entry("destinationsCompletes", destinations.stream().filter(this::destinationEstComplete).toList()),
                Map.entry("main", cartesTransport.stream().sorted().toList()),
                Map.entry("inPlay", cartesTransportPosees.stream().sorted().toList()),
                Map.entry("ports", ports.stream().map(Ville::nom).toList()),
                Map.entry("routes", routes.stream().map(Route::getNom).toList()));
    }



}
