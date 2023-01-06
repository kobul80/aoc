package cz.kobul.aoc2022;

import java.util.List;

/**
 * https://adventofcode.com/2022/day/10
 * start 7:00, end 7:35
 */
public class Task10 extends Aoc {

	enum Type {
		NOP,
		ADDX;
		public int cycles() {
			return this == NOP ? 1 : 2;
		}
	}

	record Instr(Type type, int amount) {}

	public Instr parse(String input) {
		if (input.trim().equals("noop")) {
			return new Instr(Type.NOP, 0);
		} else {
			return new Instr(Type.ADDX, Integer.parseInt(input.split(" ")[1]));
		}
	}

	List<Instr> instr;

	public void solve() throws Exception {
		String fileName = getDefaultInputFileName();

		readFileToListString(fileName);
		instr = getStringStream(fileName).map(this::parse).toList();

		logResult(1, solve1());
		logResult(2, solve2());
	}

	protected Object solve1() {
		int cycle = 1;
		int x = 1;
		long sum = 0;
		int nextCnt = 20;
		for (Instr in : instr) {
			if ((cycle >= nextCnt || cycle + in.type.cycles() > nextCnt) && nextCnt <= 220) {
				int sig = (nextCnt) * x;
				sum += sig;
				System.out.println("cycle " + cycle + " sig " + sig + " x " + x);
				nextCnt += 40;
			}
			if (in.type == Type.ADDX) {
				x = x + in.amount;
			}
			cycle += in.type.cycles();
		}
		return sum;
	}

	protected Object solve2() {
		char[][] display = new char[6][40];

		int cycle = 1;
		int x = 1;

		int ip = 0;
		while (cycle < 241) {
			// cycle start
			Instr in = instr.get(ip);
			ip++;

			int rowPos = (cycle - 1) % 40;
			int spritePos = x - 1;
			if (rowPos >= spritePos && rowPos <= spritePos+2) {
				display[(cycle -1) / 40][rowPos] = '#';
			}

			cycle++;
			// cycle end 1

			if (in.type == Type.ADDX) {
				// before cycle 2


				//"in" cycle 2

				rowPos = (cycle - 1) % 40;
				spritePos = x - 1;
				if (rowPos >= spritePos && rowPos <= spritePos+2) {
					display[(cycle -1) / 40][rowPos] = '#';
				}

				cycle++;

				x += in.amount;
				// after cycle 2
			}


			// cycle end
		}

		printCharCharArray(display);

		return "EKRHEPUZ";
	}

	public static void main(String[] args) throws Exception {
		new Task10().solve();
	}

}

