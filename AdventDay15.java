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
import java.util.Set;

import static java.util.stream.Collectors.toCollection;

public class AdventDay15 {
    private String[] file;

    private class Graph {
        private Map<String, HashMap<String, Integer>> graph;

        public Graph() {
            this.graph = new HashMap<String, HashMap<String, Integer>>();
        }

        public void setEdge(String a, String b, int weight) {
            HashMap<String, Integer> edges = graph.get(a);
            edges.put(b, weight);
        }

        public int numOfEdges(String a) {
            return graph.get(a).size();
        }

        public HashMap<String, Integer> getEdge(String a) {
            return graph.get(a);
        }

        public void addVertex(String a) {
            graph.put(a, new HashMap<String, Integer>());
        }

        public boolean containVertex(String a) {
            return graph.containsKey(a);
        }

        public void removeEdge(String a, String b) {
            graph.get(a).remove(b);
        }

        public void adjustEdge(String a, String b) {
            if (numOfEdges(a) > 1) {
                Set<String> edges = getEdge(a).keySet();
                ArrayList<String> toDelete = new ArrayList<String>();

                for (String key : edges) {
                    if (!key.equals(b))
                        toDelete.add(key);
                }
                toDelete.forEach(k -> {
                    removeEdge(k, a);
                    removeEdge(a, k);
                });
            }
        }
    }

    private class Range {
        private int firstLow;
        private int firstHigh;
        private int secLow;
        private int secHigh;

        public Range(int firstLow, int firstHigh, int secLow, int secHigh) {
            this.firstLow = firstLow;
            this.firstHigh = firstHigh;
            this.secLow = secLow;
            this.secHigh = secHigh;
        }

        public boolean inRange(int val) {
            return (val >= this.firstLow && val <= this.firstHigh)
                    || (val >= this.secLow && val <= this.secHigh);
        }

        public void getRanges() {
            StdOut.printf("fl: %d, fh: %d, sl: %d, sh: %d", this.firstLow, this.firstHigh,
                          this.secLow, this.secHigh);
        }
    }

    public AdventDay15(String[] file) {
        this.file = file;
    }

    private int checkTicket(ArrayList<Integer> a, Map<String, Range> m) {
        int res = 0;

        for (int i = 0; i < a.size(); i++) {
            boolean checked = false;
            for (String key : m.keySet()) {
                if (m.get(key).inRange(a.get(i))) {
                    checked = true;
                    break;
                }
            }
            if (!checked) res += a.get(i);
        }
        return res;
    }

    private ArrayList<Integer> parseYourTicket(String line) {
        return Arrays.stream(line.split(","))
                     .mapToInt(Integer::parseInt).boxed()
                     .collect(toCollection(ArrayList::new));
    }

    private int parseTickets(String line,
                             ArrayList<ArrayList<Integer>> tickets,
                             Map<String, Range> m) {
        ArrayList<Integer> arr = Arrays.stream(line.split(","))
                                       .mapToInt(Integer::parseInt).boxed()
                                       .collect(toCollection(ArrayList::new));

        int res = checkTicket(arr, m);
        if (res <= 0) tickets.add(arr);
        return res;
    }

    private void parseFields(String line, Map<String, Range> m) {
        String[] str = line.split("-");
        String key = str[0].split(":")[0].trim();
        Range r = new Range(Integer.parseInt(str[0].split(":")[1].trim()),
                            Integer.parseInt(str[1].split("or")[0].trim()),
                            Integer.parseInt(str[1].split("or")[1].trim()),
                            Integer.parseInt(str[2].trim()));
        m.put(key, r);
    }

    private Graph createGraph(ArrayList<ArrayList<Integer>> tickets, Map<String, Range> rangeMap) {
        Graph g = new Graph();
        Map<String, Range> checkMap = new HashMap<String, Range>(rangeMap);

        for (int i = 0; i < tickets.get(0).size(); i++) {
            Map<String, Range> checkMapMod = new HashMap<String, Range>(checkMap);
            String col = String.valueOf(i);
            g.addVertex(col);
            ArrayList<String> toDelete = new ArrayList<String>();

            for (int j = 0; j < tickets.size(); j++) {
                for (String key : checkMapMod.keySet()) {
                    if (!checkMapMod.get(key).inRange(tickets.get(j).get(i))) {
                        toDelete.add(key);
                    }
                }

                toDelete.forEach(checkMapMod::remove);
                if (checkMapMod.size() == 1) break;
            }

            Set<String> keys = checkMapMod.keySet();

            for (String key : keys) {
                if (!g.containVertex(key)) g.addVertex(key);
                g.setEdge(key, col, 0);
                g.setEdge(col, key, 0);

                if (checkMapMod.size() == 1) {
                    checkMap.remove(key);
                }
            }
        }
        return g;
    }

    public void findRes() {
        int invalidTickets = 0;
        long res = 1;
        Graph g;
        ArrayList<Integer> yourTicket = new ArrayList<Integer>();
        Map<String, Range> rangeMap = new HashMap<String, Range>();
        ArrayList<ArrayList<Integer>> tickets = new ArrayList<ArrayList<Integer>>();
        Map<String, Integer> mapOfFields = new HashMap<String, Integer>();
        int i = 0;

        while (i < file.length) {
            if (file[i].contains("-")) {
                parseFields(file[i], rangeMap);
            }
            else if (file[i].contains("your ticket")) {
                yourTicket = parseYourTicket(file[++i]);
            }
            else if (file[i].contains(",")) {
                invalidTickets += parseTickets(file[i], tickets, rangeMap);
            }
            i++;
        }

        g = createGraph(tickets, rangeMap);
        int j = -1;
        int oneEdge = 0;

        while (oneEdge < tickets.get(0).size() - 1) {
            j = j >= tickets.get(0).size() - 1 ? 0 : j + 1;

            String col = String.valueOf(j);
            if (j == 0) oneEdge = 0;
            if (g.numOfEdges(col) == 1) {
                oneEdge++;
                String edge = g.getEdge(col).keySet().iterator().next();
                if (!mapOfFields.containsKey(edge)) {
                    mapOfFields.put(edge, j);
                    g.adjustEdge(edge, col);
                }
            }
        }

        for (String k : mapOfFields.keySet()) {
            if (k.contains("departure")) res *= yourTicket.get(mapOfFields.get(k));
        }

        StdOut.println(res);
        StdOut.println(invalidTickets);
    }

    public static void main(String[] args) {
        AdventDay15 a = new AdventDay15(StdIn.readAllLines());
        a.findRes();
    }
}
