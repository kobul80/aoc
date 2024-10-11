package cz.kobul.aoc2023;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * https://adventofcode.com/2023/day/25
 * start: 8:00
 * end: 22:40 :/
 */
public class Task25 extends Aoc2023 {

    @Override
    public void solve() throws Exception {
//        String fileName = getDefaultTestFileName();
                String fileName = getDefaultInputFileName();

        logResult(1, solve1(readFileToListString(fileName)));
//        logResult(2, solve2(readFileToListString(fileName)));
    }     

    Map<String, Set<String>> components = newMap();   
    
    Set<String> reacheable(String start, Map<String, Set<String>> comps, Set<Edge> missing) {
        LinkedList<String> q = new LinkedList<>();
        Set<String> visited = newSet();
        Iterator<String> it = comps.keySet().iterator();
        String first = it.next();
        q.add(first);
        visited.add(first);
        while (!q.isEmpty()) {
            String v = q.removeFirst();
            for (String n : components.get(v)) {
                if (!visited.contains(n) && !missing.contains(new Edge(v, n)) && !missing.contains(new Edge(n, v))) {
                    q.add(n);
                    visited.add(n);
                }
            }
        }
        return visited;
    }
    
    Set<Set<String>> connectedGroups(Map<String, Set<String>> _comps, Set<Edge> missing) {
        Map<String, Set<String>> comps = new HashMap<>(_comps);
//        comps.keySet().removeAll(missing);
        Set<Set<String>> result = new HashSet<>();
        while (!comps.isEmpty()) {
            Set<String> reach = reacheable(comps.keySet().iterator().next(), comps, missing);
            comps.keySet().removeAll(reach);
            result.add(reach);
        }
        return result;
    }

    record Edge(String src, String dest) {}
    
    
    List<String> dfs(String start, Map<String, Set<String>> comps) {
        LinkedList<String> q = new LinkedList<>();
        Set<String> visited = newSet();
        Iterator<String> it = comps.keySet().iterator();
    
        String first = it.next();
        q.add(first);
        visited.add(first);
        List<String> vertexPreOrder = new ArrayList<>();
        
        while (!q.isEmpty()) {
            String v = q.removeFirst();
            vertexPreOrder.add(v);
            for (String n : components.get(v)) {
                if (!visited.contains(n)) {
                    q.add(n);
                    visited.add(n);
                }
            }
        }
       
        return vertexPreOrder;
    }
    
    /** spocita pocet 'propojek' mezi skupinami vektoru g1 a g2 */
    int cost(Set<String> g1, Set<String> g2) {
        int cnt = 0;
        for (String v1 : g1) {
            for (String v2 : components.get(v1)) {
                if (g2.contains(v2)) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    /** spocita pocet 'propojek' mezi skupinami vektoru g1 a g2, pokud z g1 presuneme moveV do g2 */
    int cost(Set<String> g1, Set<String> g2, String moveV) {
        int cnt = 0;
        for (String v1 : g1) {
            if (!v1.equals(moveV)) {
                for (String v2 : components.get(v1)) {
                    if (g2.contains(v2) || v2.equals(moveV)) {
                        cnt++;
                    }
                }                
            }
        }
        return cnt;
    }
    
    public Object solve1(List<String> lines) {

       
        List<Edge> edges = newList();
        for (String l : lines) {
            String[] s = l.replace(":", "").split(" ");
            for (int i = 1; i < s.length; i++) {
                Edge e = new Edge(s[0], s[i]);
                edges.add(e);
                components.computeIfAbsent(s[0], (k) -> newSet()).add(s[i]);
                components.computeIfAbsent(s[i], (k) -> newSet()).add(s[0]);
            }
        }
        
        // predtridim vektory dle dfs
        List<String> vertices = dfs(components.keySet().iterator().next(), components);
//        List<String> vertices = new ArrayList<>(components.keySet());
        
        // rozdelim na dve poloviny
        Set<String> v1 = new HashSet<>(vertices.subList(0, vertices.size()/2));
        Set<String> v2 = new HashSet<>(vertices.subList(vertices.size()/2, vertices.size()));

        // prehazuju sem/tam, abych maximalne zmensil spoj mezi dvema skupinami
        Set<String> moved = new HashSet<String>();
        int cost = cost(v1, v2);
        while (cost > 3) {
            cost = cost(v1, v2);
            String minV = null;
            int minCost = cost;
            for (String v : v1) {
                int newCost = cost(v1, v2, v);
                if (newCost <= minCost && !moved.contains(minV)) {
                    minCost = newCost;
                    minV = v;
                }
            }
            if (minV != null) {
               moved.add(minV);
               v1.remove(minV);
               v2.add(minV);
               if (v1.size()<v2.size()/2) {
                   Set<String> vx = v1;
                   v1 = v2;
                   v2 = vx;
               }
               cost = minCost;
               System.out.println(minCost + " " + v1.size() + " " + v2.size());
            } else {
                Set<String> vx = v1;
                v1 = v2;
                v2 = vx;                
            }
        }

//        592171
        if (cost(v1, v2) == 3) {
            return v1.size() * v2.size();
        }
        return null;        
    }
    
    public Object solve2(List<String> lines) {
        return null;        
    }
    
    public static void main(String[] args) throws Exception {
        new Task25().run();
    }

}
