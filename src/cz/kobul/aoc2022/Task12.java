package cz.kobul.aoc2022;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://adventofcode.com/2022/day/11
 * start 6:10, end 8:19
 */
public class Task12 extends Aoc {

    int[][] map;
    Pos start;
    Pos end;

    int maxRow() {
        return map.length;
    }

    int maxCol() {
        return map[0].length;
    }

    int get(Pos p) {
        return map[p.row()][p.col()];
    }

    boolean possible(Pos from, Pos to) {
        return to.row() >= 0 && to.col() >= 0 && to.row() < maxRow() && to.col() < maxCol()
                && (get(to) <= get(from) + 1);
    }

    List<Pos> possibleSteps(Pos from) {
        List<Pos> result = new ArrayList<>();
        if (possible(from, from.up())) {
            result.add(from.up());
        }
        if (possible(from, from.down())) {
            result.add(from.down());
        }
        if (possible(from, from.left())) {
            result.add(from.left());
        }
        if (possible(from, from.right())) {
            result.add(from.right());
        }
        return result;
    }

    public int[][] readFileToIntIntArraySE(String fileName) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        int cnt = lines.get(0).length();

        int[][] result = new int[lines.size()][cnt];
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            for (int col = 0; col < line.length(); col++) {
                char ch = line.charAt(col);
                if (ch == 'E') {
                    end = new Pos(row, col);
                    ch = 'z';
                } else if (ch == 'S') {
                    start = new Pos(row, col);
                    ch = 'a';
                }
                result[row][col] = (ch - 97);
            }
        }
        return result;
    }


    public void solve() throws Exception {
        map = readFileToIntIntArraySE(getDefaultInputFileName());

        logResult(1, solve1());
        logResult(2, solve2());
    }

    Map<Pos, Node<Pos>> nodes = new HashMap<>();

    protected Object solve1() {
        for (int row = 0; row < maxRow(); row++) {
            for (int col = 0; col < maxCol(); col++) {
                Pos pos = new Pos(row, col);
                Node<Pos> n = new Node<Pos>(pos);
                nodes.put(pos, n);
            }
        }

        Graph<Pos> g = new Graph<Pos>();

        for (Node<Pos> n : nodes.values()) {
            for (Pos adj : possibleSteps(n.getVal())) {
                n.addDestination(nodes.get(adj), 1);
            }
            g.addNode(n);
        }

        g.calculateShortestPathFromSource(nodes.get(start));

        return nodes.get(end).getShortestPath().size();
    }

    protected Object solve2() {
        List<Pos> aNodes = new ArrayList<>();
        for (int row = 0; row < maxRow(); row++) {
            for (int col = 0; col < maxCol(); col++) {
                Pos pos = new Pos(row, col);
                if (get(pos) == 0) {
                    aNodes.add(pos);
                }
            }
        }

        int min = Integer.MAX_VALUE;

        for (Pos aNode : aNodes) {
            for (int row = 0; row < maxRow(); row++) {
                for (int col = 0; col < maxCol(); col++) {
                    Pos pos = new Pos(row, col);
                    Node<Pos> n = new Node<Pos>(pos);
                    nodes.put(pos, n);
                }
            }

            Graph<Pos> g = new Graph<Pos>();

            for (Node<Pos> n : nodes.values()) {
                for (Pos adj : possibleSteps(n.getVal())) {
                    n.addDestination(nodes.get(adj), 1);
                }
                g.addNode(n);
            }

            g.calculateShortestPathFromSource(nodes.get(aNode));

            int pathLen = nodes.get(end).getShortestPath().size();
            if (pathLen > 0 && pathLen < min) {
                min = pathLen;
            }
        }
        return min;
    }

    public static void main(String[] args) throws Exception {
        new Task12().run();
    }

}

