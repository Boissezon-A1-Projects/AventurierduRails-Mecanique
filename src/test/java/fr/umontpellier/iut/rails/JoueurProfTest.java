package fr.umontpellier.iut.rails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.umontpellier.iut.rails.data.CarteTransport;
import fr.umontpellier.iut.rails.data.Couleur;
import fr.umontpellier.iut.rails.data.Destination;
import fr.umontpellier.iut.rails.data.TypeCarteTransport;
import fr.umontpellier.iut.rails.data.Ville;

public class JoueurProfTest {
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

        // initialisation des pions wagon et bateau du joueur 1
        TestUtils.setAttribute(joueur1, "nbPionsWagon", 20);
        TestUtils.setAttribute(joueur1, "nbPionsWagonEnReserve", 5);
        TestUtils.setAttribute(joueur1, "nbPionsBateau", 40);
        TestUtils.setAttribute(joueur1, "nbPionsBateauEnReserve", 10);
    }

    @Test
    void testPiocherDestinationsDefausser0() {
        List<Destination> destinations = TestUtils.getDestinations(joueur1);
        destinations.clear();

        Destination destAB = new Destination("A", "B", 10);
        Destination destCD = new Destination("C", "D", 10);
        Destination destEF = new Destination("E", "F", 10);
        Destination destGH = new Destination("G", "H", 10);
        Destination destIJ = new Destination("I", "K", 10);
        pileDestinations.add(0, destAB);
        pileDestinations.add(1, destCD);
        pileDestinations.add(2, destEF);
        pileDestinations.add(3, destGH);
        pileDestinations.add(4, destIJ);

        jeu.setInput(
                "DESTINATION",
                ""); // fin du tour
        joueur1.jouerTour();
        assertEquals(4, destinations.size());
        assertTrue(destinations.contains(destAB));
        assertTrue(destinations.contains(destCD));
        assertTrue(destinations.contains(destEF));
        assertTrue(destinations.contains(destGH));
        assertEquals(destIJ, pileDestinations.get(0));
    }

    @Test
    void testPiocherDestinationsDefausser2() {
        List<Destination> destinations = TestUtils.getDestinations(joueur1);
        destinations.clear();

        Destination destAB = new Destination("A", "B", 10); // D66
        Destination destCD = new Destination("C", "D", 10); // D67
        Destination destEF = new Destination("E", "F", 10); // D68
        Destination destGH = new Destination("G", "H", 10); // D69
        Destination destIJ = new Destination("I", "J", 10); // D70
        pileDestinations.add(0, destAB);
        pileDestinations.add(1, destCD);
        pileDestinations.add(2, destEF);
        pileDestinations.add(3, destGH);
        pileDestinations.add(4, destIJ);

        jeu.setInput(
                "DESTINATION",
                "D70", // ne fait rien car cette destination n'est pas retournée
                "D67", // défausse C-D
                "D68", // défausse E-F
                ""); // fin du tour
        joueur1.jouerTour();
        assertEquals(2, destinations.size());
        assertTrue(destinations.contains(destAB));
        assertTrue(destinations.contains(destGH));
        assertEquals(destIJ, pileDestinations.get(0));
        int nbDest = pileDestinations.size();
        assertEquals(destCD, pileDestinations.get(nbDest - 2)); // cartes replacées sous la pile
        assertEquals(destEF, pileDestinations.get(nbDest - 1)); //
    }

    @Test
    void testPiocherDestinationsDefausser3() {
        List<Destination> destinations = TestUtils.getDestinations(joueur1);
        destinations.clear();

        Destination destAB = new Destination("A", "B", 10); // D66
        Destination destCD = new Destination("C", "D", 10); // D67
        Destination destEF = new Destination("E", "F", 10); // D68
        Destination destGH = new Destination("G", "H", 10); // D69
        Destination destIJ = new Destination("I", "K", 10); // D70
        pileDestinations.add(0, destAB);
        pileDestinations.add(1, destCD);
        pileDestinations.add(2, destEF);
        pileDestinations.add(3, destGH);
        pileDestinations.add(4, destIJ);

        jeu.setInput(
                "DESTINATION",
                "D70", // ne fait rien car cette destination n'est pas retournée
                "D67", // défausse C-D
                "D68", // défausse E-F
                "D66"); // défausse A-B -> fin du tour
        joueur1.jouerTour();
        assertEquals(1, destinations.size());
        assertTrue(destinations.contains(destGH));
        assertEquals(destIJ, pileDestinations.get(0));
        int nbDest = pileDestinations.size();
        assertEquals(destCD, pileDestinations.get(nbDest - 3)); // cartes replacées sous la pile
        assertEquals(destEF, pileDestinations.get(nbDest - 2)); //
        assertEquals(destAB, pileDestinations.get(nbDest - 1)); //
    }

    @Test
    void testPrendrePionsWagon() {
        jeu.setInput(
                "PIONS WAGON", // prendre des pions wagon
                "-2", // choix non valide
                "2"); // choix valide
        joueur1.jouerTour();
        assertEquals(22, TestUtils.getNbPionsWagon(joueur1));
        assertEquals(3, TestUtils.getNbPionsWagonEnReserve(joueur1));
        assertEquals(38, TestUtils.getNbPionsBateau(joueur1));
        assertEquals(12, TestUtils.getNbPionsBateauEnReserve(joueur1));
        assertEquals(-2, TestUtils.getScore(joueur1));
    }

    @Test
    void testPrendrePionsBateau() {
        jeu.setInput("PIONS BATEAU",
                "", // choix non valide
                "5"); // choix valide
        joueur1.jouerTour();
        assertEquals(15, TestUtils.getNbPionsWagon(joueur1));
        assertEquals(10, TestUtils.getNbPionsWagonEnReserve(joueur1));
        assertEquals(45, TestUtils.getNbPionsBateau(joueur1));
        assertEquals(5, TestUtils.getNbPionsBateauEnReserve(joueur1));
        assertEquals(-5, TestUtils.getScore(joueur1));
    }

    @Test
    void testPrendreCartesTransportPiocheWagonPiocheWagon() {
        CarteTransport c1 = piocheWagon.get(0); // premières cartes de la pioche wagon
        CarteTransport c2 = piocheWagon.get(1);
        CarteTransport c3 = piocheWagon.get(2);
        jeu.setInput(
                "WAGON", // prendre une carte wagon de la pioche
                "WAGON"); // prendre une carte wagon de la pioche
        cartesJoueur1.clear();
        joueur1.jouerTour();
        assertEquals(2, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c1));
        assertTrue(cartesJoueur1.contains(c2));
        assertEquals(c3, piocheWagon.get(0));
    }

    @Test
    void testPrendreCartesTransportPiocheWagonPiocheBateau() {
        CarteTransport c1 = piocheWagon.get(0); // premières cartes de la pioche wagon
        CarteTransport c2 = piocheWagon.get(1);
        CarteTransport c3 = piocheBateau.get(0);
        CarteTransport c4 = piocheBateau.get(1);
        jeu.setInput(
                "WAGON", // prendre une carte wagon de la pioche
                "BATEAU"); // prendre une carte bateau de la pioche
        cartesJoueur1.clear();
        joueur1.jouerTour();
        assertEquals(2, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c1));
        assertTrue(cartesJoueur1.contains(c3));
        assertEquals(c2, piocheWagon.get(0));
        assertEquals(c4, piocheBateau.get(0));
    }

    @Test
    void testPrendreCartesTransportPiocheBateauPasse() {
        CarteTransport c1 = piocheBateau.get(0); // premières cartes de la pioche wagon
        CarteTransport c2 = piocheBateau.get(1);
        jeu.setInput(
                "BATEAU", // prendre une carte wagon de la pioche
                ""); // fin du tour (une seule carte piochée)
        cartesJoueur1.clear();
        joueur1.jouerTour();
        assertEquals(1, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c1));
        assertEquals(c2, piocheBateau.get(0));
    }

    @Test
    void testPrendreCartesTransportVisibleEnBateauVisibleEnWagon() {
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, false); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, true); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true); // C145
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, true, false); // C146
        CarteTransport cWagon0 = piocheWagon.get(0); // première carte de la pioche wagon
        CarteTransport cBateau0 = piocheBateau.get(0); // première carte de la pioche bateau
        cartesTransportVisibles.clear();
        cartesTransportVisibles.addAll(List.of(c1, c2, c3, c4, c5, c6));

        jeu.setInput(
                "C141", // prendre une carte wagon visible
                "BATEAU", // retourner une carte bateau
                "C143", // prendre une carte wagon visible
                "WAGON"); // retourner une carte wagon
        cartesJoueur1.clear();
        joueur1.jouerTour();

        assertEquals(2, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c1));
        assertTrue(cartesJoueur1.contains(c3));
        assertEquals(6, cartesTransportVisibles.size());
        assertTrue(cartesTransportVisibles.contains(c2));
        assertTrue(cartesTransportVisibles.contains(c4));
        assertTrue(cartesTransportVisibles.contains(c5));
        assertTrue(cartesTransportVisibles.contains(c6));
        assertTrue(cartesTransportVisibles.contains(cWagon0));
        assertTrue(cartesTransportVisibles.contains(cBateau0));
    }

    @Test
    void testPrendreCartesTransportVisibleEnWagonPiocheWagon() {
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, false); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, true); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true); // C145
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, true, false); // C146
        CarteTransport cWagon0 = piocheWagon.get(0); // première carte de la pioche wagon
        CarteTransport cWagon1 = piocheWagon.get(1); // deuxième carte de la pioche wagon
        cartesTransportVisibles.clear();
        cartesTransportVisibles.addAll(List.of(c1, c2, c3, c4, c5, c6));

        jeu.setInput(
                "C141", // prendre une carte wagon visible
                "WAGON", // retourner une carte wagon
                "C144", // choix invalide (joker)
                "WAGON"); // prendre une carte de la pioche wagon
        cartesJoueur1.clear();
        joueur1.jouerTour();

        assertEquals(2, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c1));
        assertTrue(cartesJoueur1.contains(cWagon1));
        assertEquals(6, cartesTransportVisibles.size());
        assertTrue(cartesTransportVisibles.contains(c2));
        assertTrue(cartesTransportVisibles.contains(c3));
        assertTrue(cartesTransportVisibles.contains(c4));
        assertTrue(cartesTransportVisibles.contains(c5));
        assertTrue(cartesTransportVisibles.contains(c6));
        assertTrue(cartesTransportVisibles.contains(cWagon0));
    }

    @Test
    void testPrendreCartesTransportVisibleEnBateauPasse() {
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, false); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, true); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true); // C145
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, true, false); // C146
        CarteTransport cBateau0 = piocheBateau.get(0); // première carte de la pioche bateau
        cartesTransportVisibles.clear();
        cartesTransportVisibles.addAll(List.of(c1, c2, c3, c4, c5, c6));

        jeu.setInput(
                "C145", // prendre une carte wagon visible
                "", // choix invalide (il faut remplacer la carte prise)
                "BATEAU", // retourner une carte bateau
                "C144", // choix invalide (joker)
                ""); // fin du tour (une seule carte piochée)
        cartesJoueur1.clear();
        joueur1.jouerTour();
        assertEquals(1, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c5));
        assertEquals(6, cartesTransportVisibles.size());
        assertTrue(cartesTransportVisibles.contains(c1));
        assertTrue(cartesTransportVisibles.contains(c2));
        assertTrue(cartesTransportVisibles.contains(c3));
        assertTrue(cartesTransportVisibles.contains(c4));
        assertTrue(cartesTransportVisibles.contains(c6));
        assertTrue(cartesTransportVisibles.contains(cBateau0));
    }

    @Test
    void testPrendreCartesTransportJokerEnBateau() {
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, false); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, true); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true); // C145
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, true, false); // C146
        CarteTransport cBateau0 = piocheBateau.get(0); // première carte de la pioche bateau
        cartesTransportVisibles.clear();
        cartesTransportVisibles.addAll(List.of(c1, c2, c3, c4, c5, c6));

        jeu.setInput(
                "C144", // prendre une carte joker visible
                "BATEAU"); // retourner une carte bateau (fin du tour)
        cartesJoueur1.clear();
        joueur1.jouerTour();

        assertEquals(1, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c4));
        assertEquals(6, cartesTransportVisibles.size());
        assertTrue(cartesTransportVisibles.contains(c1));
        assertTrue(cartesTransportVisibles.contains(c2));
        assertTrue(cartesTransportVisibles.contains(c3));
        assertTrue(cartesTransportVisibles.contains(c5));
        assertTrue(cartesTransportVisibles.contains(c6));
        assertTrue(cartesTransportVisibles.contains(cBateau0));
    }

    @Test
    void testCaptureRouteTerrestre() {
        cartesJoueur1.clear();
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, false); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true); // C145
        cartesJoueur1.addAll(List.of(c1, c2, c3, c4, c5));

        jeu.setInput(
                "R62", // choix invalide : Darwin - Jakarta (maritime 2 NOIR)
                "R4", // choix valide : Al-Qahira - Djibouti (terrestre 2 ROUGE)
                "C141", // wagon rouge
                "C142", // wagon vert (invalide)
                "C144"); // joker
        joueur1.jouerTour();

        assertEquals(3, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c2));
        assertTrue(cartesJoueur1.contains(c3));
        assertTrue(cartesJoueur1.contains(c5));
        assertTrue(defausseWagon.contains(c1));
        assertTrue(defausseWagon.contains(c4));
        assertEquals(1, routesJoueur1.size());
        assertEquals("R4", routesJoueur1.get(0).getNom());
        assertEquals(2, TestUtils.getScore(joueur1));
    }

    @Test
    void testCaptureRouteTerrestreBis() {
        cartesJoueur1.clear();
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, false); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true); // C145
        cartesJoueur1.addAll(List.of(c1, c2, c3, c4, c5));

        jeu.setInput(
                "R19", // choix invalide : Bangkok - Mumbai (terrestre 3 JAUNE)
                "R18", // choix valide : Bangkok - Mumbai (terrestre 3 ROUGE)
                "C141", // wagon rouge
                "C142", // wagon vert (invalide)
                "C144", // joker
                "", // choix invalide (le joueur doit finir de payer la route))
                "C143"); // wagon rouge
        joueur1.jouerTour();

        assertEquals(2, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c2));
        assertTrue(cartesJoueur1.contains(c5));
        assertTrue(defausseWagon.contains(c1));
        assertTrue(defausseWagon.contains(c3));
        assertTrue(defausseWagon.contains(c4));
        assertEquals(1, routesJoueur1.size());
        assertEquals("R18", routesJoueur1.get(0).getNom());
        assertEquals(4, TestUtils.getScore(joueur1));
    }

    @Test
    void testCaptureRouteMaritime() {
        cartesJoueur1.clear();
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, false, true); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, true, false); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, false, true); // C145
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C146
        cartesJoueur1.addAll(List.of(c1, c2, c3, c4, c5, c6));

        jeu.setInput(
                "R114", // Murmansk - Reykjavik (maritime 4 VERT)
                "C141", // bateau vert
                "C142", // bateau rouge (invalide)
                "C144", // joker
                "C146", // wagon vert (invalide)
                "C143"); // double bateau vert
        joueur1.jouerTour();

        assertEquals(3, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c2));
        assertTrue(cartesJoueur1.contains(c5));
        assertTrue(cartesJoueur1.contains(c6));
        assertTrue(defausseBateau.contains(c1));
        assertTrue(defausseBateau.contains(c3));
        assertTrue(defausseWagon.contains(c4));
        assertEquals(1, routesJoueur1.size());
        assertEquals("R114", routesJoueur1.get(0).getNom());
        assertEquals(7, TestUtils.getScore(joueur1));
    }

    @Test
    void testCaptureRouteMaritimeGrise() {
        cartesJoueur1.clear();
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, false, true); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, false, false); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, false); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C145
        cartesJoueur1.addAll(List.of(c1, c2, c3, c4, c5));

        jeu.setInput(
                "R78", // Hong Kong - Tokyo (maritime 3 GRIS)
                "C145", // joker
                "C142", // bateau rouge
                "C141", // bateau vert (invalide)
                "C144"); // bateau rouge
        joueur1.jouerTour();

        assertEquals(2, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c1));
        assertTrue(cartesJoueur1.contains(c3));
        assertTrue(defausseBateau.contains(c2));
        assertTrue(defausseBateau.contains(c4));
        assertTrue(defausseWagon.contains(c5));
        assertEquals(1, routesJoueur1.size());
        assertEquals("R78", routesJoueur1.get(0).getNom());
        assertEquals(4, TestUtils.getScore(joueur1));
    }

    @Test
    void testCaptureRoutePaire() {
        cartesJoueur1.clear();
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, false); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, false); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, false); // C145
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C146
        cartesJoueur1.addAll(List.of(c1, c2, c3, c4, c5, c6));

        jeu.setInput(
                "R91", // Lahore - Tehran (paire 2 GRIS)
                "C141", // wagon vert
                "C145", // wagon rouge
                "C144", // joker
                "C143", // wagon jaune (invalide)
                "C146"); // wagon rouge
        joueur1.jouerTour();

        assertEquals(2, cartesJoueur1.size());
        assertEquals(4, defausseWagon.size());
        assertTrue(cartesJoueur1.contains(c2));
        assertTrue(cartesJoueur1.contains(c3));
        assertTrue(defausseWagon.contains(c1));
        assertTrue(defausseWagon.contains(c4));
        assertTrue(defausseWagon.contains(c5));
        assertTrue(defausseWagon.contains(c6));
        assertEquals(1, routesJoueur1.size());
        assertEquals("R91", routesJoueur1.get(0).getNom());
        assertEquals(2, TestUtils.getScore(joueur1));
    }

    @Test
    void testConstruirePort() {
        Route route = routes.stream().filter(r -> r.getNom().equals("R49")).findFirst().get(); // Casablanca - Lagos
        routes.remove(route);
        routesJoueur1.add(route);
        cartesJoueur1.clear();
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, true, false); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, false, true); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, false, true); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C145
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, true); // C146
        CarteTransport c7 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, false); // C147
        CarteTransport c8 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, true); // C148
        cartesJoueur1.addAll(List.of(c1, c2, c3, c4, c5, c6, c7, c8));
        Ville casablanca = ports.stream().filter(p -> p.nom().equals("Casablanca")).findFirst().get();

        jeu.setInput(
                "New York", // invalide car pas de route connectée
                "Casablanca", // Construire un port à Casablanca
                "C141", // invalide car pas d'ancre
                "C142", // bateau jaune
                "C143", // invalide (couleur incorrecte)
                "C145", // joker
                "C144", // bateau jaune
                "C147", // invalide (pas d'ancre)
                "C146"); // wagon jaune
        joueur1.jouerTour();

        assertEquals(4, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c1));
        assertTrue(cartesJoueur1.contains(c3));
        assertTrue(cartesJoueur1.contains(c7));
        assertTrue(cartesJoueur1.contains(c8));
        assertEquals(2, defausseWagon.size());
        assertEquals(2, defausseBateau.size());
        assertTrue(defausseWagon.contains(c5));
        assertTrue(defausseWagon.contains(c6));
        assertTrue(defausseBateau.contains(c2));
        assertTrue(defausseBateau.contains(c4));
        assertFalse(ports.contains(casablanca));
        assertTrue(portsJoueur1.contains(casablanca));
        assertEquals(0, TestUtils.getScore(joueur1));
    }

    @Test
    void testPrendreCartesTransportVisiblePilesVides() {
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, false); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, true); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true); // C145
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, true, false); // C146
        cartesTransportVisibles.clear();
        cartesTransportVisibles.addAll(List.of(c1, c2, c3, c4, c5, c6));
        piocheWagon.clear();
        defausseWagon.clear();
        piocheBateau.clear();
        defausseBateau.clear();

        jeu.setInput(
                "C141", // prendre une carte transport visible
                "C143"); // prendre une carte transport visible
        // comme les piles sont vides, on ne demande pas de choix au joueur pour
        // remplacer les cartes piochées

        cartesJoueur1.clear();
        joueur1.jouerTour();

        assertEquals(2, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c1));
        assertTrue(cartesJoueur1.contains(c3));
        assertEquals(4, cartesTransportVisibles.size());
        assertTrue(cartesTransportVisibles.contains(c2));
        assertTrue(cartesTransportVisibles.contains(c4));
        assertTrue(cartesTransportVisibles.contains(c5));
        assertTrue(cartesTransportVisibles.contains(c6));
        assertTrue(piocheWagon.isEmpty());
        assertTrue(defausseWagon.isEmpty());
        assertTrue(piocheBateau.isEmpty());
        assertTrue(defausseBateau.isEmpty());
    }

    @Test
    void testCapturerRouteTerrestreColoreeExemple1() {
        cartesJoueur1.clear();
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, true); // C145
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C146
        cartesJoueur1.addAll(List.of(c1, c2, c3, c4, c5, c6));

        jeu.setInput(
                "R18", // route terrestre Bangkok-Mumbai (couleur ROUGE, longueur 3)
                "C141", // (ok)
                "C143", // (invalide, car ne sert pas à payer une route rouge)
                "C142", // (ok)
                "C146" // (ok, la route est payée)
        );

        joueur1.jouerTour();

        assertEquals(3, defausseWagon.size());
        assertTrue(defausseWagon.contains(c1));
        assertTrue(defausseWagon.contains(c2));
        assertTrue(defausseWagon.contains(c6));
        assertEquals(3, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c3));
        assertTrue(cartesJoueur1.contains(c4));
        assertTrue(cartesJoueur1.contains(c5));

        assertEquals(1, routesJoueur1.size());
        assertEquals("R18", routesJoueur1.get(0).getNom());
        assertEquals(4, TestUtils.getScore(joueur1));
    }

    @Test
    void testCapturerRouteTerrestreGriseExemple2() {
        cartesJoueur1.clear();
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, true); // C145
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C146
        cartesJoueur1.addAll(List.of(c1, c2, c3, c4, c5, c6));

        jeu.setInput(
                "R104", // route terrestre Los Angeles - Winnipeg (couleur GRIS, longueur 3)
                "C146", // (ok)
                "C145", // (invalide, car pas possible de payer intégralement en jaune)
                "C143", // (ok, mais il est maintenant obligé de finir en vert)
                "C141", // (invalide, car déjà défaussé une carte verte)
                "C144" // (ok, la route est payée)
        );

        joueur1.jouerTour();

        assertEquals(3, defausseWagon.size());
        assertTrue(defausseWagon.contains(c3));
        assertTrue(defausseWagon.contains(c4));
        assertTrue(defausseWagon.contains(c6));
        assertEquals(3, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c1));
        assertTrue(cartesJoueur1.contains(c2));
        assertTrue(cartesJoueur1.contains(c5));

        assertEquals(1, routesJoueur1.size());
        assertEquals("R104", routesJoueur1.get(0).getNom());
        assertEquals(4, TestUtils.getScore(joueur1));
    }

    @Test
    void testCapturerRouteMaritimeExemple3() {
        cartesJoueur1.clear();
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, false, true); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, true, false); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, true, false); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, true, false); // C145
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C146
        cartesJoueur1.addAll(List.of(c1, c2, c3, c4, c5, c6));

        jeu.setInput(
                "R30", // route maritime Buenos Aires - Valparaiso (couleur VERT, longueur 3)
                "C143", // (ok)
                "C144" // (ok, les règles précisent bien qu'on peut dépasser le coût avec des cartes
                       // double bateau, et aucune carte n'est inutile)
        );

        joueur1.jouerTour();

        assertEquals(2, defausseBateau.size());
        assertTrue(defausseBateau.contains(c3));
        assertTrue(defausseBateau.contains(c4));
        assertEquals(4, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c1));
        assertTrue(cartesJoueur1.contains(c2));
        assertTrue(cartesJoueur1.contains(c5));
        assertTrue(cartesJoueur1.contains(c6));

        assertEquals(1, routesJoueur1.size());
        assertEquals("R30", routesJoueur1.get(0).getNom());
        assertEquals(4, TestUtils.getScore(joueur1));
    }

    @Test
    void testCapturerRouteMaritimeExemple4() {
        cartesJoueur1.clear();
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, false, true); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, true, false); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, true, false); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, true, false); // C145
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C146
        cartesJoueur1.addAll(List.of(c1, c2, c3, c4, c5, c6));

        jeu.setInput(
                "R114", // route maritime Murmansk - Reykjavik (couleur VERT, longueur 4)
                "C141", // (ok)
                "C143", // (ok)
                "C144", // (invalide car si on accepte de défausser ces trois cartes la carte `C141` est
                        // inutile)
                "C146" // (ok, la route est payée)
        );

        joueur1.jouerTour();

        assertEquals(1, defausseWagon.size());
        assertEquals(2, defausseBateau.size());
        assertTrue(defausseWagon.contains(c6));
        assertTrue(defausseBateau.contains(c1));
        assertTrue(defausseBateau.contains(c3));
        assertEquals(3, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c2));
        assertTrue(cartesJoueur1.contains(c4));
        assertTrue(cartesJoueur1.contains(c5));

        assertEquals(1, routesJoueur1.size());
        assertEquals("R114", routesJoueur1.get(0).getNom());
        assertEquals(7, TestUtils.getScore(joueur1));
    }

    @Test
    void testCapturerRouteMaritimeExemple5() {
        cartesJoueur1.clear();
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, false, true); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, true, false); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, true, false); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, false, true); // C144
        cartesJoueur1.addAll(List.of(c1, c2, c3, c4));

        jeu.setInput(
                "R114", // route maritime Murmansk - Reykjavik (couleur VERT, longueur 4)
                "C141", // (invalide, car il n'est pas possible de payer exactement 4 avec les cartes en
                        // main, la carte C141 sera donc forcément inutile à la fin du paiement)
                "C142", // (ok)
                "C143" // (ok)
        );

        joueur1.jouerTour();

        assertEquals(2, defausseBateau.size());
        assertTrue(defausseBateau.contains(c2));
        assertTrue(defausseBateau.contains(c3));
        assertEquals(2, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c1));
        assertTrue(cartesJoueur1.contains(c4));

        assertEquals(1, routesJoueur1.size());
        assertEquals("R114", routesJoueur1.get(0).getNom());
        assertEquals(7, TestUtils.getScore(joueur1));
    }

    @Test
    void testCapturerRoutePaireExemple6() {
        cartesJoueur1.clear();
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C145
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C146
        CarteTransport c7 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VIOLET, false, true); // C147
        CarteTransport c8 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VIOLET, false, true); // C148
        CarteTransport c9 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, true); // C149
        cartesJoueur1.addAll(List.of(c1, c2, c3, c4, c5, c6, c7, c8, c9));

        jeu.setInput(
                "R22", // route paire Lahore - Beijing (longueur 3)
                "C141", // (ok début d'une paire rouge)
                "C145", // (ok début d'une paire verte)
                "C149", // (invalide, pas de paire jaune possible)
                "C142", // (ok)
                "C143", // (ok début d'une 2e paire rouge)
                "C147", // (invalide, on ne peut pas commencer une 4e paire)
                "C146", // (ok)
                "C144" // (ok, la route est payée)
        );

        joueur1.jouerTour();

        assertEquals(6, defausseWagon.size());
        assertTrue(defausseWagon.contains(c1));
        assertTrue(defausseWagon.contains(c2));
        assertTrue(defausseWagon.contains(c3));
        assertTrue(defausseWagon.contains(c4));
        assertTrue(defausseWagon.contains(c5));
        assertTrue(defausseWagon.contains(c6));
        assertEquals(3, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c7));
        assertTrue(cartesJoueur1.contains(c8));
        assertTrue(cartesJoueur1.contains(c9));

        assertEquals(1, routesJoueur1.size());
        assertEquals("R22", routesJoueur1.get(0).getNom());
        assertEquals(4, TestUtils.getScore(joueur1));
    }

    @Test
    void testCapturerRoutePaireExemple7() {
        cartesJoueur1.clear();
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VIOLET, false, true); // C145
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C146
        cartesJoueur1.addAll(List.of(c1, c2, c3, c4, c5, c6));

        jeu.setInput(
                "R59", // route paire Dar Es Salaam - Luanda (longueur 2)
                "C145", // (ok, on peut finir la paire avec le Joker)
                "C144", // (invalide, pas de paire verte possible)
                "C141", // (ok)
                "C142", // (ok)
                "C143", // (invalide, il faut compléter la paire violette)
                "C146" // (ok, la route est payée)
        );

        joueur1.jouerTour();

        assertEquals(4, defausseWagon.size());
        assertTrue(defausseWagon.contains(c1));
        assertTrue(defausseWagon.contains(c2));
        assertTrue(defausseWagon.contains(c5));
        assertTrue(defausseWagon.contains(c6));
        assertEquals(2, cartesJoueur1.size());
        assertTrue(cartesJoueur1.contains(c3));
        assertTrue(cartesJoueur1.contains(c4));

        assertEquals(1, routesJoueur1.size());
        assertEquals("R59", routesJoueur1.get(0).getNom());
        assertEquals(2, TestUtils.getScore(joueur1));
    }
}
