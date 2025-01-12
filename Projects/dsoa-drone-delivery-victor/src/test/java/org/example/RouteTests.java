

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RouteTests {
    @Test
    void routeTestArcaHank() {
        ArrayList<Node> g = GraphData.createGraph();
        ArrayList<Node> r = (new Route(g.get(2), g.get(10))).aStar();
        // Correct amount of stops
        assertEquals(6, r.size());

        // Check correct mid route
        assertEquals(g.get(4).getName(), r.get(1).getName());
        assertEquals(g.get(7).getName(), r.get(3).getName());
    }

    @Test
    void routeTestRichBole() {
        ArrayList<Node> g = GraphData.createGraph();
        ArrayList<Node> r = (new Route(g.get(11), g.get(1))).aStar();

        // Correct amount of stops
        assertEquals(5, r.size());

        // Check correct mid route
        assertEquals(g.get(9).getName(), r.get(1).getName());
        assertEquals(g.get(3).getName(), r.get(3).getName());
    }
}