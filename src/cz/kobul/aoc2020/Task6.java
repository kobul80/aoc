package cz.kobul.aoc2020;

import java.util.List;
import java.util.Set;

/**
 * https://adventofcode.com/2020/day/6
 * start: 15:05
 * end: 15:13
 */
public class Task6 extends Aoc2020 {

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }

    public Object solve1(List<String> lines) {
        long sum = 0;
        Set<Character> ans = newSet();
        for (String line : lines) {
            if (line.isBlank()) {
                sum += ans.size();
                ans.clear();
            } else {
                for (int i = 0; i < line.length(); i++) {
                    ans.add(line.charAt(i));
                }
            }
        }
        sum += ans.size();
        return sum;
    }

    public Object solve2(List<String> lines) {
        long sum = 0;
        Set<Character> ans = newSet();
        boolean first = true;
        for (String line : lines) {
            if (line.isBlank()) {
                sum += ans.size();
                ans.clear();
                first = true;
            } else {
                Set<Character> a = newSet();
                for (int i = 0; i < line.length(); i++) {
                    a.add(line.charAt(i));
                }
                if (first) {
                    ans.addAll(a);
                    first = false;
                } else {
                    ans.retainAll(a);
                }
            }
        }
        sum += ans.size();
        return sum;
    }	
    
    public static void main(String[] args) throws Exception {
        new Task6().run();
    }

}
