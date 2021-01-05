/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class AdventDay8 {

    private static int findSet(String[] arr, int target) {
        int sum = 0;
        Queue<Integer> queue = new Queue<Integer>();
        int j = 0;

        while (sum != target && j < arr.length) {
            if (sum > target) {
                sum -= queue.dequeue();
            }
            else {
                int line = Integer.parseInt(arr[j]);
                sum += line;
                queue.enqueue(line);
                j++;
            }
        }

        ArrayList<Integer> newArr = new ArrayList<Integer>();
        for (int q : queue) {
            newArr.add(q);
        }

        Collections.sort(newArr);
        return newArr.get(0) + newArr.get(newArr.size() - 1);
    }

    public static void main(String[] args) {
        int pre = Integer.parseInt(args[0]);
        String[] arr = StdIn.readAllLines();
        int[] arrInt25 = new int[pre];
        int result = 0;
        int i = 0;
        boolean check = true;

        for (int j = 0; j < pre; j++) {
            arrInt25[j] = Integer.parseInt(arr[j]);
        }

        Arrays.sort(arrInt25);

        while (check) {
            check = false;

            if (i > 0) {
                int prev = Integer.parseInt(arr[i - 1]);
                int n = Integer.parseInt(arr[i - 1 + pre]);
                int k = pre - 1;
                while (k >= 0) {
                    if (arrInt25[k] == prev) {
                        if (n >= arrInt25[k] || k == 0) {
                            arrInt25[k] = n;
                            break;
                        }
                        else {
                            prev = arrInt25[k - 1];
                            arrInt25[k] = arrInt25[k - 1];
                        }
                    }
                    if (n > arrInt25[k]) {
                        int temp = arrInt25[k];
                        arrInt25[k] = n;
                        n = temp;
                    }
                    k--;
                }
            }

            result = Integer.parseInt(arr[i + pre]);

            i++;

            int low = 0;
            int high = arrInt25.length - 1;
            while (low < high) {
                int sum = arrInt25[low] + arrInt25[high];
                if (sum > result) high--;
                else if (sum < result) low++;
                else {
                    check = true;
                    break;
                }
            }

        }
        StdOut.print(result);
        StdOut.println();
        StdOut.print(findSet(arr, result));
        StdOut.println();
    }
}
