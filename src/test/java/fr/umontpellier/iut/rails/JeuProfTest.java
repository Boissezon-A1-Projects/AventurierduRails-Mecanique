package fr.umontpellier.iut.rails;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.umontpellier.iut.rails.data.CarteTransport;
import fr.umontpellier.iut.rails.data.Destination;
import fr.umontpellier.iut.rails.data.TypeCarteTransport;

public class JeuProfTest {
    private IOJeu jeu;
    private List<CarteTransport> piocheWagon;
    private List<CarteTransport> defausseWagon;
    private List<CarteTransport> piocheBateau;
    private List<CarteTransport> defausseBateau;
    private List<CarteTransport> cartesTransportVisibles;
    private List<Joueur> joueurs;

    @BeforeAll
    static void staticInit() {
        System.setOut(new PrintStream(OutputStream.nullOutputStream()));
    }

    @BeforeEach
    public void setUp() {
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
        joueurs = (List<Joueur>) TestUtils.getAttribute(jeu, "joueurs");
    }

    @Test
    void testCartesWagon() {
        // regrouper toutes les cartes wagon du jeu
        List<CarteTransport> cartes = new ArrayList<>();
        cartes.addAll(piocheWagon);
        cartes.addAll(defausseWagon);
        cartes.addAll(piocheBateau);
        cartes.addAll(defausseBateau);
        cartes.addAll(cartesTransportVisibles);

        for (Joueur j : joueurs) {
            cartes.addAll(TestUtils.getCartesTransport(j));
            cartes.addAll(TestUtils.getCartesTransportPosees(j));
        }

        assertEquals(140, cartes.size());
    }

    @Test
    void testInitialisationCartesVisibles() {
        try {
            jeu.run();
        } catch (IndexOutOfBoundsException ignored) {
        }
        int nbWagon = 0;
        int nbBateau = 0;
        for (CarteTransport c : cartesTransportVisibles) {
            if (c.getType() == TypeCarteTransport.BATEAU) {
                nbBateau += 1;
            } else {
                nbWagon += 1;
            }
        }
        // on vérifie que 6 cartes transport ont bien été retournées en début de partie,
        // et qu'il y a bien 3 cartes wagon et 3 cartes bateau
        assertEquals(6, cartesTransportVisibles.size());
        assertEquals(3, nbWagon);
        assertEquals(3, nbBateau);
    }

    @Test
    void testDestinationsInitialesTousLesJoueursPassent() {
        jeu.setInput("", "10", "", "10", "", "10", "", "10");
        try {
            jeu.run();
        } catch (IndexOutOfBoundsException ignored) {
        }
        // on vérifie que chaque joueur a 5 destinations
        for (Joueur j : joueurs) {
            assertEquals(5, TestUtils.getDestinations(j).size());
        }
    }

    @Test
    void testDestinationsInitialesJoueursDefaussent() {
        List<String> instructions = new ArrayList<>();
        for (Joueur j : joueurs) {
            // le joueur défausse autant de destinations que possible
            for (int i = 0; i < 60; i++) {
                instructions.add("D" + i);
            }
            // le joueur prend 10 pions wagon
            instructions.add("10");
        }
        jeu.setInput(instructions);
        try {
            jeu.run();
        } catch (IndexOutOfBoundsException ignored) {
        }
        // on vérifie que chaque joueur a 3 destinations
        for (Joueur j : joueurs) {
            assertEquals(3, TestUtils.getDestinations(j).size());
        }
    }

    @Test
    void testPionsWagonEtBateau() {
        jeu.setInput(
                "", // j1 garde toutes ses destinations
                "9", // choix invalide
                "10", // j1 prend 10 pions wagon
                "", // j2 garde toutes ses destinations
                "15", // j2 prend 15 pions wagon
                "", // j3 garde toutes ses destinations
                "26", // choix invalide
                "20", // j3 prend 20 pions wagon
                "", // j4 garde toutes ses destinations
                "25"); // j4 prend 25 pions wagon
        try {
            jeu.run();
        } catch (IndexOutOfBoundsException ignored) {
        }
        // on vérifie que chaque joueur a 5 destinations
        Joueur joueur;
        joueur = joueurs.get(0);
        assertEquals(10, TestUtils.getNbPionsWagon(joueur));
        assertEquals(15, TestUtils.getNbPionsWagonEnReserve(joueur));
        assertEquals(50, TestUtils.getNbPionsBateau(joueur));
        assertEquals(0, TestUtils.getNbPionsBateauEnReserve(joueur));
        joueur = joueurs.get(1);
        assertEquals(15, TestUtils.getNbPionsWagon(joueur));
        assertEquals(10, TestUtils.getNbPionsWagonEnReserve(joueur));
        assertEquals(45, TestUtils.getNbPionsBateau(joueur));
        assertEquals(5, TestUtils.getNbPionsBateauEnReserve(joueur));
        joueur = joueurs.get(2);
        assertEquals(20, TestUtils.getNbPionsWagon(joueur));
        assertEquals(5, TestUtils.getNbPionsWagonEnReserve(joueur));
        assertEquals(40, TestUtils.getNbPionsBateau(joueur));
        assertEquals(10, TestUtils.getNbPionsBateauEnReserve(joueur));
        joueur = joueurs.get(3);
        assertEquals(25, TestUtils.getNbPionsWagon(joueur));
        assertEquals(0, TestUtils.getNbPionsWagonEnReserve(joueur));
        assertEquals(35, TestUtils.getNbPionsBateau(joueur));
        assertEquals(15, TestUtils.getNbPionsBateauEnReserve(joueur));
    }
}
