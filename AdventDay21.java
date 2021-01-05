/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AdventDay21 {
    private String[] file;

    private class Cards {
        public ArrayDeque<Integer> d;
        public int winner;

        public Cards(ArrayDeque<Integer> d, int winner) {
            this.d = d;
            this.winner = winner;
        }
    }

    public AdventDay21(String[] file) {
        this.file = file;
    }

    private ArrayList<ArrayDeque<Integer>> parseFile() {
        ArrayList<ArrayDeque<Integer>> a = new ArrayList<ArrayDeque<Integer>>();
        ArrayDeque<Integer> d = new ArrayDeque<Integer>();
        for (int i = 0; i < this.file.length; i++) {
            if (file[i].contains("Player")) {
                d = new ArrayDeque<Integer>();
            }
            else if (file[i].isEmpty()) {
                a.add(d);
            }
            else {
                d.addLast(Integer.parseInt(file[i]));
            }
        }

        a.add(d);
        return a;
    }

    private ArrayDeque<Integer> createDeque(ArrayDeque<Integer> d, int i) {
        ArrayDeque<Integer> r = new ArrayDeque<Integer>();
        while (i != 0) {
            r.addLast(d.removeFirst());
            i--;
        }
        return r;
    }

    private Cards playRecurr(ArrayList<ArrayDeque<Integer>> a) {
        ArrayDeque<Integer> f = a.get(0);
        ArrayDeque<Integer> s = a.get(1);
        int winner;

        Set<String> combF = new HashSet<String>();
        Set<String> combS = new HashSet<String>();

        while (!f.isEmpty() && !s.isEmpty()) {
            String str1 = f.toString();
            String str2 = s.toString();

            if (!combF.isEmpty() && !combS.isEmpty() && combF.contains(str1) && combS
                    .contains(str2)) {
                return new Cards(f, 1);
            }

            combF.add(str1);
            combS.add(str2);

            int cardF = f.removeFirst();
            int cardS = s.removeFirst();
            if (cardF <= f.size() && cardS <= s.size()) {
                ArrayList<ArrayDeque<Integer>> newArr = new ArrayList<ArrayDeque<Integer>>();
                newArr.add(createDeque(f.clone(), cardF));
                newArr.add(createDeque(s.clone(), cardS));
                winner = playRecurr(newArr).winner;
            }
            else
                winner = 0;

            if (cardF > cardS && winner == 0 || winner == 1) {
                f.addLast(cardF);
                f.addLast(cardS);
            }
            else {
                s.addLast(cardS);
                s.addLast(cardF);
            }
        }
        return f.isEmpty() ? new Cards(s, 2) : new Cards(f, 1);
    }

    private ArrayDeque<Integer> play(ArrayList<ArrayDeque<Integer>> a) {
        ArrayDeque<Integer> f = a.get(0);
        ArrayDeque<Integer> s = a.get(1);
        while (!f.isEmpty() && !s.isEmpty()) {
            int cardF = f.removeFirst();
            int cardS = s.removeFirst();
            if (cardF > cardS) {
                f.addLast(cardF);
                f.addLast(cardS);
            }
            else {
                s.addLast(cardS);
                s.addLast(cardF);
            }
        }
        return f.isEmpty() ? s : f;
    }

    public void solve() {
        long res = 0;
        int i = 1;
        ArrayList<ArrayDeque<Integer>> a = parseFile();
        ArrayList<ArrayDeque<Integer>> b = new ArrayList<ArrayDeque<Integer>>();
        b.add(a.get(0).clone());
        b.add(a.get(1).clone());

        ArrayDeque<Integer> solved = play(a);
        while (!solved.isEmpty()) {
            res += i * solved.removeLast();
            i++;
        }
        StdOut.println(res);
        res = 0;
        i = 1;

        solved = playRecurr(b).d;
        while (!solved.isEmpty()) {
            res += i * solved.removeLast();
            i++;
        }
        StdOut.println(res);

    }

    public static void main(String[] args) {
        AdventDay21 a = new AdventDay21(StdIn.readAllLines());
        a.solve();
    }
}
