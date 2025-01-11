
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class Utils {

    /**
     * Metod för att beräkna distansen mellan två geografiska koordinater
     */
    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {

        // Konvertera grader till radians
        lat1 = lat1 * Math.PI / 180.0;
        lon1 = lon1 * Math.PI / 180.0;
        lat2 = lat2 * Math.PI / 180.0;
        lon2 = lon2 * Math.PI / 180.0;

        // Räkna ut distansen med haversinformeln
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1)
                * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.asin(Math.sqrt(a));

        // Jordens radie i km
        double r = 6371;
        // returnera resultatet i km
        return (c * r);

    }

    public static Node getNodeByKey(ArrayList<Node> nodes, String key) {
        for (Node node : nodes) {
            if (node.getKey().equals(key)) {
                return node;
            }
        }
        return null;
    }

    public static ArrayList<Node> sortNodesByName(ArrayList<Node> nodes) {
        ArrayList<Node> sortedNodes = new ArrayList<>(nodes);
        int n = sortedNodes.size();
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < n - i; j++) {
                if (sortedNodes.get(j - 1).getName().compareTo(sortedNodes.get(j).getName()) > 0) {
                    Node temp = sortedNodes.get(j - 1);
                    sortedNodes.set(j - 1, sortedNodes.get(j));
                    sortedNodes.set(j, temp);
                }
            }
        }
        return sortedNodes;
    }
    public static ArrayList<Node> sortNodesByLat(ArrayList<Node> nodes) {
        ArrayList<Node> sortedNodes = new ArrayList<>(nodes);
        int n = sortedNodes.size();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (sortedNodes.get(j).getLatitude() > sortedNodes.get(j + 1).getLatitude()) {
                    Node temp = sortedNodes.get(j);
                    sortedNodes.set(j, sortedNodes.get(j + 1));
                    sortedNodes.set(j + 1, temp);
                }
            }
        }
        Collections.reverse(sortedNodes);
        return sortedNodes;
    }

}
