import java.util.ArrayList;

public class UI {
    public static String listNodesAndLinks(ArrayList<Node> nodes) {
        StringBuilder output = new StringBuilder();
        output.append("NOD    NAMN              GRANNAR\n");
        for (Node node : nodes) {
            output.append("[").append(node.getKey()).append("] ").append(node.getName()).append("    ");
            for (Node neighbor : node.getNeighbors()) {
                output.append("[").append(neighbor.getKey()).append("] ");
            }
            output.append("\n");
        }
        return output.toString();
    }
}
