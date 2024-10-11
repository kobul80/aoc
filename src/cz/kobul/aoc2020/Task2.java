package cz.kobul.aoc2020;

import java.util.List;

/**
 * https://adventofcode.com/2020/day/2
 * start: 13:06
 * end: 13:15
 */
public class Task2 extends Aoc2020 {

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }


    public Object solve1(List<String> lines) {
//        1-4 m: mrfmmbjxr
//        5-16 b: bbbbhbbbbpbxbbbcb
        long sum = 0;
        for (String line : lines) {
            String[] s = line.split(" ");
            String[] t = s[0].split("-");
            
            int[] interval = new int[] { Integer.parseInt(t[0]), Integer.parseInt(t[1])};
            char ch = s[1].charAt(0);
            int cnt = 0;
            for (int i =0 ; i < s[2].length(); i++) {
                if (s[2].charAt(i) == ch) {
                    cnt++;
                }
            }
            if (cnt >= interval[0] && cnt <= interval[1]) {
                sum++;
            }
        }
        return sum;
    }

    public Object solve2(List<String> lines) {
        long sum = 0;
        for (String line : lines) {
            String[] s = line.split(" ");
            String[] t = s[0].split("-");
            
            int[] interval = new int[] { Integer.parseInt(t[0]), Integer.parseInt(t[1])};
            char ch = s[1].charAt(0);

            try {
                char ch1 = s[2].charAt(interval[0]-1);
                char ch2 = s[2].charAt(interval[1]-1);                
                if ((ch1 == ch && ch2 != ch) || (ch1 != ch && ch2 == ch)) {
                    sum++;
                }
            } catch (Exception ex) {
                // nothing
            }
        }
        return sum;
    }	
    
    public static void main(String[] args) throws Exception {
        new Task2().run();
    }

}
