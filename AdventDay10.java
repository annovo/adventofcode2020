/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class AdventDay10 {
    private int[][] curr;
    private int[][] temp;

    public AdventDay10(String[] file) {
        curr = new int[file.length][file[0].length()];
        temp = new int[file.length][file[0].length()];
        fillWith(file);
    }

    private void fillWith(String[] file) {
        for (int i = 0; i < file.length; i++) {
            String str = file[i];
            for (int j = 0; j < str.length(); j++) {
                if (str.charAt(j) == '.') curr[i][j] = -1;
                if (str.charAt(j) == 'L') curr[i][j] = 0;
            }
        }
    }

    private int compare(int i, int j, String[] operator) {
        int search = -1;
        int k = i;
        int h = j;
        while (search == -1 && k >= 0 && h >= 0 && k < curr.length && h < curr[0].length) {
            search = curr[k][h];
            if (operator[0].equals("-")) k--;
            if (operator[0].equals("+")) k++;
            if (operator[1].equals("-")) h--;
            if (operator[1].equals("+")) h++;
        }
        return search;
    }

    public void stabilizeLongView() {
        boolean changed = true;

        while (changed) {
            changed = false;
            for (int i = 0; i < curr.length; i++) {
                for (int j = 0; j < curr[i].length; j++) {
                    if (curr[i][j] >= 0) {
                        int adj = 0;

                        if (compare(i, j - 1, new String[] { "", "-" }) == 1) adj++;
                        if (compare(i, j + 1, new String[] { "", "+" }) == 1) adj++;
                        if (compare(i - 1, j, new String[] { "-", "" }) == 1) adj++;
                        if (compare(i + 1, j, new String[] { "+", "" }) == 1) adj++;
                        if (compare(i - 1, j - 1, new String[] { "-", "-" }) == 1) adj++;
                        if (compare(i - 1, j + 1, new String[] { "-", "+" }) == 1) adj++;
                        if (compare(i + 1, j + 1, new String[] { "+", "+" }) == 1) adj++;
                        if (compare(i + 1, j - 1, new String[] { "+", "-" }) == 1) adj++;

                        if (adj == 0 && curr[i][j] == 0) {
                            changed = true;
                            temp[i][j] = 1;
                        }
                        else if (adj > 4 && curr[i][j] == 1) {
                            changed = true;
                            temp[i][j] = 0;
                        }
                        else {
                            temp[i][j] = curr[i][j];
                        }
                    }
                    else {
                        temp[i][j] = curr[i][j];
                    }
                }
            }
            curr = temp;
            temp = new int[curr.length][curr[0].length];
        }
    }

    public void stabilize() {
        boolean changed = true;

        while (changed) {
            changed = false;
            for (int i = 0; i < curr.length; i++) {
                for (int j = 0; j < curr[i].length; j++) {
                    if (curr[i][j] >= 0) {
                        int adj = 0;

                        if (j - 1 >= 0 && curr[i][j - 1] == 1) adj++;
                        if (j + 1 < curr[i].length && curr[i][j + 1] == 1) adj++;
                        if (i - 1 >= 0 && curr[i - 1][j] == 1) adj++;
                        if (i - 1 >= 0 && j - 1 >= 0 && curr[i - 1][j - 1] == 1) adj++;
                        if (i - 1 >= 0 && j + 1 < curr[i].length && curr[i - 1][j + 1] == 1) adj++;
                        if (i + 1 < curr.length && curr[i + 1][j] == 1) adj++;
                        if (i + 1 < curr.length && j - 1 >= 0 && curr[i + 1][j - 1] == 1) adj++;
                        if (i + 1 < curr.length && j + 1 < curr[i].length
                                && curr[i + 1][j + 1] == 1) adj++;

                        if (adj == 0 && curr[i][j] == 0) {
                            changed = true;
                            temp[i][j] = 1;
                        }
                        else if (adj > 3 && curr[i][j] == 1) {
                            changed = true;
                            temp[i][j] = 0;
                        }
                        else {
                            temp[i][j] = curr[i][j];
                        }
                    }
                    else {
                        temp[i][j] = curr[i][j];
                    }
                }
            }
            curr = temp;
            temp = new int[curr.length][curr[0].length];
        }
    }

    public int getOccupied() {
        int result = 0;

        for (int i = 0; i < curr.length; i++) {
            for (int j = 0; j < curr[i].length; j++) {
                if (curr[i][j] == 1) result++;
            }
        }

        return result;
    }


    public static void main(String[] args) {
        AdventDay10 a = new AdventDay10(StdIn.readAllLines());
        a.stabilizeLongView();
        StdOut.print(a.getOccupied());
        StdOut.println();
    }
}
