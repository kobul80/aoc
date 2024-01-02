package cz.kobul.aoc2022;

import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * https://adventofcode.com/2022/day/21
 * start 6:02 end 7:30
 */
public class Task21 extends Aoc2022 {

    protected Object solve1() {
        return (long) monkeys.get("root").value();
    }

    protected double diff(MonkeyNo humn, MonkeyEvl root, long val) {
        humn.value = val;
        double v1 = root.mo1.value();
        double v2 = root.mo2.value();
        return Math.abs(v1 - v2);
    }

    protected long solve2Numeric(MonkeyNo humn, MonkeyEvl root, long start, long end) {
        long center = start + Math.abs(end - start) / 2;
        double diffs = diff(humn, root, start);
        double diffe = diff(humn, root, end);
        double diffc = diff(humn, root, center);
        if (diffc == 0) {
            return center;
        }
        if (diffc + diffs < diffc + diffe) {
            return solve2Numeric(humn, root, start, center);
        } else {
            return solve2Numeric(humn, root, center, end);
        }
    }

    protected Object solve2() {
        MonkeyNo humn = new MonkeyNo("humn", 1);
//        monkeys.put("humn", humn);
//        Humn humn = new Humn();
        monkeys.put("humn", humn);
        monkeys.values().forEach(m -> m.parse(monkeys));

        MonkeyEvl root = (MonkeyEvl) monkeys.get("root");
        return solve2Numeric(humn, root, -1000000000000000L, 1000000000000000L);
    }

    static interface Mo {
        String name();
        double value();
        String expr();
        void parse(Map<String, Mo> monkeys);
    }

    static class Humn implements Mo {
        @Override
        public String name() {
            return "humn";
        }
        @Override
        public String expr() {
            return "x";
        }
        @Override
        public double value() {
            return 0;
        }
        @Override
        public void parse(Map<String, Mo> monkeys) {
        }
    }

    static class MonkeyNo implements Mo {
        String name;
        double value;

        public MonkeyNo(String name, long value) {
            super();
            this.name = name;
            this.value = value;
        }

        @Override
        public String name() { return name; }
        @Override
        public double value() { return value; }

        @Override
        public String expr() {
            return Double.toString(value);
        }

        @Override
        public void parse(Map<String, Mo> monkeys) {
        }
    }

    static Pattern exp = Pattern.compile("\\s*([A-Za-z]+)\\s*([+-\\\\*/])\\s+([A-Za-z]+)");

    static class MonkeyEvl implements Mo {
        String name;
        String expr;
        Mo mo1;
        Mo mo2;
        String op;

        public MonkeyEvl(String name, String expr) {
            super();
            this.name = name;
            this.expr = expr;
        }

        @Override
        public String expr() {
            if (mo1 instanceof MonkeyNo && mo2 instanceof MonkeyNo) {
                return "" + value();
            }
            return "(" + mo1.expr() + " " + op + " " + mo2.expr() + ")";
        }

        @Override
        public void parse(Map<String, Mo> monkeys) {
            Matcher m = exp.matcher(expr);
            if (m.matches()) {
                mo1 = monkeys.get(m.group(1));
                mo2 = monkeys.get(m.group(3));
                op = m.group(2);
            } else {
                throw new IllegalArgumentException("Invalid expression " + expr);
            }
        }

        @Override
        public double value() {
            return switch (op) {
            case "+" -> mo1.value() + mo2.value();
            case "-" -> mo1.value() - mo2.value();
            case "*" -> mo1.value() * mo2.value();
            case "/" -> mo1.value() / mo2.value();
            default -> throw new IllegalStateException("Illegal operator" + op);
            };
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }

    }

    public static Mo parse(String input) {
        String[] mo = input.split(":");
        String name = mo[0];
        String eval = mo[1].trim();
        int no;
        try {
            no = Integer.parseInt(eval);
            return new MonkeyNo(name, no);
        } catch (Exception ex) {
            // vyraz
            return new MonkeyEvl(name, eval);
        }
    }

    static Map<String, Mo> monkeys;

    public void solve() throws Exception {
        monkeys = getStringStream(getDefaultInputFileName()).map(Task21::parse).collect(Collectors.toMap(Mo::name, Function.identity()));
        monkeys.values().stream().forEach(m -> m.parse(monkeys));


        logResult(1, solve1());
        logResult(2, solve2());
    }

    public static void main(String[] args) throws Exception {
        new Task21().run();
    }

}
