import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Set;
import java.util.HashSet;

/**
 * Created by Xingan Wang on 1/30/18.
 */
public class BoggleSolver
{
    private final TrieSET trie;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        trie = new TrieSET();
        for (String s : dictionary) {
            trie.add(s);
        }
    }

    private void mergeValidWordsFrom(TrieSET.Node node, String prefix, int currRow, int currCol, BoggleBoard b, Set<String> words, boolean[][] visited) {
        node = trie.queryPrefix(node, prefix);
        if (prefix.length() >= 3 && trie.contains(prefix)) {
            words.add(prefix);
        }
        if (node == null) {
            return;
        }
        for (int i = Math.max(0, currRow-1); i <= Math.min(currRow+1, b.rows()-1); i++) {
            for (int j = Math.max(0, currCol-1); j <= Math.min(currCol+1, b.cols()-1); j++) {
                if ((i == currRow && j == currCol) || visited[i][j]) continue;
                visited[i][j] = true;
                mergeValidWordsFrom(node, getStr(prefix, b.getLetter(i, j)), i, j, b, words, visited);
                visited[i][j] = false;
            }
        }
    }
    private String getStr(String prefix, char c) {
        return c == 'Q' ? prefix + "QU" : prefix + c;
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Set<String> words = new HashSet<>();
        int nrows = board.rows(), ncols = board.cols();
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                boolean[][] visited = new boolean[nrows][ncols];
                visited[i][j] = true;
                mergeValidWordsFrom(null, getStr("", board.getLetter(i, j)), i, j, board, words, visited);
            }
        }
        return words;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int score = 0, len = word.length();
        if (!trie.contains(word) || len < 3) return score;
        if (len < 5) return 1;
        if (len == 5) return 2;
        if (len == 6) return 3;
        if (len == 7) return 5;
        return 11;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}
