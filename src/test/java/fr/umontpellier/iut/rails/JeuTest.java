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

import static org.junit.jupiter.api.Assertions.*;

class JeuTest {
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
    public void test_changecartevisiblesitropdejoker_cartepleine(){
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, false); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, true); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true);  // C145
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true);  // C146
        cartesTransportVisibles.clear();
        cartesTransportVisibles.addAll(List.of(c1, c2, c3, c4, c5, c6));
        jeu.changeCarteVisibleSiTropJoker();
        int wagon =0;
        int bateau =0;
        for (CarteTransport carte: cartesTransportVisibles) {
                if(carte.getType().equals(TypeCarteTransport.JOKER) || carte.getType().equals(TypeCarteTransport.WAGON)){
                    wagon++;
                }
                if(carte.getType().equals(TypeCarteTransport.BATEAU)){
                    bateau++;
                }
        }
        assertEquals(3, wagon);
        assertEquals(3, bateau);
    }
    @Test
    public void test_changecartevisiblesitropdejoker_plusdewagons(){
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, false); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, true); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true);  // C145
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true);  // C146
        cartesTransportVisibles.clear();
        cartesTransportVisibles.addAll(List.of(c1, c2, c3, c4, c5, c6));
        piocheWagon.clear();
        defausseWagon.clear();
        jeu.changeCarteVisibleSiTropJoker();
        int wagon =0;
        int bateau =0;
        for (CarteTransport carte: cartesTransportVisibles) {
            if(carte.getType().equals(TypeCarteTransport.JOKER) || carte.getType().equals(TypeCarteTransport.WAGON)){
                wagon++;
            }
            if(carte.getType().equals(TypeCarteTransport.BATEAU)){
                bateau++;
            }
        }
        assertEquals(0, wagon);
        assertEquals(6, bateau);
    }
    @Test
    public void test_changecartevisiblesitropdejoker_plusdebateaux(){
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, false); // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true);  // C145
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true);  // C146
        cartesTransportVisibles.clear();
        cartesTransportVisibles.addAll(List.of(c1, c2, c3, c4, c5, c6));
        piocheBateau.clear();
        defausseBateau.clear();
        jeu.changeCarteVisibleSiTropJoker();
        int wagon =0;
        int bateau =0;
        for (CarteTransport carte: cartesTransportVisibles) {
            if(carte.getType().equals(TypeCarteTransport.JOKER) || carte.getType().equals(TypeCarteTransport.WAGON)){
                wagon++;
            }
            if(carte.getType().equals(TypeCarteTransport.BATEAU)){
                bateau++;
            }
        }
        assertEquals(6, wagon);
        assertEquals(0, bateau);
    }
    @Test
    public void test_changecartevisiblesitropdejoker_plusdebateauxniwagons(){
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true);; // C141
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C142
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C143
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true);; // C144
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true);  // C145
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true);  // C146
        cartesTransportVisibles.clear();
        cartesTransportVisibles.addAll(List.of(c1, c2, c3, c4, c5, c6));
        piocheBateau.clear();
        defausseBateau.clear();
        piocheWagon.clear();
        defausseWagon.clear();
        jeu.changeCarteVisibleSiTropJoker();
        int wagon =0;
        int bateau =0;
        for (CarteTransport carte: cartesTransportVisibles) {
            if(carte.getType().equals(TypeCarteTransport.JOKER) || carte.getType().equals(TypeCarteTransport.WAGON)){
                wagon++;
            }
            if(carte.getType().equals(TypeCarteTransport.BATEAU)){
                bateau++;
            }
        }
        assertEquals(4, wagon);
        assertEquals(2, bateau);
    }
}