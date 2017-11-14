import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;

import java.util.*;

/**
 * Created by Xingan Wang on 11/12/17.
 */
public class BaseballElimination {

    private final String[] teams;
    private final Map<String, Integer> teamId;
    private final Map<String, List<String>> R;
    private final int[] w;
    private final int[] l;
    private final int[] r;
    private final int[][] g;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        int n = in.readInt();
        teams = new String[n];
        teamId = new HashMap<>();
        R = new HashMap<>();
        w = new int[n];
        l = new int[n];
        r = new int[n];
        g = new int[n][n];
        for (int i = 0; i < n; i++) {
            if (!in.hasNextLine()) {
                throw new IllegalArgumentException("invalid input");
            }
            teams[i] = in.readString();
            teamId.put(teams[i], i);
            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();
            for (int j = 0; j < n; j++) {
                g[i][j] = in.readInt();
            }
        }
    }

    public int numberOfTeams() {
        return teams.length;
    }

    public Iterable<String> teams() {
        return Arrays.asList(teams);
    }

    public int wins(String team) {
        checkTeam(team);
        return w[teamId.get(team)];
    }

    public int losses(String team) {
        checkTeam(team);
        return l[teamId.get(team)];
    }

    public int remaining(String team) {
        checkTeam(team);
        return r[teamId.get(team)];
    }

    public int against(String team1, String team2) {
        checkTeam(team1);
        checkTeam(team2);
        return g[teamId.get(team1)][teamId.get(team2)];
    }

    public boolean isEliminated(String team) {
        checkTeam(team);
        if (!R.containsKey(team)) {
            R.put(team, eliminate(team));
        }
        return R.get(team).size() != 0;
    }

    public Iterable<String> certificateOfElimination(String team) {
        checkTeam(team);
        if (!R.containsKey(team)) {
            R.put(team, eliminate(team));
        }
        return R.get(team);
    }

    private List<String> eliminate(String team) {
        // trivial
        int x = teamId.get(team);
        for (int i = 0; i < teams.length; i++) {
            if (x == i) continue;
            if (w[x] + r[x] < w[i]) {
                return Collections.singletonList(teams[i]);
            }
        }
        // nontrivial
        // s: 0
        // i-j: 1 -- (n-2)!
        // i: (n-2)!+1 -- (n-2)!+(n-1)
        int n = teams.length;
        if (n <= 2) return Collections.emptyList();
        int sum = sum(n - 2);
        int t = sum + n - 1 + 1;
        FlowNetwork fn = new FlowNetwork(1 + t);
        int[] teamVIdx = new int[n];
        for (int i = 0; i < n; i++) {
            if (i != x) {
                int idx = i <= x ? i : i - 1;
                teamVIdx[i] = sum + idx + 1;
                fn.addEdge(new FlowEdge(teamVIdx[i], t, w[x] + r[x] - w[i]));
            }
        }
        int idx = 1;
        for (int i = 0; i < n - 1; i++) {
            if (i == x) continue;
            for (int j = i + 1; j < n; j++) {
                if (j == x) continue;
                fn.addEdge(new FlowEdge(0, idx, g[i][j]));
                fn.addEdge(new FlowEdge(idx, teamVIdx[i], Double.POSITIVE_INFINITY));
                fn.addEdge(new FlowEdge(idx, teamVIdx[j], Double.POSITIVE_INFINITY));
                idx++;
            }
        }
        FordFulkerson ff = new FordFulkerson(fn, 0, t);
        idx = 1;
        Set<String> retR = new HashSet<>();
        for (int i = 0; i < n - 1; i++) {
            if (i == x) continue;
            for (int j = i + 1; j < n; j++) {
                if (j == x) continue;
                if (ff.inCut(idx)) {
                    retR.add(teams[i]);
                    retR.add(teams[j]);
                }
                idx++;
            }
        }
        return new ArrayList<>(retR);
    }

    private void checkTeam(String t) {
        if (t == null || !teamId.containsKey(t))
            throw new IllegalArgumentException("invalid team input");
    }

    private int sum(int n) {
        return (1 + n) * n / 2;
    }

}
