package cz.kobul.aoc2023;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * https://adventofcode.com/2023/day/19
 * start: 
 * end: 
 */
public class Task19 extends Aoc2023 {

    @Override
    public void solve() throws Exception {
//        String fileName = getDefaultTestFileName();
        String fileName = getDefaultInputFileName();

        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2a(readFileToListString(fileName)));
    }     

    record Co(char ch, char cmp, int cnt) {
        
    }

    record Cond(char ch, char cmp, int cnt, String nextRule) {
        Co toCo() {
            return new Co(ch, cmp, cnt);
        }
        Co toInvCo() {
            return new Co(ch, (cmp == '<' ? '>' : '<'), (cmp == '>' ? cnt + 1 : cnt - 1));
        }
    }
    
    record Next(String nextRuleName, Set<Character> accepted) {}
    
    record Rule(String name, List<Cond> conds, String defaultNext) {
        
        String process(PartCnt pc) {
            for (Cond cond : conds) {
                int partCnt = pc.parts.get(cond.ch);
                if (cond.cmp == '<' && partCnt < cond.cnt) {
                    return cond.nextRule;
                } 
                if (cond.cmp == '>' && partCnt > cond.cnt) {
                    return cond.nextRule;
                } 
            }
            return defaultNext;
        }
        
    }
    
    record PartCnt(Map<Character, Integer> parts) {}
    
    public Long solve1(List<String> lines) {
        boolean start = true;
        Map<String, Rule> rules = new HashMap<>();
        List<PartCnt> parts = new ArrayList<>();
        for (String line : lines) {
            if (line.isBlank()) {
                start = false;                
            } else if (start) {
                String[] s0 = line.split("\\{");
                String ruleName = s0[0];
                String[] cos = s0[1].replace("}", "").split(",");
                List<Cond> conds = new ArrayList<>();
                String defaultNext = "";
                for (String c : cos) {
                    if (c.contains(":")) {
                        String nextRule = c.split(":")[1];
                        char ch = c.charAt(0);
                        char cmp = c.charAt(1);
                        int cnt = Integer.parseInt(c.substring(2).split(":")[0]);
                        conds.add(new Cond(ch, cmp, cnt, nextRule));
                    } else {
                        defaultNext = c;
                    }
                }
                rules.put(ruleName, new Rule(ruleName, conds, defaultNext));
            } else {
                String[] s = line.replace("{", "").replace("}", "").split(",");
                Map<Character, Integer> cnts = new HashMap<>();
                for (String p : s) {
                    char part = p.charAt(0);
                    Integer cnt = Integer.valueOf(p.substring(2));
                    cnts.put(part, cnt);
                }
                parts.add(new PartCnt(cnts));
            }
        }


        long sum = 0;
        for (PartCnt partCnt : parts) {
            System.out.println(partCnt.parts);
            boolean result = process(rules, partCnt);
            if (result) {
                for (int cnt: partCnt.parts.values()) {
                    sum+= cnt ;
                }
            }
        }
        
        return sum;
    }

    protected boolean process(Map<String, Rule> rules, PartCnt partCnt) {
        String rule = "in";
        while (!(rule.equals("A") || rule.equals("R"))) {
            Rule r = rules.get(rule);
            rule = r.process(partCnt);
        }
        return rule.equals("A");
    }

    String toStr(Collection<Co> cos) {
        StringBuilder result = new StringBuilder();
        result.append("(");
        boolean first = true;
        for (Co co : cos) {
            if (first) {
                first = false;
            } else {
                result.append("&&");
            }
            result.append(co.ch).append(co.cmp).append(co.cnt);
        }
        result.append(")");
        return result.toString();
    }
    
    public Long solve2(List<String> lines) {
        boolean start = true;
        Map<String, Rule> rules = new HashMap<>();
        for (String line : lines) {
            if (line.isBlank()) {
                start = false;                
            } else if (start) {
                String[] s0 = line.split("\\{");
                String ruleName = s0[0];
                String[] cos = s0[1].replace("}", "").split(",");
                List<Cond> conds = new ArrayList<>();
                String defaultNext = "";
                for (String c : cos) {
                    if (c.contains(":")) {
                        String nextRule = c.split(":")[1];
                        char ch = c.charAt(0);
                        char cmp = c.charAt(1);
                        int cnt = Integer.parseInt(c.substring(2).split(":")[0]);
                        conds.add(new Cond(ch, cmp, cnt, nextRule));
                    } else {
                        defaultNext = c;
                    }
                }
                rules.put(ruleName, new Rule(ruleName, conds, defaultNext));
            } 
        }

        BitSet full = new BitSet(4000);
        full.set(0, 4000);
        
        Map<Character, BitSet> possB = new HashMap<>();
        possB.put('x', (BitSet)full.clone());
        possB.put('m', (BitSet)full.clone());
        possB.put('a', (BitSet)full.clone());
        possB.put('s', (BitSet)full.clone());

        return solve2("in", possB, rules);
    }
    
    public Long solve2a(List<String> lines) {
        boolean start = true;
        Map<String, Rule> rules = new HashMap<>();
        for (String line : lines) {
            if (line.isBlank()) {
                start = false;                
            } else if (start) {
                String[] s0 = line.split("\\{");
                String ruleName = s0[0];
                String[] cos = s0[1].replace("}", "").split(",");
                List<Cond> conds = new ArrayList<>();
                String defaultNext = "";
                for (String c : cos) {
                    if (c.contains(":")) {
                        String nextRule = c.split(":")[1];
                        char ch = c.charAt(0);
                        char cmp = c.charAt(1);
                        int cnt = Integer.parseInt(c.substring(2).split(":")[0]);
                        conds.add(new Cond(ch, cmp, cnt, nextRule));
                    } else {
                        defaultNext = c;
                    }
                }
                rules.put(ruleName, new Rule(ruleName, conds, defaultNext));
            } 
        }

        
        findAllPaths("in", Set.of(), rules);
        
        System.out.println(allPaths.size());
        
        StringBuilder res = new StringBuilder();
        for (Set<Co> p : allPaths) {
            if (!p.isEmpty()) {
                res.append(toStr(p));
                res.append("||");                
            }
        }
        System.out.println(res.toString());
//        System.out.println(allPaths);
        
//        System.out.println(allPaths.size());
//        Map<Character, Set<Integer>> possible = new HashMap<>();
//        possible.put('x', new HashSet<>());
//        possible.put('m', new HashSet<>());
//        possible.put('a', new HashSet<>());
//        possible.put('s', new HashSet<>());

//        Set<Set<Co>> testPaths = new HashSet<>();
////        (s<1351&&a<2006&&x<1416)||(m<1801)||(s<1351&&a<2006&&x>2662)
//        testPaths.add(Set.of(new Co('s', '>', 2770)));        
//        testPaths.add(Set.of(new Co('s', '<', 1351)));
//        testPaths.add(Set.of(new Co('m', '<', 1801)));
         
        
        List<Map<Character, Set<Integer>>> allPoss = new ArrayList<>();
        for (Set<Co> cos : allPaths) {
            Map<Character, Set<Integer>> poss = new HashMap<>();
            poss.put('x', get(1, 4000));
            poss.put('m', get(1, 4000));
            poss.put('a', get(1, 4000));
            poss.put('s', get(1, 4000));
            for (Co co : cos) {
                if (co.cmp == '<') {
                    poss.get(co.ch()).removeAll(get(co.cnt, 4000));
                }
                if (co.cmp == '>') {
                    poss.get(co.ch()).removeAll(get(1, co.cnt));
                }
            }
            allPoss.add(poss);
        }
//
        
        BitSet full = new BitSet(4000);
        full.set(0, 4000);
        
        List<Map<Character, BitSet>> allPossB = new ArrayList<>();
        for (Set<Co> cos : allPaths) {
            Map<Character, BitSet> possB = new HashMap<>();
            possB.put('x', (BitSet)full.clone());
            possB.put('m', (BitSet)full.clone());
            possB.put('a', (BitSet)full.clone());
            possB.put('s', (BitSet)full.clone());
            for (Co co : cos) {
                if (co.cmp == '<') {
                    possB.get(co.ch()).set(co.cnt - 1, 4000, false);
                }
                if (co.cmp == '>') {
                    possB.get(co.ch()).set(0, co.cnt, false);
                }
            }
            allPossB.add(possB);
            
//            System.out.println(allPaths);
//            for (int i = 0; i < 100; i++) {
//                PartCnt cnt = getRandom(poss);
//                boolean result = process(rules, cnt);
//                if (result == false) {
//                    System.out.println(cos);
//                    System.out.println(cnt + "=" + result);                                    
//                }
//            }

        }

        //167409079868000
        //167409079868000
        
        long result = countCombB(allPossB.subList(0, 24));
        System.out.println(iterations);
        
        return result;
    }

    public PartCnt getRandom(Map<Character, Set<Integer>> t) {
        Map<Character, Integer> r= new HashMap<>();
        for (Entry<Character, Set<Integer>> e : t.entrySet()) {
            r.put(e.getKey(), new ArrayList<>(e.getValue()).get((int)(e.getValue().size() * Math.random())));
        }
        return new PartCnt(r);
    }
    
    protected long countComb(Map<Character, Set<Integer>> a, Map<Character, Set<Integer>> b) {
        return comb(a) + comb(b) - comb(prunik(a, b));
    }
    
    protected long countComb(Map<Character, Set<Integer>> a, Map<Character, Set<Integer>> b, Map<Character, Set<Integer>> c) {
        return countComb(a, b) + comb(c) - countComb(prunik(a, c), prunik(b, c));
    }

    protected long countComb(Map<Character, Set<Integer>> a, Map<Character, Set<Integer>> b, Map<Character, Set<Integer>> c, Map<Character, Set<Integer>> d) {
        return countComb(a, b, c) + comb(d) - countComb(prunik(a, d), prunik(b, d), prunik(c, d));
    }

    long iterations = 0;
    
    protected long countComb(List<Map<Character, Set<Integer>>> c) {
        if (c.size() == 1) {
            return comb(c.get(0));
        } else {
//            System.out.println("countComb(" + c.size() + ")");
            Map<Character, Set<Integer>> last = c.get(c.size() - 1);
            List<Map<Character, Set<Integer>>> pr = new ArrayList<>();
            for (int i = 0; i < c.size() - 1; i++) {
                pr.add(prunik(last, c.get(i)));
            }
            return countComb(c.subList(0, c.size() -1)) + comb(last) - countComb(pr); 
        }
        
    }

    protected long countCombB(List<Map<Character, BitSet>> c) {
        iterations++;
        if (iterations % 1000000 == 0) {
            System.out.println(iterations);
        }
        if (c.size() == 1) {
            return combB(c.get(0));
        } else {
//            System.out.println("countComb(" + c.size() + ")");
            Map<Character, BitSet> last = c.get(c.size() - 1);
            List<Map<Character, BitSet>> pr = new ArrayList<>();
            for (int i = 0; i < c.size() - 1; i++) {
                pr.add(prunikb(last, c.get(i)));
            }
            return countCombB(c.subList(0, c.size() -1)) + combB(last) - countCombB(pr); 
        }
        
    }
    

    public long comb(Map<Character, Set<Integer>> p1) {
        long cnt = 1;
        for (Entry<Character, Set<Integer>> e : p1.entrySet()) {
            Set<Integer> s1 = e.getValue();
            cnt *= (long) s1.size();
            if (cnt == 0) {
                return 0;
            }
        }
        return cnt;
    }

    public long prunikCnt(Set<Integer>s1, Set<Integer> s2) {
        long p =0 ;
        if (s1.size() < s2.size()) {
            for (Integer i : s1) {
                if (s2.contains(i)) {
                    p++;
                }
            }
        } else {
            for (Integer i : s2) {
                if (s1.contains(i)) {
                    p++;
                }
            }            
        }
        return p;
    }

    public Set<Integer> prunik(Set<Integer>s1, Set<Integer> s2) {
        Set<Integer> res = new HashSet<>();
        if (s1.size() < s2.size()) {
            for (Integer i : s1) {
                if (s2.contains(i)) {
                    res.add(i);
                }
            }
        } else {
            for (Integer i : s2) {
                if (s1.contains(i)) {
                    res.add(i);
                }
            }            
        }
        return res;
    }
    
    public void removePrunik(Map<Character, Set<Integer>> p, Map<Character, Set<Integer>> pr) {
        for (Entry<Character, Set<Integer>> e : p.entrySet()) {
            e.getValue().removeAll(pr.get(e.getKey()));
        }
    }
    
    public Map<Character, Set<Integer>> prunik(Map<Character, Set<Integer>> p1, Map<Character, Set<Integer>> p2) {
        Map<Character, Set<Integer>> result = new HashMap<>();
        for (Entry<Character, Set<Integer>> e : p1.entrySet()) {
            Set<Integer> s1 = e.getValue();
            Set<Integer> s2= p2.get(e.getKey());
            result.put(e.getKey(), prunik(s1, s2));
        }
        return result;        
    }

    public Map<Character, BitSet> prunikb(Map<Character, BitSet> p1, Map<Character, BitSet> p2) {
        Map<Character, BitSet> result = new HashMap<>();
        for (Entry<Character, BitSet> e : p1.entrySet()) {
            BitSet s1 = e.getValue();
            BitSet s2= p2.get(e.getKey());
            BitSet res = ((BitSet)s1.clone());
            res.and(s2);
            result.put(e.getKey(), res);
        }
        return result;        
    }


    
    public long prunikCnt(Map<Character, Set<Integer>> p1, Map<Character, Set<Integer>> p2) {
      long c = 1;
   
      for (Entry<Character, Set<Integer>> e : p1.entrySet()) {
          Set<Integer> s1 = e.getValue();
          Set<Integer> s2= p2.get(e.getKey());
          c *= prunikCnt(s1, s2);
          if (c == 0) {
              return 0;
          }
      }
      return c;        
  }
  


    public Set<Integer> get(int from, int to) {
        Set<Integer> res = new HashSet<>();
        for (int i = from; i <= to; i++) {
            res.add(i);
        }
        return res;
    }
    
    Set<Set<Co>> allPaths = new HashSet<>();

    public void findAllPaths(String rn, Set<Co> conds, Map<String, Rule> rules) {
        Rule r = rules.get(rn);
//        Set<Co> toRemove = new HashSet<>();
        for (Cond c : r.conds) {
            if (c.nextRule.equals("A")) {
                allPaths.add(add(conds, c.toCo()));
            } else if (!c.nextRule.equals("R")) {
                findAllPaths(c.nextRule, add(conds, c.toCo()), rules);
            }
            conds = add(conds, c.toInvCo());
        }
        if (r.defaultNext.equals("A")) {
            allPaths.add(conds);
        } else if (!r.defaultNext.equals("R")) {
            findAllPaths(r.defaultNext, conds, rules);
        }
    }

    public long combB(Map<Character, BitSet> p1) {
        long cnt = 1;
        for (Entry<Character, BitSet> e : p1.entrySet()) {
            BitSet s1 = e.getValue();
            cnt *= (long) s1.cardinality();
            if (cnt == 0) {
                return 0;
            }
        }
        return cnt;
    }
    
    public Map<Character, BitSet> apply(Map<Character, BitSet> combs, Co co) {
        Map<Character, BitSet> res = new HashMap<>(combs);
        BitSet b = (BitSet)combs.get(co.ch).clone();
        if (co.cmp == '<') {
            b.set(co.cnt - 1, 4000, false);
        }
        if (co.cmp == '>') {
            b.set(0, co.cnt, false);
        }
        res.put(co.ch, b);
        return Collections.unmodifiableMap(res);
    }    
    
    public long solve2(String rn, Map<Character, BitSet> combs, Map<String, Rule> rules) {
        Rule r = rules.get(rn);
        long result = 0;
        for (Cond c : r.conds) {
            if (c.nextRule.equals("A")) {
                result += combB(apply(combs, c.toCo()));
            } else if (!c.nextRule.equals("R")) {
                result += solve2(c.nextRule, apply(combs, c.toCo()), rules);
            }
            combs = apply(combs, c.toInvCo());
        }
        if (r.defaultNext.equals("A")) {
            result += combB(combs);
        } else if (!r.defaultNext.equals("R")) {
            result += solve2(r.defaultNext, combs, rules);
        }
        return result;
    }
    

    public static void main(String[] args) throws Exception {
        new Task19().run();
    }

}
