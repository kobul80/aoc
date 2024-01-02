package cz.kobul.aoc2022;

import java.util.HashSet;
import java.util.Set;

/**
 * https://adventofcode.com/2022/day/6
 * start 6:50, end 7:02
 */
public class Task6 extends Aoc2022 {

    public static void main(String[] args) throws Exception {
        new Task6().solve();
    }

    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();

        String content = getStringStream(fileName).findFirst().get();

        logResult(1, solve1(content));
        logResult(2, solve2(content));
    }

    private int solve1(String content) {
        for (int i = 3; i < content.length(); i++) {
            Set<Character> last4 = new HashSet<>();
            last4.add(content.charAt(i - 3));
            last4.add(content.charAt(i - 2));
            last4.add(content.charAt(i - 1));
            last4.add(content.charAt(i));
            if (last4.size() == 4) {
                return i + 1;
            }
        }
        return -1;
    }

    private int solve2(String content) {
        for (int i = 13; i < content.length(); i++) {
            Set<Character> last14 = new HashSet<>();
            for (int j = i - 13; j <= i; j++) {
                last14.add(content.charAt(j));
            }
            if (last14.size() == 14) {
                return i + 1;
            }
        }
        return -1;
    }


}
