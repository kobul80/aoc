package cz.kobul.aoc2023;

import java.util.ArrayList;
import java.util.List;

/**
 * https://adventofcode.com/2023/day/14
 * start: 6:00
 * end: 6:30
 */
public class Task14 extends Aoc2023 {

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }
    
    public Integer solve1(List<String> lines) {
        Map2d map = new Map2d(lines);
        tilt(map, Dir.UP);
        return beamLoad(map);
   }

    /**
     * @param map
     * @return zatez severniho paprsku :-)
     */
    protected int beamLoad(Map2d map) {
        int res = 0;
        for (int r = 0; r < map.rows(); r++) {
            for (int c = 0; c< map.cols(); c++) {
                Pos p = new Pos(r, c);
                if (map.get(p)=='O') {
                    res += (map.rows() -r);
                }
            }
        }
        return res;
    }
    
    /**
     * Nakloni a setrese mapu na vsechny strany - SEVER, ZAPAD, JIH, VYCHOD (UP, LEFT, DOWN, RIGHT)
     * @param map
     */
    protected void tiltAll(Map2d map) {
        tilt(map, Dir.UP);
        tilt(map, Dir.LEFT);
        tilt(map, Dir.DOWN);
        tilt(map, Dir.RIGHT);
    }    

    /**
     * Nakloni a setrese mapu jednim smerem
     * @param map
     * @param dir
     */
    protected void tilt(Map2d map, Dir dir) {
        boolean move = true;
        while (move) {
            move = false;
            for (int r = 0; r < map.rows(); r++) {
                for (int c = 0; c< map.cols(); c++) {
                    Pos p = new Pos(r, c);
                    Pos mp = p.move(dir);
                    if (map.isIn(mp)) {
                        if (map.get(p) == 'O' && map.get(mp) != '#' && map.get(mp) != 'O') {
                            map.point(mp, 'O');
                            map.point(p, '.');
                            move = true;
                        }                        
                    }
                }
            }
        }
    }

    public Integer solve2(List<String> lines) {
        Map2d map = new Map2d(lines);
        List<Integer> loads = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            tiltAll(map);
            loads.add(beamLoad(map));
        }
        Cycle<Integer> c = findMaxCycle(loads);
        
        if (c == null) {
            throw new IllegalStateException("Cycle not found!");
        }
        
        // posunu o 1 zpet - mam cislovane od 0 a ne od 1
        int index = (999999999 - c.startPos()) % c.cyclicNumbers().size();
        return c.cyclicNumbers().get(index);
    }

    public static void main(String[] args) throws Exception {
        new Task14().run();
    }
    
}
