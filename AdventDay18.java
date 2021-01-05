/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AdventDay18 {
    private String[] file;

    private class Graph {
        private Map<String, ArrayList<ArrayList<String>>> g;

        public Graph() {
            this.g = new HashMap<String, ArrayList<ArrayList<String>>>();
        }

        public void addVertex(String c) {
            this.g.put(c, new ArrayList<ArrayList<String>>());
        }

        public ArrayList<ArrayList<String>> getVertex(String c) {
            return g.get(c);
        }

        public void addEdge(String v) {
            ArrayList<ArrayList<String>> edges = this.g.get(v);
            edges.add(new ArrayList<String>());
        }

        public void updateLastEdge(String v, String e) {
            ArrayList<ArrayList<String>> edges = this.g.get(v);
            edges.get(edges.size() - 1).add(e);
        }
    }

    public AdventDay18(String[] f) {
        this.file = f;
    }

    private String[] parseMessages() {
        return Arrays.stream(this.file).filter(l -> !l.contains(":") && !l.isEmpty())
                     .toArray(String[]::new);
    }

    private void addToGraph(String line, Graph g) {
        String[] s = line.split(":");
        g.addVertex(s[0]);
        if (s[1].contains("\"")) {
            g.addEdge(s[0]);
            g.updateLastEdge(s[0], s[1].split("\"")[1]);
        }
        else {
            Arrays.stream(s[1].split("\\|")).forEach(set -> {
                String[] str = set.split(" ");
                g.addEdge(s[0]);
                Arrays.stream(str).filter(c -> !c.isEmpty())
                      .forEach(e -> g.updateLastEdge(s[0], e));
            });
        }
    }

    private Graph parseInstructions() {
        Graph g = new Graph();
        Arrays.stream(this.file).filter(l -> l.contains(":")).forEach(line -> addToGraph(line, g));
        return g;
    }

    private boolean isNumeric(String s) {
        try {
            int d = Integer.parseInt(s);
        }
        catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private ArrayList<Integer> searchLine(String s, ArrayList<Integer> indexS, Graph g,
                                          String instr) {
        ArrayList<ArrayList<String>> v = g.getVertex(instr);
        ArrayList<Integer> result = new ArrayList<Integer>();

        for (int i = 0; i < v.size(); i++) {
            ArrayList<Integer> r = new ArrayList<Integer>(indexS);
            for (int j = 0; j < v.get(i).size(); j++) {
                if (isNumeric(v.get(i).get(j))) {
                    r = searchLine(s, r, g, v.get(i).get(j));
                }
                else {
                    ArrayList<Integer> t = new ArrayList<Integer>();
                    for (int k = 0; k < indexS.size(); k++) {
                        if (indexS.get(k) > s.length() - 1) continue;
                        if (v.get(i).get(j).equals(s.substring(indexS.get(k), indexS.get(k) + 1)))
                            t.add(indexS.get(k) + 1);
                    }
                    return t;
                }
                if (r.isEmpty()) break;
            }
            if (!r.isEmpty()) result.addAll(r);
        }
        return result;
    }

    public void countLines() {
        int res = 0;
        Graph g = parseInstructions();
        String[] lines = parseMessages();
        for (int i = 0; i < lines.length; i++) {
            ArrayList<Integer> indexS = new ArrayList<Integer>();
            indexS.add(0);
            ArrayList<Integer> searched = new ArrayList<Integer>(
                    searchLine(lines[i], indexS, g, "0"));
            if (searched.contains(lines[i].length())) res++;
        }
        StdOut.println(res);
    }

    public static void main(String[] args) {
        AdventDay18 a = new AdventDay18(StdIn.readAllLines());
        a.countLines();
    }
}
