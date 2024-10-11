package cz.kobul.aoc2023;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * https://adventofcode.com/2023/day/17
 * start: 7:06
 * end: 12:37
 */
public class Task17 extends Aoc2023 {

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();

        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }

    record Move4(Pos pos, Dir last, int lastCnt) {
        Move4 move(Dir dir) {
            return new Move4(pos.move(dir), dir, (dir == last ? lastCnt+1 : 1));
        }
    };

    Map<Move4, Integer> minPathLength = new HashMap<>();
    Map<Pos, Integer> minPosLength = new HashMap<>();

    record NextMove(Dir dir, int heat) {}

    void shortest1(Move4 start, Pos finish, Map2d m, int heat) {
        //      System.out.println(start + " " + heat);
        Pos pos = start.pos;
        if (start.last != null) {
            int h = ((int)m.get(pos) - 48);
            if (h < 0) {
                System.out.println(pos);
            }
            heat += h;      
            Integer minHeat1 = minPosLength.get(pos);
            
                
            if ((minHeat1 == null || heat < minHeat1.intValue())) {
                // nejsem v cili, nebo posledni 4 kroky jsou stejne
                minPosLength.put(pos, heat);
                //              minPaths.put(pos, path);
            }

            Integer max = (minPosLength.get(finish));
            if (max != null && max.intValue() < heat) {
                return;
            }

            //          } else {
            //              return;
            //          }

            Integer minHeat = minPathLength.get(start);
            if (minHeat != null && minHeat.intValue() <= heat) {
                // mame delsi nebo stejnou cestu, koncime
                return; 
            } else {
                minPathLength.put(start, heat);
            }
            if (pos.equals(finish)) {
                System.out.println(heat);
                return;
            }
        }
        List<Dir> possible = new ArrayList<>();
        for (Dir dir : Dir.values()) {
            if (m.isIn(pos.move(dir)) && (start.last == null || dir != start.last.back())) {
                possible.add(dir);
            }
        }                            
        
        if (start.lastCnt == 3) {
            possible.remove(start.last);
        }
//        for (Dir dir : possible) {
//            shortest1(start.move(dir), finish, m, heat);
//        }

        List<NextMove> nextMoves = new ArrayList<>();
        for (Dir dir : possible) {
            nextMoves.add(new NextMove(dir, ((int)m.get(pos.move(dir))) - 48));
        }
        for (NextMove nextMove : nextMoves.stream().sorted(Comparator.comparing(NextMove::heat)).toList()) {
            shortest1(start.move(nextMove.dir), finish, m, heat);
        }
    }  

    
    void shortest2(Move4 start, Pos finish, Map2d m, int heat) {
        //      System.out.println(start + " " + heat);
        Pos pos = start.pos;
        if (start.last != null) {
            int h = ((int)m.get(pos) - 48);
            if (h < 0) {
                System.out.println(pos);
            }
            heat += h;      
            Integer minHeat1 = minPosLength.get(pos);
            
                
            if ((minHeat1 == null || heat < minHeat1.intValue()) && (!pos.equals(finish) || start.lastCnt() >= 4)) {
                // nejsem v cili, nebo posledni 4 kroky jsou stejne
                minPosLength.put(pos, heat);
                //              minPaths.put(pos, path);
            }

            Integer max = (minPosLength.get(finish));
            if (max != null && max.intValue() < heat) {
                return;
            }

            //          } else {
            //              return;
            //          }

            Integer minHeat = minPathLength.get(start);
            if (minHeat != null && minHeat.intValue() <= heat) {
                // mame delsi nebo stejnou cestu, koncime
                return; 
            } else {
                minPathLength.put(start, heat);
            }
            if (pos.equals(finish)) {
                System.out.println(heat);
                return;
            }
        }
        List<Dir> possible = new ArrayList<>();
        if (start.last == null) {
            for (Dir dir : Dir.values()) {
                if (m.isIn(pos.move(dir))) {
                    possible.add(dir);
                }
            }            
        } else {
            if (start.lastCnt() < 4) {
                // musim se pohybovat stejnym smerem, jako prve
                if (m.isIn(pos.move(start.last))) {
                    possible.add(start.last);                    
                }
            } else {
                for (Dir dir : Dir.values()) {
                    if (m.isIn(pos.move(dir)) && (start.last == null || dir != start.last.back())) {
                        possible.add(dir);
                    }
                }                            
            }
        }
        
        if (start.lastCnt == 10) {
            possible.remove(start.last);
        }
        List<NextMove> nextMoves = new ArrayList<>();
        for (Dir dir : possible) {
            nextMoves.add(new NextMove(dir, ((int)m.get(pos.move(dir))) - 48));
        }
        for (NextMove nextMove : nextMoves.stream().sorted(Comparator.comparing(NextMove::heat)).toList()) {
            shortest2(start.move(nextMove.dir), finish, m, heat);
        }
    }  

    public Integer solve1(List<String> lines) {
        Map2d map = new Map2d(lines);

        Pos start = new Pos(0,0);
        Pos end = new Pos(map.rows()-1, map.cols()-1);
        shortest1(new Move4(start, null, 0), end, map, 0);
        return minPosLength.get(end);
    }

    public Integer solve2(List<String> lines) {
        Map2d map = new Map2d(lines);

        minPosLength.clear();
        minPathLength.clear();
        
        Pos start = new Pos(0,0);
        Pos end = new Pos(map.rows()-1, map.cols()-1);
        shortest2(new Move4(start, null, 0), end, map, 0);
        return minPosLength.get(end);
    }

    public static void main(String[] args) throws Exception {
        new Task17().run();
    }

}
