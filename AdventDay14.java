/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */


import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AdventDay14 {
    private Map<Integer, Integer> file;
    private int turn;

    public AdventDay14(String s) {
        int[] arr = Arrays.stream(s.split(",")).mapToInt(Integer::parseInt).toArray();
        this.turn = arr.length - 1;
        this.file = IntStream.range(0, arr.length)
                             .boxed()
                             .collect(Collectors.toMap(i -> arr[i],
                                                       i -> i,
                                                       (exist, replace) -> replace));
    }

    private int updateMap(int key, int val, Map<Integer, Integer> m) {
        int curr;
        if (m.containsKey(key)) {
            curr = val - m.get(key);
            m.replace(key, val);
        }
        else {
            m.put(key, val);
            curr = 0;
        }
        return curr;
    }

    public void findRes(int lastTurn) {
        int curr = 0;
        while (turn != (lastTurn - 2)) {
            curr = updateMap(curr, ++turn, file);
        }
        StdOut.println(curr);
    }

    public static void main(String[] args) {
        AdventDay14 a = new AdventDay14(StdIn.readLine());
        a.findRes(Integer.parseInt(args[0]));
    }
}
