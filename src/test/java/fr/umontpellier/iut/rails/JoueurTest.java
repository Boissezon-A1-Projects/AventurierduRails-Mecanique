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
}