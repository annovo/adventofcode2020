/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AdventDay20 {
    private String[] file;

    private enum ItemType {
        Allergen, Ingredient
    }

    private class Item {
        private int count;
        private ItemType type;
        private Set<String> connections;
        private boolean established;


        public Item(ItemType t) {
            this.count = 1;
            this.type = t;
            this.connections = new HashSet<String>();
            this.established = false;
        }

        public ItemType getType() {
            return this.type;
        }

        public void oneMore() {
            this.count++;
        }

        public int quantity() {
            return this.count;
        }

        public void addConnection(String i) {
            this.connections.add(i);
        }

        public void establish() {
            this.established = true;
        }

        public boolean isEstablish() {
            return this.established;
        }

        public void removeConnection(String s) {
            this.connections.remove(s);
        }

        public void updateConnections(Set<String> s) {
            this.connections = s;
        }

        public Set<String> getConnections() {
            return this.connections;
        }

        public int numOfConnections() {
            return this.connections.size();
        }
    }


    public AdventDay20(String[] f) {
        this.file = f;
    }

    private ArrayList<Set<String>> findIntersections(Set<String> a, Set<String> b) {
        ArrayList<Set<String>> arr = new ArrayList<Set<String>>();
        Set<String> inter = new HashSet<String>();
        Set<String> exclude = new HashSet<String>();
        for (String k : b) {
            if (a.contains(k)) inter.add(k);
            else exclude.add(k);
        }
        arr.add(inter);
        arr.add(exclude);
        return arr;
    }

    private Map<String, Item> parseFile() {
        Map<String, Item> g = new HashMap<String, Item>();


        for (int i = 0; i < file.length; i++) {
            String[] all = file[i].split(" \\(contains ");
            String[] ingredients = all[0].split(" ");
            String[] allergens = all[1].split(", |\\)");
            boolean firstpass = true;

            for (int k = 0; k < allergens.length; k++) {
                String allerg = allergens[k];
                boolean containAller = g.containsKey(allerg);
                Set<String> connections = new HashSet<String>();
                if (!containAller) {
                    g.put(allerg, new Item(ItemType.Allergen));
                }
                else if (g.get(allerg).isEstablish()) continue;

                for (int j = 0; j < ingredients.length; j++) {
                    String ingr = ingredients[j];
                    if (!g.containsKey(ingr)) {
                        g.put(ingr, new Item(ItemType.Ingredient));
                    }
                    else {
                        Item item = g.get(ingr);
                        if (firstpass) item.oneMore();
                        if (item.isEstablish()) continue;
                    }

                    if (containAller) {
                        if (g.get(allerg).numOfConnections() != 1) connections.add(ingr);
                    }
                    else {
                        Item itemI = g.get(ingr);
                        Item itemA = g.get(allerg);
                        itemA.addConnection(ingr);
                        itemI.addConnection(allerg);
                    }
                }

                firstpass = false;
                if (!connections.isEmpty()) {
                    ArrayList<Set<String>> arr = findIntersections(connections,
                                                                   g.get(allerg).getConnections());
                    for (String food : arr.get(1)) {
                        g.get(food).removeConnection(allerg);
                    }

                    Set<String> inter = arr.get(0);
                    if (inter.size() == 1) {
                        String f = inter.iterator().next();
                        g.get(allerg).establish();
                        g.get(f).establish();
                        for (String al : g.get(f).getConnections()) {
                            g.get(al).removeConnection(f);
                        }
                        Set<String> newAl = new HashSet<String>();
                        newAl.add(allerg);
                        g.get(f).updateConnections(newAl);
                    }
                    g.get(allerg).updateConnections(inter);
                }
            }

            if (firstpass) {
                for (int j = 0; j < ingredients.length; j++) {
                    String ingr = ingredients[j];
                    if (!g.containsKey(ingr)) {
                        g.put(ingr, new Item(ItemType.Ingredient));
                    }
                    else {
                        Item item = g.get(ingr);
                        item.oneMore();
                    }
                }
            }
        }
        return g;
    }

    private void refineConnections(Map<String, Item> g) {
        for (String k : g.keySet()) {
            Item i = g.get(k);
            if (i.numOfConnections() == 1 && !i.isEstablish()) {
                i.establish();
                String f = i.getConnections().iterator().next();
                g.get(f).establish();
                for (String al : g.get(f).getConnections()) {
                    if (!al.equals(k)) g.get(al).removeConnection(f);
                }
                Set<String> newAl = new HashSet<String>();
                newAl.add(k);
                g.get(f).updateConnections(newAl);
            }
        }
    }

    public void getResult() {
        int res = 0;
        Map<String, Item> g = parseFile();
        boolean refined = false;
        while (!refined) {
            refined = true;
            for (String k : g.keySet()) {
                if (g.get(k).numOfConnections() > 1) {
                    refineConnections(g);
                    refined = false;
                }
            }
        }

        ArrayList<String> allergens = new ArrayList<String>();

        for (String k : g.keySet()) {
            int num = g.get(k).numOfConnections();
            if (g.get(k).type == ItemType.Allergen) allergens.add(k);
            if (num == 0)
                res += g.get(k).quantity();
        }

        Collections.sort(allergens);
        String resStr = allergens.stream().reduce("", (r, a) ->
                r + g.get(a).getConnections().iterator().next() + ",");
        StdOut.println(res);
        StdOut.println(resStr);
    }

    public static void main(String[] args) {
        AdventDay20 a = new AdventDay20(StdIn.readAllLines());
        a.getResult();
    }
}
