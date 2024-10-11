package cz.kobul.aoc2023;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * https://adventofcode.com/2023/day/22
 * start: 6:00
 * end: 
 */
public class Task22 extends Aoc2023 {

    @Override
    public void solve() throws Exception {
//        String fileName = getDefaultTestFileName();
                String fileName = getDefaultInputFileName();

        logResult(1, solve1g(readFileToListString(fileName)));
        logResult(2, solve2g(readFileToListString(fileName)));
    }     

    static boolean isIn(Brick b2, int x, int y, int z) {
        return (x >= b2.x1 && x <= b2.x2)
                && (y >= b2.y1 && y <= b2.y2)
                && (z >= b2.z1 && z <= b2.z2);
    }
       
    static boolean[][][] grid;
    
    static record Brick(int x1, int y1, int z1, int x2, int y2, int z2) { 
        static Brick of(int[] l) {
            return new Brick(l[0], l[1], l[2], l[3], l[4], l[5]);
        }
        Brick moveDown() {
            return new Brick(x1, y1, z1-1, x2, y2, z2 -1);
        }
        boolean intersects(Brick b2) {
            return intersect(this, b2);
        }
        boolean isDown() {
            return z1 <= 1 || z2 <= 1;
        }
        void draw() { 
            draw(true);
        }
        void remove() {
            draw(false);
        }
        boolean intersectsGrid() {
            for (int z = z1; z <= z2; z++) {
                for (int y = y1; y <= y2; y++) {
                    for (int x = x1; x <= x2; x++) {
                        if (grid[x][y][z]) {
                            return true;
                        }
                    }
                }
            }  
            return false;
        }
        private void draw(boolean val) {
            for (int z = z1; z <= z2; z++) {
                for (int y = y1; y <= y2; y++) {
                    for (int x = x1; x <= x2; x++) {
                        grid[x][y][z] = val;
                    }
                }
            }
        }
    }

    protected Res moveBricksNum(final List<Brick> _br) {
        Set<Integer> fall = new HashSet<>();
        LinkedList<Brick> br = new LinkedList<>(_br);
        boolean move = true;
        while (move) {
            move = false;
            for (int i = 0; i < br.size(); i++) {
                Brick b1 = br.removeFirst();
                while (!b1.isDown() && !(colides(b1.moveDown(), br))) {
                    b1 = b1.moveDown();
                    move = true;
                    fall.add(i);
                }
                br.add(b1);
            }
        }
        return new Res(br, fall.size());
    }

    protected Res moveBricksGrid(final List<Brick> _br) {
        Set<Integer> fall = new HashSet<>();
        LinkedList<Brick> br = new LinkedList<>(_br);
        boolean move = true;
        while (move) {
            move = false;
            for (int i = 0; i < br.size(); i++) {
                Brick b1 = br.removeFirst();
                b1.remove();
                while (!b1.isDown() && !(b1.moveDown().intersectsGrid())) {
                    b1 = b1.moveDown();
                    move = true;
                    fall.add(i);
                }
                b1.draw();
                br.add(b1);
            }
        }
        return new Res(br, fall.size());
    }

    protected void clearGrid() {
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                Arrays.fill(grid[x][y], false);
                grid[x][y][0] = true;
            }
        }
    }

    protected void bottomLine() {
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                grid[x][y][0] = true;
            }
        }
    }
    

    protected void initGrid(List<Brick> br) {
        int maxX = Math.max(
                br.stream().map(Brick::x1).reduce(Integer::max).get(),
                br.stream().map(Brick::x2).reduce(Integer::max).get());
        int maxY = Math.max(
                br.stream().map(Brick::y1).reduce(Integer::max).get(),
                br.stream().map(Brick::y2).reduce(Integer::max).get());
        int maxZ = Math.max(
                br.stream().map(Brick::z1).reduce(Integer::max).get(),
                br.stream().map(Brick::z2).reduce(Integer::max).get());
//        grid = new boolean[maxZ + 1][maxY + 1][maxX + 1];
        grid = new boolean[maxX + 1][maxY + 1][maxZ + 1];
        bottomLine();
    }
    
    protected boolean[][][] duplicateGrid() {
        boolean[][][] newGrid = new boolean[grid.length][grid[0].length][grid[0][0].length];
        int maxZ = grid[0][0].length;
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                System.arraycopy(grid[x][y], 0, newGrid[x][y], 0, maxZ); 
            }
        }
        return newGrid;
    }

    
    public List<Brick> parse(List<String> lines) {
        return lines.stream().map(l -> Brick.of(toIntArray(l))).sorted((b1, b2) -> Integer.signum(b1.z1 - b2.z2)).toList();
    }
    
    List<Brick> bricks; 
    
    public String solve1g(List<String> lines) {
        bricks = parse(lines);
        List<Brick> br = bricks;
        initGrid(br);
        
        for (Brick b : br) {
            b.draw();
        }
        
        br = moveBricksGrid(br).br;
        
        boolean[][][] backup = duplicateGrid();
        
        int result = 0;
        int sumMoves = 0;
//        for (Brick b1 : br) {
//            sumMoves += moveBricksGrid(remove(br, b1)).moves;
//        }
//        return sumMoves; 

        for (Brick b1 : br) {
            b1.remove();
            int moves = moveBricksGrid(remove(br, b1)).moves;
            if (moves == 0) {
                result++;
            } else {
                sumMoves += moves;
                grid = backup;
                backup = duplicateGrid();
            }
            b1.draw();
        }
        return result + " " + sumMoves;
    }
    
    public Integer solve2g(List<String> lines) {
//        List<Brick> br= parse(lines);
//
//        initGrid(br);
//
//        br = moveBricksGrid(br).br;

//        boolean[][][] backup = duplicateGrid();
//
//        int sumMoves = 0;
//        for (Brick b1 : br) {
//            sumMoves += moveBricksGrid(remove(br, b1)).moves;
//        }
//        return sumMoves; 
        return null;
    }

    public Integer solve1n(List<String> lines) {
        List<Brick> br = parse(lines);
        
        br = moveBricksNum(br).br;
        
        int result = 0;
        for (Brick b1 : br) {
            if (moveBricksNum(remove(br, b1)).moves == 0) {
                result++;
            }
        }
        return result;
    }
    
    public Integer solve2n(List<String> lines) {
        List<Brick> br= parse(lines);
        
        br = moveBricksNum(br).br;
        
        int sumMoves = 0;
        for (Brick b1 : br) {
            sumMoves += moveBricksNum(remove(br, b1)).moves;
        }
        return sumMoves; 
    }
    

    record Res(List<Brick> br, int moves) {}
    
    protected static boolean colides(Brick b1, List<Brick> br) {
        for (Brick b2 : br) {
            if (b1.intersects(b2)) {
                return true;
            }
        }
        return false;
    }
          
    record Rect(int x1, int y1, int x2, int y2) {
    }

    public static boolean intersecta(int box1a1, int box1a2, int box2a1, int box2a2) {
        int min1 = Math.min(box1a1, box1a2);
        int min2 = Math.min(box2a1, box2a2);
        int max1 = Math.max(box1a1, box1a2);
        int max2 = Math.max(box2a1, box2a2);
        
        return !(min2 > max1 || min1 > max2);
    }
    
    public static boolean intersect(Rect r1, Rect r2) {
        return intersecta(r1.x1, r1.x2, r2.x1, r2.x2)
                || intersecta(r1.y1, r1.y2, r2.y1, r2.y2);
    }

    public static boolean intersect(Brick r1, Brick r2) {
        return intersecta(r1.x1, r1.x2, r2.x1, r2.x2)
                && intersecta(r1.y1, r1.y2, r2.y1, r2.y2)
                && intersecta(r1.z1, r1.z2, r2.z1, r2.z2);
    }
    
    public static void main(String[] args) throws Exception {
        new Task22().run();
    }

}
