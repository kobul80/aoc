package cz.kobul.aoc2022;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * https://adventofcode.com/2022/day/15
 * start  end
 */
public class Task15 extends Aoc {

    static class Sensor {

        Pos pos;
        Pos beacon;
        int mdist;


        public Sensor(Pos pos, Pos beacon) {
            this.pos = pos;
            this.beacon = beacon;
            this.mdist = pos.mdist(beacon);
        }

        @Override
        public String toString() {
            return pos + " B@" + beacon + " dist " + mdist;
        }
    }

    static Pattern p = Pattern.compile("Sensor at x=(-?[0-9]+), y=(-?[0-9]+): closest beacon is at x=(-?[0-9]+), y=(-?[0-9]+)");

    public static Sensor parse(String input) {
        Matcher m = p.matcher(input);
        if (m.matches()) {
            return new Sensor(Pos.ofColRow(m.group(1) + "," + m.group(2)), Pos.ofColRow(m.group(3) + "," + m.group(4)));
        }
        throw new IllegalArgumentException("Invalid input " + input);
    }

    public void solve() throws Exception {
        List<Sensor> sens = getStringStream(getDefaultInputFileName()).map(Task15::parse).toList();

        logResult(1, solve1(sens));
        logResult(2, solve2(sens));
    }

    protected Object solve1(List<Sensor> sens) {
        int y = 2000000;
        int rowLen = 100000000;
        byte[] row = new byte[rowLen];
        for (Sensor s : sens) {
            if (s.pos.row() + s.mdist >= y || s.pos.row() - s.mdist <= y) {
                int cnt = s.mdist - Math.abs(y - s.pos.row()) + 1;
                for (int i = 0; i < cnt; i++) {
                    row[rowLen/2 + s.pos.col() + i] = 1;
                    row[rowLen/2 + s.pos.col() - i] = 1;
                }
            }
        }
        // vypnout existujici beacons
        for (Sensor s : sens) {
            if (s.beacon.row() == y) {
                row[rowLen/2 + s.beacon.col()] = 0;
            }
        }

        long cnt = 0;
        for (byte b : row) {
            if (b == 1) {
                cnt++;
            }
        }
        return cnt;
    }

    record Line(int fromC, int toC) {
        boolean contains(Line line) {
            return line.fromC >= fromC && line.toC <= toC;
        }
        List<Line> add(Line line) {
            if (contains(line)) {
                return List.of(this);
            } else if (line.contains(this)) {
                return List.of(line);
            } else if (fromC < line.fromC() && toC < line.toC()) {
                if (toC >= line.fromC) {
                    return List.of(new Line(fromC, line.toC));
                } else {
                    return List.of(this, line);
                }
            } else if (fromC > line.fromC() && toC > line.toC()) {
                if (fromC <= line.toC) {
                    return List.of(new Line(line.fromC, toC));
                } else {
                    return List.of(this, line);
                }
            } else {
                throw new IllegalStateException("Invalid state for: " + this + " + " + line);
            }

        }
    }

    protected Object solve2(List<Sensor> sens) {
        for (int y = 0; y <= 4000000; y++) {
            List<Line> lines = new ArrayList<>();
            for (Sensor s : sens) {
                if (s.pos.row() + s.mdist >= y && s.pos.row() - s.mdist <= y) {
                    int cnt = s.mdist - Math.abs(y - s.pos.row());
                    if (cnt < 0) {
                        throw new IllegalStateException("invalid cnt " + cnt);
                    }
                    int fromC = Math.max(s.pos.col() - cnt, 0);
                    int toC = Math.min(s.pos.col() + cnt, 4000000);
                    lines.add(new Line(fromC, toC));
                }
            }
            List<Line> sorted = lines.stream().sorted(Comparator.comparing(Line::fromC)).collect(Collectors.toList());
            for (int i = 0; i < sorted.size(); i++) {
                for (int j = 1; j < sorted.size(); j++) {
                    if (i != j && sorted.get(i) != null && sorted.get(j) != null) {
                        List<Line> res = sorted.get(i).add(sorted.get(j));
                        if (res.size() == 1) {
                            sorted.set(i, res.get(0));
                            sorted.set(j, null);
                        }
                    }
                }
            }
            List<Line> merged = sorted.stream().filter(p -> p != null).sorted(Comparator.comparing(Line::fromC)).toList();
            if (merged.size() == 2) {
                return (merged.get(0).toC()+1)*4000000L + y;
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        new Task15().run();
    }

}

