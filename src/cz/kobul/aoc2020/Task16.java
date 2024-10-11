package cz.kobul.aoc2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * https://adventofcode.com/2020/day/16
 * start: 16:44
 * end: 17:15
 */
public class Task16 extends Aoc2020 {

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }
    
    record Intr(int from, int to) {
        boolean match(int val) {
            return val >= from && val <= to;
        }
    }

    public boolean checkIntervals(int value, List<Intr> intervals) {
        for (Intr i : intervals) {
            if (i.match(value)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean checkAllItrs(int value, Collection<List<Intr>> intervals) {
        for (List<Intr> l : intervals) {
            if (checkIntervals(value, l)) {
                return true;
            }
        }
        return false;
    }

    
    public Object solve1(List<String> lines) {
        Map<String, List<Intr>> items = newMap();
        List<Integer> myTicket = new ArrayList<Integer>();
        List<List<Integer>> otherTickets = newList();
        int state = 0;
        for (String l : lines) {
            if (l.isBlank()) {
                state++;
            } else {
                if (state == 0) {
                    String[] s = l.split(": ");
                    String name = s[0];
                    String[] i = s[1].split(" or ");
                    List<Intr> intrs = newList();
                    for (String in : i) {
                        String[] nos = in.split("-");
                        intrs.add(new Intr(Integer.parseInt(nos[0]), Integer.parseInt(nos[1])));
                    }
                    items.put(name, intrs);                
                }
                if (state == 1) {
                    if (!l.contains("your")) {
                        myTicket = toIntegerList(l);
                    }
                }
                if (state == 2) {
                    if (!l.contains("nearby")) {
                        otherTickets.add(toIntegerList(l));
                    }
                }                
            }
        }
        
        long sum = 0;
        for (List<Integer> vals : otherTickets) {
            for (int val : vals) {
                if (!checkAllItrs(val, items.values())) {
                    sum+= val;
                }
            }
        }
        return sum;
    }

    public Object solve2(List<String> lines) {
        Map<String, List<Intr>> items = newMap();
        List<Integer> myTicket = new ArrayList<Integer>();
        List<List<Integer>> otherTickets = newList();
        int state = 0;
        for (String l : lines) {
            if (l.isBlank()) {
                state++;
            } else {
                if (state == 0) {
                    String[] s = l.split(": ");
                    String name = s[0];
                    String[] i = s[1].split(" or ");
                    List<Intr> intrs = newList();
                    for (String in : i) {
                        String[] nos = in.split("-");
                        intrs.add(new Intr(Integer.parseInt(nos[0]), Integer.parseInt(nos[1])));
                    }
                    items.put(name, intrs);                
                }
                if (state == 1) {
                    if (!l.contains("your")) {
                        myTicket = toIntegerList(l);
                    }
                }
                if (state == 2) {
                    if (!l.contains("nearby")) {
                        otherTickets.add(toIntegerList(l));
                    }
                }                
            }
        }


        for (Iterator<List<Integer>> i = otherTickets.iterator(); i.hasNext(); ) {
            List<Integer> vals = i.next();
            boolean remove = false;
            for (int val : vals) {
                if (!checkAllItrs(val, items.values())) {
                    remove = true;
                }
            }
            if (remove) {
                i.remove();
            }
        }

        Map<String, Set<Integer>> fieldIdxes = newMap();
        
        for (String name : items.keySet()) {
            List<Intr> validIntr = items.get(name);
            Set<Integer> validCols = newSet();
            for (int i = 0; i < myTicket.size(); i++) {
                boolean res = true;
                for (List<Integer> vals : otherTickets) {
                    res &= checkIntervals(vals.get(i), validIntr);
                }
                res &= checkIntervals(myTicket.get(i), validIntr);
                if (res) {
                    validCols.add(i);
                }
            }
            fieldIdxes.put(name, validCols);            
        }

        Map<String, Integer> fieldIdx = newMap();
        
        while (fieldIdx.size() < fieldIdxes.size()) {
            for (Entry<String, Set<Integer>> e : fieldIdxes.entrySet()) {
                if (e.getValue().size() == 1){
                    Integer idx = e.getValue().iterator().next();
                    fieldIdx.put(e.getKey(), idx);
                    for (Set<Integer> s : fieldIdxes.values()) {
                        s.remove(idx);
                    }
                }
            }            
        }
        
        long result = 1;
        for (String name : items.keySet()) {
            if (name.startsWith("departure")) {
                result *= myTicket.get(fieldIdx.get(name));
            }
        }
        
        return result;
    }	
    
    public static void main(String[] args) throws Exception {
        new Task16().run();
    }

}
