package cz.kobul.aoc2023;

import java.util.ArrayList;
import java.util.List;

/**
 * https://adventofcode.com/2023/day/9
 * start: 7:40
 * end: 7:58 
 */
public class Task9 extends Aoc2023 {
    
    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }
    
    static record Sequence(int[] nos) {
        Sequence diffs() {
            if (nos.length <= 1) {
                return null;
            }
            int[] res = new int[nos.length -1];
            for (int i = 0; i < nos.length -1; i++) {
                res[i] = nos[i+1] - nos[i];
            }
            return new Sequence(res);
        }
        
        boolean allZeroes() {
            if (nos.length == 0) {
                return true;
            }
            for (int i = 0; i < nos.length; i++) {
                if (nos[i] != 0) {
                    return false;
                }
            }            
            return true;
        }
        
        int last() {
            return nos[nos.length -1];
        }
        int first() {
            return nos[0];
        }
    }
    
    public long solve(Sequence s) {
        List<Sequence> all = new ArrayList<>();
        all.add(s);
        Sequence curr = s;
        while (!curr.allZeroes()) {
            curr = curr.diffs();
            all.add(curr);
        }
        long last = 0;
        for (int i = all.size() -2 ; i >= 0; i--) {
            Sequence seq = all.get(i);
            last = seq.last() + last;
        }
        return last;
    }

    public long solve2(Sequence s) {
        List<Sequence> all = new ArrayList<>();
        all.add(s);
        Sequence curr = s;
        while (!curr.allZeroes()) {
            curr = curr.diffs();
            all.add(curr);
        }
        long first = 0;
        for (int i = all.size() -2 ; i >= 0; i--) {
            Sequence seq = all.get(i);
            first= seq.first() - first;
        }
        return first;
    }
    
    public Long solve1(List<String> lines) {
        long result = 0;
        for (String s : lines) {
            List<Integer> nos = toIntegerList(s);
            long sq = solve(new Sequence(nos.stream().mapToInt(Integer::intValue).toArray()));
            result += sq;
        }
        return result;
    }    

    public Long solve2(List<String> lines) {
        long result = 0;
        for (String s : lines) {
            List<Integer> nos = toIntegerList(s);
            long sq = solve2(new Sequence(nos.stream().mapToInt(Integer::intValue).toArray()));
            result += sq;
        }
        return result;
    }
    
    public static void main(String[] args) throws Exception {
        new Task9().run();
    }

}
