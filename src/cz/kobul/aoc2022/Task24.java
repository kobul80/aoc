package cz.kobul.aoc2022;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * https://adventofcode.com/2022/day/24
 * start 8:24 end 23:01
 */
public class Task24 extends Aoc2022 {

    protected Object solve1() {
        State s0 = new State(0, input.start);

        traceQ(s0, input.end);
        return shortestToEnd.cycle;
    }

    Map<Uq, State> shortestPaths = new HashMap<>();

    State shortestToEnd = null;

    Set<Uq> visitedStates = new HashSet<>();

    record Uq(Pos pos, int cycleMod) {}

    protected void traceQ(State s, Pos end) {
        Queue<State> q = new ConcurrentLinkedQueue<>();
        q.add(s);
        int last = 0;
        while (!q.isEmpty()) {
            State newS = q.poll();
            if (newS.cycle > last) {
                last = newS.cycle;
            }
            List<State> newStates = traceImpl(newS, end);
            for (State add : newStates) {
                Uq uq = new Uq(add.current, add.cycle % input.cycles);
                if (!visitedStates.contains(uq)) {
                    q.add(add);
                    visitedStates.add(uq);
                }
            }
            if (shortestToEnd != null) {
                return;
            }
        }
    }

    protected void traceStack(State s, Pos end) {
        for (State ns : traceImpl(s, end)) {
            traceStack(ns, end);
        }
    }

    protected List<State> traceImpl(State s, Pos end) {
        if (s.cycle > 0) {
            int cycleMod = s.cycle % input.cycles;
            Uq key = new Uq(s.current, cycleMod);
            State shortest = shortestPaths.get(key);
            if (shortest == null || s.cycle < shortest.cycle) {
                shortestPaths.put(key, s);
                if (s.current.equals(end) && (shortestToEnd == null || shortestToEnd.cycle > s.cycle)) {
                    shortestToEnd = s;
                }
            } else if (s.cycle > shortest.cycle) {
                return Collections.emptyList();
            }
        }

        Pos curr = s.current;
        int cycle = s.cycle + 1;
        Set<Pos> movedBlPos = input.bls.get(cycle % input.cycles);

        List<State> result = new ArrayList<>();
        for (Dir d : Dir.values()) {
            Pos newPos = curr.move(d);
            boolean blocked = input.walls.contains(newPos) || movedBlPos.contains(newPos) || newPos.row() < 0 || newPos.row() >= input.rows();
            if (!blocked) {
                result.add(new State(cycle, newPos));
            }
        }
        if (!movedBlPos.contains(curr)) {
            result.add(new State(cycle, curr));
        }
        return result;
    }

    record Move(Pos pos, Dir d) {}

    record State(int cycle, Pos current) implements Comparable<State> {
        public int mdist(Pos pos1, Pos pos2) {
            return Math.abs(pos1.row() - pos2.row()) + Math.abs(pos1.col() - pos2.col());
        }

        public int mdistToEnd() {
            return mdist(current, input.end);
        }

        @Override
        public int compareTo(State o) {
            return o.mdistToEnd() -mdistToEnd();
        }
    }


    void print(State s, Input i) {
        Map2d map = new Map2d(8, 8);
        for (Pos w : i.walls) {
            map.point(w, '#');
        }
        map.point(s.current, 'o');
        map.print();
    }

    protected Object solve2() {
        State s0 = new State(0, input.start);

        traceQ(s0, input.end);
        State s1 = new State(shortestToEnd.cycle, input.end);
        shortestPaths.clear();
        shortestToEnd = null;
        visitedStates.clear();

        traceQ(s1, input.start);
        State s2 = new State(shortestToEnd.cycle, input.start);
        shortestPaths.clear();
        shortestToEnd = null;
        visitedStates.clear();

        traceQ(s2, input.end);
        return shortestToEnd.cycle;
    }

    record Bl(Pos pos, Dir dir) {
        Pos newPos() {
            return pos.move(dir);
        }
        char ch() {
            return switch (dir) {
            case LEFT -> '<';
            case RIGHT-> '>';
            case UP -> '^';
            case DOWN -> 'v';
            };
        }
    }

    record Input(Map<Integer, Set<Pos>> bls, Pos start, Pos end, Set<Pos> walls, int rows, int cols, int cycles) {
        boolean wall(Pos p) {
            return walls.contains(p);
        }

    }

    Input parse(List<String> lines) {
        List<Bl> blizzards = new ArrayList<>();
        Set<Pos> walls = new HashSet<>();
        Pos start = null;
        Pos end = null;
        for (int r = 0; r< lines.size(); r++) {
            String row = lines.get(r);
            for (int c = 0; c < row.length(); c++) {
                Pos p = new Pos(r, c);
                if (row.charAt(c) == '>') {
                    blizzards.add(new Bl(p, Dir.RIGHT));
                }
                if (row.charAt(c) == '<') {
                    blizzards.add(new Bl(p, Dir.LEFT));
                }
                if (row.charAt(c) == 'v') {
                    blizzards.add(new Bl(p, Dir.DOWN));
                }
                if (row.charAt(c) == '^') {
                    blizzards.add(new Bl(p, Dir.UP));
                }
                if (row.charAt(c) == '#') {
                    walls.add(p);
                }
                if (r == 0 && row.charAt(c) == '.') {
                    start = p;
                }
                if (r == lines.size() -1 && row.charAt(c) == '.')  {
                    end = p;
                }
            }
        }
        int cols =lines.get(0).length();
        int rows = lines.size();
        // simulate blizzards
        int cycles = (int) lcm(lines.size() - 2, lines.get(0).length() -2);
        Map<Integer, Set<Pos>> blizPos = new HashMap<>();
        for (int i = 0; i < cycles; i++) {
            blizPos.put(i, blizzards.stream().map(Bl::pos).collect(Collectors.toSet()));

            List<Bl> newBlizzards = new ArrayList<>();
            for (Bl bl : blizzards) {
                Pos newPos = null;
                if (walls.contains(bl.newPos())) {
                    switch (bl.dir) {
                    case LEFT: { newPos = new Pos(bl.pos.row(), cols - 2); break; }
                    case RIGHT: { newPos = new Pos(bl.pos.row(), 1); break; }
                    case UP: {newPos = new Pos(rows - 2, bl.pos.col()); break; }
                    case DOWN:{ newPos = new Pos(1, bl.pos.col()); break; }
                    default: break;
                    }
                } else {
                    newPos = bl.newPos();
                }
                newBlizzards.add(new Bl(newPos, bl.dir));
                blizzards = newBlizzards;
            }
        }

        return new Input(blizPos, start, end, walls, rows, cols, cycles);
    }

    static Input input;

    public void solve() throws Exception {
        List<String> lines = readFileToListString(getDefaultInputFileName());

        input = parse(lines);

        logResult(1, solve1());
        logResult(2, solve2());
    }

    public static void main(String[] args) throws Exception {
        new Task24().run();
    }


}
