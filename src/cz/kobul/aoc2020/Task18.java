package cz.kobul.aoc2020;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.stream.Collectors;

import cz.kobul.aoc.TermSolver;

/**
 * https://adventofcode.com/2020/day/18
 * start: 18:54
 * end: 19:29
 */
public class Task18 extends Aoc2020 {

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }
   
//    [P1] 5019432542701
//    [P2] 70518821989947
    
    public Object solve1(List<String> lines) {
        long sum = 0;
        for (String l : lines) {
//            TermSolver.solveInfix(l);
            sum += solve(l);
        }
        return sum;
    }

    private long solveImpl(String l) {
        String[] p = l.split(" ");
        long start = Integer.parseInt(p[0]);
        int idx = 1;
        while (idx < p.length) {            
            if ("+".equals(p[idx])) {
                start = start + Integer.parseInt(p[idx+1]);
                idx += 2;
            } else if ("*".equals(p[idx])) {
                start = start * Integer.parseInt(p[idx+1]);
                idx += 2;                
            }
        }
        return start;
    }
    
    private long solve(String l) {        
        Stack<Integer> parentIdx = new Stack<Integer>();
        int index = 0;
        while (index < l.length()) {
            char ch = l.charAt(index);
            if (ch == '(') {
                parentIdx.push(index);
            }
            if (ch == ')') {
                int startIdx = parentIdx.pop();
                long result = solveImpl(l.substring(startIdx + 1, index));
                StringBuilder newL = new StringBuilder(l);
                String r= Long.toString(result);
                newL.replace(startIdx, index+1, r);
                index = index - (index - startIdx) + r.length() - 1;
                l = newL.toString();
            }
            index++;
        }
        
        return solveImpl(l);
    }

    private long solve2(String l) {        
        Stack<Integer> parentIdx = new Stack<Integer>();
        int index = 0;
        while (index < l.length()) {
            char ch = l.charAt(index);
            if (ch == '(') {
                parentIdx.push(index);
            }
            if (ch == ')') {
                int startIdx = parentIdx.pop();
                long result = solve2Impl(l.substring(startIdx + 1, index));
                StringBuilder newL = new StringBuilder(l);
                String r= Long.toString(result);
                newL.replace(startIdx, index+1, r);
                index = index - (index - startIdx) + r.length() - 1;
                l = newL.toString();
            }
            index++;
        }
        
        return solve2Impl(l);
    }

    private long solve2Impl(String l) {
        List<String> p = new ArrayList<>(List.of(l.split(" ")));
        int idx = 1;
        while (idx < p.size()) {            
            if ("+".equals(p.get(idx))) {
                long r = Integer.parseInt(p.get(idx-1)) + Integer.parseInt(p.get(idx+1));
                p.remove(idx+1);
                p.remove(idx);
                p.set(idx-1, Long.toString(r));
                idx-=2;
            }                 
            idx += 2;
        }
        String newL = p.stream().collect(Collectors.joining(" "));
        return solveImpl(newL);
    }
    
    public Object solve2(List<String> lines) {
        long sum = 0;
        for (String l : lines) {
            sum += solve2(l);
        }
        return sum;
    }	
    
    public static void main(String[] args) throws Exception {
        new Task18().run();
    }

}
