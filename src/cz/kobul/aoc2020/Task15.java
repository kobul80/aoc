package cz.kobul.aoc2020;

import java.util.List;
import java.util.Map;

/**
 * https://adventofcode.com/2020/day/15
 * start: 16:20
 * end: 16:41
 */
public class Task15 extends Aoc2020 {

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }
    
    record LT(int t1, int t2) {}
    
    public Object solve1(List<String> lines) {
        List<Long> nums = toLongList(lines.get(0));
        Map<Long, LT> numLast = newMap();
        for (int i = 0; i < nums.size(); i++) {
            numLast.put(nums.get(i), new LT(i+1, -1));
        }
        long last = nums.get(nums.size()-1);
        int turn = nums.size() + 1;
        while (turn <= 2020) {
            LT lt = numLast.get(last);
            long next;  
            if (lt.t2 == -1) {
                next = 0;
            } else {
                next = lt.t1 - lt.t2;
            }
            LT nextNumLt = numLast.get(next);
            numLast.put(next, new LT(turn, nextNumLt == null ? -1 : nextNumLt.t1));
            last = next;
            turn++;
        }
        return last;
    }

    public Object solve2(List<String> lines) {
        List<Long> nums = toLongList(lines.get(0));
        Map<Long, LT> numLast = newMap();
        for (int i = 0; i < nums.size(); i++) {
            numLast.put(nums.get(i), new LT(i+1, -1));
        }
        long last = nums.get(nums.size()-1);
        int turn = nums.size() + 1;
        while (turn <= 30000000) {
            LT lt = numLast.get(last);
            long next;  
            if (lt.t2 == -1) {
                next = 0;
            } else {
                next = lt.t1 - lt.t2;
            }
            LT nextNumLt = numLast.get(next);
            numLast.put(next, new LT(turn, nextNumLt == null ? -1 : nextNumLt.t1));
            last = next;
            turn++;
        }
        return last;
    }	
    
    public static void main(String[] args) throws Exception {
        new Task15().run();
    }

}
