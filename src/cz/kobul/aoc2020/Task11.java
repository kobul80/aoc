package cz.kobul.aoc2020;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * https://adventofcode.com/2020/day/11
 * start: 8:45
 * end: 9:09
 */
public class Task11 extends Aoc2020 {

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }

    
    public int adjfull(Map2d map, Pos p) {
        int full = 0;
        for (char ch : Arrays.stream(DirFull.values()).map(d -> map.get(p.move(d))).toList()) {
            if (ch == '#') { full++; }
        }
        return full;
    }
    
    public int adjfull2(Map2d map, Pos p) {
        int full = 0;
        
        for (DirFull dir : DirFull.values()) {
            Pos p1 = p.move(dir);
            while (map.isIn(p1) && (map.get(p1) == '.')) {
                p1 = p1.move(dir);
            }
            if (map.get(p1) == '#') {
                full++;
            }            
        }
        return full;
    }
    

    
    public Object solve1(List<String> lines) {
        Map2d map = new Map2d(lines);
        boolean someChange = true;
        while (someChange) {
            List<Pos> empty = map.getPositions('L');
            Set<Pos> full = new HashSet<>(map.getPositions('#'));
            List<Pos> occupy = newList();
            List<Pos> free = newList();
            for (Pos e : empty) {
                if (adjfull(map, e) == 0) {
                    occupy.add(e);
                }
            }
            for (Pos f : full) {
                if (adjfull(map, f) >= 4) {
                    free.add(f);
                }
            }
            map.points(occupy, '#');
            map.points(free, 'L');
//            map.print();
            someChange = occupy.size() > 0 || free.size() > 0;
        }
        return map.getPositions('#').size();
    }

    public Object solve2(List<String> lines) {
        Map2d map = new Map2d(lines);
        boolean someChange = true;
        while (someChange) {
            List<Pos> empty = map.getPositions('L');
            Set<Pos> full = new HashSet<>(map.getPositions('#'));
            List<Pos> occupy = newList();
            List<Pos> free = newList();
            for (Pos e : empty) {
                if (adjfull2(map, e) == 0) {
                    occupy.add(e);
                }
            }
            for (Pos f : full) {
                if (adjfull2(map, f) >= 5) {
                    free.add(f);
                }
            }
            map.points(occupy, '#');
            map.points(free, 'L');
//            map.print();
            someChange = occupy.size() > 0 || free.size() > 0;
        }
        return map.getPositions('#').size();
    }	
    
    public static void main(String[] args) throws Exception {
        new Task11().run();
    }

}
