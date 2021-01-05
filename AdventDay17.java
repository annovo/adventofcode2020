/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class AdventDay17 {
    private String[] file;

    public AdventDay17(String[] f) {
        this.file = f;
    }

    private long countAll(Stack<Character> c, Stack<Long> num) {
        long res = num.pop();
        char curr;

        while (!c.isEmpty()) {
            curr = c.pop();
            if (curr == '+')
                res += num.pop();
            if (curr == '*')
                res *= num.pop();
            if (curr == ')')
                break;
        }
        return res;
    }

    private void add(Stack<Long> n, Stack<Character> c) {
        n.push(n.pop() + n.pop());
        c.pop();
    }

    private long evaluate(String line, boolean prioritize) {
        long res = 0;
        int i = line.length() - 1;

        Stack<Character> c = new Stack<Character>();
        Stack<Long> numbers = new Stack<Long>();

        while (i >= 0) {
            if (line.charAt(i) == '+' || line.charAt(i) == '*' || line.charAt(i) == ')')
                c.push(line.charAt(i));
            
            if (Character.isDigit(line.charAt(i))) {
                numbers.push((long) Character.digit(line.charAt(i), 10));
                if (prioritize && !c.isEmpty() && c.peek() == '+') add(numbers, c);
            }

            if (line.charAt(i) == '(' || i == 0) {
                res = countAll(c, numbers);
                numbers.push(res);
                if (prioritize && !c.isEmpty() && c.peek() == '+') add(numbers, c);
            }
            i--;
        }
        if (!numbers.isEmpty()) return countAll(c, numbers);
        return res;
    }

    public void countLine(boolean prioritize) {
        long res = 0;
        for (int i = 0; i < file.length; i++) {
            res += evaluate(file[i], prioritize);
        }
        StdOut.println(res);
    }

    public static void main(String[] args) {
        AdventDay17 a = new AdventDay17(StdIn.readAllLines());
        boolean prior = args.length > 0 && args[0].equals("prior");
        a.countLine(prior);
    }
}
