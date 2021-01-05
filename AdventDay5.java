/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class AdventDay5 {

    private static int getIntVal(char c) {
        return (int) c - 97;
    }

    private static int countBit(String str) {
        int res = 0;

        for (int i = 0; i < str.length(); i++) {
            int p = getIntVal(str.charAt(i));
            int temp = 1 << p;
            res |= temp;
        }

        return res;
    }

    private static int countSum(int b) {
        int res = 0;
        while (b > 0) {
            int temp = b | 1;
            if (temp == b) res++;
            b >>= 1;
        }
        return res;
    }

    public static void main(String[] args) {
        int res = 0;
        int temp = 0;
        boolean first = true;

        while (!StdIn.isEmpty()) {
            String str = StdIn.readLine();
            if (!str.isEmpty()) {
                temp = first ? countBit(str) : temp & countBit(str);
                first = false;
            }
            else {
                res += countSum(temp);
                first = true;
            }
        }

        if (temp > 0) res += countSum(temp);

        StdOut.print(res);
        StdOut.println();

    }
}
