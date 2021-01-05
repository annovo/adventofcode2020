/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

public class AdventDay22 {
    private String labelsStr = "871369452";

    private int[] createArr(int length) {
        int[] a = new int[length + 1];
        int i = 0;
        while (i < length - 1) {
            if (i < this.labelsStr.length() - 1) {
                int pos = this.labelsStr.charAt(i) - '0';
                a[pos] = this.labelsStr.charAt(i + 1) - '0';
            }
            else if (i == this.labelsStr.length() - 1) {
                int pos = this.labelsStr.charAt(i) - '0';
                a[pos] = i + 2;
                i++;
            }
            else {
                a[i] = i + 1;
            }
            i++;
        }

        if (i > this.labelsStr.length()) {
            a[i] = i + 1;
            a[i + 1] = this.labelsStr.charAt(0) - '0';
        }
        else a[this.labelsStr.charAt(i) - '0'] = this.labelsStr.charAt(0) - '0';

        return a;
    }

    private void shuffleLabels(int[] a, int curr) {
        int dest = curr - 1;
        boolean found = false;
        int next = curr;

        while (!found) {
            found = true;
            next = curr;
            for (int i = 0; i < 3; i++) {
                next = a[next];
                if (a[dest] == 0)
                    dest = a.length - 1;
                if (dest == next) {
                    found = false;
                    dest--;
                }
            }
        }
        int destPoint = a[dest];
        a[dest] = a[curr];
        int nextPoint = a[next];
        a[curr] = nextPoint;
        a[next] = destPoint;
    }

    private String labelToStr(int[] a) {
        String res = "";
        int pos = a[1];
        for (int i = 1; i < labelsStr.length(); i++) {
            res += pos;
            pos = a[pos];
        }
        return res;
    }

    public void getLabels(int rounds) {
        int[] arr = createArr(labelsStr.length());
        int curr = labelsStr.charAt(0) - '0';
        for (int i = 0; i < rounds; i++) {
            shuffleLabels(arr, curr);
            curr = arr[curr];
        }
        StdOut.println(labelToStr(arr));
    }

    public void getMillionsLabels(int rounds) {
        long newLabel = 0;
        int[] arr = createArr(1000000);
        int curr = labelsStr.charAt(0) - '0';

        for (int i = 0; i < rounds; i++) {
            shuffleLabels(arr, curr);
            curr = arr[curr];
        }
        newLabel = arr[1];
        newLabel *= arr[arr[1]];
        StdOut.println(newLabel);
    }

    public static void main(String[] args) {
        AdventDay22 a = new AdventDay22();
        a.getLabels(100);
        a.getMillionsLabels(10000000);
    }
}
