import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class GraphData {

    public static ArrayList<Node> createGraph() {

        /**
         * Noderna instansieras med namn och koordinater i en
         * HashMap med en kortare sträng som nyckel (ID)
         *
         */
        LinkedHashMap<String, Node> mapData = new LinkedHashMap<>();

        /** Temporär lista för nodernas grannar */
        HashMap<String, String[]> neighbors = new HashMap<>();

        //         key              name                        lat      lon
        mapData.put("sorn", new Node("Sörnäs uni.bibliotek     ", 60.1802, 24.9592)); // 0
        mapData.put("bole", new Node("Böle bibliotek           ", 60.2008, 24.9355)); // 1
        mapData.put("arca", new Node("Arcadas bibliotek        ", 60.2007, 24.9662)); // 2
        mapData.put("tolo", new Node("Tölö bibliotek           ", 60.1833, 24.9175)); // 3
        mapData.put("diak", new Node("Diaks bibliotek          ", 60.1983, 24.9707)); // 4
        mapData.put("vall", new Node("Vallgårds bibliotek      ", 60.1923, 24.9626)); // 5
        mapData.put("mejl", new Node("Mejlans uni.bibliotek    ", 60.1901, 24.9072)); // 6
        mapData.put("berg", new Node("Berghälls bibliotek      ", 60.1837, 24.9536)); // 7
        mapData.put("oodi", new Node("Centrumbiblioteket Ode   ", 60.1741, 24.9382)); // 8
        mapData.put("hels", new Node("Helsingfors uni.bibliotek", 60.1709, 24.9493)); // 9
        mapData.put("hank", new Node("Hankens bibliotek        ", 60.1707, 24.9241)); // 10
        mapData.put("rich", new Node("Richardsgatans bibliotek ", 60.1663, 24.9468)); // 11
        mapData.put("bush", new Node("Busholmens bibliotek     ", 60.1593, 24.9207)); // 12
        mapData.put("gumt", new Node("Gumtäkts uni.bibliotek   ", 60.2039, 24.9638)); // 13
        mapData.put("lill", new Node("Lillhoplax bibliotek     ", 60.2008, 24.8954)); // 14

        /**
         * Data för nodernas grannar enligt linjerna på kartan. HashMap med ID
         * och en enkel String[]-array för grannarnas ID.
         */
        neighbors.put("sorn", new String[]{"berg"});
        neighbors.put("bole", new String[]{"lill", "tolo", "gumt"});
        neighbors.put("arca", new String[]{"gumt", "diak"});
        neighbors.put("tolo", new String[]{"bole", "mejl", "berg", "oodi", "hank"});
        neighbors.put("diak", new String[]{"arca", "vall"});
        neighbors.put("vall", new String[]{"diak", "berg"});
        neighbors.put("mejl", new String[]{"lill", "tolo"});
        neighbors.put("berg", new String[]{"vall", "tolo", "sorn", "oodi"});
        neighbors.put("oodi", new String[]{"tolo", "berg", "hank", "hels"});
        neighbors.put("hels", new String[]{"oodi", "rich"});
        neighbors.put("hank", new String[]{"tolo", "oodi", "bush"});
        neighbors.put("rich", new String[]{"hels", "bush"});
        neighbors.put("bush", new String[]{"hank", "rich"});
        neighbors.put("gumt", new String[]{"bole", "arca"});
        neighbors.put("lill", new String[]{"bole", "mejl"});


        /**
         * Iterera noderna enligt ID och lägg till grannarna
         */
        for (String key : mapData.keySet()) {

            /* Ange nyckeln (t.ex. "bole") som ID åt noden, kan vara bra att ha för UI  */
            mapData.get(key).setKey(key);

            /* Iterera nodens grannar och lägg till dem till noden */
            for (String neighbor : neighbors.get(key)) {
                mapData.get(key).addNeighbor(mapData.get(neighbor));
            }

        }

        return new ArrayList<Node>(mapData.values());
    }
}