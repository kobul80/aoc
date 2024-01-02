package cz.kobul.aoc2022;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * https://adventofcode.com/2022/day/5
 */
public class Task5 extends Aoc2022 {

    public static record Move (int count, int from, int to) {
    }
    
    public static record Parsed(List<Stack<Character>> buckets, List<Move> moves) {}
    
    public static Parsed parse(List<String> lines) {
        List<Stack<Character>> buckets = new ArrayList<>();
        List<Move> moves = new ArrayList<>();

        List<char[]> bucketsInput = new ArrayList<>();

        boolean arr = true;


        for (String line : lines) {
            if (arr) {
                if (!line.contains("1")) {
                    // rozlozeni
                    int cnt = (line.length() / 4) + 1;
                    char[] ln = new char[cnt];
                    for (int i = 0; i < cnt; i++) {
                        ln[i] = line.charAt((i * 4) + 1);
                    }
                    bucketsInput.add(ln);
                } else {
                    arr = false;
                }
            } else {
                // pohyby
                List<Integer> l = getIntegersFromString(line);
                if (l.size() == 3) {
                    moves.add(new Move(l.get(0), l.get(1), l.get(2)));
                }
            }
        }

        int bucketCnt = bucketsInput.get(0).length;
        for (int i = 0; i < bucketCnt; i++) {
            buckets.add(new Stack<Character>());
        }

        for (int i = bucketsInput.size() - 1; i >= 0; i--) {
            char[] line = bucketsInput.get(i);
            for (int j = 0; j < line.length; j++) {
                if (line[j] != ' ') {
                    buckets.get(j).push(line[j]);
                }
            }
        }

        return new Parsed(buckets, moves);
    }

    public static Object solve1(List<String> lines) {
        Parsed parsed = parse(lines);
        List<Stack<Character>>  buckets = parsed.buckets();
        List<Move> moves = parsed.moves();

        for (Move m : moves) {
            for (int i = 0; i < m.count; i++) {
                Character ch = buckets.get(m.from - 1).pop();
                buckets.get(m.to - 1).push(ch);
            }
        }

        StringBuilder result = new StringBuilder();
        for (Stack<Character> b : buckets) {
            result.append(b.peek());
        }
        return result;

    }

    public static Object solve2(List<String> lines) {
        Parsed parsed = parse(lines);
        List<Stack<Character>>  buckets = parsed.buckets();
        List<Move> moves = parsed.moves();

        for (Move m : moves) {
            List<Character> crates = new ArrayList<>();
            for (int i = 0; i < m.count; i++) {
                Character ch = buckets.get(m.from - 1).pop();
                crates.add(0, ch);
            }
            for (Character crate : crates) {
                buckets.get(m.to - 1).push(crate);
            }
        }

        StringBuilder result = new StringBuilder();
        for (Stack<Character> b : buckets) {
            result.append(b.peek());
        }
        return result;
    }

    public void solve() throws Exception {
        String fileName = "c:/tmp/aoc2022/5/input.txt";

        List<String> lines = Files.readAllLines(Paths.get(fileName));

        logResult(1, solve1(lines));
        logResult(2, solve2(lines));
    }
    
    public static void main(String[] args) throws Exception {
    	new Task5().run();
    }

}
