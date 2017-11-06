import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Xingan Wang on 10/29/17.
 */
public class SAPTest {

    private SAP sap;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void init() {
        System.out.println("Testing SAP...");
    }

    @Before
    public void initializeSAP() {
        In in = new In("src/test/resources/wordnet/digraph1.txt");
        Digraph G = new Digraph(in);
        sap = new SAP(G);
    }

    @Test
    public void evaluateLength() {
        assertEquals("3, 11 should have length 4", 4, sap.length(3, 11));
        assertEquals("9, 12 should have length 3", 3, sap.length(9, 12));
        assertEquals("7, 2 should have length 4", 4, sap.length(7, 2));
    }

    @Test
    public void shoudlThrow() {
        thrown.expect(IllegalArgumentException.class);
        List<Integer> v = Arrays.asList(-1, 2, 7);
        List<Integer> w = Arrays.asList(1, 4, 6, 10, 11);
        sap.length(v, w);
    }

    @Test
    public void evaluateAncestor() {
        assertEquals("3, 11 should have ancestor 1", 1, sap.ancestor(3, 11));
        assertEquals("9, 12 should have ancestor 5", 5, sap.ancestor(9, 12));
        assertEquals("7, 2 should have ancestor 0", 0, sap.ancestor(7, 2));
    }

}
