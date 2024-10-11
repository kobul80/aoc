package cz.kobul.aoc2023;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://adventofcode.com/2023/day/7
 * start: 6:00
 * end: 6:44
 */
public class Task7 extends Aoc2023 {

    static String CARDS = "AKQJT98765432";
    
    static String CARDS2 = "AKQT98765432J";
    
//    Five of a kind, where all five cards have the same label: AAAAA
//    Four of a kind, where four cards have the same label and one card has a different label: AA8AA
//    Full house, where three cards have the same label, and the remaining two cards share a different label: 23332
//    Three of a kind, where three cards have the same label, and the remaining two cards are each different from any other card in the hand: TTT98
//    Two pair, where two cards share one label, two other cards share a second label, and the remaining card has a third label: 23432
//    One pair, where two cards share one label, and the other three cards have a different label from the pair and each other: A23A4
//    High card, where all cards' labels are distinct: 23456
    
    static List<String> dist(String hand) {
        Map<Character, String> strs = new HashMap<>();
        for (int i = 0; i < hand.length(); i++) {
            strs.compute(hand.charAt(i), (k, s) -> s==null ? "" + k : s + k);
        }
        return strs.values().stream().sorted(Comparator.comparing(String::length).reversed()).toList();
    }
    
    static int type(String hand) {
        List<String> dist = dist(hand);
        if (dist.size() == 1) {
            return 1;
        } else if (dist.size() == 2) {
            int s1 = dist.get(0).length();
            if (s1 == 4) {
                return 2;
            } 
            if (s1 == 3) {
                return 3;
            }               
        } else if (dist.size() == 3) {
            int s1 = dist.get(0).length();
            if (s1 == 3) {
                return 4;
            }
            if (s1 == 2) {
                return 5;
            }
        } else if (dist.size() == 4) {
            return 6;
        } 
        return 7;
    }

    
    static record Hand(String hand, long bid) implements Comparable<Hand> {

        int handType() {
             return type(hand);
        }
        
        @Override
        public int compareTo(Hand o) {
            String h1 = hand;
            String h2 = o.hand;
            
            int t1 = handType();
            int t2 = o.handType();
            int diff = t2 - t1;
            if (diff != 0) {
                return diff;
            }
            
            for (int i = 0; i < 5; i++) {
                int c1 = CARDS.indexOf(h1.charAt(i));
                int c2 = CARDS.indexOf(h2.charAt(i));
                diff = c2 - c1;
                if (diff != 0) {
                    return diff;
                }
            }
            return 0;
        }
    }

    static record Hand2(String hand, long bid) implements Comparable<Hand2> {

        int handType() {
            if (hand.contains("J")) {
                int minT = 7;
                String toRepl = hand.replace("J", "");
                if (toRepl.isEmpty()) {
                    return 1;
                }
                for (int i = 0; i < toRepl.length(); i++) {
                    minT = Math.min(minT, type(hand.replace('J', toRepl.charAt(i))));
                }
                return minT;
            }
            return type(hand);
        }
        
        @Override
        public int compareTo(Hand2 o) {
            String h1 = hand;
            String h2 = o.hand;
            
            int t1 = handType();
            int t2 = o.handType();
            int diff = t2 - t1;
            if (diff != 0) {
                return diff;
            }
            
            for (int i = 0; i < 5; i++) {
                int c1 = CARDS2.indexOf(h1.charAt(i));
                int c2 = CARDS2.indexOf(h2.charAt(i));
                diff = c2 - c1;
                if (diff != 0) {
                    return diff;
                }
            }
            return 0;
        }
    }

    
    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }
   
    public Long solve1(List<String> lines) {
        List<Hand> hands = new ArrayList<>();
        for (String line : lines){
            String[] l = line.split(" ");
            hands.add(new Hand(l[0], Long.parseLong(l[1])));
        }
        List<Hand> sorted = hands.stream().sorted().toList();
        System.out.println(sorted);
        long res = 0;
        int i = 1;
        for (Hand ha : sorted) {
            res += ha.bid * i;
            i++;
        }
        return res;
    }
    
    public Long solve2(List<String> lines) {
        List<Hand2> hands = new ArrayList<>();
        for (String line : lines){
            String[] l = line.split(" ");
            hands.add(new Hand2(l[0], Long.parseLong(l[1])));
        }
        List<Hand2> sorted = hands.stream().sorted().toList();
        System.out.println(sorted);
        long res = 0;
        int i = 1;
        for (Hand2 ha : sorted) {
            res += ha.bid * i;
            i++;
        }
        return res;
    }

    public static void main(String[] args) throws Exception {
        new Task7().run();
    }

}
