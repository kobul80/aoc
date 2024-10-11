package cz.kobul.aoc2023;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * https://adventofcode.com/2023/day/15
 * start: 9:05
 * end: 9:35
 */
public class Task15 extends Aoc2023 {

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();

        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }

    public Integer solve1(List<String> lines) {
        return Arrays.stream(lines.get(0).split(",")).map(this::hash).reduce(Integer::sum).get();
    }

    public Long solve2(List<String> lines) {

        Box[] boxes = new Box[256];
        for (int i = 0; i < boxes.length; i++) {
            boxes[i] = new Box();
        }
        String[] instr = lines.get(0).split(",");
        for (String in : instr) {
            String[] s = in.split("[=-]");
            String lensName = s[0];
            int box = (int) hash(lensName);
            if (in.contains("-")) {
                boxes[box].lens.remove(lensName);
            } else {
                boxes[box].lens.put(lensName, Integer.valueOf(s[1]));
            }
        }
        long result = 0;
        for (int i = 0; i < boxes.length; i++) {
            int idx = 1;
            for (int foc : boxes[i].lens.values()) {
                result += ((i + 1) * (idx) * foc);
                idx++;
            }
        }
        return result;
    }

    public int hash(String s) {
        int result = 0;
        for (int i = 0; i < s.length(); i++) {
            result += ((int) s.charAt(i));
            result *= 17;
            result %= 256;
        }
        return result;
    }

    static class Box {
        Map<String, Integer> lens = new LinkedHashMap<>(); 
    }

    public static void main(String[] args) throws Exception {
        new Task15().run();
    }

}
