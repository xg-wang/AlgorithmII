import edu.princeton.cs.algs4.In;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by Xingan Wang on 10/30/17.
 */
public class OutcastTest {

    private Outcast outcast;

    @BeforeClass
    public static void init() {
        System.out.println("Testing Outcast...");
    }

    @Before
    public void initializeOutcast() {
        WordNet wordNet = new WordNet("src/test/resources/wordnet/synsets.txt", "src/test/resources/wordnet/hypernyms.txt");
        outcast = new Outcast(wordNet);
    }

    @Test
    public void evaluateOutcast() {
        In in = new In("src/test/resources/wordnet/outcast5.txt");
        String[] nouns = in.readAllStrings();
        assertEquals("outcast5 is table", "table", outcast.outcast(nouns));
    }
}
