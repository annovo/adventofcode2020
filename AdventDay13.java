/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AdventDay13 {
    private String[] file;

    public class Mask {
        private long maskAND;
        private long maskOR;
        private ArrayList<Integer> shifts;

        public Mask(long maskOR, long maskAND, ArrayList<Integer> shifts) {
            this.maskAND = maskAND;
            this.maskOR = maskOR;
            this.shifts = shifts;
        }

        public long getAndMask() {
            return this.maskAND;
        }

        public long getOrMask() {
            return this.maskOR;
        }

        public ArrayList<Integer> getShifts() {
            return this.shifts;
        }
    }

    public AdventDay13(String[] s) {
        this.file = s;
    }

    private long[] parseMaskForVal(String line) {
        String s = line.split("=")[1].trim();
        String maskAnd = "";
        String maskOr = "";

        for (int i = 0; i < s.length(); i++) {
            maskOr = s.charAt(i) == '1' ? maskOr.concat("1") : maskOr.concat("0");
            maskAnd = s.charAt(i) == '0' ? maskAnd.concat("0") : maskAnd.concat("1");
        }

        return new long[] { Long.valueOf(maskOr, 2), Long.valueOf(maskAnd, 2) };
    }

    private long[] parseMem(String line) {
        return Arrays.stream(line.split("mem\\[")[1].split("\\] = ")).mapToLong(Long::parseLong)
                     .toArray();
    }

    private long applyMaskTo(long maskOr, long maskAnd, long val) {
        return (val | maskOr) & maskAnd;
    }

    private Mask parseMaskForMem(String line) {
        String s = line.split("=")[1].trim();
        String maskAnd = "";
        String maskOr = "";
        ArrayList<Integer> shifts = new ArrayList<Integer>();

        for (int i = 0; i < s.length(); i++) {
            maskOr = s.charAt(i) == '1' ? maskOr.concat("1") : maskOr.concat("0");
            maskAnd = s.charAt(i) == 'X' ? maskAnd.concat("0") : maskAnd.concat("1");
            if (s.charAt(i) == 'X') shifts.add(s.length() - 1 - i);
        }

        return new Mask(Long.valueOf(maskOr, 2), Long.valueOf(maskAnd, 2), shifts);
    }

    private void updateMap(long key, long val, Map<Long, Long> m) {
        if (m.containsKey(key)) m.replace(key, val);
        else m.put(key, val);
    }

    private void updateMemArr(long mem, ArrayList<Long> arr) {
        int length = arr.size();
        for (int i = 0; i < length; i++) {
            arr.add(mem | arr.get(i));
        }
    }

    public long countMemValPart2() {
        long res = 0;
        Map<Long, Long> memoryMap = new HashMap<Long, Long>();
        Mask mask = new Mask(0, 0, new ArrayList<Integer>());
        long[] mem;

        for (int i = 0; i < file.length; i++) {
            if (!file[i].contains("mem")) mask = parseMaskForMem(file[i]);
            else {
                mem = parseMem(file[i]);
                mem[0] = applyMaskTo(mask.getOrMask(), mask.getAndMask(), mem[0]);

                ArrayList<Long> arrOfMem = new ArrayList<Long>();
                arrOfMem.add(mem[0]);

                for (int j = mask.getShifts().size() - 1; j >= 0; j--) {
                    long newMem = mem[0] | (1L << mask.getShifts().get(j));
                    updateMemArr(newMem, arrOfMem);
                }

                for (int j = 0; j < arrOfMem.size(); j++) {
                    updateMap(arrOfMem.get(j), mem[1], memoryMap);
                }
            }
        }

        for (long key : memoryMap.keySet()) {
            res += memoryMap.get(key);
        }
        return res;
    }

    public long countMemVal() {
        long res = 0;
        long[] mem;
        long[] masks = new long[2];
        Map<Long, Long> memoryMap = new HashMap<Long, Long>();

        for (int i = 0; i < file.length; i++) {
            if (!file[i].contains("mem")) masks = parseMaskForVal(file[i]);
            else {
                mem = parseMem(file[i]);
                mem[1] = applyMaskTo(masks[0], masks[1], mem[1]);

                if (memoryMap.containsKey(mem[0])) memoryMap.replace(mem[0], mem[1]);
                else memoryMap.put(mem[0], mem[1]);
            }
        }

        for (long key : memoryMap.keySet()) {
            res += memoryMap.get(key);
        }

        return res;
    }

    public static void main(String[] args) {
        AdventDay13 a = new AdventDay13(StdIn.readAllLines());
        StdOut.print(a.countMemValPart2());
        StdOut.println();
    }
}
