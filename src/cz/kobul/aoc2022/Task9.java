package cz.kobul.aoc2022;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * https://adventofcode.com/2022/day/9 start 6:00, end 7:11 :(
 */
public class Task9 extends Aoc2022 {

	record Move(Dir dir, int cnt) {
	}

	public Move parse(String row) {
		String[] x = row.split(" ");
		return new Move(Dir.of(x[0]), Integer.parseInt(x[1]));
	}

	public void solve() throws Exception {
		String fileName = getDefaultInputFileName();

		List<Move> moves = getStringStream(fileName).map(this::parse).toList();

		logResult(1, solve1(moves));
		logResult(2, solve2(moves));
	}

	protected Object solve1(List<Move> moves) {
		Set<Pos> visited = new HashSet<>();

		Pos head = new Pos(0, 0);
		Pos lastHead = head;
		Pos tail = new Pos(0, 0);

		visited.add(tail);

		for (Move move : moves) {
			for (int i = 0; i < move.cnt; i++) {
				lastHead = head;
				head = head.move(move.dir);
				if (!tail.touch(head)) {
					tail = lastHead;
				}
				visited.add(tail);
			}
		}

		return visited.size();
	}

	protected Object solve2(List<Move> moves) {
		Set<Pos> visited = new HashSet<>();

		List<Pos> rope = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			rope.add(new Pos(0, 0));
		}

		visited.add(rope.get(9));

		for (Move move : moves) {
			for (int i = 0; i < move.cnt; i++) {
				Pos newPos = rope.get(0).move(move.dir);
				rope.set(0, newPos);

				for (int j = 1; j < rope.size(); j++) {
					Pos me = rope.get(j);
					if (!me.touch(rope.get(j - 1))) {
						rope.set(j, me.moveToTouch(rope.get(j - 1)));
					}
				}
				visited.add(rope.get(9));
			}
		}

		return visited.size();

	}

	public void print(List<Pos> rope) {
		for (int y = -5; y < 5; y++) {
			for (int x = -5; x < 5; x++) {
				int i = getMin(rope, x, y);
				System.out.print(i == -1 ? "." : Integer.toString(i));
			}
			System.out.println();
		}
		System.out.println();
	}

	public int getMin(List<Pos> rope, int x, int y) {
		for (int i = 0; i < rope.size(); i++) {
			if (rope.get(i).col() == x && rope.get(i).row() == y) {
				return i;
			}
		}
		return -1;
	}

	public static void main(String[] args) throws Exception {
		new Task9().run();
	}

}
