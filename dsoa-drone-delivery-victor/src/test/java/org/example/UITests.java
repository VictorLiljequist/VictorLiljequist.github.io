
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UITests {
    @Test
    void testGetNodeByKey() {
        ArrayList<Node> g = GraphData.createGraph();
        assertEquals("arca", Utils.getNodeByKey(g, "arca").getKey());
        assertEquals("hank", Utils.getNodeByKey(g, "hank").getKey());
    }

}


