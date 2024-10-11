package cz.kobul.aoc2023;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * https://adventofcode.com/2023/day/10
 * start: 6:55
 * end: 8:33
 */
public class Task10 extends Aoc2023 {


    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }
   

//    | is a vertical pipe connecting north and south.
//    - is a horizontal pipe connecting east and west.
//    L is a 90-degree bend connecting north and east.
//    J is a 90-degree bend connecting north and west.
//    7 is a 90-degree bend connecting south and west.
//    F is a 90-degree bend connecting south and east.
//    . is ground; there is no pipe in this tile.
//    S is the starting position of the animal; there is a pipe on this tile, but your sketch doesn't show what shape the pipe has.

    
    enum Pipe {
        VERT('|', Dir.UP, Dir.DOWN),
        HOR('-', Dir.RIGHT, Dir.LEFT),
        NE('L', Dir.UP, Dir.RIGHT),
        NW('J', Dir.LEFT,Dir.UP),
        SW('7', Dir.DOWN, Dir.LEFT),
        SE('F', Dir.RIGHT, Dir.DOWN);
        private char id;
        private List<Dir> dirs;
        private Pipe(char id, Dir ...dirs) {
            this.id = id;
            this.dirs = List.of(dirs);
        }
        public char getId() {
            return id;
        }
        
        Pos move(Pos current, Pos from) {
            if (current.move(dirs.get(1)).equals(from)) {
                return current.move(dirs.get(0));
            } 
            return current.move(dirs.get(1));
            
        }
        
        static Pipe of(char ch) {
            for (Pipe pipe : Pipe.values()) {
                if (pipe.getId() == ch) {
                    return pipe;
                }
            }
            throw new IllegalStateException("Unknown pipe " + ch);
        }
        
    }
    
    
    public Long solve1(List<String> lines) {
        Map2d map = new Map2d(lines.size(), lines.get(0).length());
//        Pos start = new Pos(1,1);
        Pos start = new Pos(22, 91);
        for (int r = 0; r < lines.size(); r++) {
            for (int c = 0; c < lines.get(r).length(); c++) {
                char ch = lines.get(r).charAt(c);
                Pos pos = new Pos(r, c);
                map.point(pos, ch);
                if (ch == 'S') {
                    start = pos;
                }
            }
        }
//        System.out.println(start);
        
        Pos last = start;
        Pos current = start;
        Pipe pipe = Pipe.of(map.get(current));
        current = pipe.move(current, null);
        long moves = 1;
        while (!current.equals(start)) {
            pipe = Pipe.of(map.get(current));
            Pos prev = current;
            current = pipe.move(current, last);            
            last = prev;
            moves++;
        }

//        map.print();
//        System.out.println(start);
        return moves/2;
    }    

    public Long solve2(List<String> lines) {
        Map2d map = new Map2d(lines.size(), lines.get(0).length());
//      Pos start = new Pos(1,1);
      Pos start = new Pos(22, 91);
//      Pos start = new Pos(0, 4);
      for (int r = 0; r < lines.size(); r++) {
          for (int c = 0; c < lines.get(r).length(); c++) {
              char ch = lines.get(r).charAt(c);
              Pos pos = new Pos(r, c);
              map.point(pos, ch);
              if (ch == 'S') {
                  start = pos;
              }
          }
      }

//      System.out.println(start);

      Set<Pos> visited = new HashSet<>(); 
      
      visited.add(start);
      Pos last = start;
      Pos current = start;
      Pipe pipe = Pipe.of(map.get(current));
      current = pipe.move(current, null);
      visited.add(current);
      while (!current.equals(start)) {
          pipe = Pipe.of(map.get(current));
          Pos prev = current;
          current = pipe.move(current, last);
          visited.add(current);
          last = prev;
      }

      
      for (int r = 0; r < map.rows(); r++) {
          for (int c = 0; c < map.cols(); c++) {
              Pos pos = new Pos(r, c);
              if (!visited.contains(pos)) {
                  map.point(pos, '.');
              }
          }
      }

      
      last = start;
      current = start;
      pipe = Pipe.of(map.get(current));
      current = pipe.move(current, null);
      visited.add(current);
      while (!current.equals(start)) {
          pipe = Pipe.of(map.get(current));
          if (pipe == Pipe.VERT) {
              if (last.move(Dir.UP).equals(current)) {
                  // jsem na svisle a prijel jsem zdola
                  floodFill(map, Dir.RIGHT, current);
              } else {
                  floodFill(map, Dir.LEFT, current);
              }
          }
          if (pipe == Pipe.HOR) {
              if (last.move(Dir.RIGHT).equals(current)) {
                  // jsem na vodorovne a prijel jsem zleva
                  floodFill(map, Dir.DOWN, current);
              } else {
                  floodFill(map, Dir.UP, current);
              }
          }
          
//          NE('L', Dir.UP, Dir.RIGHT),
//          NW('J', Dir.LEFT,Dir.UP),
//          SW('7', Dir.DOWN, Dir.LEFT),
//          SE('F', Dir.RIGHT, Dir.DOWN);

          if (pipe == Pipe.NE) {
              if (last.move(Dir.DOWN).equals(current)) {
                  // jsem na L a prijel jsem shora
                  floodFill(map, Dir.DOWN, current);
              }
          }

          if (pipe == Pipe.SE) {
              if (last.move(Dir.LEFT).equals(current)) {
                  // jsem na L a prijel jsem shora
                  floodFill(map, Dir.UP, current);
              }
          }

          if (pipe == Pipe.SW) {
              if (last.move(Dir.UP).equals(current)) {
                  // jsem na L a prijel jsem shora
                  floodFill(map, Dir.UP, current);
              }
          }

          if (pipe == Pipe.NW) {
              if (last.move(Dir.RIGHT).equals(current)) {
                  // jsem na L a prijel jsem shora
                  floodFill(map, Dir.DOWN, current);
              }
          }

          
          Pos prev = current;
          current = pipe.move(current, last);
          visited.add(current);
          last = prev;
      }

      long in = 0;
      for (int r = 0; r < map.rows(); r++) {
          for (int c = 0; c < map.cols(); c++) {
              Pos pos = new Pos(r, c);
              if (map.get(pos) == '1') {
                  in++;
              }
          }
      }
//      System.out.println(in);
      
      
      map.print();
//      System.out.println(start);
//      return moves/2;
//      return null;
      return in;
    }

    protected void floodFill(Map2d map, Dir dir, Pos current) {
        Pos moved = current.move(dir);
          if (map.get(moved) == '.') {
              map.floodFill(moved, '1');
          }
    }
    
    public static void main(String[] args) throws Exception {
        new Task10().run();
    }

}
