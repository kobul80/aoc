package cz.kobul.aoc2020;

import java.util.List;
import java.util.Set;

/**
 * https://adventofcode.com/2020/day/17
 * start: 17:25
 * end: 17:41
 */
public class Task17 extends Aoc2020 {

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }

    List<Pos3> getNei(Pos3 c) {
        List<Pos3> res = newList();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    if (!(i == 0 && j == 0 && k==0)) {
                        res.add(new Pos3(c.x() + i, c.y() + j, c.z() + k));
                    }
                }
            }
        }
        return res;
    }

    List<Pos4> getNei(Pos4 c) {
        List<Pos4> res = newList();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    for (int l = -1; l < 2; l++) {
                        if (!(i == 0 && j == 0 && k==0 && l==0)) {
                            res.add(new Pos4(c.x() + i, c.y() + j, c.z() + k, c.w() + l));
                        }
                    }
                }
            }
        }
        return res;
    }

    int neighb(Pos3 c, Set<Pos3> other) {
        int nei = 0;
        for (Pos3 o : getNei(c)) {
            if (other.contains(o)) {
                nei ++;
            }
        }
        return nei;
    }

    int neighb(Pos4 c, Set<Pos4> other) {
        int nei = 0;
        for (Pos4 o : getNei(c)) {
            if (other.contains(o)) {
                nei ++;
            }
        }
        return nei;
    }


    public Object solve1(List<String> lines) {
        Map2d map = new Map2d(lines);
        List<Pos> pos = map.getPositions('#');
        Set<Pos3> cubes = newSet();
        for (Pos p: pos) {
            cubes.add(new Pos3(p.row(), p.col(), 0));
        }
        for (int i = 0; i < 6; i++) {
            Set<Pos3> newCubes = newSet();
            for (Pos3 c : cubes) {
                int n = neighb(c, cubes);
                if (n == 2 || n == 3) {
                    newCubes.add(c);
                }
            }
            Set<Pos3> allIna = newSet();
            for (Pos3 c : cubes) {
                allIna.addAll(getNei(c));
            }
            allIna.removeAll(cubes);
            for (Pos3 c : allIna) {
                int n = neighb(c, cubes);
                if (n == 3) {
                    newCubes.add(c);
                }
            }   
            cubes = newCubes;
        }
        return cubes.size();
    }

    record Pos4(int x, int y, int z, int w) {}
    
    public Object solve2(List<String> lines) {
        Map2d map = new Map2d(lines);
        List<Pos> pos = map.getPositions('#');
        Set<Pos4> cubes = newSet();
        for (Pos p: pos) {
            cubes.add(new Pos4(p.row(), p.col(), 0, 0));
        }
        for (int i = 0; i < 6; i++) {
            Set<Pos4> newCubes = newSet();
            for (Pos4 c : cubes) {
                int n = neighb(c, cubes);
                if (n == 2 || n == 3) {
                    newCubes.add(c);
                }
            }
            Set<Pos4> allIna = newSet();
            for (Pos4 c : cubes) {
                allIna.addAll(getNei(c));
            }
            allIna.removeAll(cubes);
            for (Pos4 c : allIna) {
                int n = neighb(c, cubes);
                if (n == 3) {
                    newCubes.add(c);
                }
            }   
            cubes = newCubes;
        }
        return cubes.size();
    }	
    
    public static void main(String[] args) throws Exception {
        new Task17().run();
    }

}
