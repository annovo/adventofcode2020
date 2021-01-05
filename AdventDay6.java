/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdventDay6 {

    private static List<String> parseLine(String str) {
        str = str.replace(".", "");
        str = str.replace("bags", "bag");
        String[] temp = str.split(" contain ");

        List<String> res = new ArrayList<String>();
        res.add(temp[0].trim());

        if (str.contains("no other"))
            return res;

        temp = temp[1].split(", ");
        Collections.addAll(res, temp);
        return res;
    }

    public static void main(String[] args) {
        WeightedGraph<String> graph = new WeightedGraph<String>();
        String goal = "shiny gold bag";
        while (!StdIn.isEmpty()) {
            List<String> str = parseLine(StdIn.readLine());

            if (!graph.containVertex(str.get(0)))
                graph.addVertex(str.get(0));

            if (str.size() > 1) {
                for (int i = 1; i < str.size(); i++) {
                    String part1 = str.get(i).split("[0-9]")[1].trim();
                    if (!graph.containVertex(part1))
                        graph.addVertex(part1);
                    if (!graph.containEdge(str.get(0), part1))
                        graph.setEdge(str.get(0), part1,
                                      Integer.parseInt(str.get(i).split(" ")[0]));
                }
            }

        }
        StdOut.print(graph.numOfWeights(goal));
        StdOut.println();

    }
}
