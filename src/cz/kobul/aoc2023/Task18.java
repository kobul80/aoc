package cz.kobul.aoc2023;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * https://adventofcode.com/2023/day/18
 * start: 6:00
 * end: 8:47
 */
public class Task18 extends Aoc2023 {

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();

        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }  
    
    public Input parse1(List<String> lines) {
        int maxr = 0;
        int maxc = 0;
        int minr = Integer.MAX_VALUE;
        int minc = Integer.MAX_VALUE;
        Pos p = new Pos(0, 0);
        List<Instr> instrs = new ArrayList<>();
        for (String line : lines) {
            String[] s = line.split(" ");
            Dir dir = null;
            for (Dir d : Dir.values()) {
                if (d.name().startsWith(s[0])) {
                    dir = d;
                }
            }
            int cnt = Integer.parseInt(s[1]);
            for (int i = 0; i < cnt; i++) {
                p = p.move(dir);
                maxr = Math.max(maxr, p.row());
                maxc = Math.max(maxc, p.col());
                minr = Math.min(minr, p.row());
                minc = Math.min(minc, p.col());
            }
            instrs.add(new Instr(dir, cnt));
        }
        Pos start = new Pos(Math.abs(minr), Math.abs(minc));
        return new Input(instrs, minr, maxr, minc, maxc, start);
    }
    
    
    public Long solve1(List<String> lines) {
        return solveNum(parse1(lines));
    }

    public Long solve2(List<String> lines) {
        return solveNum(parse2(lines));
    }
    
    record Instr(Dir dir, int cnt) {}

    record Intr(int from, int to) {
        long len() {
            return Math.abs(to - from + 1);
        }
        
    }
    
    record Input(List<Instr> instrs, int minr, int maxr, int minc, int maxc, Pos start) {}

    protected Long solve(Input inp) {
        Set<Integer> hsplit = new HashSet<>();
        Set<Integer> vsplit = new HashSet<>();

        Pos p = inp.start;
        for (Instr in : inp.instrs()) {
            hsplit.add(p.col());
            vsplit.add(p.row());
            p = p.move(in.dir, in.cnt);
        }
        
        List<Integer> _hs= hsplit.stream().sorted().toList();
        List<Integer> _vs= vsplit.stream().sorted().toList();

        List<Intr> hs = toIntervals(_hs);
        List<Intr> vs = toIntervals(_vs);
        
       
        Map2d map = new Map2d(vs.size(), hs.size()); 
        
        p = inp.start;
        int col = hs.indexOf(new Intr(p.col(), p.col()));
        int row = vs.indexOf(new Intr(p.row(), p.row()));
        Pos start = new Pos(row, col);

        for (Instr in : inp.instrs()) {
            int cnt = 0;
            p = p.move(in.dir, in.cnt);
            if (in.dir == Dir.UP || in.dir == Dir.DOWN) {
                cnt = Math.abs(vs.indexOf(new Intr(p.row(), p.row())) - start.row());
            } else if (in.dir == Dir.LEFT || in.dir == Dir.RIGHT) {
                cnt = Math.abs(hs.indexOf(new Intr(p.col(), p.col())) - start.col());
            }
            for (int i = 0; i < cnt; i++) {
                map.point(start, '#');
                start = start.move(in.dir);
            }
        }
        
//        map.print();
        map.floodFill(map.getPositions('#').get(0).downRight(), '#');
        
//        map.print();
        long sum = 0;
        for (Pos filled : map.getPositions('#')) {
            Intr v= vs.get(filled.row());
           Intr h= hs.get(filled.col());
           long s = (long)v.len() * (long)h.len();
            sum += s;
        }
        return sum;
    }
    
    protected Long solveNum(Input inp) {
        Pos p = inp.start;
        
        List<Pos> points = new ArrayList<>();
        points.add(p);
        for (Instr in : inp.instrs()) {
            p = p.move(in.dir, in.cnt);
            points.add(p);
        }

        return polyArea(points);
    }
    
    protected List<Intr> toIntervals(List<Integer> _hs) {
        List<Intr> hs = new ArrayList<>();
        for (int i = 1; i < _hs.size(); i++) {
            int from = _hs.get(i - 1);
            int to = _hs.get(i);
            
            hs.add(new Intr(from, from));
            if (to - from > 1) {
                hs.add(new Intr(from+1, to-1));
            }
            if (i == _hs.size() - 1) {
                hs.add(new Intr(to, to));
            }
        }
        return hs;
    }
    
    public Input parse2(List<String> lines) {
        int maxr = 0;
        int maxc = 0;
        int minr = Integer.MAX_VALUE;
        int minc = Integer.MAX_VALUE;
        Pos p = new Pos(0, 0);
        List<Instr> instrs = new ArrayList<>();
        
        for (String line : lines) {
            String[] s = line.split(" ");
            int cnt = Integer.parseInt(s[2].substring(2, 7), 16);
            int dirI = Integer.parseInt(s[2].substring(7, 8), 16);
//            0 means R, 1 means D, 2 means L, and 3 means U
            Dir dir = switch (dirI) {
                case 0 -> Dir.RIGHT;
                case 1 -> Dir.DOWN;
                case 2 -> Dir.LEFT;
                case 3 -> Dir.UP;
                default -> null;
            };
            p = p.move(dir, cnt);
            maxr = Math.max(maxr, p.row());
            maxc = Math.max(maxc, p.col());
            minr = Math.min(minr, p.row());
            minc = Math.min(minc, p.col());
            instrs.add(new Instr(dir, cnt));
        }
        Pos start = new Pos(Math.abs(minr), Math.abs(minc));
        return new Input(instrs, minr, maxr, minc, maxc, start);
    }

    public static void main(String[] args) throws Exception {

        new Task18().run();
    }

}
