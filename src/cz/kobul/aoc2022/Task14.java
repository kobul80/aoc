package cz.kobul.aoc2022;

import java.util.ArrayList;
import java.util.List;

/**
 * https://adventofcode.com/2022/day/14
 * start 6:10 end 6:50
 */
public class Task14 extends Aoc2022 {

    char ROCK = '#';
    char SAND = 'o';
    char VOID = ' ';

    Pos sandStart = new Pos(0, 500);

    public void solve() throws Exception {
        List<String> lines = readFileToListString(getDefaultInputFileName());

        logResult(1, solve1(parseMap(lines)));
        logResult(2, solve2(parseMap(lines)));
    }


    protected Map2d parseMap(List<String> lines) {
        Map2d map = new Map2d(sandStart);
        for (String line : lines ) {
            String[] l = line.split(" -> ");
            List<Pos> object = new ArrayList<>();
            for (String p : l) {
                Pos pos = Pos.ofColRow(p);
                object.add(pos);
            }
            for (int i =1; i < object.size(); i++) {
                map.line(object.get(i-1), object.get(i), ROCK);
            }
        }
        return map;
    }


    int moveSand(Map2d map) {
        Pos current = sandStart;
        int moves = 0;
        while (current.row() < map.maxRowPP()) {
            if (map.isVoid(current.down())) {
                current = current.down();
                moves++;
            } else if (map.isVoid(current.downLeft())) {
                current = current.downLeft();
                moves++;
            } else if (map.isVoid(current.downRight())) {
                current = current.downRight();
                moves++;
            } else {
                // nic neni volny, zustavam
                map.point(current, SAND);
                return moves;
            }
        }
        return -1;
    }

    protected Object solve1(Map2d map) {
        int total = 0;
        while (moveSand(map) != -1) {
            total ++;
        }
        return total;
    }


    protected Object solve2(Map2d map) {
        int lineAdd = 300;
        map.line(new Pos(map.maxRowPP() + 1, map.minCol() - lineAdd), new Pos(map.maxRowPP() + 1, map.maxColPP() + lineAdd), ROCK);
        int total = 0;
        while (map.get(sandStart) != SAND) {
            moveSand(map);
            total ++;
        }
        return total;
    }

    public static void main(String[] args) throws Exception {
        new Task14().run();
    }

}

