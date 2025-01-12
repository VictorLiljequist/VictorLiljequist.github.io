
import java.util.ArrayList;
import java.util.Scanner;

public class Node {
    private String name;
    private String key;
    private double latitude;
    private double longitude;
    private double f;
    private double g;
    private ArrayList<Node> neighbors;
    private Node previous;

    public Node(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.key = name + "_" + latitude + "_" + longitude;
        this.f = Double.POSITIVE_INFINITY;
        this.g = Double.POSITIVE_INFINITY;
        this.neighbors = new ArrayList<>();
    }

    public void addNeighbor(Node neighbor) {
        this.neighbors.add(neighbor);
    }

    public ArrayList<Node> getNeighbors() {
        return this.neighbors;
    }

    public String getName() {
        return name;
    }

    /*public void setName(String name) {
        this.name = name;
    }
    */
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double calculateH(Node endNode) {
        return Utils.getDistance(this.getLatitude(), this.getLongitude(), endNode.getLatitude(), endNode.getLongitude());
    }
    public static Node getNodeInput(ArrayList<Node> nodes) {
        Scanner scanner = new Scanner(System.in);
        Node node;
        String input = scanner.nextLine();
        node = Utils.getNodeByKey(nodes, input);
        return node;
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }
    public Node getPrevious() {
        return previous;
    }
    public double distanceToPrevious() {
        if (previous != null) {
            return Utils.getDistance(latitude, longitude, previous.getLatitude(), previous.getLongitude());
        }
        return 0.0;
    }

}