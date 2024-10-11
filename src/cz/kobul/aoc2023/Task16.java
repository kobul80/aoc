package cz.kobul.aoc2023;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * https://adventofcode.com/2023/day/16
 * start: 7:02
 * end: 7:39
 */
public class Task16 extends Aoc2023 {

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();

        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }

    record Beam(Pos pos, Dir dir){

        List<Beam> move(Map2d map) {
            List<Beam> result = new ArrayList<>();
            for (Beam b : moveImpl(map)) {
                if (map.isIn(b.pos())) {
                    result.add(b);
                }
            }
            return result;
        }
        
        List<Beam> moveImpl(Map2d map) {
            char ch = map.get(pos);
            if (ch == '.') {
                return List.of(newDirMove(dir));
            } else if (ch == '|') {
                if (dir == Dir.LEFT || dir == Dir.RIGHT) {
                    return List.of(newDirMove(Dir.UP), newDirMove(Dir.DOWN));                    
                }
                return List.of(new Beam(pos.move(dir), dir));
            } else if (ch == '-') {
                if (dir == Dir.UP|| dir == Dir.DOWN) {
                    return List.of(newDirMove(Dir.LEFT), newDirMove(Dir.RIGHT));                    
                }
                return List.of(newDirMove(dir));
            } else if (ch == '/') {
                if (dir == Dir.RIGHT) {
                    return List.of(newDirMove(Dir.UP));
                } else if (dir == Dir.LEFT) {
                    return List.of(newDirMove(Dir.DOWN));
                } else if (dir == Dir.UP) {
                    return List.of(newDirMove(Dir.RIGHT));
                } else if (dir == Dir.DOWN) {
                    return List.of(newDirMove(Dir.LEFT));
                }
            } else if (ch == '\\') {
                if (dir == Dir.RIGHT) {
                    return List.of(newDirMove(Dir.DOWN));
                } else if (dir == Dir.LEFT) {
                    return List.of(newDirMove(Dir.UP));
                } else if (dir == Dir.UP) {
                    return List.of(newDirMove(Dir.LEFT));
                } else if (dir == Dir.DOWN) {
                    return List.of(newDirMove(Dir.RIGHT));
                } 
            }
            throw new IllegalStateException("Unknown char " + ch);
        }
        
        Beam newDirMove(Dir dir) {
            return new Beam(pos.move(dir), dir);
        }
    }

    public Integer solve1(List<String> lines) {
        Map2d map = new Map2d(lines);
        return solve(map, new Beam(new Pos(0,0), Dir.RIGHT));
    }

    protected Integer solve(Map2d map, Beam start) {
        Set<Pos> energized = newSet();
        LinkedList<Beam> beams = newLinkedList();
        beams.add(start);
        Set<Beam> allBeams = newSet();
        while (!beams.isEmpty()) {
            Beam b = beams.removeFirst();
            if (allBeams.add(b)) {
                energized.add(b.pos);
                List<Beam> r = b.move(map);
                beams.addAll(r);                
            }
        }
        return energized.size();
    }

    public Integer solve2(List<String> lines) {
        int result = 0;
        Map2d map = new Map2d(lines);
        for (int r = 0; r < map.rows(); r++) {
            result = max(result, solve(map, new Beam(new Pos(r,0), Dir.RIGHT)));            
            result = max(result, solve(map, new Beam(new Pos(r,map.cols()-1), Dir.LEFT)));            
        }
        for (int c = 0; c < map.cols(); c++) {
            result = max(result, solve(map, new Beam(new Pos(0,c), Dir.DOWN)));            
            result = max(result, solve(map, new Beam(new Pos(map.rows()-1,c), Dir.UP)));            
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        new Task16().run();
    }

}
