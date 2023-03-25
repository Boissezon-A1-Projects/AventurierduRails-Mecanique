package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JoueurTest {
    private IOJeu jeu;
    private List<CarteTransport> piocheWagon;
    private List<CarteTransport> defausseWagon;
    private List<CarteTransport> piocheBateau;
    private List<CarteTransport> defausseBateau;
    private List<CarteTransport> cartesTransportVisibles;
    private List<Destination> pileDestinations;
    private List<Route> routes;
    private List<Ville> ports;
    private List<Joueur> joueurs;
    private Joueur joueur1;
    private List<CarteTransport> cartesJoueur1;
    private List<Route> routesJoueur1;
    private List<Ville> portsJoueur1;

    private List<Destination> destinationJoueur1;

    @BeforeAll
    static void staticInit() {
        System.setOut(new PrintStream(OutputStream.nullOutputStream()));
    }

    @BeforeEach
    void setUp() {
        // réinitialisation des compteurs
        TestUtils.setAttribute(CarteTransport.class, "compteur", 1);
        TestUtils.setAttribute(Destination.class, "compteur", 1);
        TestUtils.setAttribute(Route.class, "compteur", 1);

        jeu = new IOJeu(new String[] { "Guybrush", "Largo", "LeChuck", "Elaine" });
        PilesCartesTransport pilesWagon = (PilesCartesTransport) TestUtils.getAttribute(jeu,
                "pilesDeCartesWagon");
        piocheWagon = (List<CarteTransport>) TestUtils.getAttribute(pilesWagon, "pilePioche");
        defausseWagon = (List<CarteTransport>) TestUtils.getAttribute(pilesWagon, "pileDefausse");
        PilesCartesTransport pilesBateau = (PilesCartesTransport) TestUtils.getAttribute(jeu,
                "pilesDeCartesBateau");
        piocheBateau = (List<CarteTransport>) TestUtils.getAttribute(pilesBateau, "pilePioche");
        defausseBateau = (List<CarteTransport>) TestUtils.getAttribute(pilesBateau, "pileDefausse");
        cartesTransportVisibles = (List<CarteTransport>) TestUtils.getAttribute(jeu,
                "cartesTransportVisibles");
        pileDestinations = (List<Destination>) TestUtils.getAttribute(jeu, "pileDestinations");
        routes = (List<Route>) TestUtils.getAttribute(jeu, "routesLibres");
        ports = (List<Ville>) TestUtils.getAttribute(jeu, "portsLibres");
        joueurs = (List<Joueur>) TestUtils.getAttribute(jeu, "joueurs");
        joueur1 = joueurs.get(0);
        cartesJoueur1 = (List<CarteTransport>) TestUtils.getAttribute(joueur1, "cartesTransport");
        routesJoueur1 = (List<Route>) TestUtils.getAttribute(joueur1, "routes");
        portsJoueur1 = (List<Ville>) TestUtils.getAttribute(joueur1, "ports");
        destinationJoueur1 = (List<Destination>) TestUtils.getAttribute(joueur1, "destinations");
        // initialisation des pions wagon et bateau du joueur 1
        TestUtils.setAttribute(joueur1, "nbPionsWagon", 20);
        TestUtils.setAttribute(joueur1, "nbPionsWagonEnReserve", 5);
        TestUtils.setAttribute(joueur1, "nbPionsBateau", 40);
        TestUtils.setAttribute(joueur1, "nbPionsBateauEnReserve", 10);
    }


    /*@Test
    void verificationCarteConstructionPort_2WAGONSJAUNE_2BATEAUJAUNE() {
        String[] joueurs = {"d"};
        Joueur j = new Joueur("d",new Jeu(joueurs), Joueur.CouleurJouer.BLEU);
        List<CarteTransport> test = Arrays.asList(new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE,false,true),new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE,false,true),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE,false,true),new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE,false,true));
        j.setCartesTransport(test);

        assertTrue(j.verificationCarteConstruirePort(j.getCartesTransport()));

    }

    @Test
    void verificationCarteConstructionPort_Faux_2WAGONSJAUNE_2BATEAUJAUNE() {
        String[] joueurs = {"d"};
        Joueur j = new Joueur("d",new Jeu(joueurs), Joueur.CouleurJouer.BLEU);
        List<CarteTransport> test = Arrays.asList(new CarteTransport(TypeCarteTransport.WAGON, Couleur.BLANC,false,false),new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE,false,true),
                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT,false,false),new CarteTransport(TypeCarteTransport.WAGON, Couleur.VIOLET,false,false),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.NOIR,true,false),new CarteTransport(TypeCarteTransport.BATEAU, Couleur.NOIR,true,false),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC,false,true),new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC,true,false),new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE,false,false),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE,true,false),new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT,true,false),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VIOLET,true,false),new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VIOLET,true,false),new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true));
        j.setCartesTransport(test);

        assertFalse(j.verificationCarteConstruirePort(j.getCartesTransport()));

    }
    @Test
    void verificationCarteConstructionPort_2WAGONSJAUNE_2BATEAUJAUNE_1JOKER() {
        String[] joueurs = {"d"};
        Joueur j = new Joueur("d",new Jeu(joueurs), Joueur.CouleurJouer.BLEU);
        List<CarteTransport> test = Arrays.asList(new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE,false,true),new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE,false,true),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE,false,true),new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE,false,true),new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true));
        j.setCartesTransport(test);

        assertTrue(j.verificationCarteConstruirePort(j.getCartesTransport()));

    }

    @Test
    void verificationCarteConstructionPort_3WAGONSJAUNE_4BATEAUJAUNE_ETAUTRE_2JOKER() {
        String[] joueurs = {"d"};
        Joueur j = new Joueur("d",new Jeu(joueurs), Joueur.CouleurJouer.BLEU);
        List<CarteTransport> test = Arrays.asList(new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE,false,true),new CarteTransport(TypeCarteTransport.WAGON, Couleur.VIOLET,false,true),new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE,false,true),new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE,false,true)
                ,new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE,false,true),new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT,false,true),new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE,false,true),new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE,false,true),new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE,false,true),new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true),new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true));
        j.setCartesTransport(test);

        assertTrue(j.verificationCarteConstruirePort(j.getCartesTransport()));

    }
    @Test
    void verificationCarteConstructionPort_1WAGONSJAUNE_2BATEAUJAUNE_1JOKER() {
        String[] joueurs = {"d"};
        Joueur j = new Joueur("d",new Jeu(joueurs), Joueur.CouleurJouer.BLEU);
        List<CarteTransport> test = Arrays.asList(new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE,false,true)
                ,new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE,false,true),new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE,false,true)
                ,new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true));
        j.setCartesTransport(test);

        assertTrue(j.verificationCarteConstruirePort(j.getCartesTransport()));

    }
    @Test
    void verificationCarteConstructionPort_1WAGONSJAUNE_4BATEAUJAUNE_3JOKER_ETAUTRE() {
        String[] joueurs = {"d"};
        Joueur j = new Joueur("d",new Jeu(joueurs), Joueur.CouleurJouer.BLEU);
        List<CarteTransport> test = Arrays.asList(new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE,false,true)
                ,new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE,false,true),new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE,false,true),new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE,false,true),new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE,false,true)
                ,new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true),new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true),new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true)
                ,new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VIOLET,false,true),new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT,false,true),new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE,false,true));
        j.setCartesTransport(test);

        assertTrue(j.verificationCarteConstruirePort(j.getCartesTransport()));

    }
    @Test
    void verificationCarteConstructionPort_2WAGONSJAUNE_1BATEAUJAUNE_1JOKER() {
        String[] joueurs = {"d"};
        Joueur j = new Joueur("d",new Jeu(joueurs), Joueur.CouleurJouer.BLEU);
        List<CarteTransport> test = Arrays.asList(new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE,false,true),new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE,false,true),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE,false,true),
                new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true));
        j.setCartesTransport(test);

        assertTrue(j.verificationCarteConstruirePort(j.getCartesTransport()));

    }
    @Test
    void verificationCarteConstructionPort_3WAGONSVIOLET_1BATEAUVIOLET_3JOKER_ETAUTRES() {
        String[] joueurs = {"d"};
        Joueur j = new Joueur("d",new Jeu(joueurs), Joueur.CouleurJouer.BLEU);
        List<CarteTransport> test = Arrays.asList(new CarteTransport(TypeCarteTransport.WAGON, Couleur.VIOLET,false,true),new CarteTransport(TypeCarteTransport.WAGON, Couleur.VIOLET,false,true),new CarteTransport(TypeCarteTransport.WAGON, Couleur.VIOLET,false,true),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VIOLET,false,true),
                new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true),new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true),new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true),
            new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT,false,true), new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE,false,true)
        );
        j.setCartesTransport(test);

        assertTrue(j.verificationCarteConstruirePort(j.getCartesTransport()));

    }
    @Test
    void verificationCarteConstructionPort_1WAGONSJAUNE_1BATEAUJAUNE_2JOKER() {
        String[] joueurs = {"d"};
        Joueur j = new Joueur("d",new Jeu(joueurs), Joueur.CouleurJouer.BLEU);
        List<CarteTransport> test = Arrays.asList(new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE,false,true),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE,false,true),
                new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true),new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true)
                );
        j.setCartesTransport(test);

        assertTrue(j.verificationCarteConstruirePort(j.getCartesTransport()));

    }
    @Test
    void verificationCarteConstructionPort_1WAGONSVERT_1BATEAUVERT_3JOKER_ETAUTRE() {
        String[] joueurs = {"d"};
        Joueur j = new Joueur("d",new Jeu(joueurs), Joueur.CouleurJouer.BLEU);
        List<CarteTransport> test = Arrays.asList(new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT,false,true),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT,false,true),
                new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true),
                new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE,false,true),new CarteTransport(TypeCarteTransport.WAGON, Couleur.VIOLET,false,true));
        j.setCartesTransport(test);

        assertTrue(j.verificationCarteConstruirePort(j.getCartesTransport()));

    }
    @Test
    void verificationCarteConstructionPort_0WAGONSJAUNE_2BATEAUJAUNE_2JOKERS() {
        String[] joueurs = {"d"};
        Joueur j = new Joueur("d",new Jeu(joueurs), Joueur.CouleurJouer.BLEU);
        List<CarteTransport> test = Arrays.asList(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE,false,true),new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE,false,true),
                new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true), new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true));
        j.setCartesTransport(test);

        assertTrue(j.verificationCarteConstruirePort(j.getCartesTransport()));

    }
    @Test
    void verificationCarteConstructionPort_0WAGONSJAUNE_3BATEAUJAUNE_4JOKERS_ETAUTRE() {
        String[] joueurs = {"d"};
        Joueur j = new Joueur("d",new Jeu(joueurs), Joueur.CouleurJouer.BLEU);
        List<CarteTransport> test = Arrays.asList(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE,false,true),new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE,false,true),new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE,false,true),
                new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true), new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true), new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true), new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true)
        ,new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT,false,true),new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VIOLET,false,true)
        );
        j.setCartesTransport(test);

        assertTrue(j.verificationCarteConstruirePort(j.getCartesTransport()));

    }

    @Test
    void verificationCarteConstructionPort_2WAGONSJAUNE_0BATEAUJAUNE_2JOKERS() {
        String[] joueurs = {"d"};
        Joueur j = new Joueur("d",new Jeu(joueurs), Joueur.CouleurJouer.BLEU);
        List<CarteTransport> test = Arrays.asList(new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE,false,true),new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE,false,true),
                new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true),new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true));
        j.setCartesTransport(test);

        assertTrue(j.verificationCarteConstruirePort(j.getCartesTransport()));

    }
    @Test
    void verificationCarteConstructionPort_3WAGONSJAUNE_0BATEAUJAUNE_4JOKERS_ETAUTRE() {
        String[] joueurs = {"d"};
        Joueur j = new Joueur("d",new Jeu(joueurs), Joueur.CouleurJouer.BLEU);
        List<CarteTransport> test = Arrays.asList(new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE,false,true),new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE,false,true),new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE,false,true),
                new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true),new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true),new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true),new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true)
        ,new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT,false,true),new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VIOLET,false,true));
        j.setCartesTransport(test);

        assertTrue(j.verificationCarteConstruirePort(j.getCartesTransport()));

    }
    @Test
    void verificationCarteConstructionPort_0WAGONSJAUNE_1BATEAUJAUNE_3JOKERS() {
        String[] joueurs = {"d"};
        Joueur j = new Joueur("d",new Jeu(joueurs), Joueur.CouleurJouer.BLEU);
        List<CarteTransport> test = Arrays.asList(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE,false,true),
                new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true),new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true),new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true));
        j.setCartesTransport(test);

        assertTrue(j.verificationCarteConstruirePort(j.getCartesTransport()));

    }

    @Test
    void verificationCarteConstructionPort_1WAGONSJAUNE_0BATEAUJAUNE_3JOKERS() {
        String[] joueurs = {"d"};
        Joueur j = new Joueur("d",new Jeu(joueurs), Joueur.CouleurJouer.BLEU);
        List<CarteTransport> test = Arrays.asList(new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE,false,true),
                new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true),new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true),new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true));
        j.setCartesTransport(test);

        assertTrue(j.verificationCarteConstruirePort(j.getCartesTransport()));

    }

    @Test
    void verificationCarteConstructionPort_0WAGONSJAUNE_0BATEAUJAUNE_4JOKERS() {
        String[] joueurs = {"d"};
        Joueur j = new Joueur("d",new Jeu(joueurs), Joueur.CouleurJouer.BLEU);
        List<CarteTransport> test = Arrays.asList(new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true),new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true),new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true),new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true));
        j.setCartesTransport(test);

        assertTrue(j.verificationCarteConstruirePort(j.getCartesTransport()));

    }
    @Test
    void verificationCarteConstructionPort_0WAGONSJAUNE_0BATEAUJAUNE_6JOKERS() {
        String[] joueurs = {"d"};
        Joueur j = new Joueur("d",new Jeu(joueurs), Joueur.CouleurJouer.BLEU);
        List<CarteTransport> test = Arrays.asList(new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true),new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true),new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true),new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true),new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true),new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS,false,true));
        j.setCartesTransport(test);

        assertTrue(j.verificationCarteConstruirePort(j.getCartesTransport()));

    }*/

    @Test
    void generer_fils(){
        Ville ville = new Ville("Marseille", true);
        List<Ville> res = new ArrayList<>();
        for (Route route: routes) {
            if(route.getNom().equals("R50") ||route.getNom().equals("R74") ||route.getNom().equals("R73") ||route.getNom().equals("R68") ||route.getNom().equals("R69") ||route.getNom().equals("R12") ){
                routesJoueur1.add(route);
                if(route.getVille1().equals(ville)){
                    res.add(route.getVille2());
                }
                if(route.getVille2().equals(ville)){
                    res.add(route.getVille1());
                }
            }
        }

        System.out.println(res);
        List<Ville> resGen = joueur1.genererFils(ville);
        System.out.println(resGen.toString());
        assertTrue(res.containsAll(resGen) && resGen.containsAll(res));
    }

    @Test
    void destination_est_complete_false_pasDeRoutesHongHonk(){
        destinationJoueur1.add( new Destination("Lagos", "Hong Kong", 14));
        for (Route route: routes) {
            if(route.getNom().equals("R86") ||route.getNom().equals("R85") ||route.getNom().equals("R35") ||route.getNom().equals("R16") ||route.getNom().equals("R121")) {
                routesJoueur1.add(route);
            }
        }
        assertFalse(joueur1.destinationEstComplete(destinationJoueur1.get(0)));
    }

    @Test
    void destination_est_complete_false_route_pas_relie(){
        destinationJoueur1.add( new Destination("Lagos", "Hong Kong", 14));
        for (Route route: routes) {
            if(route.getNom().equals("R86") ||route.getNom().equals("R85") ||route.getNom().equals("R35") ||route.getNom().equals("R16") ||route.getNom().equals("R14")) {
                routesJoueur1.add(route);
            }
        }
        assertFalse(joueur1.destinationEstComplete(destinationJoueur1.get(0)));
    }

    @Test
    void destination_est_complete_true(){
        destinationJoueur1.add( new Destination("Lagos", "Hong Kong", 14));
        for (Route route: routes) {
            if(route.getNom().equals("R86") ||route.getNom().equals("R85") ||route.getNom().equals("R35") ||route.getNom().equals("R16") ||route.getNom().equals("R14") ||route.getNom().equals("R37")  ||route.getNom().equals("R121")) {
                routesJoueur1.add(route);
            }
        }
        assertTrue(joueur1.destinationEstComplete(destinationJoueur1.get(0)));
    }

    @Test
    void destination_est_complete_true_maisdesroutesrandomsenplus(){
        destinationJoueur1.add( new Destination("Lagos", "Hong Kong", 14));
        for (Route route: routes) {
            if(route.getNom().equals("R86") ||route.getNom().equals("R85") ||route.getNom().equals("R41")||route.getNom().equals("R116")||route.getNom().equals("R35") ||route.getNom().equals("R16") ||route.getNom().equals("R14") ||route.getNom().equals("R37")  ||route.getNom().equals("R121")) {
                routesJoueur1.add(route);
            }
        }
        assertTrue(joueur1.destinationEstComplete(destinationJoueur1.get(0)));
    }

    @Test
    void destination_est_complete_true_longue(){
        destinationJoueur1.add( new Destination(List.of( "Lagos","Cape Town","Perth", "Hong Kong"), 14,17,45));
        for (Route route: routes) {
            if(route.getNom().equals("R86") ||route.getNom().equals("R85") ||route.getNom().equals("R41")||route.getNom().equals("R116")||route.getNom().equals("R35") ||route.getNom().equals("R16") ||route.getNom().equals("R14") ||route.getNom().equals("R37")  ||route.getNom().equals("R121")) {
                routesJoueur1.add(route);
            }
        }
        assertTrue(joueur1.destinationEstComplete(destinationJoueur1.get(0)));
    }

    @Test
    void test_couleursPossiblesPourRoutesGrises(){
        RouteTerrestre route = new RouteTerrestre(new Ville("bkbki",false),new Ville("qkdgbc",false),Couleur.GRIS,4);
        CarteTransport carte1 = new CarteTransport(TypeCarteTransport.WAGON,Couleur.JAUNE,false,true);
        CarteTransport carte2 = new CarteTransport(TypeCarteTransport.WAGON,Couleur.VERT,false,true);
        CarteTransport carte3 = new CarteTransport(TypeCarteTransport.WAGON,Couleur.JAUNE,false,true);
        CarteTransport carte4 = new CarteTransport(TypeCarteTransport.WAGON,Couleur.JAUNE,false,true);
        CarteTransport carte5 = new CarteTransport(TypeCarteTransport.WAGON,Couleur.VERT,false,true);
        CarteTransport carte6 = new CarteTransport(TypeCarteTransport.WAGON,Couleur.JAUNE,false,true);

        cartesJoueur1.add(carte1);cartesJoueur1.add(carte2);cartesJoueur1.add(carte3);cartesJoueur1.add(carte4);cartesJoueur1.add(carte5);cartesJoueur1.add(carte6);
        ArrayList<Couleur> expected = new ArrayList<>(); expected.add(Couleur.JAUNE);
        assertEquals(expected, joueur1.couleursPossiblesRouteGrise(route));
    }

    @Test
    void test_couleursPossiblesPourRoutesGrises2(){
        RouteTerrestre route = new RouteTerrestre(new Ville("bkbki",false),new Ville("qkdgbc",false),Couleur.GRIS,4);
        CarteTransport carte1 = new CarteTransport(TypeCarteTransport.WAGON,Couleur.JAUNE,false,true);
        CarteTransport carte2 = new CarteTransport(TypeCarteTransport.WAGON,Couleur.VERT,false,true);
        CarteTransport carte3 = new CarteTransport(TypeCarteTransport.WAGON,Couleur.JAUNE,false,true);
        CarteTransport carte4 = new CarteTransport(TypeCarteTransport.WAGON,Couleur.VERT,false,true);
        CarteTransport carte5 = new CarteTransport(TypeCarteTransport.WAGON,Couleur.VERT,false,true);
        CarteTransport carte6 = new CarteTransport(TypeCarteTransport.WAGON,Couleur.JAUNE,false,true);

        cartesJoueur1.add(carte1);cartesJoueur1.add(carte2);cartesJoueur1.add(carte3);cartesJoueur1.add(carte4);cartesJoueur1.add(carte5);cartesJoueur1.add(carte6);
        ArrayList<Couleur> expected = new ArrayList<>();
        assertEquals(expected, joueur1.couleursPossiblesRouteGrise(route));
    }

    @Test
    void test_couleursPossiblesPourRoutesGrises3(){
        RouteTerrestre route = new RouteTerrestre(new Ville("bkbki",false),new Ville("qkdgbc",false),Couleur.GRIS,4);
        CarteTransport carte1 = new CarteTransport(TypeCarteTransport.WAGON,Couleur.VIOLET,false,true);
        CarteTransport carte2 = new CarteTransport(TypeCarteTransport.WAGON,Couleur.VERT,false,true);
        CarteTransport carte3 = new CarteTransport(TypeCarteTransport.WAGON,Couleur.VIOLET,false,true);
        CarteTransport carte4 = new CarteTransport(TypeCarteTransport.WAGON,Couleur.VERT,false,true);
        CarteTransport carte5 = new CarteTransport(TypeCarteTransport.WAGON,Couleur.VERT,false,true);
        CarteTransport carte6 = new CarteTransport(TypeCarteTransport.WAGON,Couleur.JAUNE,false,true);
        CarteTransport carte7 = new CarteTransport(TypeCarteTransport.JOKER,Couleur.GRIS,false,true);


        cartesJoueur1.add(carte1);cartesJoueur1.add(carte2);cartesJoueur1.add(carte3);cartesJoueur1.add(carte4);cartesJoueur1.add(carte5);cartesJoueur1.add(carte6);
        cartesJoueur1.add(carte7);
        ArrayList<Couleur> expected = new ArrayList<>(); expected.add(Couleur.VERT);
        assertEquals(expected, joueur1.couleursPossiblesRouteGrise(route));
    }
    @Test
    void test_couleursPossiblesPourRoutesGrises4(){
        RouteTerrestre route = new RouteTerrestre(new Ville("bkbki",false),new Ville("qkdgbc",false),Couleur.GRIS,4);
        CarteTransport carte1 = new CarteTransport(TypeCarteTransport.WAGON,Couleur.VIOLET,false,true);
        CarteTransport carte2 = new CarteTransport(TypeCarteTransport.WAGON,Couleur.VERT,false,true);
        CarteTransport carte3 = new CarteTransport(TypeCarteTransport.WAGON,Couleur.VIOLET,false,true);
        CarteTransport carte4 = new CarteTransport(TypeCarteTransport.WAGON,Couleur.VERT,false,true);
        CarteTransport carte5 = new CarteTransport(TypeCarteTransport.WAGON,Couleur.VERT,false,true);
        CarteTransport carte6 = new CarteTransport(TypeCarteTransport.WAGON,Couleur.JAUNE,false,true);
        CarteTransport carte7 = new CarteTransport(TypeCarteTransport.JOKER,Couleur.GRIS,false,true);
        CarteTransport carte8 = new CarteTransport(TypeCarteTransport.JOKER,Couleur.GRIS,false,true);
        CarteTransport carte9 = new CarteTransport(TypeCarteTransport.JOKER,Couleur.GRIS,false,true);

        cartesJoueur1.add(carte1);cartesJoueur1.add(carte2);cartesJoueur1.add(carte3);cartesJoueur1.add(carte4);cartesJoueur1.add(carte5);cartesJoueur1.add(carte6);
        cartesJoueur1.add(carte7);cartesJoueur1.add(carte8);cartesJoueur1.add(carte9);
        ArrayList<Couleur> expected = new ArrayList<>(); expected.add(Couleur.VERT); expected.add(Couleur.JAUNE); expected.add(Couleur.VIOLET);
        assertTrue(expected.containsAll(joueur1.couleursPossiblesRouteGrise(route)) && joueur1.couleursPossiblesRouteGrise(route).containsAll(expected));
    }

    @Test
    void testCapturerRoutePaireDeuxJokersUtilisés() {
        cartesJoueur1.clear();
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VIOLET, false, true); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C145
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C146
        cartesJoueur1.addAll(List.of(c1, c2, c3, c4, c5, c6));

        jeu.setInput(
                "R59", // route paire Dar Es Salaam - Luanda (longueur 2)
                "C146", // commence par un joker
                "C144", // paire violet-joker finie
                "C141", // (ok) paire rouge
                "C142", // (ok) paire rouge
                "C143" //invalide


        );

        joueur1.jouerTour();

        assertEquals(4, defausseWagon.size());
        assertTrue(defausseWagon.contains(c1));
        assertTrue(defausseWagon.contains(c2));
        assertTrue(defausseWagon.contains(c6));
        assertTrue(defausseWagon.contains(c4));
        assertEquals(2, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c5));
        assertTrue(cartesJoueur1.contains(c3));

        assertEquals(1, routesJoueur1.size());
        assertEquals("R59", routesJoueur1.get(0).getNom());
        assertEquals(2, TestUtils.getScore(joueur1));
    }


    @Test
    void testCapturerRoutePaireDeuxJokersUtilisés2() {
        cartesJoueur1.clear();
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VIOLET, false, true); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C145
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C146
        CarteTransport c7 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C147
        cartesJoueur1.addAll(List.of(c1, c2, c3, c4, c5, c6, c7));

        jeu.setInput(
                "R59", // route paire Dar Es Salaam - Luanda (longueur 2)
                "C142",
                "C141",
                "C144",
                "C146",
                "C143" //invalide

        );

        joueur1.jouerTour();

        assertEquals(4, defausseWagon.size());
        assertTrue(defausseWagon.contains(c1));
        assertTrue(defausseWagon.contains(c2));
        assertTrue(defausseWagon.contains(c6));
        assertTrue(defausseWagon.contains(c4));
        assertEquals(3, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c5));
        assertTrue(cartesJoueur1.contains(c3));
        assertTrue(cartesJoueur1.contains(c7));

        assertEquals(1, routesJoueur1.size());
        assertEquals("R59", routesJoueur1.get(0).getNom());
        assertEquals(2, TestUtils.getScore(joueur1));
    }
    @Test
    void testCapturerRouteMaritime1() {
        cartesJoueur1.clear();
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, false, true); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, true, false); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, true, true); // C145
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C146
        CarteTransport c7 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VIOLET, false, true); // C147
        cartesJoueur1.addAll(List.of(c1, c2, c3, c4,c5,c6,c7));

        jeu.setInput(
                "R37", // longueur 5
                "C147", // non
                "C145", //ok
                "C142", // (ok)
                "C146", // non
                "C143" // (ok)
        );

        joueur1.jouerTour();

        assertEquals(3, defausseBateau.size());
        assertTrue(defausseBateau.contains(c5));
        assertTrue(defausseBateau.contains(c2));
        assertTrue(defausseBateau.contains(c3));
        assertEquals(4, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c1));
        assertTrue(cartesJoueur1.contains(c4));
        assertTrue(cartesJoueur1.contains(c6));
        assertTrue(cartesJoueur1.contains(c7));

        assertEquals(1, routesJoueur1.size());
        assertEquals("R37", routesJoueur1.get(0).getNom());
        assertEquals(10, TestUtils.getScore(joueur1));
    }
    @Test
    void testCapturerRouteMaritime2() {
        cartesJoueur1.clear();
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, false, true); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, true, false); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, true, true); // C145
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C146
        CarteTransport c7 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VIOLET, false, true); // C147
        cartesJoueur1.addAll(List.of(c1, c2, c3, c4,c5,c6,c7));

        jeu.setInput(
                "R37", // longueur 5
                "C147", // non
                "C145", //ok
                "C142", // (ok)
                "C144", // oui
                "C143", // non
                "C141" // oui
        );

        joueur1.jouerTour();

        assertEquals(3, defausseBateau.size());
        assertTrue(defausseBateau.contains(c5));
        assertTrue(defausseBateau.contains(c2));

        assertTrue(defausseBateau.contains(c1));
        assertEquals(1,defausseWagon.size());
        assertTrue(defausseWagon.contains(c4));
        assertEquals(3, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c3));

        assertTrue(cartesJoueur1.contains(c6));
        assertTrue(cartesJoueur1.contains(c7));

        assertEquals(1, routesJoueur1.size());
        assertEquals("R37", routesJoueur1.get(0).getNom());
        assertEquals(10, TestUtils.getScore(joueur1));
    }
    @Test
    void testCapturerRouteMaritime3() {
        cartesJoueur1.clear();
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, false, true); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, true, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, true, false); // C143
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, true, true); // C144
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C145
        CarteTransport c7 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VIOLET, false, true); // C146
        cartesJoueur1.addAll(List.of(c1, c2, c3, c5,c6,c7));

        jeu.setInput(
                "R116", // longueur 6
                "C142", // oui
                "C141", // non
                "C143", // (ok)
                "C144" // oui

        );

        joueur1.jouerTour();

        assertEquals(3, defausseBateau.size());
        assertTrue(defausseBateau.contains(c5));
        assertTrue(defausseBateau.contains(c2));

        assertTrue(defausseBateau.contains(c3));

        assertEquals(3, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c1));
        assertTrue(cartesJoueur1.contains(c6));
        assertTrue(cartesJoueur1.contains(c7));

        assertEquals(1, routesJoueur1.size());
        assertEquals("R116", routesJoueur1.get(0).getNom());
        assertEquals(15, TestUtils.getScore(joueur1));
    }
    @Test
    void testCapturerRouteMaritime3MaisJoker() {
        cartesJoueur1.clear();
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, true, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, true, false); // C143
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, true, true); // C144
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C145
        CarteTransport c7 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VIOLET, false, true); // C146
        cartesJoueur1.addAll(List.of(c1, c2, c3, c5,c6,c7));

        jeu.setInput(
                "R116", // longueur 6
                "C142", // oui
                "C141", // non
                "C143", // (ok)
                "C144" // oui

        );

        joueur1.jouerTour();

        assertEquals(3, defausseBateau.size());
        assertTrue(defausseBateau.contains(c5));
        assertTrue(defausseBateau.contains(c2));

        assertTrue(defausseBateau.contains(c3));

        assertEquals(3, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c1));
        assertTrue(cartesJoueur1.contains(c6));
        assertTrue(cartesJoueur1.contains(c7));

        assertEquals(1, routesJoueur1.size());
        assertEquals("R116", routesJoueur1.get(0).getNom());
        assertEquals(15, TestUtils.getScore(joueur1));
    }
    @Test
    void testCapturerRouteMaritime4() {
        cartesJoueur1.clear();
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, false, true); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, true, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, true, false); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, true, true); // C145
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C146
        CarteTransport c7 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VIOLET, false, true); // C147
        cartesJoueur1.addAll(List.of(c1, c2, c3, c4, c5,c6,c7));

        jeu.setInput(
                "R116", // longueur 6
                "C142", // oui
                "C141", // oui
                "C143", // (ok)
                "C145", //non
                "C144"// oui

        );

        joueur1.jouerTour();

        assertEquals(3, defausseBateau.size());
        assertTrue(defausseBateau.contains(c1));
        assertTrue(defausseBateau.contains(c2));

        assertTrue(defausseBateau.contains(c3));
        assertEquals(1,defausseWagon.size());
        assertTrue(defausseWagon.contains(c4));
        assertEquals(3, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c5));
        assertTrue(cartesJoueur1.contains(c6));
        assertTrue(cartesJoueur1.contains(c7));

        assertEquals(1, routesJoueur1.size());
        assertEquals("R116", routesJoueur1.get(0).getNom());
        assertEquals(15, TestUtils.getScore(joueur1));
    }

    @Test
    void testCapturerRouteMaritime5() {
        cartesJoueur1.clear();
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, false, true); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, true, false); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, true, true); // C145
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, true, true); // C146
        CarteTransport c7 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C147
        cartesJoueur1.addAll(List.of(c1, c2, c3, c4, c5,c6,c7));

        jeu.setInput(
                "R51", // longueur 6
                "C141", // oui
                "C143", // oui
                "C144", // oui
                "C147", //oui
                "C145"// oui


        );

        joueur1.jouerTour();

        assertEquals(3, defausseBateau.size());
        assertTrue(defausseBateau.contains(c1));
        assertTrue(defausseBateau.contains(c3));
        assertTrue(defausseBateau.contains(c5));
        assertEquals(2,defausseWagon.size());
        assertTrue(defausseWagon.contains(c4));
        assertTrue(defausseWagon.contains(c7));

        assertEquals(2, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c2));
        assertTrue(cartesJoueur1.contains(c6));


        assertEquals(1, routesJoueur1.size());
        assertEquals("R51", routesJoueur1.get(0).getNom());
        assertEquals(18, TestUtils.getScore(joueur1));
    }

    @Test
    void testCapturerRouteMaritimeGrise() {
        cartesJoueur1.clear();
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, false); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C145
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, true, true); // C146
        CarteTransport c7 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C147
        cartesJoueur1.addAll(List.of(c1, c2, c3, c4, c5,c6,c7));

        jeu.setInput(
                "R105", // longueur 6
                "C141", // oui
                "C142", // oui
                "C143", // oui
                "C145", //oui
                "C146"// oui


        );

        joueur1.jouerTour();

        assertEquals(2, defausseBateau.size());
        assertTrue(defausseBateau.contains(c2));
        assertTrue(defausseBateau.contains(c6));
        assertEquals(3,defausseWagon.size());
        assertTrue(defausseWagon.contains(c3));
        assertTrue(defausseWagon.contains(c1));

        assertEquals(2, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c4));
        assertTrue(cartesJoueur1.contains(c7));


        assertEquals(1, routesJoueur1.size());
        assertEquals("R105", routesJoueur1.get(0).getNom());
        assertEquals(15, TestUtils.getScore(joueur1));
    }

    @Test
    void testCapturerRouteMaritimeGrise2() {
        cartesJoueur1.clear();
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, true, false); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C145
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, true, true); // C146
        CarteTransport c7 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, false, true); // C147
        cartesJoueur1.addAll(List.of(c1, c2, c3, c4, c5,c6,c7));

        jeu.setInput(
                "R105", // longueur 6
                "C142", // oui (S VERT)
                "C141", // oui (JOKER)
                "C143", // oui (double vert)
                "C145", //oui (joker)
                "C147", // oui (simple vert)
                "C146",// non (double vert)
                "C144"//non (joker)


        );

        joueur1.jouerTour();

        assertEquals(3, defausseBateau.size());
        assertTrue(defausseBateau.contains(c3));
        assertTrue(defausseBateau.contains(c2));
        assertEquals(2,defausseWagon.size());
        assertTrue(defausseWagon.contains(c1));
        assertTrue(defausseWagon.contains(c5));

        assertEquals(2, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c4));
        assertTrue(cartesJoueur1.contains(c6));


        assertEquals(1, routesJoueur1.size());
        assertEquals("R105", routesJoueur1.get(0).getNom());
        assertEquals(15, TestUtils.getScore(joueur1));
    }

}