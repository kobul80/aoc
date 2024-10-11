package cz.kobul.aoc2020;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * https://adventofcode.com/2020/day/7
 * start: 15:15
 * end: 15:51
 */
public class Task7 extends Aoc2020 {

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }

    public Object solve1(List<String> lines) {
        Set<String> ans = newSet();
        Map<String, Set<String>> bags = newMap();
        for (String line : lines) {
            String[] l = line.split(" bags contain ");            
            bags.computeIfAbsent(l[0], k -> newSet()).addAll(List.of(l[1].split(",\\s*")).stream().map(k -> k.split(" ")[1] + " " +k.split(" ")[2]).toList());
        }
        LinkedList<LinkedList<String>> q = newLinkedList();
        for (String b : bags.keySet()) {
            q.add(newLinkedList(b));
        }
        while (!q.isEmpty()) {
            LinkedList<String> f = q.removeFirst();
            if (f.size() > 1 && "shiny gold".equals(f.getLast())) {
                ans.add(f.getFirst());
            } else {
                Set<String> inner = bags.get(f.getLast());
                if (inner != null) {
                    for (String b : inner) {
                        q.add(add(f, b));
                    }                    
                }
            }
        }
        return ans.size();
    }

    public long getCnt(String bag, Map<String, Map<String, Integer>> bags) {
        Map<String, Integer> b = bags.get(bag);
        if (b == null || b.isEmpty()) {
            return 1;
        } 
        long sum = 1;
        for (Entry<String, Integer> e : b.entrySet()) {
            sum += e.getValue() * getCnt(e.getKey(), bags);
        }
        return sum;
    }
    
    public Object solve2(List<String> lines) {
        Map<String, Map<String, Integer>> bags = newMap();
        for (String line : lines) {
            String[] l = line.split(" bags contain ");            
            Map<String, Integer> b = bags.computeIfAbsent(l[0], k -> newMap());
            if (!"no other bags.".equals(l[1])) {
                for (String b1 : List.of(l[1].split(",\\s*"))) {
                    String[] b2 = b1.split(" ");
                    String bn = b2[1] + " " +b2[2];
                    int cnt = Integer.parseInt(b2[0]);
                    b.put(bn, cnt);
                }                
            }
        }
        return getCnt("shiny gold", bags) - 1;
    }	
    
    public static void main(String[] args) throws Exception {
        new Task7().run();
    }

}
