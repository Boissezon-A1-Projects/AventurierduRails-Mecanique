package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.CarteTransport;
import fr.umontpellier.iut.rails.data.Couleur;
import fr.umontpellier.iut.rails.data.TypeCarteTransport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JoueurTest {


    @Test
    void verificationCarteConstructionPort_2WAGONSJAUNE_2BATEAUJAUNE() {
        String[] joueurs = {"d"};
        Joueur j = new Joueur("d",new Jeu(joueurs), Joueur.CouleurJouer.BLEU);
        List<CarteTransport> test = Arrays.asList(new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE,false,true),new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE,false,true),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE,false,true),new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE,false,true));
        j.setCartesTransport(test);

        assertTrue(j.verificationCarteConstruirePort(j.getCartesTransport()));

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

    }







}