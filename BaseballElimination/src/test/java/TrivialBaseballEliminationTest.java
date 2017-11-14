import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Xingan Wang on 11/12/17.
 */
public class TrivialBaseballEliminationTest {

    BaseballElimination division;

    @BeforeClass
    public static void init() {
        System.out.println("Testing trivial BaseballElimination...");
    }

    @Before
    public void initBaseballElimination() {
        division = new BaseballElimination("src/test/resources/teams4.txt");
    }

    @Test
    public void evaluateTeams() {
        assertEquals("should have 4 teams", division.numberOfTeams(), 4);
        assertEquals("should match team names", division.teams(), Arrays.asList("Atlanta", "Philadelphia", "New_York", "Montreal"));
    }

    @Test
    public void evaluateEliminated() {
        assertFalse("Atlanta should not be eliminated", division.isEliminated("Atlanta"));
        assertTrue("Philadelphia should be eliminated", division.isEliminated("Philadelphia"));
        assertFalse("New_York should not be eliminated", division.isEliminated("New_York"));
        assertTrue("Montreal should be eliminated", division.isEliminated("Montreal"));
    }

    @Test
    public void evaluateCertificate() {
        HashSet<String> s = new HashSet<String>();
        for (String str : division.certificateOfElimination("Philadelphia")) {
            s.add(str);
        }
        assertEquals("Philadelphia is eliminated by the subset R = { Atlanta New_York }",
                s, new HashSet<>(Arrays.asList("Atlanta", "New_York")));
        assertEquals("Montreal is eliminated by the subset R = { Atlanta }", division.certificateOfElimination("Montreal"), Arrays.asList("Atlanta"));
    }

}
