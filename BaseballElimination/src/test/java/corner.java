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
public class corner {

    BaseballElimination division;

    @BeforeClass
    public static void init() {
        System.out.println("Testing corner cases..");
    }

    @Before
    public void initBaseballElimination() {
        division = new BaseballElimination("src/test/resources/teams7.txt");
    }

    @Test
    public void evaluateEliminated() {
        assertTrue("Ireland", division.isEliminated("Ireland"));
    }
}
