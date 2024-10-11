package cz.kobul.aoc2020;

import java.util.List;

/**
 * https://adventofcode.com/2020/day/12
 * start: 9:48
 * end: 10:17
 */
public class Task12 extends Aoc2020 {

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }


//    Action N means to move north by the given value.
//    Action S means to move south by the given value.
//    Action E means to move east by the given value.
//    Action W means to move west by the given value.
//    Action L means to turn left the given number of degrees.
//    Action R means to turn right the given number of degrees.
//    Action F means to move forward by the given value in the direction the ship is currently facing.


    public Object solve1(List<String> lines) {
        Pos p = Pos.of();
        Dir dir= Dir.RIGHT;
        for (String l: lines) {
            char cmd = l.charAt(0);
            int dist = Integer.parseInt(l.substring(1));
            switch (cmd) {
            case 'F': p = p.move(dir, dist); break;
            case 'L': for (int i = 0; i < dist/90; i++) { dir = dir.rot(Rot.L); }; break;
            case 'R': for (int i = 0; i < dist/90; i++) { dir = dir.rot(Rot.R); }; break;
            case 'N': p = p.up(dist); break;
            case 'S': p = p.down(dist); break;
            case 'E': p = p.right(dist); break;
            case 'W': p = p.left(dist); break;
            }
        }
        return p.mdist(Pos.of());
    }
    
    public Object solve2(List<String> lines) {
        Pos p = Pos.of();
        Pos off = new Pos(-1, 10);
        for (String l: lines) {
            char cmd = l.charAt(0);
            int dist = Integer.parseInt(l.substring(1));
            switch (cmd) {
            case 'F': for (int i = 0; i < dist; i++) { p = p.move(off); }; break;
            case 'L': for (int i = 0; i < dist/90; i++) { off = off.rot(Rot.L); }; break;
            case 'R': for (int i = 0; i < dist/90; i++) { off = off.rot(Rot.R); }; break;
            case 'N': off = off.up(dist); break;
            case 'S': off = off.down(dist); break;
            case 'E': off = off.right(dist); break;
            case 'W': off = off.left(dist); break;
            }
        }
        return p.mdist(Pos.of());
    }	
    
    public static void main(String[] args) throws Exception {
        new Task12().run();
    }

}
