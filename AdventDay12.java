/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class AdventDay12 {
    private String[] file;

    public AdventDay12(String[] s) {
        this.file = s;
    }

    private int[] parseID(String s) {
        return Arrays.stream(s.split(",")).filter(str -> !str.equals("x"))
                     .mapToInt(Integer::parseInt).toArray();
    }

    private int[][] parseTimeStamp(String s) {
        String[] str = s.split(",");
        int j = 0;
        for (int i = 0; i < str.length; i++) {
            if (!str[i].equals("x")) {
                j++;
            }
        }
        int[][] arr = new int[j][2];
        j = 0;
        for (int i = 0; i < str.length; i++) {
            if (!str[i].equals("x")) {
                arr[j][0] = Integer.parseInt(str[i]);
                arr[j][1] = i == 0 ? i : arr[j][0] - i % arr[j][0];
                j++;
            }
        }
        return arr;
    }

    public void findTime() {
        int[][] times = parseTimeStamp(file[1]);
        Arrays.sort(times, new Comparator<int[]>() {
            public int compare(int[] o1, int[] o2) {
                return Integer.compare(o2[1], o1[1]);
            }
        });

        long n = times[0][0];
        long res = times[0][1];

        for (int i = 1; i < times.length; i++) {
            while (res % times[i][0] != times[i][1]) {
                res += n;
            }
            n *= times[i][0];
        }

        StdOut.print(res);
        StdOut.println();
    }

    public int findID() {
        int time = Integer.parseInt(file[0]);
        int[] ids = parseID(file[1]);
        int found = 0;
        int tempTime = time;

        while (found == 0) {
            for (int i = 0; i < ids.length; i++) {
                if (tempTime % ids[i] == 0) {
                    found = ids[i];
                }
            }
            tempTime++;
        }

        return (tempTime - 1 - time) * found;
    }

    public static void main(String[] args) {
        AdventDay12 a = new AdventDay12(StdIn.readAllLines());
        a.findTime();
        // StdOut.print(a.findID());
        StdOut.println();
    }
}
