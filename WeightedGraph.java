/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class WeightedGraph<Item> {
    private Map<Item, HashMap<Item, Integer>> graph;
    private Map<Item, Integer> vertexOrder;
    private int length = 0;

    public WeightedGraph() {
        graph = new HashMap<Item, HashMap<Item, Integer>>();
        vertexOrder = new HashMap<Item, Integer>();
    }

    public void setEdge(Item a, Item b, int weight) {
        HashMap<Item, Integer> edges = graph.get(a);
        edges.put(b, weight);
    }

    public HashMap<Item, Integer> getEdge(Item a) {
        return graph.get(a);
    }

    public void addVertex(Item a) {
        vertexOrder.put(a, length);
        length++;
        graph.put(a, new HashMap<Item, Integer>());
    }

    private int numOfWeightsRec(Item a) {
        int res = 0;

        Set<Item> edges = graph.get(a).keySet();
        
        if (edges.isEmpty()) return 1;

        for (Item i : edges) {
            res += graph.get(a).get(i) * numOfWeightsRec(i);
        }

        return res + 1;
    }

    public int numOfWeights(Item a) {
        return numOfWeightsRec(a) - 1;
    }

    public int numofConnections(Item a) {
        LinkedList<Item> temp = new LinkedList<Item>();
        int res = 0;
        boolean[] visited = new boolean[length];
        visited[vertexOrder.get(a)] = true;
        temp.add(a);

        while (temp.size() > 0) {
            Item s = temp.poll();
            Set<Item> edges = graph.get(s).keySet();
            for (Item i : edges) {
                if (!visited[vertexOrder.get(i)]) {
                    res++;
                    visited[vertexOrder.get(i)] = true;
                    temp.add(i);
                }
            }
        }

        return res;
    }

    public boolean containEdge(Item a, Item b) {
        return graph.get(a).containsKey(b);
    }

    public boolean containVertex(Item a) {
        return graph.containsKey(a);
    }

    public static void main(String[] args) {

    }
}
