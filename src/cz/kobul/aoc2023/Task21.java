package cz.kobul.aoc2023;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * https://adventofcode.com/2023/day/21
 * start: 6:00
 * end: 8:40
 */
public class Task21 extends Aoc2023 {

    @Override
    public void solve() throws Exception {
//        String fileName = getDefaultTestFileName();
        String fileName = getDefaultInputFileName();

        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }     

    public Integer solve1(List<String> lines) {
        Map2d map = new Map2d(lines);
        HashSet<Pos> positions= new HashSet<Pos>(map.getPositions('S'));
        int steps = 64;
        while (steps > 0) {
            List<Pos> posit = new ArrayList<>(positions);
            positions.clear();
            for (Dir dir : Dir.values()) {
                for (Pos pos : posit) {
                    Pos p = pos.move(dir);
                    if (map.get(p) != '#') {
                        positions.add(p);
                    }
                }
            }
            steps--;
        }
        return positions.size();
    }

    String dupl(String s, int cnt) {
        StringBuilder result = new StringBuilder();
        int mid = (cnt/2);
        for (int i = 0; i < cnt; i++) {
            if (s.contains("S") && i != mid) {
                result.append(s.replace('S', '.'));
            } else {
                result.append(s);                
            }
        }
        return result.toString();
    }
    
    Map2d duplicate(List<String> lines, int cnt) {
        List<String> result = new ArrayList<>();
        int mid = (cnt/2);
        for (int i = 0; i < cnt; i++) {
            for (String l :lines) {
                String d= dupl(l, cnt);
                if (d.contains("S") && i != mid) {
                    result.add(d.replace('S', '.'));                    
                } else {
                    result.add(d);
                }
            }
        }
        return new Map2d(result);
    }
    
    public Long solve2(List<String> lines) {
        Map<Integer, Map<Integer, Integer>> countSumsByCycle = new HashMap<>();
        for (int duplicate : new int[]{ 9 }) {
            Map2d map = duplicate(lines, duplicate);
            List<Pos> positions= map.getPositions('S');
            if (positions.size() > 1) {
                throw new IllegalStateException("Multiple starts! " + positions);
            }
            int steps = 65 + ((duplicate / 2) * 131);
            int step = 1;
            while (step <= steps) {
                // nahrada hashsetu, je daleko rychlejsi to mit takhle - v 2d poli je true, kam uz jsem 'vlezl'
                // - pri prvnim true pridam do seznamu na dalsi kolo
                boolean[][] st = new boolean[map.rows()][map.cols()];
                List<Pos> newPositions = new ArrayList<>(positions.size() * 3);
                for (Pos pos : positions) {
                    for (Dir dir : Dir.values()) {
                        Pos p = pos.move(dir);
                        if (map.isIn(pos) && map.get(p) != '#') {
                            if (!st[p.row()][p.col()]) {
                                st[p.row()][p.col()] = true;
                                newPositions.add(p);
                            }
                            
                        } 
                    }
                }
                positions = newPositions;
                step++;
            }
            
            Map2d map2 = map.duplicate();
            map2.points(positions, 'O');
                       
            Map<Integer, Integer> counts = new HashMap<>();
            
            for (int r =0; r < duplicate; r++) {
                for (int c = 0; c < duplicate; c++) {
                    int count = (int) map2.subMap(new Pos(r * 131, c * 131), 131, 131).count('O');
                    counts.compute(count, (k, v) -> (v == null ? 1 : v + 1));
                    System.out.print(lpad(Integer.toString(count), 7, ' '));                
                    
                }
                System.out.println();
            }
            System.out.println(counts);
            
            Map<Integer, Integer> countSums = new HashMap<>();
            for (Entry<Integer, Integer> e : counts.entrySet()) {
                int number = e.getKey();
                int count = e.getValue();
                countSums.compute(count, (k, v) -> (v == null ? number : v+number));
            }
            System.out.println(countSums);
            
            countSumsByCycle.put(duplicate/2, countSums);
        }
        
        long cycle = 202300; // (numberOfSteps - 65) / 131
        long sum = 0;
        int refCycle = 4;
        Map<Integer, Integer> x = countSumsByCycle.get(refCycle);
        for (Entry<Integer, Integer> e : x.entrySet()) {
            int cnt = e.getKey();
            int number = e.getValue();
            if (cnt == refCycle) {
                sum += (cycle * number);
            } else if (cnt == refCycle - 1) {
                sum += ((cycle - 1) * number);                
            } else if (cnt == 1) {
                sum += (number);
            } else if (cnt == refCycle * refCycle) {
                sum += (number * (cycle) * (cycle));
            } else if (cnt == (refCycle -1) * (refCycle -1)) {
                sum += (number * (cycle - 1) * (cycle - 1));                
            }
        }
        
        return sum;
//        594606492802848
    }    
    
    public static void main(String[] args) throws Exception {
        new Task21().run();
    }

}
