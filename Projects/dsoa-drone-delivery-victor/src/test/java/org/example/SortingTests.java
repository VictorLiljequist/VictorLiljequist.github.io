

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SortingTests {
    @Test
    void testSortByName() {
        ArrayList<Node> g = GraphData.createGraph();
        var s = Utils.sortNodesByName(g);
        assertEquals("arca", s.get(0).getKey());
        assertEquals("hank", s.get(7).getKey());
        assertEquals("vall", s.get(s.size()-1).getKey());
    }

    @Test
    void testSortByLat() {
        ArrayList<Node> g = GraphData.createGraph();
        var s = Utils.sortNodesByLat(g);
        assertEquals("gumt", s.get(0).getKey());
        assertEquals("berg", s.get(7).getKey());
        assertEquals("bush", s.get(s.size()-1).getKey());
    }

    @Test
    void testSortByNameShouldMakeCopy() {
        // sorting should make copy, not change the original array
        ArrayList<Node> g = GraphData.createGraph();
        var s = Utils.sortNodesByName(g);
        assertNotEquals(g, s);
    }

    @Test
    void testSortByLatShouldMakeCopy() {
        // sorting should make copy, not change the original array
        ArrayList<Node> g = GraphData.createGraph();
        var s = Utils.sortNodesByLat(g);
        assertNotEquals(g, s);
    }
}


