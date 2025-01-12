import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * Projekt 1 - ruttsökning med A*
 * <p>
 * Datastrukturer och algoritmer
 * <p>
 * Programmeringsteam: Victor Liljequist
 */
public class Main {
    public static void main(String[] args) {
        while (true) {
            ArrayList<Node> nodes = GraphData.createGraph();
            //System.out.println(Utils.listNodesAndLinks(nodes));
            //String output = Utils.listNodesAndLinks(nodes);


            Scanner scanner = new Scanner(System.in);
            System.out.println("Sortera alfabetiskt(1) eller Norr-Söder (2)?");
            int choice = scanner.nextInt();
            scanner.nextLine();

            ArrayList<Node> sortedNodesByName = Utils.sortNodesByName(nodes);
            ArrayList<Node> sortedNodesByLat = Utils.sortNodesByLat(nodes);

            if (choice == 1) {
                System.out.println(UI.listNodesAndLinks(sortedNodesByName));
            }
            if (choice == 2) {
                System.out.println(UI.listNodesAndLinks(sortedNodesByLat));
            }
        /*
        System.out.print("Ange startnod: ");
        String startNodeKey = scanner.nextLine();

        System.out.print("Ange slutnod: ");
        String endNodeKey = scanner.nextLine();

        Node startNode = Utils.getNodeByKey(nodes, startNodeKey);
        Node endNode = Utils.getNodeByKey(nodes, endNodeKey);

        double distance = startNode.calculateH(endNode);
        System.out.println("Geografiskt avstånd: " + distance + " km");

        */
            System.out.print("Ange startNode:");
            Node startRoute = Node.getNodeInput(nodes);
            System.out.print("Ange destinationNode:");
            Node destRoute = Node.getNodeInput(nodes);

            Route route = new Route(startRoute, destRoute);
            ArrayList<Node> shortestRoute = route.aStar();

            double kortRoute = 0;

            for (Node node : shortestRoute) {
                kortRoute += node.distanceToPrevious();
                System.out.println("[" + node.getKey() + "] " + node.getName() + " " + String.format("%.2f", node.distanceToPrevious()) + " km");
            }
            System.out.println("Kortaste rutten:" + String.format("%.2f", kortRoute) + "km");

            System.out.print("Sök pånytt? Y/N: ");
            String input = scanner.nextLine();
            if (!input.equalsIgnoreCase("y")) {
                break;
            }
        }
    }
}

