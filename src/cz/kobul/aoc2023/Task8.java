package cz.kobul.aoc2023;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * https://adventofcode.com/2023/day/8
 * start: 6:00
 * end: 6:58
 */
public class Task8 extends Aoc2023 {

    static record Node(String name, String left, String right) {}
    
    enum Ins {
        L,
        R;
    }
    
    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }
   
    public Long solve1(List<String> lines) {
        List<Ins>ins= new ArrayList<>();
        Map<String, Node> nodes = new HashMap<>();
        for (int i =  0; i < lines.get(0).length(); i++) {
            ins.add(Ins.valueOf("" + lines.get(0).charAt(i)));
        }
        
        for (int i = 2; i < lines.size(); i++) {
            String[] s1 = lines.get(i).split(" = ");
            String[] s2 = s1[1].trim().replace("(", "").replace(")", "").split(", ");
            nodes.put(s1[0], new Node(s1[0], s2[0], s2[1]));
        }
        
        int id = 0;
        long s = 0;
        String n = "AAA";
        while (!"ZZZ".equals(n)) {
            Ins in = ins.get(id);
            id = (id + 1) % ins.size();
            if (in == Ins.L) {
                n = nodes.get(n).left;
            } else {
                n = nodes.get(n).right;                
            }
            s++;
        }
        return s;
    }
    
    boolean allZ(List<String> s) {
        for (int i = 0; i < s.size(); i++) {
            String st = s.get(i);
            boolean z = (st.charAt(st.length() - 1) != 'Z');
            if (!z) {
               return false;
            }
        }
        return true;
    }

    boolean allZ(List<String> s, long step) {
        boolean allZ =true;
        for (int i = 0; i < s.size(); i++) {
            String st = s.get(i);
            boolean z = (st.charAt(st.length() - 1) == 'Z');
            if (z) {
                System.out.println("Z" + i +" @ " + step);
            }
            allZ &= z;
        }
        return allZ;
    }
    

    public BigDecimal solve2(List<String> lines) {
        List<Ins>ins= new ArrayList<>();
        Map<String, Node> nodes = new HashMap<>();
        for (int i =  0; i < lines.get(0).length(); i++) {
            ins.add(Ins.valueOf("" + lines.get(0).charAt(i)));
        }
        
        for (int i = 2; i < lines.size(); i++) {
            String[] s1 = lines.get(i).split(" = ");
            String[] s2 = s1[1].trim().replace("(", "").replace(")", "").split(", ");
            nodes.put(s1[0], new Node(s1[0], s2[0], s2[1]));
        }
        
        int id = 0;
        long s = 0;
        List<String> starts = nodes.values().stream().map(Node::name).filter(n -> n.endsWith("A")).collect(Collectors.toList());
        long[] firstEnds = new long[starts.size()];
        while (Arrays.stream(firstEnds).filter(l -> l == 0).count() > 0) {
            Ins in = ins.get(id);
            if (in == Ins.L) {
                for (int i = 0; i < starts.size(); i++) {
                    String next = nodes.get(starts.get(i)).left;
                    if (next.endsWith("Z") && firstEnds[i] == 0) {
                        firstEnds[i] = s+1;
                    }
                    starts.set(i, next);
                }
            } else {
                for (int i = 0; i < starts.size(); i++) {
                    String next = nodes.get(starts.get(i)).right;
                    if (next.endsWith("Z") && firstEnds[i] == 0) {
                        firstEnds[i] = s+1;
                    }
                    starts.set(i, next);
                }                
            }
            id = (id + 1) % ins.size();            
            s++;
        }

//        System.out.println(Arrays.toString(firstEnds));
        
        long gcd = Long.MAX_VALUE;
        for (int i = 1; i < firstEnds.length; i++) {
            gcd = Math.min(gcd, gcd(firstEnds[0], firstEnds[i]));
        }
        
//        System.out.println(gcd);
        BigDecimal r = BigDecimal.ONE;
        for (long no : firstEnds) {
            r = r.multiply(BigDecimal.valueOf(no / gcd));
        }
        r = r.multiply(new BigDecimal(gcd));
        
        return r;
    }
    
    public static void main(String[] args) throws Exception {
        new Task8().run();
    }

}
