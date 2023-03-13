package fr.umontpellier.iut.rails;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

public class ScenarioProfTest {
    private IOJeu jeu;
    private List<CarteTransport> cartesJoueur1;
    private List<Destination> pileDestinations;
    private List<Joueur> joueurs;
    private Joueur joueur1;

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

        jeu = new IOJeu(new String[] { "Guybrush", "Largo" });
        pileDestinations = (List<Destination>) TestUtils.getAttribute(jeu, "pileDestinations");

        joueurs = (List<Joueur>) TestUtils.getAttribute(jeu, "joueurs");
        joueur1 = joueurs.get(0);
        cartesJoueur1 = (List<CarteTransport>) TestUtils.getAttribute(joueur1, "cartesTransport");
    }

    public Destination getDestination(String nom) {
        for (Destination d : pileDestinations) {
            if (nom.equals(TestUtils.getAttribute(d, "nom"))) {
                return d;
            }
        }
        return null;
    }

    public void remonterDestination(String nom) {
        Destination d = getDestination(nom);
        if (d != null) {
            pileDestinations.remove(d);
            pileDestinations.add(0, d);
        }
    }

    public void passerMiseEnPlace() {
        // on trie la pile de destinations pour forcer l'ordre à être toujours le même
        for (int i = 65; i >= 1; i--) {
            remonterDestination("D" + i);
        }
        jeu.setInput(
                "D4", "D5", // joueur 1 garde les destionations D1, D2 et D3
                "20",
                "D9", "D10", // joueur 2 garde les destinations D6, D7 et D8
                "20");
        try {
            jeu.run();
        } catch (IndexOutOfBoundsException e) {
        }
    }

    @Test
    void testCalculerScoreFinalDebutDePartie() {
        passerMiseEnPlace();
        assertEquals(0, TestUtils.getScore(joueur1));
        // joueur 1 devrait avoir -38 points: 3 destinations à -6, -6 et -14 points et 3
        // ports non construits (-12 points)
        assertEquals(-38, joueur1.calculerScoreFinal());
    }

    @Test
    void testCalculerScoreFinalApresUnTour() {
        passerMiseEnPlace();
        remonterDestination("D65"); // remonter la destination D65 en haut de la pile
        jeu.setInput(
                "DESTINATION",
                "D11", "D12", "D13" // joueur1 garde D65, défausse les 3 autres destinations
        );
        joueur1.jouerTour();

        assertEquals(0, TestUtils.getScore(joueur1));
        // 4 destinations à -6, -6, -14 et -36 points et 3 ports non construits (-12
        // points)
        assertEquals(-74, joueur1.calculerScoreFinal());
    }

    @Test
    void testCalculerScoreFinalDestinationValidee() {
        passerMiseEnPlace();
        cartesJoueur1.clear();
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, true, false); // C143
        cartesJoueur1.addAll(List.of(c1, c2, c3));

        // tour 1 : capturer la route maritime Bangkok - Manila
        jeu.setInput(
                "R17", // route maritime Bangkok - Manila (rouge, 2)
                "C141",
                "C142");
        joueur1.jouerTour();
        // 1 route à 2 points
        assertEquals(2, TestUtils.getScore(joueur1));
        // 3 destinations à -6, -6 et -14 points
        // 3 ports non construits (-12 points)
        assertEquals(-36, joueur1.calculerScoreFinal());

        // tour 2 : capturer la route maritime Manila - Tokyo
        jeu.setInput(
                "R106", // route maritime Manila - Tokyo (jaune, 2) -> la destination D2 Bangkok - Tokyo
                        // est validée
                "C143");
        joueur1.jouerTour();
        // 2 routes à 2 et 2 points
        assertEquals(4, TestUtils.getScore(joueur1));
        // 3 destinations à -6, +6 et -14 points
        // 3 ports non construits (-12 points)
        assertEquals(-22, joueur1.calculerScoreFinal());
    }

    @Test
    void testCalculerScoreFinalPorts() {
        // Remarque : les deux premiers tours de ce test sont identiques à ceux du test
        // précédent

        passerMiseEnPlace();
        cartesJoueur1.clear();
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, true, false); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VIOLET, false, true); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VIOLET, false, true); // C145
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VIOLET, false, true); // C146
        CarteTransport c7 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VIOLET, false, true); // C147
        cartesJoueur1.addAll(List.of(c1, c2, c3, c4, c5, c6, c7));

        // tour 1 : capturer la route maritime Bangkok - Manila
        jeu.setInput(
                "R17", // route maritime Bangkok - Manila (rouge, 2)
                "C141",
                "C142");
        joueur1.jouerTour();
        // 1 route à 2 points
        assertEquals(2, TestUtils.getScore(joueur1));
        // 3 destinations à -6, -6 et -14 points
        // 3 ports non construits (-12 points)
        assertEquals(-36, joueur1.calculerScoreFinal());

        // tour 2 : capturer la route maritime Manila - Tokyo
        jeu.setInput(
                "R106", // route maritime Manila - Tokyo (jaune, 2) -> la destination D2 Bangkok - Tokyo
                        // est validée
                "C143");
        joueur1.jouerTour();
        // 2 routes à 2 et 2 points
        assertEquals(4, TestUtils.getScore(joueur1));
        // 3 destinations à -6, +6 et -14 points
        // 3 ports non construits (-12 points)
        assertEquals(-22, joueur1.calculerScoreFinal());

        // tour 3 : construire un port à Bangkok
        jeu.setInput(
                "Bangkok",
                "C144",
                "C145",
                "C146",
                "C147");
        joueur1.jouerTour();
        // 2 routes à 2 et 2 points
        assertEquals(4, TestUtils.getScore(joueur1));
        // 3 destinations à -6, +6 et -14 points
        // 1 port à 20 points et 2 ports non construits (-8 points)
        assertEquals(2, joueur1.calculerScoreFinal());
    }

    @Test
    void testCalculerScoreFinalItineraireValide() {
        passerMiseEnPlace();
        remonterDestination("D61"); // remonter l'itinéraire D61 Casablanca - Al-Qahira - Tehran en haut de la pile

        cartesJoueur1.clear();
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, true); // C144
        cartesJoueur1.addAll(List.of(c1, c2, c3, c4));

        // premier tour (piocher l'itinéraire D61 Casablanca - Al-Qahira - Tehran)
        jeu.setInput(
                "DESTINATION",
                "D11", "D12", "D13" // joueur1 garde D61, défausse les 3 autres destinations
        );
        joueur1.jouerTour();
        assertEquals(0, TestUtils.getScore(joueur1));
        // 4 destinations à -6, -6, -14 et -15 points
        // 3 ports non construits (-12 points)
        assertEquals(-53, joueur1.calculerScoreFinal());

        jeu.setInput(
                "R2", // route terrestre Casablanca - Al-Qahira (grise, 3)
                "C141",
                "C142",
                "C143");
        joueur1.jouerTour();
        // 1 route à 4 points
        assertEquals(4, TestUtils.getScore(joueur1));
        // 4 destinations à -6, -6, -14 et -15 points
        // 3 ports non construits (-12 points)
        assertEquals(-49, joueur1.calculerScoreFinal());

        jeu.setInput(
                "R6", // route terrestre Al-Qahira - Tehran (jaune, 1)
                "C144");
        joueur1.jouerTour();
        // 2 routes à 4 et 1 points
        assertEquals(5, TestUtils.getScore(joueur1));
        // 4 destinations à -6, -6, -14 et +6 points
        // 3 ports non construits (-12 points)
        assertEquals(-27, joueur1.calculerScoreFinal());
        // IMPORTANT : Ici l'itinéraire D61 Casablanca - Al-Qahira - Tehran est validé
        // dans l'ordre, mais on ne compte
        // que la valeur intermédiaire (+6 points) car il n'est pas demandé dans ce
        // projet de déterminer si les villes
        // sont traversées dans l'ordre ou non pour les itinéraires (on ne donne que la
        // valeur intermédiaire, à partir
        // du moment où les villes sont toutes connectées)
    }
}