import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.TopologicalX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Xingan Wang on 10/28/17.
 */
public class WordNet {

    private final SAP sap;
    // synset can be mapped to multiple ids
    private final HashMap<String, List<Integer>> synsetMap = new HashMap<>();
    // noun can be included in multiple synsets
    private final HashMap<String, List<String>> nounMap = new HashMap<>();
    // id map to exactly one synset
    private final HashMap<Integer, String> idMap = new HashMap<>();

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        In synsetsIn = new In(synsets);
        In hypernymsIn = new In(hypernyms);
        String line;
        String[] split;
        int v = 0;
        while ((line = synsetsIn.readLine()) != null) {
            split = line.split(",");
            if (split.length < 3) {
                throw new IllegalStateException("Each line should have 3 elements.");
            }
            int id = Integer.parseInt(split[0]);
            String synset = split[1];
            for (String n : synset.split(" ")) {
                List<String> currSynsets = nounMap.getOrDefault(n, new ArrayList<>());
                currSynsets.add(synset);
                nounMap.put(n, currSynsets);
            }
            idMap.put(id, synset);
            List<Integer> list = synsetMap.getOrDefault(synset, new ArrayList<>());
            list.add(id);
            synsetMap.put(synset, list);
            v++;
        }
        Digraph G = new Digraph(v);
        while ((line = hypernymsIn.readLine()) != null) {
            split = line.split(",");
            if (split.length < 1) throw new IllegalStateException("Hypernyms line should have at least 1 nums");
            int sourceId = Integer.parseInt(split[0]);
            for (int i = 1; i < split.length; i++) {
                G.addEdge(sourceId, Integer.parseInt(split[i]));
            }
        }
        if (!(new TopologicalX(G).hasOrder()))
            throw new IllegalArgumentException("Graph not DAG.");
        sap = new SAP(G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException("word is null");
        return nounMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        Iterable<Integer> idsA = nounToIds(nounA);
        Iterable<Integer> idsB = nounToIds(nounB);
        return sap.length(idsA, idsB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        Iterable<Integer> idsA = nounToIds(nounA);
        Iterable<Integer> idsB = nounToIds(nounB);
        return idMap.get(sap.ancestor(idsA, idsB));
    }

    private Iterable<Integer> nounToIds(String noun) {
        if (noun == null || !nounMap.containsKey(noun))
            throw new IllegalArgumentException("noun is invalid");
        List<Integer> ids = new ArrayList<>();
        for (String synset : nounMap.get(noun)) {
            ids.addAll(synsetMap.get(synset));
        }
        return ids;
    }

}
