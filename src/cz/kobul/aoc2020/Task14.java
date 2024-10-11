package cz.kobul.aoc2020;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.List;
import java.util.Map;

/**
 * https://adventofcode.com/2020/day/14
 * start: 13:53
 * end: 14:33
 */
public class Task14 extends Aoc2020 {

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }

    long cnv(String mask, long val) {
        StringBuilder result = new StringBuilder(mask);
        String v = lpad(Long.toString(val, 2), mask.length(), '0');
        for (int i = 0; i < mask.length(); i++) {
            if (mask.charAt(i) == 'X') {
                result.setCharAt(i, v.charAt(i));
            }
        }
        return Long.parseLong(result.toString(), 2);
    }

    long repl(String mask, long no, int xcnt) {
        String v = lpad(Long.toString(no, 2), xcnt, '0');
        int idx = 0;
        StringBuilder res = new StringBuilder(mask);
        for (int i = 0; i < mask.length(); i++) {
            if (mask.charAt(i) == 'X') {
                res.setCharAt(i, v.charAt(idx));
                idx++;
            }
        }
        return Long.valueOf(res.toString(), 2);
    }
    
    long[] cnvAddr(String mask, long val) {
        StringBuilder result = new StringBuilder(mask);
        String v = lpad(Long.toString(val, 2), mask.length(), '0');
        int xcnt = 0;
        for (int i = 0; i < mask.length(); i++) {
            if (mask.charAt(i) == '0') {
                result.setCharAt(i, v.charAt(i));
            } else if (mask.charAt(i) == '1') {
                result.setCharAt(i, '1');                
            } else if (mask.charAt(i) == 'X') {
                xcnt++;
            }
        }
        String mask1 = result.toString();
        int comb = (int) Math.pow(2, xcnt);
        long[] res = new long[comb];
        for (int i = 0; i < comb; i++) {
            res[i] = repl(mask1, i, xcnt);
        }
        return res;
    }
    
    public Object solve1(List<String> lines) {
        String mask ="";
        Map<Long, Long> mem = newMap();
        for (String l : lines) {
            if (l.startsWith("mask")) {
                mask = l.substring(7);
            } else {
                long[] ll = toLongArray(l);
                long addr = ll[0];
                long val = ll[1];
                long valc = cnv(mask, val);
                mem.put(addr, valc);
            }
        }
        BigInteger res = BigInteger.ZERO;
        for (long v : mem.values()) {
            BigInteger val = BigInteger.valueOf(v);
            res = res.add(val);
        }
        return res;
    }

    public Object solve2(List<String> lines) {
        String mask ="";
        Map<Long, Long> mem = newMap();
        for (String l : lines) {
            if (l.startsWith("mask")) {
                mask = l.substring(7);
            } else {
                long[] ll = toLongArray(l);
                long val = ll[1];
                long[] addrc = cnvAddr(mask, ll[0]);
                for (long addr : addrc) {
                    mem.put(addr, val);
                }
            }
        }
        BigInteger res = BigInteger.ZERO;
        for (long v : mem.values()) {
            BigInteger val = BigInteger.valueOf(v);
            res = res.add(val);
        }
        return res;
    }	
    
    public static void main(String[] args) throws Exception {
        new Task14().run();
    }

}
