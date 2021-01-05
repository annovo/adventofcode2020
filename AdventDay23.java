/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AdventDay23 {
    private String[] file;
    private Map<String, Pair> dir;

    private class Pair {
        public int x;
        public int y;

        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Pair add(Pair p) {
            return new Pair(this.x + p.x,
                            this.y + p.y);
        }

        @Override
        public boolean equals(Object ob) {
            if (this == ob) return true;
            if (ob == null || ob.getClass() != getClass()) return false;
            Pair p = (Pair) ob;
            return p.x == this.x && p.y == this.y;
        }

        public int hashCode() {
            int result = this.x ^ (this.x >>> 16);
            result = 31 * result + this.y ^ (this.y >>> 16);
            return result;
        }
    }


    public AdventDay23(String[] f) {
        this.file = f;

        this.dir = new HashMap<String, Pair>();
        this.dir.put("e", new Pair(1, 0));
        this.dir.put("se", new Pair(0, 1));
        this.dir.put("sw", new Pair(-1, 1));
        this.dir.put("w", new Pair(-1, 0));
        this.dir.put("nw", new Pair(0, -1));
        this.dir.put("ne", new Pair(1, -1));
    }

    private ArrayList<ArrayList<Pair>> parseFile() {
        ArrayList<ArrayList<Pair>> c = new ArrayList<ArrayList<Pair>>();
        for (int i = 0; i < this.file.length; i++) {
            c.add(new ArrayList<>());
            int j = 0;
            while (j < this.file[i].length()) {
                String key = "" + this.file[i].charAt(j);
                if (this.dir.containsKey(key))
                    c.get(i).add(this.dir.get(key));
                else {
                    key += this.file[i].charAt(++j);
                    c.get(i).add(this.dir.get(key));
                }
                j++;
            }
        }
        return c;
    }

    private void executeCommands(ArrayList<ArrayList<Pair>> c, Map<Pair, Boolean> t) {
        for (int i = 0; i < c.size(); i++) {
            Pair start = new Pair(0, 0);
            for (int j = 0; j < c.get(i).size(); j++)
                start = start.add(c.get(i).get(j));

            if (!t.containsKey(start))
                t.put(start, true);
            else
                t.replace(start, !t.get(start));
        }
    }

    private Map<Pair, Boolean> flip(Map<Pair, Boolean> t) {
        Map<Pair, Boolean> newT = new HashMap<Pair, Boolean>();
        Set<Pair> set = new HashSet<Pair>();

        for (Map.Entry<Pair, Boolean> kv : t.entrySet()) {
            int blacks = 0;
            Pair p = kv.getKey();

            for (Pair k : this.dir.values()) {
                Pair d = k.add(p);
                if (t.containsKey(d) && t.get(d))
                    blacks++;
                else if (!t.containsKey(d) && kv.getValue())
                    set.add(d);
            }

            if (kv.getValue() && !(blacks == 0 || blacks > 2) || !kv.getValue() && blacks == 2)
                newT.put(p, true);
        }

        for (Pair p : set) {
            int blacks = 0;
            for (Pair k : this.dir.values()) {
                Pair d = k.add(p);
                if (t.getOrDefault(d, false))
                    blacks++;
            }
            if (blacks == 2)
                newT.put(p, true);
        }
        return newT;
    }

    public void solve(int rounds) {
        int res = 0;
        ArrayList<ArrayList<Pair>> commands = parseFile();
        Map<Pair, Boolean> tiles = new HashMap<Pair, Boolean>();
        tiles.put(new Pair(0, 0), false);

        executeCommands(commands, tiles);

        for (Pair p : tiles.keySet())
            if (tiles.get(p)) res++;

        StdOut.println(res);

        for (int i = 0; i < rounds; i++)
            tiles = flip(tiles);

        StdOut.println(tiles.size());
    }


    public static void main(String[] args) {
        AdventDay23 a = new AdventDay23(StdIn.readAllLines());
        a.solve(Integer.parseInt(args[0]));
    }
}
