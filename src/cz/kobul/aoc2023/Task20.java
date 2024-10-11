package cz.kobul.aoc2023;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.Result;

import java.util.Set;


/**
 * https://adventofcode.com/2023/day/20
 * start: 6:00
 * end: 8:25
 */
public class Task20 extends Aoc2023 {

    @Override
    public void solve() throws Exception {
//        String fileName = getDefaultTestFileName();
                String fileName = getDefaultInputFileName();

        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }     

    long low = 0;
    long high = 0;

    abstract class Module {
        String name;
        List<String> results;

        boolean count(boolean pulse) {
            if (pulse) {
                high++;
            } else {
                low++;
            }
            return pulse;
        }

        List<Pulse> getResult(boolean pulse) {
            List<Pulse> res = new ArrayList<>();
            for (String r : results) {
//                System.out.println(name + (pulse ? " -high" : " -low") + " -> " + r);
                res.add(new Pulse(name, r, count(pulse)));
            }
            return res;
        }
        
        List<Pulse> process(String from, boolean pulse) {
            return getResult(pulse);
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + " " + name + " " + results;
        }
    }

    class Button extends Module {
    }

    class Broadcast extends Module {
    }

    record Pulse(String src, String dest, boolean pulse) {}
    
    class Flipflop extends Module {
        boolean state = false;

        List<Pulse> process(String from, boolean pulse) {
            if (!pulse) {
                state = !state;
            } else {
                return List.of();
            }
            return getResult(state);
        }
    }

    class Conj extends Module {
        Map<String, Boolean> states = new HashMap<>();

        boolean allOn() {
//            System.out.println("[ conj " + name + " " + states + " ]");
            if (states.isEmpty()) {
                return false;
            }
            for (boolean b : states.values()) {
                if (!b) {
                    return false;
                }
            }
            return true;
        }

        List<Pulse>  process(String from, boolean pulse) {
            states.put(from, pulse);
            return getResult(!allOn());
        }

    }

    public Long solve1(List<String> lines) {

        Map<String, Module> allModules = parseInput(lines);

        for (int i = 0; i < 1000; i++) {
            oneCycle(allModules, i);                        
        }
        
        System.out.println(low + " " + high);
        return (long) low * high;
    }

    Map<Pulse, Long> pulsesByCycle = new HashMap<>();
    
    protected void oneCycle(Map<String, Module> allModules, long cycleIdx) {
        LinkedList<Pulse> pulses = new LinkedList<>();
        pulses.add(new Pulse("", "Button", false));
        while (!pulses.isEmpty()) {
            Pulse p = pulses.removeFirst();
            Module m = allModules.get(p.dest());
            if (m != null) {
                List<Pulse> newPulses = m.process(p.src, p.pulse);
                for (Pulse newPulse : newPulses) {
                    if (!pulsesByCycle.containsKey(newPulse)) {
                        pulsesByCycle.put(newPulse, cycleIdx);
                    }
                }
                pulses.addAll(newPulses);
            }
        }
    }

    protected Map<String, Module> parseInput(List<String> lines) {
        Map<String, Module> allModules = new HashMap<>();
        for (String line : lines) {

            String[] s = line.split("->");
            String name;
            Module module;
            char ch = s[0].charAt(0);
            if (ch == '%') {
                module = new Flipflop();
                name = s[0].substring(1).trim();
            } else if (ch == '&') {
                module = new Conj();
                name = s[0].substring(1).trim();
            } else {
                module = new Broadcast();
                name = s[0].trim();
            }
            module.name = name;
            String[] next = s[1].trim().split(",");
            List<String> results = new  ArrayList<>();
            for (String n : next) {
                results.add(n.trim());
            }
            module.results = results;
            allModules.put(name, module);
        }
        Button button = new Button();
        button.name = "Button";
        button.results = List.of("broadcaster");
        allModules.put(button.name, button);
        
        // vyplnit vstupni stavy Conj
        for (Module m : allModules.values()) {
            for (String dest : m.results) {
                Module m2 = allModules.get(dest);
                if (m2 instanceof Conj) {
                    ((Conj)m2).states.put(m.name, false);
                }
            }
        }
        
//        System.out.println(allModules);
        
        return allModules;
    }

    public Long solve2(List<String> lines) {
        pulsesByCycle.clear();
        Map<String, Module> allModules = parseInput(lines);

        Set<Pulse> neededPulses = new HashSet<>();
        for (Module m : allModules.values()) {
            if (m.results.contains("rx")) {
                for (String name : ((Conj) m).states.keySet()) {
                    neededPulses.add(new Pulse(name, m.name, true));
                }
            }
        }
        
        long cycle = 1;
        while (!pulsesByCycle.keySet().containsAll(neededPulses)) {
            oneCycle(allModules, cycle);
            cycle++;
        }
        
        long[] firstcycles = new long[neededPulses.size()];
        int pos = 0;
        for (Pulse p : neededPulses) {
            firstcycles[pos] = pulsesByCycle.get(p);
            pos++;
        }
        return lcm(firstcycles);
    }    

    public static void main(String[] args) throws Exception {
        new Task20().run();
    }

}
