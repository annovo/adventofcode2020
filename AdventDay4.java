/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class AdventDay4 {
    private static final int LOW_ROW = 0;
    private static final int HIGH_ROW = 127;
    private static final int LOW_COL = 0;
    private static final int HIGH_COL = 7;

    private static int countID(int row, int col) {
        return row * 8 + col;
    }

    private static int findRow(String s) {
        int num = LOW_ROW;
        int low = LOW_ROW;
        int high = HIGH_ROW;

        for (int i = 0; i < s.length(); i++) {
            switch (s.substring(i, i + 1)) {
                case "F":
                    high = (low + high) / 2;
                    num = low;
                    break;
                case "B":
                    low = (low + high) / 2 + 1;
                    num = high;
                    break;
                default:
                    throw new IllegalArgumentException("no such symbol for rows");
            }
        }
        return num;
    }

    private static int findCol(String s) {
        int num = LOW_COL;
        int low = LOW_COL;
        int high = HIGH_COL;

        for (int i = 0; i < s.length(); i++) {
            switch (s.substring(i, i + 1)) {
                case "L":
                    high = (low + high) / 2;
                    num = low;
                    break;
                case "R":
                    low = (low + high) / 2 + 1;
                    num = high;
                    break;
                default:
                    throw new IllegalArgumentException("no such symbol for cols");
            }
        }
        return num;
    }

    public static void main(String[] args) {
        int index = 0;
        ArrayList<Integer> tempArr = new ArrayList<Integer>();
        while (!StdIn.isEmpty()) {
            String str = StdIn.readLine();
            int row = findRow(str.substring(0, 7));
            int col = findCol(str.substring(7));
            int temp = countID(row, col);
            tempArr.add(temp);
            index = temp > index ? temp : index;
        }
        StdOut.print(index);
        StdOut.println();

        int[] arr = new int[index + 1];

        for (int k : tempArr) {
            arr[k] = 1;
        }

        for (int i = 1; i < arr.length - 1; i++) {
            if (arr[i - 1] == 1 && arr[i] == 0 && arr[i + 1] == 1) {
                StdOut.print(i);
                StdOut.println();
                break;
            }
        }
    }
}
