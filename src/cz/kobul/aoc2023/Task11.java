package cz.kobul.aoc2023;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * https://adventofcode.com/2023/day/11
 * start: 6:00
 * end: 6:32
 */
public class Task11 extends Aoc2023 {

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }

    public Long solve1(List<String> lines) {
        return solve(lines, 2);
    }

    public Long solve2(List<String> lines) {
        return solve(lines, 1000000);
    }

    protected long solve(List<String> lines, int expansionFactor) {
        Map2d map = new Map2d(lines);

        Set<Integer> emptyRows = newSet();
        Set<Integer> emptyCols = newSet();
        for (int r = 0; r < map.rows(); r++) {
            if (!map.row(r).contains("#")) {
                emptyRows.add(r);
            }
        }
        for (int c = 0; c < map.cols(); c++) {
            if (!map.col(c).contains("#")) {
                emptyCols.add(c);
            }
        }

        List<Pos> galaxies = map.getPositions('#');

        long sum = 0;
        for (int g1 = 0; g1 < galaxies.size() - 1; g1++) { 
            for (int g2 = g1 + 1; g2 < galaxies.size(); g2++) {
                Pos p1 = galaxies.get(g1);
                Pos p2 = galaxies.get(g2);
                
                long dist = p1.mdist(p2)
                        + (expansionFactor-1) * retainAll(range(p1.row(), p2.row()), emptyRows).size()
                        + (expansionFactor-1) * retainAll(range(p1.col(), p2.col()), emptyCols).size();
                
                sum += dist;
            }
        }
        return sum;
    }
    
    public static void main(String[] args) throws Exception {
        new Task11().run();
    }

}
