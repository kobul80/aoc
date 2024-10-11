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
public class Task11a extends Aoc2023 {

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
        Set<Integer> galCols = new HashSet<>();
        Set<Integer> emptyRows = new HashSet<>();
        Set<Integer> emptyCols = new HashSet<>();
        for (int r = 0; r < lines.size(); r++) {
            String l = lines.get(r);
            if (!l.contains("#")) {
                emptyRows.add(r);
            }
            for (int c = 0; c < l.length(); c++) {
                if (l.charAt(c) == '#') {
                    galCols.add(c);
                }
            }
        }
        for (int c = 0; c < lines.get(0).length(); c++) {
            if (!galCols.contains(c)) {
                emptyCols.add(c);
            }
        }
        
        List<Pos> galaxies = new ArrayList<>();

        Map2d map = new Map2d(lines.size(), lines.get(0).length());
        for (int r = 0; r < lines.size(); r++) {
            for (int c = 0; c < lines.get(r).length(); c++) {
                char ch = lines.get(r).charAt(c);
                Pos pos = new Pos(r, c);
                map.point(pos, ch);
                if (ch == '#') {
                    galaxies.add(pos);
                }
            }
        }

        long sum = 0;
        for (int g1 = 0; g1 < galaxies.size() - 1; g1++) { 
            for (int g2 = g1 + 1; g2 < galaxies.size(); g2++) {
                Pos p1 = galaxies.get(g1);
                Pos p2 = galaxies.get(g2);
                int rFrom = Math.min(p1.row(), p2.row());
                int rTo = Math.max(p1.row(), p2.row());

                int cFrom = Math.min(p1.col(), p2.col());
                int cTo = Math.max(p1.col(), p2.col());
                
                long p = 0;
                for (int r = rFrom; r < rTo; r++) {
                    if (emptyRows.contains(r)) {
                        p += (1 * expansionFactor);
                    } else {
                        p++;
                    }
                }

                for (int c = cFrom; c < cTo; c++) {
                    if (emptyCols.contains(c)) {
                        p += (1 * expansionFactor);
                    } else {
                        p++;
                    }
                }

                sum += p;
            }
        }
        return sum;
    }
   
//    [P1] 9769724
//    [P2] 603020563700
    
    public static void main(String[] args) throws Exception {
        new Task11a().run();
    }

}
