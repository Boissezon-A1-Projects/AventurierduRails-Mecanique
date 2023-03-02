package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.CarteTransport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PilesCartesTransport {
    private final List<CarteTransport> pilePioche;
    private final List<CarteTransport> pileDefausse;

    public PilesCartesTransport(List<CarteTransport> cartes) {
        pilePioche = cartes;
        pileDefausse = new ArrayList<>();
        // on mélange la pile de pioche
        Collections.shuffle(pilePioche);
    }

    /**
     * Retire une carte. Si la pioche est vide, alors on mélange les cartes de la défausse et on les transfère dans la pioche.
     * La fonction retire et renvoie la première carte de la pioche (si elle existe) et null sinon.
     * @return la carte retirée ou null
     */
    public CarteTransport piocher() {
        // Ne renvoie rien si les deux piles sont vides
        if(estVide()){
            return null;
        }
        // Mélange pileDefausse et passe toutes ses cartes dans pilePioche si pilePioche est vide
        else if (pilePioche.size() == 0) {
            Collections.shuffle(pileDefausse);
            for(int i = 0; i < pileDefausse.size(); i++){
                pilePioche.add(pileDefausse.get(i));
            }
        }
        // Enlève la dernière carte du paquet et la return
        CarteTransport cartePiochee = pilePioche.get(pilePioche.size());
        pilePioche.remove(cartePiochee);
        return cartePiochee;
    }

    public void defausser(CarteTransport carte) {
        pileDefausse.add(carte);
    }

    /**
     * @return true si aucune carte ne peut être prise (si les deux piles sont vides)
     */
    public boolean estVide() {
        boolean estVide = false;
        if(pilePioche.size() == 0 && pileDefausse.size() == 0){
            estVide = false;
        }
        return estVide;
    }

    public List<CarteTransport> getCartes() {
        ArrayList<CarteTransport> cartes = new ArrayList<>();
        cartes.addAll(pilePioche);
        cartes.addAll(pileDefausse);
        return cartes;
    }

    public Map<String, Object> dataMap() {
        return Map.ofEntries(
                Map.entry("pioche", pilePioche.size()),
                Map.entry("defausse", pileDefausse));
    }
}
