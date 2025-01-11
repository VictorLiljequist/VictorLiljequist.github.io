
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Route {
    private Node start;
    private Node goal;
    private ArrayList<Node> bestRoute;

    public Route(Node start, Node goal) {
        this.start = start;
        this.goal = goal;
        this.bestRoute = new ArrayList<>();
    }

    public ArrayList<Node> aStar() {
        ArrayList<Node> openNodes = new ArrayList<>();
        openNodes.add(start);
        start.setG(0);
        start.setF(start.calculateH(goal));

        while (!openNodes.isEmpty()) { // Loopar om det finns öppna noder
            Node currentNode = openNodes.stream()      // Får lägsta fnoden från openNodes
                    .min(Comparator.comparingDouble(Node::getF))
                    .orElse(null);
            if (currentNode.equals(goal)) { // Checkar om hittat målet och rekonstruerar rutten annars går vidare
                Node current = goal;
                while (current != start) {
                    bestRoute.add(0, current);
                    current = current.getPrevious();
                }
                bestRoute.add(0, start);
                return bestRoute;
            } else {
                openNodes.remove(currentNode);
                for (Node checkNaapurit : currentNode.getNeighbors()) {
                    double checkG;
                    checkG = currentNode.getG() + Utils.getDistance(currentNode.getLatitude(), currentNode.getLongitude(), checkNaapurit.getLatitude(), checkNaapurit.getLongitude());
                    if (checkG < checkNaapurit.getG()) { // kollar igenom om en bättre rutt finns
                        checkNaapurit.setPrevious(currentNode); // Lägger till den bättre vägen som previous
                        checkNaapurit.setG(checkG); // Updaterar värdet från currentnode till checkNaapurit
                        checkNaapurit.setF(checkG + checkNaapurit.calculateH(goal));// Updaterar värdet för den nya vägen samt lägget till hvärdet

                        if (!openNodes.contains(checkNaapurit)) {
                            openNodes.add(checkNaapurit); // Lägger till den nya noden/vägen om den inte finns redan
                        }

                    }
                }
            }
        }

        return null;
    }

}

