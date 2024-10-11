package cz.kobul.aoc2020;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * https://adventofcode.com/2020/day/19
 * start: 11:06
 * end: 
 */
public class Task19 extends Aoc2020 {

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultTestFileName();
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }

//    75: 105 116 | 40 131
//    50: 40 116 | 84 131
//    57: 5 116 | 15 131
//    20: 128 131 | 84 116
//    abbbaaabbbbbbabaaaabbababbaabaabbbbaababbbbbaababbbaabbabbbabbababaaabaaaabaabbbaaababab

    class Rule {
        int id;
    }
    
    class RuleF extends Rule {
        char value;

        public RuleF(int id, char value) {
            this.id = id;
            this.value = value;
        }
        @Override
        public String toString() {
            return id + " '" + value + "'";
        }
    }
    
    class RuleR extends Rule {
        List<List<Integer>> rules;
        public RuleR(int id, List<List<Integer>> rules) {
            this.id = id;
            this.rules = rules;
        }
        @Override
        public String toString() {
            return id + " " + rules;
        }
    }

    public boolean test(String v, int idx, List<Rule> rl, Map<Integer, Rule> rules) {
        System.out.println(v + " " + idx + " " + rl + " => ?");
        for (Rule r : rl) {
            if (r instanceof RuleF) {
                if (v.charAt(idx) != ((RuleF)r).value) {
                    return false;
                } else {
                    idx++;
                }
            } else {
                RuleR ru = (RuleR)r;
                boolean match = false;
                for (List<Integer> r1 : ru.rules) {
                    if (test(v, idx, r1.stream().map(rules::get).toList(), rules)) {
                        match = true;
                        break;
                    }
                }
                if (!match) {
                    return false;
                }
            }
        }
        boolean result = rl.get(0).id != 0 || (idx == v.length());    
        System.out.println(v + " " + idx + " " + rl + " => " + result);
        return result;
    }

    record Task(String v, List<Rule> rules) {}
    
    public boolean test1(String v, int idx, List<Rule> rl, Map<Integer, Rule> allRules) {
        LinkedList<Task> q = newLinkedList();
        while (!q.isEmpty()) {
            Task t = q.removeFirst();
            for (Rule r : t.rules) {
                if (r instanceof RuleF) {
                    if (v.charAt(idx) == ((RuleF)r).value) {                        
                        q.add(new Task(v.substring(1), removeFirst(rl)));
                    } 
                } 
//                else {
//                    RuleR ru = (RuleR)r;
//                    boolean match = false;
//                    for (List<Integer> r1 : ru.rules) {
//                        if (test(v, idx, r1.stream().map(rules::get).toList(), rules))) {
//                            match = true;
//                            break;
//                        }
//                    }
//                    if (!match) {
//                        return false;
//                    }
//                }
                
            }
        }
        boolean result = rl.get(0).id != 0 || (idx == v.length());    
        System.out.println(v + " " + idx + " " + rl + " => " + result);
        return result;
    }
    

    
    public Object solve1(List<String> lines) {
        int part =0;
        List<String> messages = newList();
        Map<Integer, Rule> rules = newMap();
        for (String l : lines) {
            if (l.isBlank()) {
                part++;
            } else {
                if (part == 0) {
                    String[] t = l.split(": ");
                    int id = Integer.parseInt(t[0]);
                    String[] r = t[1].split("\\|");
                    if (t[1].contains("\"")) {
                        char val = t[1].replace('"', ' ').replace(" ", "").charAt(0);
                        rules.put(id, new RuleF(id, val));
                    } else {
                        List<List<Integer>> rul = newList();
                        for (String rx : r) {
                            rul.add(toIntegerList(rx));
                        }
                        rules.put(id, new RuleR(id, rul));
                    }
                } else if (part == 1) {
                    messages.add(l);
                }
                
            }
        }
        System.out.println(rules);
        System.out.println(messages);
        
        int sum = 0;
        for (String m : messages) {
            if (test(m, 0, List.of(rules.get(0)), rules)) {
                sum++;
            }
        }
//        test("aaaabba", 0, List.of(rules.get(0)), rules);
//        

        return sum;
    }

    public Object solve2(List<String> lines) {
        return null;
    }	
    
    public static void main(String[] args) throws Exception {
        new Task19().run();
    }

}
