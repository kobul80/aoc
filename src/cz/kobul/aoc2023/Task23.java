package cz.kobul.aoc2023;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * https://adventofcode.com/2023/day/23
 * start: 6:55
 * end: 
 */
public class Task23 extends Aoc2023 {

    @Override
    public void solve() throws Exception {
//        String fileName = getDefaultTestFileName();
                String fileName = getDefaultInputFileName();

//        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2d(readFileToListString(fileName)));
    }     

   
    Map<Pos, Integer> longest = newMap();

    Map<Pos2, Integer> longest2 = newMap();

    Map<Character, List<Dir>> possible = Map.of('^', List.of(Dir.UP), '<', List.of(Dir.LEFT), '>', List.of(Dir.RIGHT), 'v', List.of(Dir.DOWN), '.', List.of(Dir.values()));
    
//    public List<Pos> getPossibleMoves(Set<Pos> visited, Pos p, Map2d map) {
//        char ch = map.get(p);
//        List<Dir> dirs = possible.get(ch);
//        List<Pos> poss = newList();
//        for (Dir dir : dirs) {
//            Pos p1 = p.move(dir);
//            if (map.isIn(p1) && map.get(p1) != '#' && !visited.contains(p1)) {
//                poss.add(p1);
//            }
//        }
//        return poss;
//    }

    public List<Pos> getPossibleMoves(Set<Pos> visited, Pos p, Map2d map) {
        return getPossibleMoves(visited, Set.of(), p, map);
    }    
    
    public List<Pos> getPossibleMoves(Set<Pos> visited, Set<Pos> disabled, Pos p, Map2d map) {
        char ch = map.get(p);
        List<Dir> dirs = possible.get(ch);
        List<Pos> poss = newList();
        for (Dir dir : dirs) {
            Pos p1 = p.move(dir);
            if (map.isIn(p1) && map.get(p1) != '#' && (!visited.contains(p1)) && (!disabled.contains(p1))) {
                poss.add(p1);
            }
        }
        return poss;
    }
    

    public void maximumPath(Pos p, Pos end, Set<Pos> visited, Map2d map) {
        int pathLen = visited.size();
        Integer max = longest.get(p);
        if (max == null || max.intValue() < pathLen) {
//            System.out.println("Longest to ");
            longest.put(p, pathLen);
            if (p.equals(end)) {
                System.out.println(pathLen);
            }
        }
        Set<Pos> newVisited = add(visited, p);
        List<Pos> poss = getPossibleMoves(visited, p, map);
        for (Pos np : poss) {
            maximumPath(np, end, newVisited, map);
        }        
    }

    protected void print(Set<Pos> visited, Map2d map) {
        print(visited, Set.of(), map);
    }
    
    protected void print(Set<Pos> visited, Set<Pos> disabled, Map2d map) {
        Map2d map2 = map.duplicate();
        int index = 0;
        Pos last = null;
        for (Pos p2 : visited) {
            char ch = 'v';
            if (last != null) {
                if (last.equals(p2.up())) {
                    ch = 'v';
                } else if (last.equals(p2.down())) {
                    ch = '^';
                } else if (last.equals(p2.left())) {
                    ch = '>';
                } else if (last.equals(p2.right())) {
                    ch = '<';
                }
                
            }
            map2.point(p2, ch);
            last = p2;
            index++;
        }
        for (Pos p : disabled) {
            map2.point(p, 'X');
        }
        map2.print();
    }
    
    public Object solve1(List<String> lines) {
        Map2d map = new Map2d(lines);
        Pos start = new Pos(0, map.row(0).indexOf('.'));
        Pos end = new Pos(map.rows() - 1, map.row(map.rows() - 1).indexOf('.'));
        maximumPath(start, end, Set.of(), map);
        
//        System.out.println(longest);
        
        return longest.get(end);
    }



    record Pos2(Pos p, Pos last) {}
    
    record Path(Set<Pos> path, Pos last) {}

    record Path2(Set<Pos> path, Pos last1, Pos last2) {}

    
//    List<Pos> poss()
//    
//    int longest(Pos from, Pos to, Map2d map) {
//        return max(longest(from, poss(to)));
//    }
    

    public int maximumPath2(Pos p, Pos end,Set<Pos> visited, Map2d map) {
        int pathLen = visited.size();
        if (p.equals(end)) {
            return pathLen;
        }
        
        Set<Pos> newVisited = add(visited, p);
        List<Pos> poss = getPossibleMoves(visited, p, map);
        int max = 0;
        for (Pos np : poss) {
            max = max(max, maximumPath2(np, end,  newVisited, map));
        }        

        return max;
    }

    Map<Pos, Set<Pos>> longestPaths = newMap();

    boolean endReachable(Map2d map, Pos pos, Pos end, Set<Pos> visited) {
        Map2d map2 = map.duplicate();
        map2.points(visited, 'O');
        map2.floodFill(pos, 'O');
        return map2.get(end) == 'O';
    }
    
//    Set<Set<Pos>> unreachablePaths = newSet();
    
    public void maximumPath4(Pos p, Pos end, Set<Pos> visited, Map2d map) {
        int pathLen = visited.size();
        Integer max = longest.get(p);
        if (max == null || max.intValue() < pathLen) {
//            System.out.println("Longest to ");
            longest.put(p, pathLen);
            if (p.equals(end)) {
                System.out.println(pathLen);
            }
        }
        Set<Pos> newVisited = add(visited, p);
        List<Pos> poss = getPossibleMoves(visited, p, map);
        if (!poss.isEmpty()) {
            for (Pos np : poss) {
                maximumPath4(np, end, newVisited, map);
            }                    
        } else {
            print(visited, map);            
        }
    }

    
    

    public void maximumPath6(Pos p, Pos end, Set<Pos> visited, Set<Pos> disabled, Map2d map) {
        int pathLen = visited.size();
        Integer max = longest.get(p);
        if (max == null || max.intValue() < pathLen) {
//            System.out.println("Longest to ");
            longest.put(p, pathLen);
            if (p.equals(end)) {
                System.out.println(pathLen);
            }
        }
        Set<Pos> newVisited = add(visited, p);
        List<Pos> poss = getPossibleMoves(visited, p, map);
        
        if (poss.size() == 2) {
//            print(newVisited, disabled, map);
            
            for (int i = 0; i < 2; i++) {
                Pos np = poss.get(i);
                Pos oth = poss.get(1-i);
                List<Pos> nm =getPossibleMoves(newVisited, disabled, oth, map);
                Set<Pos> ds = disabled;
                while (nm.size() == 1) {
                    ds = add(ds, oth);
                    oth = nm.get(0);
                    nm =getPossibleMoves(newVisited, ds, oth, map);
                }
//                print(newVisited, ds, map);
                maximumPath6(np, end, newVisited, ds, map);
            }
            
        } else {
            for (Pos np : poss) {
                maximumPath6(np, end, newVisited, disabled, map);
            }                  
        }

        
    }


    
    public int maximumPath3(Pos p, Pos end,Set<Pos> visited, Map2d map) {
//        if (unreachablePaths.contains(add(visited, p))) {
//            System.out.println("End not reacheable cached.");
//            return 0;
//        }
        int pathLen = visited.size();
//        if (!endReachable(map, p, end, visited)) {
//            unreachablePaths.add(add(visited, p));
//            System.out.println("End not reacheable.");
//            System.out.println(pathLen);
//            return 0;
//        }
        Integer maxP = longest.get(p);
        if (maxP == null || maxP.intValue() <= pathLen) {
            longest.put(p, pathLen);
            longestPaths.put(p, visited);
        } else {
            System.out.println("Path in " + p + " not longest - longest is " + maxP);
            System.out.println("Current");
            print(visited, map);
            System.out.println("Longest");
            print(longestPaths.get(p), map);
            return 0;
        }
        if (p.equals(end)) {
            return pathLen;
        }
        
        Set<Pos> newVisited = add(visited, p);
        List<Pos> poss = getPossibleMoves(visited, p, map);
        int max = 0;
        for (Pos np : poss) {
            max = max(max, maximumPath3(np, end,  newVisited, map));
        }        

        return max;
    }


    public int maximumPath5(Pos p, Pos end,Set<Pos> visited, Map2d map) {

      int pathLen = visited.size();
      if (!reachable2(map, p, end, visited)) {
          return 0;
      }
      
      if (p.equals(end)) {
          return pathLen;
      }
      
      Set<Pos> newVisited = add(visited, p);

      int max = 0;
      List<Pos> poss = getPossibleMoves(visited, p, map);
      for (Pos np : poss) {
          max = max(max, maximumPath5(np, end, newVisited, map));
      }                  

      return max;          

  }


    public Integer solve2(List<String> lines) {
        Map2d map = new Map2d(lines);
        List<Pos> directed = map.getPositions('<', '>', 'v', '^');
        for (Pos p : directed) {
            map.point(p, '.');
        }
        System.out.println(directed.size());
        Pos start = new Pos(0, map.row(0).indexOf('.'));
        Pos end = new Pos(map.rows() - 1, map.row(map.rows() - 1).indexOf('.'));
        return maximumPath5(start, end, Set.of(), map);
//        maximumPath6(start, end, Set.of(), Set.of(), map);
//        return longest.get(end);

        
    }
    
    record Edge(int src, int dest) {}
    
    public Integer solve2a(List<String> lines) {
        Map2d map = new Map2d(lines);
        for (Pos p : map.getPositions('<', '>', 'v', '^')) {
            map.point(p, '.');
        }
        Pos start = new Pos(0, map.row(0).indexOf('.'));
        Pos end = new Pos(map.rows() - 1, map.row(map.rows() - 1).indexOf('.'));

        
        List<Pos> vertices = map.getPositions('.');
        Map<Pos, Integer> vertInd = newMap();
        for (int i = 0; i < vertices.size(); i++) {
            vertInd.put(vertices.get(i), i);
        }
        List<Edge> edges = newList();
        for (int i = 0; i < vertices.size(); i++) {
            Pos vert = vertices.get(i);
            for (Pos dest : getPossibleMoves(Set.of(), vert, map)) {
                edges.add(new Edge(i, vertInd.get(dest)));
            }
        }
        
        int V = vertices.size(); // Number of vertices in graph
        int E = edges.size(); // Number of edges in graph
 
        BFGraph graph = new BFGraph(V, E);

        for (int i = 0; i < edges.size(); i++) {
            graph.edge[i].src = edges.get(i).src;
            graph.edge[i].dest = edges.get(i).dest;
            graph.edge[i].weight = -1;            
        } 
          // Function call
        int[] result = graph.BellmanFord(graph, 0);
        return Math.abs(result[vertInd.get(end)]);
//        return longest.get(end);
    }

    public Integer solve2b(List<String> lines) {
        Map2d map = new Map2d(lines);
        for (Pos p : map.getPositions('<', '>', 'v', '^')) {
            map.point(p, '.');
        }
        Pos start = new Pos(0, map.row(0).indexOf('.'));
        Pos end = new Pos(map.rows() - 1, map.row(map.rows() - 1).indexOf('.'));

        
        List<Pos> vertices = new ArrayList<>();
        
        List<Pos> allPos = map.getPositions('.');
        for (Pos p : allPos) {
            if (getPossibleMoves(Set.of(), p, map).size() > 2) {
                vertices.add(p);
            }
        }
        
        Map<Pos2, Integer> edg = new HashMap<>();
        
        vertices.add(start);
        vertices.add(end);

        Map<Pos, Integer> vertInd = newMap();
        for (int i = 0; i < vertices.size(); i++) {
            vertInd.put(vertices.get(i), i);
        }

        for (Pos p : vertices) {
            for (Pos p1 : getPossibleMoves(Set.of(), p, map)) {
                Set<Pos> visited = newSet();
                visited.add(p);
                Pos px = p1;
                List<Pos> possible = getPossibleMoves(visited, px, map);
                while (possible.size()==1) {
                    visited.add(px);
                    px = possible.get(0);
                    possible = getPossibleMoves(visited, px, map);
                }
                edg.put(new Pos2(p, px), visited.size());
            }
        }
        
//        for (int i = 0; i < vertices.size(); i++) {
//            Pos vert = vertices.get(i);
//            for (Pos dest : getPossibleMoves(Set.of(), vert, map)) {
//                edges.add(new Edge(i, vertInd.get(dest)));
//            }
//        }
//        
        int[][] E = new int[vertices.size()][vertices.size()];
        for (int e1 = 0; e1 < E.length; e1++) {
            for (int e2 = 0; e2 < E.length; e2++) {
                E[e1][e2] = AllPairShortestPath.INF;
            }
        }
        
//        int V = vertices.size(); // Number of vertices in graph
//        int E = edges.size(); // Number of edges in graph

        for (int v = 0; v < vertices.size(); v++) {
            E[v][v] = 0;
        }

        for (Entry<Pos2, Integer> e : edg.entrySet()) {
            int e1 = vertInd.get(e.getKey().p);
            int e2 = vertInd.get(e.getKey().last);
            E[e1][e2] = e.getValue();
            E[e2][e1] = e.getValue();
        }

        int[][] result = new AllPairShortestPath().floydWarshall(E);
        return result[vertInd.get(start)][vertInd.get(end)];
        
        //        return longest.get(end);
    }

    
//    record PL(Pos p, int length) {}
    
    

    boolean reachable(Map2d map, Pos pos, Pos end, Set<Pos> visited) {
        Map2d map2 = map.duplicate();
        
        map2.points(visited, 'O');
        map2.floodFill(pos, 'O');
        return map2.get(end) == 'O';
    }
    
    boolean reachable2(Map2d map, Pos pos, Pos end, Set<Pos> visited) {  
        Set<Pos> vis = new HashSet<Pos>(visited);
        Queue<Pos> queue = new ConcurrentLinkedQueue<>();
        queue.add(pos);
        while (!queue.isEmpty()) {
            Pos p = queue.poll();
            vis.add(p);
            if (p.equals(end)) {
                return true;
            }
            for (Pos p1 : getPossibleMoves(vis, p, map)) {
                queue.add(p1);
            }
            
        }
        return false;
    }
    
    
    
    
    public Integer solve2c(List<String> lines) {
        Map2d map = new Map2d(lines);
        for (Pos p : map.getPositions('<', '>', 'v', '^')) {
            map.point(p, '.');
        }
        Pos start = new Pos(0, map.row(0).indexOf('.'));
        Pos end = new Pos(map.rows() - 1, map.row(map.rows() - 1).indexOf('.'));
//        List<Pos> pos = map.getPositions('.');
//        Map<Pos, Integer> indices = new HashMap<>();
//        for (int i = 0; i < pos.size(); i++) {
//            indices.put(pos.get(i), i);
//        }
//        System.out.println(pos.size());

        Map<Pos2, Integer> longest = newMap();
        Queue<Path2> queue = new ConcurrentLinkedQueue<>();

//        visited.add(end);
        queue.add(new Path2(Set.of(), end, null)); 
        
        int max = 0;
        while (!queue.isEmpty()) {
            Path2 s = queue.poll();

            if (s.last1.equals(start)) {
                System.out.println(s.path.size() + " q: " + queue.size());
                max = Math.max(max, s.path.size());
            }

            Pos2 key = new Pos2(s.last1, s.last2);
            Integer maxP = longest.get(key);
            if (maxP == null || maxP <= s.path.size()) {
//                System.out.println("Longest path to " + s.last1 + " from " + s.last2 + " size " + s.path.size());
                longest.put(key, s.path.size());

                for (Pos p : getPossibleMoves(s.path, s.last1, map)) {
                    if (!s.path.contains(p)) {
                        queue.add(new Path2(add(s.path, p), p, s.last1));
                    }
                }
            } else {
                System.out.println("Skipping not longest path to " + s.last1);
            }
        }
        return max;
    }

    record V12(int v1, int v2) {}
    
    record PathI(Set<Integer> path, int current, int length) {}
    
    public Integer solve2d(List<String> lines) {
        Map2d map = new Map2d(lines);
        for (Pos p : map.getPositions('<', '>', 'v', '^')) {
            map.point(p, '.');
        }
        Pos start = new Pos(0, map.row(0).indexOf('.'));
        Pos end = new Pos(map.rows() - 1, map.row(map.rows() - 1).indexOf('.'));
        
        
        List<Pos> vertices = new ArrayList<>();
        
        List<Pos> allPos = map.getPositions('.');
        for (Pos p : allPos) {
            if (getPossibleMoves(Set.of(), p, map).size() > 2) {
                vertices.add(p);
            }
        }
        
        Map<V12, Integer> edgeSize = new HashMap<>();

        Map<Integer, Set<Integer>> edges = new HashMap<>();
        
        vertices.add(start);
        vertices.add(end);

        Map<Pos, Integer> vertInd = newMap();
        for (int i = 0; i < vertices.size(); i++) {
            vertInd.put(vertices.get(i), i);
        }

        for (Pos p : vertices) {
            for (Pos p1 : getPossibleMoves(Set.of(), p, map)) {
                Set<Pos> visited = newSet();
                visited.add(p);
                Pos px = p1;
                List<Pos> possible = getPossibleMoves(visited, px, map);
                while (possible.size()==1) {
                    visited.add(px);
                    px = possible.get(0);
                    possible = getPossibleMoves(visited, px, map);
                }
                int pi = vertInd.get(p);
                int pxI = vertInd.get(px);
                edgeSize.put(new V12(pi, pxI), visited.size());
                edges.compute(pi, (k,v) -> v == null ? Set.of(pxI) : add(v, pxI));
            }
        }
        
        System.out.println(vertices.size());
        
        
        LinkedList<PathI> queue = newLinkedList();

//      visited.add(end);
      queue.add(new PathI(Set.of(), vertInd.get(start), 0)); 
//      
      int endIdx = vertInd.get(end);
      
      int max = 0;
      while (!queue.isEmpty()) {
          queue.sort(Comparator.comparing(PathI::length).reversed());
          PathI s = queue.removeFirst();

          if (s.current == endIdx) {
              System.out.println(s.length + " q: " + queue.size());
              max = Math.max(max, s.length);
          }
          
          
          for (int p : edges.get(s.current)) {
              if (!s.path.contains(p)) {
                  queue.add(new PathI(add(s.path, p), p, s.length + edgeSize.get(new V12(s.current, p))));
              }
          }
      }
      return max;
    }


    
    public static void main(String[] args) throws Exception {
        new Task23().run();
    }

}
