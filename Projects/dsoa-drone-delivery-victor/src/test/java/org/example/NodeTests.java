
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NodeTests {
    @Test
    void testCreateGraph() {
        var g = GraphData.createGraph();
        assertEquals(15, g.size());
    }

    @Test
    void testCalculateH() {
        Node n1 = new Node("n1", 60.2007, 24.9662);
        Node n2 = new Node("n2", 60.1593, 24.9207);

        assertEquals("5,25", String.format("%.2f", n1.calculateH(n2)));
    }
}