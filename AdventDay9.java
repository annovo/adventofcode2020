/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class AdventDay9 {
    public long factorialUsingRecursion(int n) {
        if (n <= 2) {
            return n;
        }
        return n * factorialUsingRecursion(n - 1);
    }

    public static void main(String[] args) {
        int[] arr = Arrays.stream(StdIn.readAllLines()).mapToInt(Integer::parseInt).toArray();
        int res1 = 0;
        int res3 = 0;
        ArrayList<Integer> diffArr = new ArrayList<Integer>();
        Arrays.sort(arr);

        for (int i = -1; i < arr.length - 1; i++) {
            int dif = i == -1 ? arr[i + 1] : arr[i + 1] - arr[i];
            diffArr.add(dif);
            switch (dif) {
                case 1:
                    res1++;
                    break;
                case 3:
                    res3++;
                    break;
                default:
                    break;
            }
        }

        diffArr.add(3);
        long arng1 = 0;
        int prev = 0;
        long[] arng = new long[3];

        for (int i = 1; i < diffArr.size(); i++) {
            int sum2 = diffArr.get(i) + diffArr.get(i - 1);
            if (sum2 == 2 || sum2 == 3) {
                long temp = arng[0];
                long prevS = arng[1];
                arng[0] = prevS;
                arng[1] = arng1;
                if (sum2 + prev > 3)
                    arng1 += arng1 + 1 - prevS + temp;
                else arng1 += arng1 + 1;

                arng[2] = arng1;
                prev += diffArr.get(i);
            }
            else {
                prev = 0;
            }
        }
        StdOut.println();
        StdOut.printf("1: %d", arng1 + 1);
        StdOut.println();
        StdOut.print(res1 * (res3 + 1));
        StdOut.println();
    }
}
