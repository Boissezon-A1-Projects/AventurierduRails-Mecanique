package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.*;

import java.util.*;

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

        /* PORTS LIBRES
        List<String> optionsVilles = new ArrayList<>();
        for (Ville ville : jeu.getPortsLibres()) {
            optionsVilles.add(ville.nom());
        }*/
        List<Bouton> boutons = Arrays.asList(
                new Bouton("Piocher Cartes Transport"),
                new Bouton("Capturer Route"),
                new Bouton("Nouvelles Destinations"),
                new Bouton("Echanger Pions"),
                new Bouton("Construire Port"));

        String choix = choisir(
                "Que voulez-vous faire ?",
                null,
                boutons,
                false);
        // si les deux pioches sont vides il peut pas choisir piocher cartes transports A FAIRE
        // check si les choix sont faisables (genre si y a encore des villes avec ports libres)
        if (choix.equals("Piocher Cartes Transport")) {
            //lui demander si wagon bateau ou joker (si ce dernier est present face visible) à faire avec clique sur carte
            //appeler fonction piocher carte en fnction du choix
            log(String.format("%s Piocher Cartes Transport", toLog()));
        } else if(choix.equals("Capturer Route")) {
            //demander au joueur la route qu'il veut prendre via map
            //appeler fonction prendre possession route
            log(String.format("%s Capturer Route", toLog()));
        }else if(choix.equals("Nouvelles Destinations")) {
            //appeler fonction piocherCarteDestination
            log(String.format("%s Nouvelles Destinations", toLog()));
        }else if(choix.equals("Construire Port")){
            //demander ville sur laquelle il veut construire via map
            //demander cartes qu'il veut utiliser et add dans cartes transport posées
            //appeler fonction construirePort
            log(String.format("%s Construire Port", toLog()));
        }else{
            log(String.format("%s Echanger Pions", toLog()));
            //Echanger Pions
            List<Bouton> typeBoutons = Arrays.asList(new Bouton("WAGON"), new Bouton("BATEAU"));
            String choixTypeARecevoir = choisir("Que voulez-vous recevoir? WAGON ou BATEAU",null,typeBoutons,false); // pour savoir s'il veut recevoir bateaux ou wagons

            List<String> nombre = new ArrayList<String>(); // créations du liste pour savoir si le choix est correct (chiffre possibles)
            if(choixTypeARecevoir.equals("WAGON")){
                for (int i = 1; i <= nbPionsWagonEnReserve; i++) {
                        nombre.add(String.valueOf(i));
                }
            }
            if(choixTypeARecevoir.equals("BATEAU")){
                for (int i = 1; i <= nbPionsBateauEnReserve; i++) {
                    nombre.add(String.valueOf(i));
                }
            }

            String choixNombreARecevoir = choisir("Rentrez le nombre de pions à recevoir",nombre,null,false);

            pionsARecevoir(choixTypeARecevoir,Integer.valueOf(choixNombreARecevoir));// appel fonction qui gere l'echange

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

    public void ajouterCarteEnMain(CarteTransport carte){
        this.cartesTransport.add(carte);
    }

    /**methodes pour jouer tour*/

    /*piocher carte transport : peut prendre DEUX cartes soit visibles soit non visible deux wagons ou deux bateaux ou un wagon un bateau
      S'il prend un joker face visible il peut pas prendre d'autres cartes
      S'il prend une carte, il ne peut pas prendre un joker apres
      S'il tombe sur un joker dasn une pioche face cachée bah GG WP et il peut en prendre une deuxieme
      methode : lit ce que prend le jouer (soit WAGON soit BATEAU soit JOKER) et de quelle pioche puis l'ajoute dans sa main ou les/la retourne (à voir)
      DOIT PTET PRENDRE DES ARGUMENTS JSP*/
    public void piocherCarteTransport(String choixCarte1, String choixCarte2){
        throw new RuntimeException("Methode pas encore implémentée !");
    }

    /* prendre possession route : pose autant de WAGON ou BATEAU de la couleur de la route choisie
    * toute carte jouée doivent etre du meme type donc route maritime -> bateau ; route terrestre -> wagon
    * ATTENTION : 1.route grise -> n'importe quelle serie de carte (wagon ou bateau) de la meme couleur
    * 2. route paire : doit utiliser deux fois plus de cartes; ex: une route paire à deux wagons peut etre prise avec quatres wagons
    * 3. route double : un joueur peut prendre que l'une des deux routes pas les deux
    * 4. cartes double-bateau : prend pour deux bateaux; ex: une route à 4 bateaux peut prendre 2 doubles bateaux ou encore 2 simple 1 double
    * methode : lit quelle route prend le joueur et prend son type, verifie si les cartes qu'il veut mettre sont bonnes,
    * pose les pions dont le joueur a besoin (enleve de la main du joueur en gros),
    * les cartes qu'il a choisit s'enlevent de sa main et sont défaussées (dans la bonne defausse)
    * le score s'ajoute en fonction de la longueur de la route (vf fonction get score de route)
    * pareil je sais pas ce qu'elle doit rnvoyer mais le score ig ou elle fait tout et renvoie rien
    */
    public void prendrePossessionRoute(String choixRoute){
        throw new RuntimeException("Methode pas encore implémentée !");
    }

    /*piocher carte destination: tire 4 destinations de la pioche et en garde au minimum une (et jusqu'à 4)
    * ATTENTION : 1. si moins de quatres cartes dans la pioche il prend ce qui reste
    * cartes conservees sont mises sous la pioche destination
    * methode : donne 4 cartes (ou moins) au joueur, 1ere fois lui demande de choisir une carte et l'ajoute dans sa main
    * et reste des fois : choix entre les autres cartes ou passer et si passer met les cartes destinations sous la pioche*/
    public void piocherCarteDestination(){
        throw new RuntimeException("Methode pas encore implémentée !");
    }

    /*construire un port: deux cartes WAGONS deux cartes BATEAUX (ou joker) marquees d'une ancre et de la meme couleur
    * ATTENTION : 4.peut que le faire si le joueur a deja une route qui mene à la ville
    * 2. si ville est un Port (faire un get)
    * 3. si ville n'a pas deja un port
    * 5. les cartes utilisées doivent etre marquées d'une ancre (get) et meme couleur (sauf si joker)
    * 1. si la length de la liste des ports n'est pas égale à 3 (ptet à faire en premier ça)
    * methode : verifie toutes les conditions + ajoute un port dans la liste si c'est faisable*/
    public Ville construirePort(Ville ville){
        boolean villeLibre=false;
        boolean villeDansRoutes= false;
        boolean memeCouleur = true;
        int compteurCouleur =0;
        int compteurWagon =0;
        int compteurBateau =0;
        int compteurJoker =0;
       if(ports.size()!=3){ // si il y a la place pour construire un port
           if(ville.estPort()){ // si la ville est un port
               for (int i = 0; i < jeu.getPortsLibres().size(); i++) {
                   if(ville==jeu.getPortsLibres().get(i)){
                       villeLibre=true;
                   }
               }
               if(villeLibre){ // si la ville n'a pas de port construit
                   for (Route route : this.routes) {
                       if(route.getVille1()==ville||route.getVille2()==ville){
                           villeDansRoutes=true;
                       }
                   }
                   if(villeDansRoutes){ // si le joueur a une route qui mene a la route
                       while(memeCouleur && compteurCouleur!=3){
                            if(cartesTransportPosees.get(compteurCouleur).getType()!=TypeCarteTransport.JOKER){
                                if(cartesTransportPosees.get(compteurCouleur).getCouleur()!=cartesTransportPosees.get(compteurCouleur+1).getCouleur()) {
                                    memeCouleur = false;
                                }
                            }
                            compteurCouleur++;
                       }
                       if(memeCouleur) {
                           for (CarteTransport carte : this.cartesTransportPosees) {
                               if (carte.getType() == TypeCarteTransport.WAGON) {
                                   compteurWagon++;
                               }
                               if (carte.getType() == TypeCarteTransport.BATEAU) {
                                   compteurBateau++;
                               }
                               if (carte.getType() == TypeCarteTransport.JOKER) {
                                   compteurJoker++;
                               }
                           }
                           if(compteurJoker==0 && compteurWagon==2 && compteurBateau==2){
                                ports.add(ville);
                           }
                           else if((compteurJoker ==1 && compteurWagon==1 && compteurBateau==2) || (compteurJoker ==1 && compteurWagon==2 && compteurBateau==1)){
                               ports.add(ville);
                           }
                           else if((compteurJoker ==2 && compteurWagon==0 && compteurBateau==2) || (compteurJoker ==2 && compteurWagon==2 && compteurBateau==0)){
                               ports.add(ville);
                           }
                           else if(compteurJoker ==2 && compteurWagon==1 && compteurBateau==1){
                               ports.add(ville);
                           }
                           else if((compteurJoker ==3 && compteurWagon==0 && compteurBateau==1) || (compteurJoker ==3 && compteurWagon==1 && compteurBateau==0)){
                               ports.add(ville);
                           }
                           else if((compteurJoker ==4 && compteurWagon==0 && compteurBateau==0)){
                               ports.add(ville);
                           }
                           else{
                               System.out.println("Vous n'avez pas mis les bonnes cartes");
                               return null;
                           }
                       }
                       else{
                           System.out.println();
                       }
                   }
                   else{
                       System.out.println("Vous n'avez pas de routes menant à cette ville");
                       return null;
                   }
               }
               else{
                   System.out.println("La ville a déjà un port");
                   return null;
               }
           }
           else{
               System.out.println("La ville que vous avez choisis n'est pas un port.");
               return null;
           }
       }
       else{
           System.out.println("Vous ne pouvez plus contruire de ports.");
           return null;
       }
        return ville;
    }

    /*echanger des pions: enchange pions w par b ou b par w
    * score joueur diminue du nombre de pions qu'il a échangé
    * ATTENTION : il faut qu'il reste des pions du type voulu dans la boite
    * methode : enleve les pions du type non voulu au joueur et ajoute les pions du type voulu au joueur
    * + diminue son score*/
    public void pionsARecevoir(String type, int nombreEchanges){
        if(type.equals("WAGON") && nbPionsWagonEnReserve!=0 && nbPionsBateau>=nombreEchanges){
            nbPionsBateau -= nombreEchanges; nbPionsBateauEnReserve += nombreEchanges;
            nbPionsWagon += nombreEchanges; nbPionsWagonEnReserve -= nombreEchanges;
            score -= nombreEchanges;
        }
        if(type.equals("BATEAU")&& nbPionsBateauEnReserve!=0 && nbPionsWagon>=nombreEchanges){
            nbPionsWagon -= nombreEchanges; nbPionsWagonEnReserve += nombreEchanges;
            nbPionsBateau += nombreEchanges; nbPionsBateauEnReserve -= nombreEchanges;
            score -= nombreEchanges;
        }
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
