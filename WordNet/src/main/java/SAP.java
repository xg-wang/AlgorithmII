import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

/**
 * Created by Xingan Wang on 10/28/17.
 */
public class SAP {

    private final Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("Digraph input is null.");
        }
        digraph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(digraph, w);
        return findLength(bfsv, bfsw);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(digraph, w);
        return findAncestor(bfsv, bfsw);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(digraph, w);
        return findLength(bfsv, bfsw);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(digraph, w);
        return findAncestor(bfsv, bfsw);
    }

    private int findLength(BreadthFirstDirectedPaths bfsv, BreadthFirstDirectedPaths bfsw) {
        int shortest = Integer.MAX_VALUE;
        for (int i = 0; i < digraph.V(); i++) {
            int vd = bfsv.distTo(i);
            if (vd >= shortest) continue;
            int wd = bfsw.distTo(i);
            if (wd >= shortest) continue;
            shortest = Math.min(shortest, vd + wd);
        }
        return shortest == Integer.MAX_VALUE ? -1 : shortest;
    }

    private int findAncestor(BreadthFirstDirectedPaths bfsv, BreadthFirstDirectedPaths bfsw) {
        int shortest = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int i = 0; i < digraph.V(); i++) {
            int vd = bfsv.distTo(i);
            if (vd >= shortest) continue;
            int wd = bfsw.distTo(i);
            if (wd >= shortest) continue;
            if (vd + wd < shortest) {
                shortest = vd + wd;
                ancestor = i;
            }
        }
        return ancestor;
    }

}
