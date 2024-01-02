package cz.kobul.aoc2022;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * https://adventofcode.com/2022/day/3
 */
public class Task3 extends Aoc2022 {

	public static final Character sameInHalfs(String input) {
        Set<Character> s1 = new HashSet<>();
        Set<Character> s2 = new HashSet<>();
        int half = input.length() / 2;
        for (int i = 0; i < half; i++) {
            s1.add(input.charAt(i));
            s2.add(input.charAt(i + half));
        }
        s1.retainAll(s2);
        return s1.iterator().next();
    }

    public static final Integer priority(Character ch) {
        int c = ch.charValue();
        if (c > 64 && c <= 90) {
            return 27 + (c - 65);
        }
        return c - 96;
    }

    public static final Set<Character> toSet(String s) {
        Set<Character> set = new HashSet<>();
        for (int i = 0; i < s.length(); i++) {
            set.add(s.charAt(i));
        }
        return set;
    }

    public static final Character commonCharacter(List<String> packs) {
        Set<Character> s1 = toSet(packs.get(0));
        for (int i = 1; i < packs.size(); i++) {
            s1.retainAll(toSet(packs.get(i)));
        }
        return s1.iterator().next();
    }
    
    protected Object solve1(Stream<String> input) {
    	return input.map(Task3::sameInHalfs).map(Task3::priority).reduce(Integer::sum).get();
    }
    
    protected Object solve2(Stream<String> input) {
    	return partition(input.toList(), 3).stream().map(Task3::commonCharacter).map(Task3::priority).reduce(Integer::sum).get();	
    }
    
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        logResult(1, solve1(getStringStream(fileName)));
        logResult(2, solve2(getStringStream(fileName)));
    }

    public static void main(String[] args) throws Exception {
        new Task3().run();
    }
    
}
    

