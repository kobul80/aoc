package cz.kobul.aoc2020;

import java.util.List;
import java.util.Map;

/**
 * https://adventofcode.com/2020/day/10
 * start: 13:38
 * end: 15:04
 */
public class Task10 extends Aoc2020 {

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }

    public Object solve1(List<String> lines) {
        List<Integer> jolts = newList();
        for (String l : lines) {
            jolts.add(Integer.parseInt(l));
        }
        jolts.add(0);
        jolts.sort(Integer::compare);
        
        int diff1 = 0;
        int diff3 = 0;
        for (int i = 1; i < jolts.size(); i++) {
            int diff = jolts.get(i) - jolts.get(i-1);
            if (diff == 1) {
                diff1++;
            } else if (diff == 3) {
                diff3++;
            }
        }
        diff3++;
        return (long) diff1 * diff3;
    }

    Map<Integer, Long> combinations = newMap();
    
    public long cnt(List<Integer> pathIdx, List<Integer> jolts) {
        int lastIdx = pathIdx.get(pathIdx.size()-1);
        if (combinations.get(lastIdx) != null) {
            return combinations.get(lastIdx);
        }
        
        int jolt = jolts.get(lastIdx);
        if (lastIdx == jolts.size()-1) {
            return 1;
        }
        
        long sum = 0;
        for (int i = 1; i <=3; i++) {
            int idx = lastIdx+i;
            if (idx < jolts.size() && (jolts.get(idx) <= jolt+3)) {
                
                sum+=cnt(add(pathIdx, idx), jolts);
            }
        }
        combinations.put(lastIdx, sum);
        return sum;
        
    }
    
    public Object solve2(List<String> lines) {
        List<Integer> jolts = newList();
        for (String l : lines) {
            jolts.add(Integer.parseInt(l));
        }
        jolts.add(0);
        jolts.sort(Integer::compare);
        return cnt(List.of(0), jolts);
    }	
    
    public static void main(String[] args) throws Exception {
        new Task10().run();
    }

}
