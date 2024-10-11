package cz.kobul.aoc2023;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * https://adventofcode.com/2023/day/17
 * start: 7:06
 * end: 
 */
public class Task17a extends Aoc2023 {

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultTestFileName();

        logResult(1, solve1(readFileToListString(fileName)));
//        logResult(2, solve2(readFileToListString(fileName)));
    }

    record Move4(Pos pos, Dir last, int lastCnt) {
        Move4 move(Dir dir) {
            return new Move4(pos.move(dir), dir, (dir == last ? lastCnt+1 : 1));
        }
    };

    record MoveHeat(Move4 move, int heat) {}
    
    Map<Move4, Integer> minPathLength = new HashMap<>();
    Map<Pos, Integer> minPosLength = new HashMap<>();
    Map<Pos, List<Pos>> minPaths = new HashMap<>();

    record NextMove(Dir dir, int heat) {}

    int getHeat(Map2d m, Pos p) {
        return ((int)m.get(p) - 48);
    }
    
    int shortest1(Queue<MoveHeat> moves, Pos finish, Map2d m) {
        //      System.out.println(start + " " + heat);
//        List<Move4> newMoves = new ArrayList<>();

        int maxHeat = Integer.MAX_VALUE;

        MoveHeat start ;
        while ((start = moves.poll()) != null) {
            System.out.println(moves.size());
            Pos pos = start.move.pos;
            int heat = start.move.last != null ? getHeat(m, pos) : 0;
            
            if (pos.equals(finish) && start.heat < maxHeat) {
                maxHeat = start.heat;
                continue;
            }

            List<Dir> possible = new ArrayList<>();
            for (Dir dir : Dir.values()) {
                if (m.isIn(pos.move(dir)) && (start.move.last == null || dir != start.move.last.back())) {
                    possible.add(dir);
                }
            }                            
            
            if (start.move.lastCnt == 3) {
                possible.remove(start.move.last);
            }
            List<NextMove> nextMoves = new ArrayList<>();
            for (Dir dir : possible) {
                nextMoves.add(new NextMove(dir, ((int)m.get(pos.move(dir))) - 48));
            }
            for (NextMove nextMove : nextMoves.stream().sorted(Comparator.comparing(NextMove::heat)).toList()) {
                moves.add(new MoveHeat(start.move.move(nextMove.dir), start.heat + heat));
            }
            
            
        }   
        return maxHeat;
//            if (start.last != null) {
//                Integer minHeat1 = minPosLength.get(pos);
//                                    
//                if ((minHeat1 == null || heat < minHeat1.intValue())) {
//                    // nejsem v cili, nebo posledni 4 kroky jsou stejne
//                    minPosLength.put(pos, heat);
//                }
//
//                Integer max = (minPosLength.get(finish));
//                if (max != null && max.intValue() < heat) {
//                    break;
//                }
//
//                Integer minHeat = minPathLength.get(start);
//                if (minHeat != null && minHeat.intValue() <= heat) {
//                    // mame delsi nebo stejnou cestu, koncime
//                    break; 
//                } else {
//                    minPathLength.put(start, heat);
//                }
//                if (pos.equals(finish)) {
//                    System.out.println(heat);
//                    break;
//                }
//            }
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
//2570
    public Integer solve1(List<String> lines) {
        Map2d map = new Map2d(lines);

        Pos start = new Pos(0,0);
        Pos end = new Pos(map.rows()-1, map.cols()-1);
        Queue<MoveHeat> q= new ConcurrentLinkedQueue<>();
        q.add(new MoveHeat(new Move4(start, null, 0), 0));
        return shortest1(q, end, map);
    }

    public Integer solve2(List<String> lines) {
        Map2d map = new Map2d(lines);

        minPosLength.clear();
        Pos start = new Pos(0,0);
        Pos end = new Pos(map.rows()-1, map.cols()-1);
        shortest2(new Move4(start, null, 0), end, map, 0);
        return minPosLength.get(end);
    }

    public static void main(String[] args) throws Exception {
        new Task17a().run();
    }

}
