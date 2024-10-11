package cz.kobul.aoc2020;

import java.util.ArrayList;
import java.util.List;

/**
 * https://adventofcode.com/2020/day/8
 * start: 7:56
 * end: 8:17
 */
public class Task8 extends Aoc2020 {

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }

    public Object solve1(List<String> lines) {
        return -run(lines);
    }

    public Object solve2(List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).contains("nop") || lines.get(i).contains("jmp")) {
                int res = run(replace(lines, i));
                if (res > 0) {
                    return res;
                }
            }
        }
        return null;
    }

    public List<String> replace(List<String> lines, int line) {
        List<String> result = new ArrayList<>(lines);
        String l = result.get(line);
        if (l.contains("nop")) {
            result.set(line, l.replace("nop", "jmp"));
        } else {
            result.set(line, l.replace("jmp", "nop"));
        }
        return result;
    }
    
    
    protected int run(List<String> lines) {
        int acc = 0;
        boolean[] visited = new boolean[lines.size()];
        int line = 0;
        while (line < lines.size()) {
            if (visited[line]) {
                return -acc;
            }
            String l = lines.get(line);
            visited[line]=true;
            String[] s = l.split(" ");
            int cnt = Integer.parseInt(s[1]);
            String instr = s[0];
            switch (instr) {
            case "acc": acc+=cnt;
                        line++;
                        break;
            case "jmp": line+=cnt;
                        break;
            default: line++;
            }
        }
        return acc;
    }	
    
    public static void main(String[] args) throws Exception {
        new Task8().run();
    }

}
