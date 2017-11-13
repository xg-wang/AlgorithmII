import edu.princeton.cs.algs4.In;

/**
 * Created by Xingan Wang on 11/12/17.
 */
public class BaseballElimination {
    public BaseballElimination(String filename) {
        In in = new In(filename);
    }
    public int numberOfTeams() {

    }                        // number of teams
    public Iterable<String> teams() {

    }                                // all teams
    public int wins(String team) {

    } // number of wins for given team
    public int losses(String team) {

    }                    // number of losses for given team
    public int remaining(String team) {

    }                 // number of remaining games for given team
    public int against(String team1, String team2) {

    } // number of remaining games between team1 and team2
    public boolean isEliminated(String team) {

    }           // is given team eliminated?
    public Iterable<String> certificateOfElimination(String team) {

    } // subset R of teams that eliminates given team; null if not eliminated

}
