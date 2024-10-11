package cz.kobul.aoc2020;

import java.util.List;

/**
 * https://adventofcode.com/2020/day/3
 * start: 13:15
 * end: 13:24
 */
public class Task3 extends Aoc2020 {

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }


    public Object solve1(List<String> lines) {
        List<String> newLines = newList();
        int mult = 1000;
        for (String line : lines) {
            StringBuilder newLine = new StringBuilder(line.length() * mult);
            for (int i = 0; i < mult; i++) {
                newLine.append(line);
            }
            newLines.add(newLine.toString());
        }
        
        Map2d map = new Map2d(newLines);
        Pos p = new Pos(0, 0);
        
        int trees = 0;
        while (p.row() < map.rows()) {
            if (map.get(p) == '#') {
                trees++;
            }
            p = p.down().right(3);
        }
        return trees;
    }

    public Object solve2(List<String> lines) {
        List<String> newLines = newList();
        int mult = 1000;
        for (String line : lines) {
            StringBuilder newLine = new StringBuilder(line.length() * mult);
            for (int i = 0; i < mult; i++) {
                newLine.append(line);
            }
            newLines.add(newLine.toString());
        }
        
        Map2d map = new Map2d(newLines);
        
//        Right 1, down 1.
//        Right 3, down 1. (This is the slope you already checked.)
//        Right 5, down 1.
//        Right 7, down 1.
//        Right 1, down 2.

        int[] trees = new int[5];
        Pos[] pos = new Pos[] { new Pos(0, 0), new Pos(0, 0), new Pos(0, 0), new Pos(0, 0), new Pos(0, 0) };
        while (pos[0].row() < map.rows()) {
            for (int i =0; i < 5; i++) {
                if (map.isIn(pos[i]) && map.get(pos[i]) == '#') {
                    trees[i]++;
                }                
            }
            pos[0] = pos[0].down().right();
            pos[1] = pos[1].down().right(3);
            pos[2] = pos[2].down().right(5);
            pos[3] = pos[3].down().right(7);
            pos[4] = pos[4].down(2).right(1);
        }
        return (long)trees[0] * trees[1] * trees[2] * trees[3] * trees[4];
    }	
    
    public static void main(String[] args) throws Exception {
        new Task3().run();
    }

}
