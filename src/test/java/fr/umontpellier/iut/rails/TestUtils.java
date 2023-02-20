package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.CarteTransport;
import fr.umontpellier.iut.rails.data.Destination;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

public class TestUtils {
    public static final long TIMEOUT_VALUE = 500;

    /**
     * Renvoie un attribut d'un objet à partir de son nom.
     * La méthode cherche s'il existe un champ déclaré dans la classe de l'objet et
     * si ce n'est pas le cas remonte dans la hiérarchie des classes jusqu'à trouver
     * un champ avec le nom voulu ou renvoie null.
     * 
     * @param object objet dont on cherche le champ
     * @param name   nom du champ
     * @return le champ de l'objet, avec un type statique Object
     */
    public static Object getAttribute(Object object, String name) {
        Class<?> c = object.getClass();
        while (c != null) {
            try {
                Field field = c.getDeclaredField(name);
                field.setAccessible(true);
                return field.get(object);
            } catch (NoSuchFieldException e) {
                c = c.getSuperclass();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * Setter générique pour forcer la valeur d'un attribut quelconque.
     * La méthode cherche s'il existe un champ déclaré dans la classe de l'objet et
     * si ce n'est pas le cas remonte dans la hiérarchie des classes jusqu'à trouver
     * un champ avec le nom voulu. Lorsque le champ est trouvé, on lui donne la
     * valeur
     * passée en argument.
     * 
     * @param object objet dont on cherche le champ
     * @param name   nom du champ
     * @param value  valeur à donner au champ
     */
    public static void setAttribute(Class<?> c, String name, Object value) {
        while (c != null) {
            try {
                Field field = c.getDeclaredField(name);
                field.setAccessible(true);
                field.set(c, value);
                return;
            } catch (NoSuchFieldException e) {
                c = c.getSuperclass();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return;
            }
        }
        throw new RuntimeException("No such field: " + name);
    }
    /**
     * Setter générique pour forcer la valeur d'un attribut quelconque.
     * La méthode cherche s'il existe un champ déclaré dans la classe de l'objet et
     * si ce n'est pas le cas remonte dans la hiérarchie des classes jusqu'à trouver
     * un champ avec le nom voulu. Lorsque le champ est trouvé, on lui donne la
     * valeur
     * passée en argument.
     * 
     * @param object objet dont on cherche le champ
     * @param name   nom du champ
     * @param value  valeur à donner au champ
     */
    public static void setAttribute(Object object, String name, Object value) {
        Class<?> c = object.getClass();
        while (c != null) {
            try {
                Field field = c.getDeclaredField(name);
                field.setAccessible(true);
                field.set(object, value);
                return;
            } catch (NoSuchFieldException e) {
                c = c.getSuperclass();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return;
            }
        }
        throw new RuntimeException("No such field: " + name);
    }

    /**
     * Met les cartes wagon passées en argument dans la main d'un joueur (la main
     * est vidée avant, pour contenir exactement les cartes indiquées)
     * 
     * @param joueur          le joueur dont on fixe les cartes en main
     * @param cartesTransport les cartes à mettre en main
     */
    public static void setCartesTransport(Joueur joueur, CarteTransport... cartesTransport) {
        List<CarteTransport> mainJoueur = (List<CarteTransport>) getAttribute(joueur, "cartesTransport");
        mainJoueur.clear();
        Collections.addAll(mainJoueur, cartesTransport);
    }

    public static void setCartesTransport(Joueur joueur, List<CarteTransport> cartesTransport) {
        List<CarteTransport> mainJoueur = (List<CarteTransport>) getAttribute(joueur, "cartesTransport");
        mainJoueur.clear();
        mainJoueur.addAll(cartesTransport);
    }

    public static List<CarteTransport> getCartesTransport(Joueur joueur) {
        return (List<CarteTransport>) getAttribute(joueur, "cartesTransport");
    }

    public static List<CarteTransport> getCartesTransportPosees(Joueur joueur) {
        return (List<CarteTransport>) getAttribute(joueur, "cartesTransportPosees");
    }

    public static List<Destination> getDestinations(Joueur joueur) {
        return (List<Destination>) getAttribute(joueur, "destinations");
    }

    public static int getNbPionsWagon(Joueur joueur) {
        return (int) getAttribute(joueur, "nbPionsWagon");
    }

    public static int getNbPionsWagonEnReserve(Joueur joueur) {
        return (int) getAttribute(joueur, "nbPionsWagonEnReserve");
    }

    public static int getNbPionsBateau(Joueur joueur) {
        return (int) getAttribute(joueur, "nbPionsBateau");
    }

    public static int getNbPionsBateauEnReserve(Joueur joueur) {
        return (int) getAttribute(joueur, "nbPionsBateauEnReserve");
    }

    public static int getScore(Joueur joueur) {
        return (int) getAttribute(joueur, "score");
    }
}
