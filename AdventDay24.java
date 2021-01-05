/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

public class AdventDay24 {
    public void solve() {
        int dk = 17773298;
        int ck = 15530095;

        long fs = 7;
        long i = 1;
        while (fs != dk && fs != ck) {
            fs = (fs * 7) % 20201227;
            i++;
        }

        long res = 1;
        int sb = fs == dk ? ck : dk;
        for (int j = 0; j < i; j++) {
            res = (res * sb) % 20201227;
        }

        StdOut.println(res);
    }

    public static void main(String[] args) {
        AdventDay24 a = new AdventDay24();
        a.solve();
    }
}
