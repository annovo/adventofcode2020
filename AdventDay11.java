/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class AdventDay11 {
    private class Vector {
        public int x;
        public int y;

        public Vector(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void rotate(int angle) {
            double x1 = x;
            double y1 = y;
            x = (int) Math.round(x1 * Math.cos(Math.toRadians((double) angle)) + y1 * Math
                    .sin(Math.toRadians((double) angle)));
            y = (int) Math.round(y1 * Math.cos(Math.toRadians((double) angle)) - x1 * Math
                    .sin(Math.toRadians((double) angle)));
        }
    }

    private Vector w;
    private Vector ship;
    private String[] arr;

    public AdventDay11(String[] a) {
        arr = a;
    }

    private void parseWithWaypoint(String s, Vector temp) {
        int val = Integer.parseInt(s.substring(1));
        char c = s.charAt(0);

        switch (c) {
            case 'F':
                ship.x += w.x * val;
                ship.y += w.y * val;
                break;
            case 'R':
                w.rotate(val);
                break;
            case 'L':
                w.rotate(-val);
                break;
            case 'N':
                temp.y += val;
                break;
            case 'S':
                temp.y -= val;
                break;
            case 'E':
                temp.x += val;
                break;
            case 'W':
                temp.x -= val;
                break;
            default:
                throw new IllegalArgumentException("no such symbol");
        }
    }

    public int getDistanceWaypoint() {
        return getDistanceWaypoint(false);
    }

    public int getDistanceWaypoint(boolean waypoint) {
        ship = new Vector(0, 0);
        w = waypoint ? new Vector(10, 1) : new Vector(1, 0);
        Vector t = waypoint ? w : ship;

        for (int i = 0; i < arr.length; i++) {
            parseWithWaypoint(arr[i], t);
        }

        return Math.abs(ship.x) + Math.abs(ship.y);
    }

    public static void main(String[] args) {
        AdventDay11 a = new AdventDay11(StdIn.readAllLines());
        StdOut.print(a.getDistanceWaypoint());
        StdOut.println();
        StdOut.print(a.getDistanceWaypoint(true));
        StdOut.println();

    }
}
