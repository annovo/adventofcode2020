/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class AdventDay7 {
    private static class Line {
        public String command;
        public int value;

        public Line(String c, int v) {
            this.command = c;
            this.value = v;
        }

        public void updateCommand(String c) {
            command = c;
        }
    }

    private static class Parser {
        private int currLineIndex = 0;
        private Line currLine;
        private String[] file;
        private boolean[] visited;
        private boolean[] visitedInf;
        private int[] resInf;
        private int result = 0;

        public Parser(String[] newFile) {
            file = newFile;
        }

        private boolean hasMore() {
            return file.length != currLineIndex;
        }


        private void parseCommand(String s) {
            String[] temp = s.split(" ");
            currLine = new Line(temp[0], Integer.parseInt(temp[1]));
        }

        private void execute() {
            switch (currLine.command) {
                case "nop":
                    currLineIndex++;
                    break;
                case "acc":
                    currLineIndex++;
                    result += currLine.value;
                    break;
                case "jmp":
                    currLineIndex += currLine.value;
                    break;
                default:
                    throw new IllegalArgumentException("no such command");
            }
        }

        private void searchForRes(int index) {
            currLineIndex = index;
            result = resInf[index];
            visited = new boolean[file.length];

            if (currLine.command.equals("nop"))
                currLine.updateCommand("jmp");
            else if (currLine.command.equals("jmp"))
                currLine.updateCommand("nop");

            execute();

            while (hasMore()) {
                if (visited[currLineIndex] || visitedInf[currLineIndex]) {
                    result = 0;
                    break;
                }
                visited[currLineIndex] = true;
                parseCommand(file[currLineIndex]);

                execute();
            }
        }

        public int findFiniteRes() {
            findInfiniteRes();
            result = 0;

            for (int i = 0; i < file.length; i++) {
                if (result != 0)
                    return result;

                if (visitedInf[i]) {
                    parseCommand(file[i]);

                    if (currLine.command.equals("nop") || currLine.command.equals("jmp")) {
                        searchForRes(i);
                    }
                }

            }

            return result;
        }

        public int findInfiniteRes() {
            currLineIndex = 0;
            result = 0;
            visitedInf = new boolean[file.length];
            resInf = new int[file.length];

            while (hasMore()) {
                if (visitedInf[currLineIndex])
                    return result;
                visitedInf[currLineIndex] = true;
                resInf[currLineIndex] = result;

                parseCommand(file[currLineIndex]);
                execute();
            }

            return result;
        }
    }

    public static void main(String[] args) {
        Parser p = new Parser(StdIn.readAllLines());
        StdOut.print(p.findInfiniteRes());
        StdOut.println();
        StdOut.print(p.findFiniteRes());
        StdOut.println();
    }
}
