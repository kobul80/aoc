package cz.kobul.aoc2022;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * https://adventofcode.com/2022/day/11
 * start 8:00, end 9:29
 */
public class Task11 extends Aoc2022 {

    enum Operation {
        ADD, MULT;
    }

    static class Monkey {
        List<Long> items = new ArrayList<>();
        Operation op;
        long arg;
        long divider;
        int monkeyTrue;
        int monkeyFalse;
        long inspected;

        public Monkey(List<Long> items, Operation op, long arg, long divider, int monkeyTrue, int monkeyFalse,
				long inspected) {
			super();
			this.items = items;
			this.op = op;
			this.arg = arg;
			this.divider = divider;
			this.monkeyTrue = monkeyTrue;
			this.monkeyFalse = monkeyFalse;
			this.inspected = inspected;
		}

		public long doOp(long item) {
            return doOpNoDiv(item) /3;
        }

        public long doOpNoDiv(long item) {
            inspected++;
            if (arg == -1) {
                return (op == Operation.ADD ? item + item : item * item);
            } else {
                return (op == Operation.ADD ? item + arg : item * arg);
            }
        }

        public int toMonkey(long result) {
            return result % divider == 0 ? monkeyTrue : monkeyFalse;
        }
        
        public long getInspected() {
			return inspected;
		}
        
    }


    public List<Monkey> parseMonkeys(List<String> lines) {
        List<Monkey> monkeys = new ArrayList<>();
        Iterator<String> it = lines.iterator();
        while (it.hasNext()) {
            String line = it.next();
            if (line.contains("Monkey")) {
                List<Long> items = getLongsFromString(it.next());
                line = it.next();
                Operation op =  (line.contains("old *")) ? Operation.MULT : Operation.ADD;
                long arg = (line.contains("old * old")) ? -1 : getIntegersFromString(line).get(0).intValue();

                long divider = getIntegersFromString(it.next()).get(0).intValue();
                int monkeyTrue = getIntegersFromString(it.next()).get(0).intValue();
                int monkeyFalse = getIntegersFromString(it.next()).get(0).intValue();
                monkeys.add(new Monkey(items, op, arg, divider, monkeyTrue, monkeyFalse, 0));
            }
        }
        return monkeys;
    }

    public void solve() throws Exception {
        List<String> lines = readFileToListString(getDefaultInputFileName());

        logResult(1, solve1(parseMonkeys(lines)));
        logResult(2, solve2(parseMonkeys(lines)));
    }

    public static Long multiply(Long i1, Long i2) {
        return i1 * i2;
    }

    protected Object solve1(List<Monkey> monkeys) {
        for (int round = 0; round < 20; round++) {
            for (Monkey monkey : monkeys) {
                for (Long item : monkey.items) {
                    long result = monkey.doOp(item);
                    int toMonkey = monkey.toMonkey(result);
                    monkeys.get(toMonkey).items.add(result);
                }
                monkey.items.clear();
            }
        }
        return monkeys.stream().map(Monkey::getInspected).sorted(Comparator.reverseOrder()).limit(2).reduce(1L, Task11::multiply);
    }

    protected Object solve2(List<Monkey> monkeys) {
        long lcm = 1;
        for (Monkey monkey : monkeys) {
            lcm = lcm(lcm, monkey.divider);
        }

        for (int round = 0; round < 10000; round++) {
            for (Monkey monkey : monkeys) {
                for (Long item : monkey.items) {
                    long result = monkey.doOpNoDiv(item) % lcm;
                    int toMonkey = monkey.toMonkey(result);
                    monkeys.get(toMonkey).items.add(result);
                }
                monkey.items.clear();
            }
        }
        return monkeys.stream().map(Monkey::getInspected).sorted(Comparator.reverseOrder()).limit(2).reduce(1L, Task11::multiply);
    }

    public static void main(String[] args) throws Exception {
        new Task11().run();
    }

}

