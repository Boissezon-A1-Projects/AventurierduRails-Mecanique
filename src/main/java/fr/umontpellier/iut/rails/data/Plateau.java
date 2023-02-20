package fr.umontpellier.iut.rails.data;

import fr.umontpellier.iut.rails.Route;
import fr.umontpellier.iut.rails.RouteMaritime;
import fr.umontpellier.iut.rails.RoutePaire;
import fr.umontpellier.iut.rails.RouteTerrestre;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Plateau {
    /**
     * Liste des villes
     */
    private final List<Ville> villes;
    /**
     * Liste des routes
     */
    private final List<Route> routes;

    public Plateau(List<Ville> villes, List<Route> routes) {
        this.villes = villes;
        this.routes = routes;
    }

    public List<Ville> getPorts() {
        return villes.stream().filter(Ville::estPort).collect(Collectors.toList());
    }

    public List<Route> getRoutes() {
        return routes;
    }

    static public Plateau makePlateauMonde() {
        Map<String, Ville> villes = new HashMap<>();
        villes.put("Winnipeg", new Ville("Winnipeg", false));
        villes.put("Mexico", new Ville("Mexico", false));
        villes.put("Moskva", new Ville("Moskva", false));
        villes.put("Tehran", new Ville("Tehran", false));
        villes.put("Djibouti", new Ville("Djibouti", false));
        villes.put("Lahore", new Ville("Lahore", false));
        villes.put("Novosibirsk", new Ville("Novosibirsk", false));
        villes.put("Yakutsk", new Ville("Yakutsk", false));
        villes.put("Beijing", new Ville("Beijing", false));
        villes.put("Port-aux-Francais", new Ville("Port-aux-Francais", false));
        villes.put("Cambridge Bay", new Ville("Cambridge Bay", true));
        villes.put("Vancouver", new Ville("Vancouver", true));
        villes.put("New York", new Ville("New York", true));
        villes.put("Los Angeles", new Ville("Los Angeles", true));
        villes.put("Miami", new Ville("Miami", true));
        villes.put("Caracas", new Ville("Caracas", true));
        villes.put("Lima", new Ville("Lima", true));
        villes.put("Rio de Janeiro", new Ville("Rio de Janeiro", true));
        villes.put("Valparaiso", new Ville("Valparaiso", true));
        villes.put("Buenos Aires", new Ville("Buenos Aires", true));
        villes.put("Reykjavik", new Ville("Reykjavik", true));
        villes.put("Edinburgh", new Ville("Edinburgh", true));
        villes.put("Murmansk", new Ville("Murmansk", true));
        villes.put("Hamburg", new Ville("Hamburg", true));
        villes.put("Marseille", new Ville("Marseille", true));
        villes.put("Casablanca", new Ville("Casablanca", true));
        villes.put("Athina", new Ville("Athina", true));
        villes.put("Al-Qahira", new Ville("Al-Qahira", true));
        villes.put("Lagos", new Ville("Lagos", true));
        villes.put("Luanda", new Ville("Luanda", true));
        villes.put("Dar Es Salaam", new Ville("Dar Es Salaam", true));
        villes.put("Toamasina", new Ville("Toamasina", true));
        villes.put("Cape Town", new Ville("Cape Town", true));
        villes.put("Mumbai", new Ville("Mumbai", true));
        villes.put("Tiksi", new Ville("Tiksi", true));
        villes.put("Hong Kong", new Ville("Hong Kong", true));
        villes.put("Bangkok", new Ville("Bangkok", true));
        villes.put("Jakarta", new Ville("Jakarta", true));
        villes.put("Manila", new Ville("Manila", true));
        villes.put("Tokyo", new Ville("Tokyo", true));
        villes.put("Petropavlovsk", new Ville("Petropavlovsk", true));
        villes.put("Anchorage", new Ville("Anchorage", true));
        villes.put("Honolulu", new Ville("Honolulu", true));
        villes.put("Port Moresby", new Ville("Port Moresby", true));
        villes.put("Darwin", new Ville("Darwin", true));
        villes.put("Perth", new Ville("Perth", true));
        villes.put("Sydney", new Ville("Sydney", true));
        villes.put("Christchurch", new Ville("Christchurch", true));

        ArrayList<Route> routes = new ArrayList<>();
        routes.add(new RouteMaritime(villes.get("Al-Qahira"), villes.get("Athina"), Couleur.VERT, 1)); // R1
        routes.add(new RouteTerrestre(villes.get("Al-Qahira"), villes.get("Casablanca"), Couleur.GRIS, 3)); // R2
        routes.add(new RouteTerrestre(villes.get("Al-Qahira"), villes.get("Djibouti"), Couleur.BLANC, 2)); // R3
        routes.add(new RouteTerrestre(villes.get("Al-Qahira"), villes.get("Djibouti"), Couleur.ROUGE, 2)); // R4
        routes.add(new RouteTerrestre(villes.get("Al-Qahira"), villes.get("Tehran"), Couleur.NOIR, 1)); // R5
        routes.add(new RouteTerrestre(villes.get("Al-Qahira"), villes.get("Tehran"), Couleur.JAUNE, 1)); // R6
        routes.add(new RouteMaritime(villes.get("Anchorage"), villes.get("Cambridge Bay"), Couleur.NOIR, 6)); // R7
        routes.add(new RouteMaritime(villes.get("Anchorage"), villes.get("Petropavlovsk"), Couleur.VIOLET, 3)); // R8
        routes.add(new RouteMaritime(villes.get("Anchorage"), villes.get("Tiksi"), Couleur.JAUNE, 8)); // R9
        routes.add(new RoutePaire(villes.get("Anchorage"), villes.get("Vancouver"), 2)); // R10
        routes.add(new RouteTerrestre(villes.get("Athina"), villes.get("Hamburg"), Couleur.VERT, 2)); // R11
        routes.add(new RouteMaritime(villes.get("Athina"), villes.get("Marseille"), Couleur.ROUGE, 2)); // R12
        routes.add(new RouteTerrestre(villes.get("Athina"), villes.get("Tehran"), Couleur.GRIS, 2)); // R13
        routes.add(new RouteTerrestre(villes.get("Bangkok"), villes.get("Hong Kong"), Couleur.NOIR, 1)); // R14
        routes.add(new RouteTerrestre(villes.get("Bangkok"), villes.get("Hong Kong"), Couleur.VIOLET, 1)); // R15
        routes.add(new RouteMaritime(villes.get("Bangkok"), villes.get("Jakarta"), Couleur.BLANC, 2)); // R16
        routes.add(new RouteMaritime(villes.get("Bangkok"), villes.get("Manila"), Couleur.ROUGE, 2)); // R17
        routes.add(new RouteTerrestre(villes.get("Bangkok"), villes.get("Mumbai"), Couleur.ROUGE, 3)); // R18
        routes.add(new RouteTerrestre(villes.get("Bangkok"), villes.get("Mumbai"), Couleur.JAUNE, 3)); // R19
        routes.add(new RouteTerrestre(villes.get("Beijing"), villes.get("Hong Kong"), Couleur.BLANC, 2)); // R20
        routes.add(new RouteTerrestre(villes.get("Beijing"), villes.get("Hong Kong"), Couleur.VERT, 2)); // R21
        routes.add(new RoutePaire(villes.get("Beijing"), villes.get("Lahore"), 3)); // R22
        routes.add(new RouteTerrestre(villes.get("Beijing"), villes.get("Novosibirsk"), Couleur.NOIR, 3)); // R23
        routes.add(new RouteTerrestre(villes.get("Beijing"), villes.get("Novosibirsk"), Couleur.ROUGE, 3)); // R24
        routes.add(new RouteTerrestre(villes.get("Beijing"), villes.get("Yakutsk"), Couleur.JAUNE, 3)); // R25
        routes.add(new RouteMaritime(villes.get("Buenos Aires"), villes.get("Cape Town"), Couleur.JAUNE, 7)); // R26
        routes.add(new RouteMaritime(villes.get("Buenos Aires"), villes.get("Cape Town"), Couleur.VIOLET, 7)); // R27
        routes.add(new RouteTerrestre(villes.get("Buenos Aires"), villes.get("Rio de Janeiro"), Couleur.BLANC, 1)); // R28
        routes.add(new RouteTerrestre(villes.get("Buenos Aires"), villes.get("Rio de Janeiro"), Couleur.ROUGE, 1)); // R29
        routes.add(new RouteMaritime(villes.get("Buenos Aires"), villes.get("Valparaiso"), Couleur.VERT, 3)); // R30
        routes.add(new RouteMaritime(villes.get("Cambridge Bay"), villes.get("Reykjavik"), Couleur.BLANC, 6)); // R31
        routes.add(new RouteTerrestre(villes.get("Cambridge Bay"), villes.get("Winnipeg"), Couleur.NOIR, 4)); // R32
        routes.add(new RouteTerrestre(villes.get("Cape Town"), villes.get("Dar Es Salaam"), Couleur.VERT, 3)); // R33
        routes.add(new RouteTerrestre(villes.get("Cape Town"), villes.get("Dar Es Salaam"), Couleur.VIOLET, 3)); // R34
        routes.add(new RouteTerrestre(villes.get("Cape Town"), villes.get("Luanda"), Couleur.GRIS, 2)); // R35
        routes.add(new RouteMaritime(villes.get("Cape Town"), villes.get("Port-aux-Francais"), Couleur.ROUGE, 5)); // R36
        routes.add(new RouteMaritime(villes.get("Cape Town"), villes.get("Port-aux-Francais"), Couleur.VERT, 5)); // R37
        routes.add(new RouteMaritime(villes.get("Cape Town"), villes.get("Rio de Janeiro"), Couleur.NOIR, 6)); // R38
        routes.add(new RouteMaritime(villes.get("Cape Town"), villes.get("Rio de Janeiro"), Couleur.BLANC, 6)); // R39
        routes.add(new RouteMaritime(villes.get("Cape Town"), villes.get("Toamasina"), Couleur.GRIS, 3)); // R40
        routes.add(new RouteMaritime(villes.get("Caracas"), villes.get("Lagos"), Couleur.ROUGE, 7)); // R41
        routes.add(new RouteTerrestre(villes.get("Caracas"), villes.get("Lima"), Couleur.BLANC, 2)); // R42
        routes.add(new RouteTerrestre(villes.get("Caracas"), villes.get("Lima"), Couleur.JAUNE, 2)); // R43
        routes.add(new RouteTerrestre(villes.get("Caracas"), villes.get("Mexico"), Couleur.ROUGE, 3)); // R44
        routes.add(new RouteTerrestre(villes.get("Caracas"), villes.get("Mexico"), Couleur.VIOLET, 3)); // R45
        routes.add(new RouteMaritime(villes.get("Caracas"), villes.get("Miami"), Couleur.BLANC, 2)); // R46
        routes.add(new RouteTerrestre(villes.get("Caracas"), villes.get("Rio de Janeiro"), Couleur.NOIR, 4)); // R47
        routes.add(new RouteTerrestre(villes.get("Caracas"), villes.get("Rio de Janeiro"), Couleur.VERT, 4)); // R48
        routes.add(new RouteTerrestre(villes.get("Casablanca"), villes.get("Lagos"), Couleur.GRIS, 4)); // R49
        routes.add(new RoutePaire(villes.get("Casablanca"), villes.get("Marseille"), 1)); // R50
        routes.add(new RouteMaritime(villes.get("Casablanca"), villes.get("Miami"), Couleur.VERT, 7)); // R51
        routes.add(new RouteMaritime(villes.get("Christchurch"), villes.get("Sydney"), Couleur.BLANC, 1)); // R52
        routes.add(new RouteMaritime(villes.get("Christchurch"), villes.get("Sydney"), Couleur.ROUGE, 1)); // R53
        routes.add(new RouteMaritime(villes.get("Christchurch"), villes.get("Valparaiso"), Couleur.JAUNE, 7)); // R54
        routes.add(new RouteTerrestre(villes.get("Dar Es Salaam"), villes.get("Djibouti"), Couleur.NOIR, 1)); // R55
        routes.add(new RouteTerrestre(villes.get("Dar Es Salaam"), villes.get("Djibouti"), Couleur.ROUGE, 1)); // R56
        routes.add(new RouteMaritime(villes.get("Dar Es Salaam"), villes.get("Jakarta"), Couleur.VERT, 7)); // R57
        routes.add(new RouteMaritime(villes.get("Dar Es Salaam"), villes.get("Jakarta"), Couleur.VIOLET, 7)); // R58
        routes.add(new RoutePaire(villes.get("Dar Es Salaam"), villes.get("Luanda"), 2)); // R59
        routes.add(new RouteMaritime(villes.get("Dar Es Salaam"), villes.get("Mumbai"), Couleur.BLANC, 4)); // R60
        routes.add(new RouteMaritime(villes.get("Dar Es Salaam"), villes.get("Toamasina"), Couleur.JAUNE, 1)); // R61
        routes.add(new RouteMaritime(villes.get("Darwin"), villes.get("Jakarta"), Couleur.NOIR, 2)); // R62
        routes.add(new RouteTerrestre(villes.get("Darwin"), villes.get("Perth"), Couleur.ROUGE, 2)); // R63
        routes.add(new RouteMaritime(villes.get("Darwin"), villes.get("Port Moresby"), Couleur.ROUGE, 1)); // R64
        routes.add(new RouteTerrestre(villes.get("Darwin"), villes.get("Sydney"), Couleur.VERT, 2)); // R65
        routes.add(new RouteMaritime(villes.get("Edinburgh"), villes.get("Hamburg"), Couleur.NOIR, 1)); // R66
        routes.add(new RouteMaritime(villes.get("Edinburgh"), villes.get("Hamburg"), Couleur.JAUNE, 1)); // R67
        routes.add(new RouteMaritime(villes.get("Edinburgh"), villes.get("Marseille"), Couleur.BLANC, 1)); // R68
        routes.add(new RouteMaritime(villes.get("Edinburgh"), villes.get("Marseille"), Couleur.VERT, 1)); // R69
        routes.add(new RouteMaritime(villes.get("Edinburgh"), villes.get("New York"), Couleur.ROUGE, 7)); // R70
        routes.add(new RouteMaritime(villes.get("Edinburgh"), villes.get("New York"), Couleur.VIOLET, 7)); // R71
        routes.add(new RouteMaritime(villes.get("Edinburgh"), villes.get("Reykjavik"), Couleur.GRIS, 2)); // R72
        routes.add(new RouteTerrestre(villes.get("Hamburg"), villes.get("Marseille"), Couleur.ROUGE, 1)); // R73
        routes.add(new RouteTerrestre(villes.get("Hamburg"), villes.get("Marseille"), Couleur.VIOLET, 1)); // R74
        routes.add(new RouteTerrestre(villes.get("Hamburg"), villes.get("Moskva"), Couleur.NOIR, 2)); // R75
        routes.add(new RouteTerrestre(villes.get("Hamburg"), villes.get("Moskva"), Couleur.BLANC, 2)); // R76
        routes.add(new RouteMaritime(villes.get("Hong Kong"), villes.get("Manila"), Couleur.VIOLET, 1)); // R77
        routes.add(new RouteMaritime(villes.get("Hong Kong"), villes.get("Tokyo"), Couleur.GRIS, 3)); // R78
        routes.add(new RouteMaritime(villes.get("Honolulu"), villes.get("Lima"), Couleur.GRIS, 6)); // R79
        routes.add(new RouteMaritime(villes.get("Honolulu"), villes.get("Los Angeles"), Couleur.JAUNE, 3)); // R80
        routes.add(new RouteMaritime(villes.get("Honolulu"), villes.get("Manila"), Couleur.BLANC, 5)); // R81
        routes.add(new RouteMaritime(villes.get("Honolulu"), villes.get("Port Moresby"), Couleur.VERT, 3)); // R82
        routes.add(new RouteMaritime(villes.get("Honolulu"), villes.get("Tokyo"), Couleur.ROUGE, 5)); // R83
        routes.add(new RouteMaritime(villes.get("Jakarta"), villes.get("Manila"), Couleur.GRIS, 2)); // R84
        routes.add(new RouteMaritime(villes.get("Jakarta"), villes.get("Perth"), Couleur.GRIS, 3)); // R85
        routes.add(new RouteTerrestre(villes.get("Lagos"), villes.get("Luanda"), Couleur.JAUNE, 1)); // R86
        routes.add(new RouteTerrestre(villes.get("Lagos"), villes.get("Luanda"), Couleur.VIOLET, 1)); // R87
        routes.add(new RouteTerrestre(villes.get("Lahore"), villes.get("Mumbai"), Couleur.NOIR, 1)); // R88
        routes.add(new RouteTerrestre(villes.get("Lahore"), villes.get("Mumbai"), Couleur.VERT, 1)); // R89
        routes.add(new RouteTerrestre(villes.get("Lahore"), villes.get("Novosibirsk"), Couleur.BLANC, 2)); // R90
        routes.add(new RoutePaire(villes.get("Lahore"), villes.get("Tehran"), 2)); // R91
        routes.add(new RouteMaritime(villes.get("Lima"), villes.get("Sydney"), Couleur.NOIR, 8)); // R92
        routes.add(new RouteMaritime(villes.get("Lima"), villes.get("Sydney"), Couleur.VIOLET, 8)); // R93
        routes.add(new RouteTerrestre(villes.get("Lima"), villes.get("Valparaiso"), Couleur.GRIS, 2)); // R94
        routes.add(new RouteTerrestre(villes.get("Lima"), villes.get("Valparaiso"), Couleur.GRIS, 2)); // R95
        routes.add(new RouteTerrestre(villes.get("Los Angeles"), villes.get("Mexico"), Couleur.BLANC, 2)); // R96
        routes.add(new RouteTerrestre(villes.get("Los Angeles"), villes.get("Mexico"), Couleur.JAUNE, 2)); // R97
        routes.add(new RouteTerrestre(villes.get("Los Angeles"), villes.get("New York"), Couleur.NOIR, 4)); // R98
        routes.add(new RouteTerrestre(villes.get("Los Angeles"), villes.get("New York"), Couleur.VIOLET, 4)); // R99
        routes.add(new RouteMaritime(villes.get("Los Angeles"), villes.get("Tokyo"), Couleur.NOIR, 7)); // R100
        routes.add(new RouteMaritime(villes.get("Los Angeles"), villes.get("Tokyo"), Couleur.VERT, 7)); // R101
        routes.add(new RouteTerrestre(villes.get("Los Angeles"), villes.get("Vancouver"), Couleur.ROUGE, 1)); // R102
        routes.add(new RouteTerrestre(villes.get("Los Angeles"), villes.get("Vancouver"), Couleur.VERT, 1)); // R103
        routes.add(new RouteTerrestre(villes.get("Los Angeles"), villes.get("Winnipeg"), Couleur.GRIS, 3)); // R104
        routes.add(new RouteMaritime(villes.get("Luanda"), villes.get("Rio de Janeiro"), Couleur.GRIS, 6)); // R105
        routes.add(new RouteMaritime(villes.get("Manila"), villes.get("Tokyo"), Couleur.JAUNE, 2)); // R106
        routes.add(new RouteTerrestre(villes.get("Miami"), villes.get("New York"), Couleur.BLANC, 2)); // R107
        routes.add(new RouteTerrestre(villes.get("Moskva"), villes.get("Murmansk"), Couleur.VIOLET, 2)); // R108
        routes.add(new RouteTerrestre(villes.get("Moskva"), villes.get("Novosibirsk"), Couleur.VERT, 4)); // R109
        routes.add(new RouteTerrestre(villes.get("Moskva"), villes.get("Novosibirsk"), Couleur.JAUNE, 4)); // R110
        routes.add(new RouteTerrestre(villes.get("Moskva"), villes.get("Tehran"), Couleur.ROUGE, 3)); // R111
        routes.add(new RouteTerrestre(villes.get("Mumbai"), villes.get("Tehran"), Couleur.BLANC, 3)); // R112
        routes.add(new RouteTerrestre(villes.get("Mumbai"), villes.get("Tehran"), Couleur.VIOLET, 3)); // R113
        routes.add(new RouteMaritime(villes.get("Murmansk"), villes.get("Reykjavik"), Couleur.VERT, 4)); // R114
        routes.add(new RouteMaritime(villes.get("Murmansk"), villes.get("Tiksi"), Couleur.ROUGE, 7)); // R115
        routes.add(new RouteMaritime(villes.get("New York"), villes.get("Reykjavik"), Couleur.JAUNE, 6)); // R116
        routes.add(new RouteTerrestre(villes.get("New York"), villes.get("Winnipeg"), Couleur.VERT, 2)); // R117
        routes.add(new RouteTerrestre(villes.get("Novosibirsk"), villes.get("Tiksi"), Couleur.GRIS, 3)); // R118
        routes.add(new RouteTerrestre(villes.get("Novosibirsk"), villes.get("Yakutsk"), Couleur.VIOLET, 3)); // R119
        routes.add(new RouteMaritime(villes.get("Perth"), villes.get("Port-aux-Francais"), Couleur.BLANC, 5)); // R120
        routes.add(new RouteMaritime(villes.get("Perth"), villes.get("Port-aux-Francais"), Couleur.VIOLET, 5)); // R121
        routes.add(new RouteTerrestre(villes.get("Perth"), villes.get("Sydney"), Couleur.BLANC, 2)); // R122
        routes.add(new RouteTerrestre(villes.get("Perth"), villes.get("Sydney"), Couleur.JAUNE, 2)); // R123
        routes.add(new RouteMaritime(villes.get("Petropavlovsk"), villes.get("Tiksi"), Couleur.NOIR, 7)); // R124
        routes.add(new RouteMaritime(villes.get("Petropavlovsk"), villes.get("Tokyo"), Couleur.GRIS, 2)); // R125
        routes.add(new RouteTerrestre(villes.get("Petropavlovsk"), villes.get("Yakutsk"), Couleur.BLANC, 3)); // R126
        routes.add(new RouteMaritime(villes.get("Port Moresby"), villes.get("Sydney"), Couleur.JAUNE, 3)); // R127
        routes.add(new RouteTerrestre(villes.get("Tiksi"), villes.get("Yakutsk"), Couleur.VERT, 1)); // R128
        routes.add(new RouteMaritime(villes.get("Tokyo"), villes.get("Vancouver"), Couleur.BLANC, 6)); // R129
        routes.add(new RouteTerrestre(villes.get("Vancouver"), villes.get("Winnipeg"), Couleur.JAUNE, 2)); // R130

        for (int i = 0; i < routes.size(); i++) {
            Route r1 = routes.get(i);
            for (int j = i + 1; j < routes.size(); j++) {
                Route r2 = routes.get(j);
                if (r1.getVille1().equals(r2.getVille1()) && r1.getVille2().equals(r2.getVille2())) {
                    r1.setRouteParallele(r2);
                    r2.setRouteParallele(r1);
                }
            }
        }
        return new Plateau(new ArrayList<>(villes.values()), routes);
    }
}
