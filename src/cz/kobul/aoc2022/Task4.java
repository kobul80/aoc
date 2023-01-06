package cz.kobul.aoc2022;

/**
 * https://adventofcode.com/2022/day/4
 */
public class Task4 extends Aoc {

    public static class Range {

        long from; long to;

        public Range(String input) {
            String[] p = input.split("-");
            from = Long.parseLong(p[0]);
            to = Long.parseLong(p[1]);
        }

        public boolean fullyContains(Range r2) {
            return r2.from>=from && r2.to <= to;
        }

        public boolean overlap(Range r2) {
            return (r2.from >= from && r2.from <=to) || (from >= r2.from && from <=r2.to);
        }

    }

    public static boolean fullyContains(String pairs) {
        String[] prs = pairs.split(",");
        Range r1 = new Range(prs[0]);
        Range r2 = new Range(prs[1]);
        return r1.fullyContains(r2) || r2.fullyContains(r1);
    }

    public static boolean overlaps(String pairs) {
        String[] prs = pairs.split(",");
        Range r1 = new Range(prs[0]);
        Range r2 = new Range(prs[1]);
        return r1.overlap(r2);
    }

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();

        logResult(1, getStringStream(fileName).filter(Task4::fullyContains).count());
        logResult(2, getStringStream(fileName).filter(Task4::overlaps).count());
    }
    
	public static void main(String[] args) throws Exception {
    	new Task4().run();
    }

}
