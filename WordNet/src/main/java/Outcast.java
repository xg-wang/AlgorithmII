import java.util.Arrays;

/**
 * Created by Xingan Wang on 10/28/17.
 */
public class Outcast {

    private final WordNet wordNet;

    public Outcast(WordNet net) {
        wordNet = net;
    }

    public String outcast(String[] nouns) {
        int maxDist = Integer.MIN_VALUE;
        int maxIdx = -1;
        for (int i = 0; i < nouns.length; i++) {
            if (!wordNet.isNoun(nouns[i])) {
                throw new IllegalArgumentException("input noun invalid.");
            }
        }
        for (int i = 0; i < nouns.length; i++) {
            String noun = nouns[i];
            int dist = Arrays.stream(nouns).mapToInt(n -> wordNet.distance(n, noun)).sum();
            if (dist > maxDist) {
                maxDist = dist;
                maxIdx = i;
            }
        }
        return nouns[maxIdx];
    }

}
