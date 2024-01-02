package cz.kobul.aoc2022;

import java.util.Comparator;

/**
 * https://adventofcode.com/2022/day/1
 */
public class Task1 extends Aoc2022 {

	@Override
	public void solve() throws Exception {
		String fileName = getDefaultInputFileName();
		logResult(1, groupBySummingLongs(getLongStream(fileName)).sorted(Comparator.reverseOrder()).findFirst().get());
		logResult(2, groupBySummingLongs(getLongStream(fileName)).sorted(Comparator.reverseOrder()).limit(3).reduce(Long::sum).get());			
	}

	public static void main(String[] args) throws Exception {
		new Task1().run();
	}

}
