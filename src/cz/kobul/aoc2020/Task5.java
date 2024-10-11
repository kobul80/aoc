package cz.kobul.aoc2020;

import java.util.List;
import java.util.Set;

/**
 * https://adventofcode.com/2020/day/5
 * start: 14:20
 * end: 14:28
 */
public class Task5 extends Aoc2020 {

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }


    public Object solve1(List<String> lines) {
        int max = 0;
        for (String line : lines) {
            max = max(max, Integer.parseInt(line.replace("F", "0").replace("B", "1").replace("L", "0").replace("R", "1"), 2));
        }
        return max;
    }
    

    public Object solve2(List<String> lines) {
        int min = 10000000;
        int max = 0;
        Set<Integer> all = newSet();
        for (String line : lines) {
            int seatId = Integer.parseInt(line.replace("F", "0").replace("B", "1").replace("L", "0").replace("R", "1"), 2);
            min = min(min, seatId);
            max = max(max, seatId);
            all.add(seatId);
        }
        for (int i = min; i <= max; i++) {
            if (!all.contains(i) && all.contains(i-1) && all.contains(i+1)) {
                return i;
            }
        }
        return null;
    }	
    
    public static void main(String[] args) throws Exception {
        new Task5().run();
    }

}
