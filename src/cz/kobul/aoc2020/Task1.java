package cz.kobul.aoc2020;

import java.util.List;

/**
 * https://adventofcode.com/2020/day/1
 * start: 13:00
 * end: 13:05
 */
public class Task1 extends Aoc2020 {

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }


    public Object solve1(List<String> lines) {
        int[] nums = lines.stream().mapToInt(Integer::parseInt).toArray();
        for (int i = 0; i < lines.size(); i++) {
            for (int j = i; j < lines.size(); j++) {
                if (i != j) {
                    int i1 = nums[i];
                    int i2 = nums[j];
                    if (i1 + i2 == 2020) {
                        return i1 * i2;
                    }
                }
            }
        }
        return null;
    }

    public Object solve2(List<String> lines) {
        int[] nums = lines.stream().mapToInt(Integer::parseInt).toArray();
        for (int i = 0; i < lines.size(); i++) {
            for (int j = i; j < lines.size(); j++) {
                for (int k = j; k < lines.size(); k++) {
                    if (i != j && j != k) {
                        int i1 = nums[i];
                        int i2 = nums[j];
                        int i3 = nums[k];
                        if (i1 + i2 + i3 == 2020) {
                            return i1 * i2 * i3;
                        }
                    }
                }
            }
        }
        return null;
    }	
    
    public static void main(String[] args) throws Exception {
        new Task1().run();
    }

}
