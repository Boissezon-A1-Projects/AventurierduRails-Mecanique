package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.CarteTransport;
import fr.umontpellier.iut.rails.data.Couleur;
import fr.umontpellier.iut.rails.data.Destination;
import fr.umontpellier.iut.rails.data.TypeCarteTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JoueurProfTest {
    private IOJeu jeu;
    private List<CarteTransport> piocheWagon;
    private List<CarteTransport> defausseWagon;
    private List<CarteTransport> piocheBateau;
    private List<CarteTransport> defausseBateau;
    private List<CarteTransport> cartesTransportVisibles;
    private List<Destination> pileDestinations;
    private List<Joueur> joueurs;
    private Joueur joueur1;
    private List<CarteTransport> cartesJoueur1;
    private List<Route> routesJoueur1;

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
        joueurs = (List<Joueur>) TestUtils.getAttribute(jeu, "joueurs");
        joueur1 = joueurs.get(0);
        cartesJoueur1 = (List<CarteTransport>) TestUtils.getAttribute(joueur1, "cartesTransport");
        routesJoueur1 = (List<Route>) TestUtils.getAttribute(joueur1, "routes");
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
    }

}
