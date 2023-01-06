package cz.kobul.aoc2022;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * https://adventofcode.com/2022/day/23
 * start 7:15 end 9:07 vcetne snidane
 */
public class Task23 extends Aoc {

    protected Object solve1(List<Elf> elves) {
        int dirStart = 0;
        for (int i = 0; i < 10; i++) {
            Set<Pos> emap = elves.stream().map(Elf::getPos).collect(Collectors.toSet());

            // vybrat smer
            for (Elf e : elves) {
                chooseDir(emap, e, dirStart);
            }

            dirStart = (dirStart + 1) % 4;

            Map<Pos, Integer> neweMap = new HashMap<>();
            // zkusit posun
            for (Elf e : elves) {
                if (e.dir != null) {
                    neweMap.compute(e.pos.move(e.dir), (t, u) -> (u == null ? Integer.valueOf(1) : Integer.valueOf(u.intValue()+1)));
                }
            }
            for (Elf e : elves) {
                if (e.dir != null) {
                    Pos newPos = e.pos.move(e.dir);
                    if (neweMap.get(newPos) == 1) {
                        // budu tam sam, posunu se
                        e.pos = newPos;
                    }
                    e.dir = null;
                }
            }

//            printElves(elves);
        }

        // najit velikost mapy a pocet prazdnych mist
        int minRow = Integer.MAX_VALUE;
        int minCol = Integer.MAX_VALUE;
        int maxRow = Integer.MIN_VALUE;
        int maxCol = Integer.MIN_VALUE;

        for (Elf e : elves) {
            minRow = Math.min(minRow, e.pos.row());
            minCol = Math.min(minCol, e.pos.col());
            maxRow = Math.max(maxRow, e.pos.row());
            maxCol = Math.max(maxCol, e.pos.col());
        }


        int size = Math.abs(maxRow - minRow + 1) * Math.abs(maxCol - minCol + 1);

        return size - elves.size();
    }

    protected Object solve2(List<Elf> elves) {
        int dirStart = 0;
        int i = 0;
        while (true) {
            i++;
            Set<Pos> emap = elves.stream().map(Elf::getPos).collect(Collectors.toSet());

            boolean someElfMoves = false;
            // vybrat smer
            for (Elf e : elves) {
                chooseDir(emap, e, dirStart);
                if (e.dir != null) {
                    someElfMoves =true;
                }
            }

            if (!someElfMoves) {
              return i;
            }

            dirStart = (dirStart + 1) % 4;

            Map<Pos, Integer> neweMap = new HashMap<>();
            // zkusit posun
            for (Elf e : elves) {
                if (e.dir != null) {
                    neweMap.compute(e.pos.move(e.dir), (t, u) -> (u == null ? Integer.valueOf(1) : Integer.valueOf(u.intValue()+1)));
                }
            }
            for (Elf e : elves) {
                if (e.dir != null) {
                    Pos newPos = e.pos.move(e.dir);
                    if (neweMap.get(newPos) == 1) {
                        // budu tam sam, posunu se
                        e.pos = newPos;
                    }
                    e.dir = null;
                }
            }
        }
    }

    class Elf {
        static char last = 'A';
        char ch;
        Pos pos;
        Dir dir;

        Elf (Pos p) {
            this.pos = p;
            ch = last;
            last++;
        }

        public Pos getPos() {
            return pos;
        }


        public Dir getDir() {
            return dir;
        }

        @Override
            public String toString() {
                return ch + " " + pos + " " + dir;
            }
    }

    List<Dir> directions = List.of(Dir.UP, Dir.DOWN, Dir.LEFT, Dir.RIGHT);

//    If there is no Elf in the N, NE, or NW adjacent positions, the Elf proposes moving north one step.
//    If there is no Elf in the S, SE, or SW adjacent positions, the Elf proposes moving south one step.
//    If there is no Elf in the W, NW, or SW adjacent positions, the Elf proposes moving west one step.
//    If there is no Elf in the E, NE, or SE adjacent positions, the Elf proposes moving east one step.


    boolean isFull(Set<Pos> map, Elf e, Dir dir) {
        return switch (dir) {
            case UP -> map.contains(e.pos.up()) || map.contains(e.pos.upLeft()) || map.contains(e.pos.upRight());
            case DOWN -> map.contains(e.pos.down()) || map.contains(e.pos.downLeft()) || map.contains(e.pos.downRight());
            case LEFT -> map.contains(e.pos.left()) || map.contains(e.pos.upLeft()) || map.contains(e.pos.downLeft());
            case RIGHT -> map.contains(e.pos.right()) || map.contains(e.pos.upRight()) || map.contains(e.pos.downRight());
        };
    }
    
    protected void chooseDir(Set<Pos> emap, Elf e, int dirStart) {
        e.dir = null;
        // kontrola, ze nekam vubec chce jit
        boolean some = false;
        for (DirFull d : DirFull.values()) {
            if (emap.contains(e.pos.move(d))) {
                some = true;
            }
        }
        if (some == false) {
            return;
        }

        for (int d = 0; d < 4; d++) {
            Dir dir = directions.get((dirStart + d) % 4);
            boolean full = isFull(emap, e, dir);
            if (!full) {
                e.dir = dir;
                return;
            }
        }
    }

    void printElves(List<Elf> elves) {
        Map2d m = new Map2d(10, 10);

        for (Elf e : elves) {
            m.point(e.pos, e.ch);
        }
        m.print();
    }

    List<Elf> parseElves(List<String> lines) {
        List<Elf> elves = new ArrayList<>();
        for (int r = 0; r< lines.size(); r++) {
            String row = lines.get(r);
            for (int c = 0; c < row.length(); c++) {
                if (row.charAt(c) == '#') {
                    Pos p = new Pos(r, c);
                    elves.add(new Elf(p));
                }
            }
        }
        return elves;
    }

    public void solve() throws Exception {
        List<String> lines = readFileToListString(getDefaultInputFileName());

        logResult(1, solve1(parseElves(lines)));
        logResult(2, solve2(parseElves(lines)));
    }

    public static void main(String[] args) throws Exception {
        new Task23().run();
    }


}
