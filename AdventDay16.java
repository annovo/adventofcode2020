/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class AdventDay16 {
    private int[][][][] arr;

    public AdventDay16(String[] file, int dim) {
        int w = dim == 3 ? 1 : 3;
        this.arr = new int[file.length + 2][file[0].length() + 2][3][w];
        w = w == 3 ? 1 : 0;
        for (int i = 0; i < file.length; i++) {
            for (int j = 0; j < file[i].length(); j++) {
                this.arr[i + 1][j + 1][1][w] = file[i].charAt(j) == '#' ? 1 : 0;
            }
        }
    }

    private boolean inRange(int val, int upper) {
        return val >= 0 && val < upper;
    }

    private int checkActive(int valI, int valJ, int valK) {
        return checkActive(valI, valJ, valK, -1);
    }

    private int checkActive(int valI, int valJ, int valK, int valW) {
        int count = 0;
        int val, lowW, highW;

        if (valW == -1) {
            valW = 0;
            highW = 0;
            lowW = highW;
        }
        else {
            lowW = -1;
            highW = 1;
        }
        val = this.arr[valI][valJ][valK][valW];

        outerloop:
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    for (int h = lowW; h <= highW; h++) {
                        if (i == 0 && j == 0 && k == 0 && h == 0) continue;

                        if (inRange(i + valI, this.arr.length) && inRange(j + valJ,
                                                                          this.arr[0].length)
                                && inRange(k + valK,
                                           this.arr[0][0].length) && inRange(h + valW,
                                                                             this.arr[0][0][0].length)
                                && this.arr[i + valI][j + valJ][k + valK][h + valW] == 1) {
                            count++;
                        }

                        if (count > 3) break outerloop;
                    }
                }
            }
        }

        if ((count == 3 || count == 2) && val == 1 || count == 3 && val == 0) return 1;
        return 0;
    }

    private int[][][][] activate() {
        int w = arr[0][0][0].length == 1 ? 1 : arr[0][0][0].length + 2;

        int[][][][] temp = new int[arr.length + 2][arr[0].length + 2][arr[0][0].length + 2][w];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                for (int k = 0; k < arr[i][j].length; k++) {
                    if (w == 1) {
                        temp[i + 1][j + 1][k + 1][w - 1] = checkActive(i, j, k);
                    }
                    else {
                        for (int h = 0; h < arr[i][j][k].length; h++) {
                            temp[i + 1][j + 1][k + 1][h + 1] = checkActive(i, j, k, h);
                        }
                    }
                }
            }
        }

        return temp;
    }

    public void countActive() {
        int res = 0;
        int cycles = 6;

        while (cycles > 0) {
            this.arr = activate();
            cycles--;
        }

        res = Arrays.stream(arr).flatMap(Arrays::stream).flatMap(Arrays::stream)
                    .flatMapToInt(Arrays::stream)
                    .reduce(0, Integer::sum);
        StdOut.println(res);
    }

    public static void main(String[] args) {
        AdventDay16 a = new AdventDay16(StdIn.readAllLines(), Integer.parseInt(args[0]));
        a.countActive();
    }
}
