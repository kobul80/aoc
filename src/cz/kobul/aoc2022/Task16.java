package cz.kobul.aoc2022;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * https://adventofcode.com/2022/day/16
 * start  end
 * -- ne nonstop :)
 */
public class Task16 extends Aoc {

    static Pattern p = Pattern.compile("Valve ([A-Z]+) has flow rate=([0-9]+); tunnels? leads? to valves? (.+)$");

    static class Valve {
        String name;
        int rate;
        List<Valve> valves = new ArrayList<>();
        String[] vStrs;

        public Valve(String name, int rate) {
            this.name = name;
            this.rate = rate;
        }
        
        public String getName() {
			return name;
		}
        
        public String[] getVStrs() {
			return vStrs;
		}
        
        public void setVStrs(String[] vStrs) {
			this.vStrs = vStrs;
		}
        
        @Override
        public String toString() {
            return name + " " + rate;
        }

        public String toStringWithSteps() {
            return name + " " + rate        + ' ' + Arrays.toString(vStrs);
        }
    }

    public void solve() throws Exception {
        List<String> lines = readFileToListString(getDefaultInputFileName());

        logResult(1, solve1(parseValves(lines)));
        logResult(2, solve2(parseValves(lines)));
    }

    protected Map<String, Valve> parseValves(List<String> lines) {
        Map<String, Valve> valves = new LinkedHashMap<>();
        for (String line: lines) {
            Matcher m = p.matcher(line);
            if (m.matches()) {
                Valve v = new Valve(m.group(1), Integer.parseInt(m.group(2)));
                v.setVStrs(m.group(3).split(",\\s*"));
                valves.put(v.getName(), v);
            }
        }
        for (Valve v : valves.values()) {
            for (String s : v.getVStrs()) {
                v.valves.add(valves.get(s));
            }
        }
        return valves;
    }


    static enum Action {
        MOVE, OPEN;
    }

    static record Move(Action action, Valve toValve, Valve fromValve) {
        @Override
        public String toString() {
            return action == Action.OPEN ? "o" + toValve.getName() : ">" + toValve.getName();
        }

    }

    long bestSum = 0;
    List<Move> bestMoves;

    long bestSum2 = 0;
    List<Move> bestMoves1;
    List<Move> bestMoves2;


    Map<Cpl, List<Valve>> shortestPaths = new HashMap<>();
    Map<Cpl, List<Move>> shortestPathMoves = new HashMap<>();


    public void simulate(Map<String, Valve> valves, List<Move> path, final int remainingTime, final int sum) {
        if (sum > bestSum) {
            bestSum = sum;
            bestMoves = path;
            System.out.println(bestSum);
        }
        if (remainingTime == 0) {
            return;
        }

        Move lastMove = path.get(path.size() - 1);
        Valve last = lastMove.toValve();

        Set<Valve> openValves = path.stream().filter(m -> m.action == Action.OPEN).map(Move::toValve).collect(Collectors.toSet());
        List<Valve> openable = valves.values().stream().filter(v -> v.rate > 0 && !openValves.contains(v)).collect(Collectors.toList());

        for (Valve v : openable) {
            List<Move> newPath = new ArrayList<>(path);
            int len = 0;
            if (v != last) {
                List<Valve> shortestPath = shortestPaths.get(new Cpl(last, v));
                len = shortestPath.size();
                Valve l = last;
                for (Valve p : shortestPath) {
                    newPath.add(new Move(Action.MOVE, p, l));
                    l = p;
                }
            }
            int newRemain = remainingTime - len - 1;
            if (newRemain >= 0) {
                newPath.add(new Move(Action.OPEN, v, null));
                simulate(valves, Collections.unmodifiableList(newPath), newRemain, sum + (v.rate * newRemain));
            }
        }
    }


    public void simulate2(Map<String, Valve> valves, List<Move> path1, List<Move> path2, final int totalTime, final int sum) {
        if (sum > bestSum2) {
            bestSum2 = sum;
            bestMoves1 = path1;
            bestMoves2 = path2;
            System.out.println(bestSum2);
        }

        Move lastMove1 = path1.get(path1.size() - 1);
        Valve last1 = lastMove1.toValve();

        Move lastMove2 = path2.get(path2.size() - 1);
        Valve last2 = lastMove2.toValve();

        Set<Valve> openValves1 = path1.stream().filter(m -> m.action == Action.OPEN).map(Move::toValve).collect(Collectors.toSet());
        Set<Valve> openValves2 = path2.stream().filter(m -> m.action == Action.OPEN).map(Move::toValve).collect(Collectors.toSet());
        Set<Valve> openValves = new HashSet<>(openValves1);
        openValves.addAll(openValves2);

        int remain1 = totalTime - (path1.size() - 1);
        int remain2 = totalTime - (path2.size() - 1);

        List<Valve> openable1 = valves.values().stream().filter(v -> v.rate > 0 && !openValves.contains(v)).collect(Collectors.toList());
        List<Valve> openable2 = new ArrayList<>(openable1);

        for (Iterator<Valve> i1 = openable1.iterator(); i1.hasNext(); ) {
            List<Valve> p1 = shortestPaths.get(new Cpl(last1, i1.next()));
            if (p1 != null && p1.size() > remain1) {
                i1.remove();
            }
        }

        for (Iterator<Valve> i2 = openable2.iterator(); i2.hasNext(); ) {
            List<Valve> p2 = shortestPaths.get(new Cpl(last1, i2.next()));
            if (p2 != null && p2.size() > remain2) {
                i2.remove();
            }
        }


        int index = 0;
        int mult = openable1.size() * openable2.size();
        for (Valve v1 : openable1) {
            for (Valve v2 : openable2) {
                index++;
                if (path1.size() < 2) {
                    System.out.println("Testing depth " + path1.size() + " " + index + "/" + mult);
                }
                if (v1 != v2) {
                    int newSum = sum;
                    List<Move> newPath1 = new ArrayList<>(path1);
                    int len1 = 0;
                    if (v1 != last1) {
                        List<Move> shortestPath = shortestPathMoves.get(new Cpl(last1, v1));
                        len1 = shortestPath.size();
                        newPath1.addAll(shortestPath);
                    }
                    int newRemain1 = remain1 - len1 - 1;
                    if (newRemain1 >= 0) {
                        newPath1.add(new Move(Action.OPEN, v1, null));
                        newSum += (v1.rate * newRemain1);
                    }


                    List<Move> newPath2 = new ArrayList<>(path2);
                    int len2 = 0;
                    if (v2 != last2) {
                        List<Move> shortestPath = shortestPathMoves.get(new Cpl(last2, v2));
                        len2 = shortestPath.size();
                        newPath2.addAll(shortestPath);
                    }
                    int newRemain2 = remain2 - len2 - 1;
                    if (newRemain2 >= 0) {
                        newPath2.add(new Move(Action.OPEN, v2, null));
                        newSum += (v2.rate * newRemain2);
                    }

                    if (newRemain2 >= 0 || newRemain1 >= 0) {
                        simulate2(valves, Collections.unmodifiableList(newPath1), Collections.unmodifiableList(newPath2), totalTime, newSum);
                    }

                }
            }
        }
    }

    record Cpl(Valve actual, Valve last) {}

    protected Object solve1(Map<String, Valve> valves) {
//        int openableCnt = (int) valves.values().stream().filter(v -> v.rate > 0).count();

        Set<Cpl> allCouples = new HashSet<>();
        for (Valve valve : valves.values()) {
            for (Valve dest : valve.valves) {
                allCouples.add(new Cpl(dest, valve));
            }
        }

        Valve startValve = valves.get("AA");

        for (Valve start : valves.values()) {
            Map<Valve, Node<Valve>> nodes = new HashMap<>();
            for (Valve v : valves.values()) {
                nodes.put(v, new Node<Valve>(v));
            }
            for (Node<Valve> n : nodes.values()) {
                for (Valve v : n.getVal().valves) {
                    n.addDestination(nodes.get(v), 1);
                }
            }

            Graph<Valve> g = new Graph<>();
            g.calculateShortestPathFromSource(nodes.get(start));

            for (Node<Valve> n : nodes.values()) {
                if (n.getVal() != start) {
                    List<Valve> shortest = n.getShortestPath().stream().map(Node::getVal).collect(Collectors.toList());
                    shortest.add(n.getVal());
                    shortest.remove(0);
                    shortestPaths.put(new Cpl(start, n.getVal()), Collections.unmodifiableList(shortest));
                }
            }

        }

//        for (Entry<Cpl, List<Valve>> e : shortestPaths.entrySet()) {
//            System.out.println(e.getKey() + " -> " + e.getValue());
//        }

        simulate(valves, List.of(new Move(Action.MOVE, startValve, null)), 30, 0);
        System.out.println(bestMoves);
        return bestSum;
    }



    protected Object solve2(Map<String, Valve> valves) {
        Set<Cpl> allCouples = new HashSet<>();
        for (Valve valve : valves.values()) {
            for (Valve dest : valve.valves) {
                allCouples.add(new Cpl(dest, valve));
            }
        }

        Valve startValve = valves.get("AA");

        for (Valve start : valves.values()) {
            Map<Valve, Node<Valve>> nodes = new HashMap<>();
            for (Valve v : valves.values()) {
                nodes.put(v, new Node<Valve>(v));
            }
            for (Node<Valve> n : nodes.values()) {
                for (Valve v : n.getVal().valves) {
                    n.addDestination(nodes.get(v), 1);
                }
            }

            Graph<Valve> g = new Graph<>();
            g.calculateShortestPathFromSource(nodes.get(start));

            for (Node<Valve> n : nodes.values()) {
                if (n.getVal() != start) {
                    List<Valve> shortest = n.getShortestPath().stream().map(Node::getVal).collect(Collectors.toList());
                    shortest.add(n.getVal());
                    shortest.remove(0);
                    List<Move> shortestMoves = new ArrayList<>();
                    Valve l = start;
                    for (Valve p : shortest) {
                        shortestMoves.add(new Move(Action.MOVE, p, l));
                        l = p;
                    }

                    shortestPaths.put(new Cpl(start, n.getVal()), Collections.unmodifiableList(shortest));
                    shortestPathMoves.put(new Cpl(start, n.getVal()), shortestMoves);
                }
            }

        }

//        List<Valve> openable = valves.values().stream().filter(v -> v.rate > 0).collect(Collectors.toList());

        simulate2(valves, List.of(new Move(Action.MOVE, startValve, null)), List.of(new Move(Action.MOVE, startValve, null)), 26, 0);
        System.out.println(bestMoves1);
        System.out.println(bestMoves2);
        return bestSum2;    
    }

    public static void main(String[] args) throws Exception {
        new Task16().run();
    }

}
