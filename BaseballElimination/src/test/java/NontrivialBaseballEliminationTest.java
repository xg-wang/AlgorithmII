import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Xingan Wang on 11/13/17.
 */
public class NontrivialBaseballEliminationTest {

    BaseballElimination division;

    @BeforeClass
    public static void init() {
        System.out.println("Testing nontrivial BaseballElimination...");
    }

    @Before
    public void initBaseballElimination() {
        division = new BaseballElimination("src/test/resources/teams5.txt");
    }

    @Test
    public void evaluateEliminated() {
        assertFalse("New_York should not be eliminated", division.isEliminated("New_York"));
        assertFalse("Baltimore should be eliminated", division.isEliminated("Baltimore"));
        assertTrue("Detroit should be eliminated", division.isEliminated("Detroit"));
    }

    @Test
    public void evaluateCertificate() {
        HashSet<String> s = new HashSet<String>();
        for (String str : division.certificateOfElimination("Detroit")) {
            s.add(str);
        }
        assertEquals("Detroit is eliminated by the subset R = { New_York Baltimore Boston Toronto }",
                s, new HashSet<String>(Arrays.asList("New_York", "Baltimore", "Boston", "Toronto")));
    }

}
