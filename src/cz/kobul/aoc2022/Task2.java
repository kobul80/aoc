package cz.kobul.aoc2022;

import java.util.Map;

/**
 * https://adventofcode.com/2022/day/2
 */
public class Task2 extends Aoc {

	enum Hand {
		ROCK,
		PAPER,
		SCISSORS;

		public int getPoints() {
			return ordinal() + 1;
		}
	}

	enum Outcome {
		WIN1(6, 0),
		DRAW(3, 3),
		WIN2(0, 6);
		int points1;
		int points2;

		private Outcome(int p1, int p2) {
			this.points1 = p1;
			this.points2 = p2;
		}

		public int getPoints1() {
			return points1;
		}

		public int getPoints2() {
			return points2;
		}
	}

	public static class Game {
		private Hand player1;
		private Hand player2;

		public Game(Hand p1, Hand p2) {
			this.player1 = p1;
			this.player2 = p2;
		}
		
		public static Game of(Hand p1, Outcome result) {
			for (Hand p2 : Hand.values()) {
				Game g = new Game(p1, p2);
				if (g.getOutcome() == result) {
					return g;
				}
			}
			throw new IllegalStateException("Invalid state");
		}

		public Outcome getOutcome() {
			if (player1 == player2) {
				return Outcome.DRAW;
			} else {
				if (player1 == Hand.ROCK) {
					return player2 == Hand.SCISSORS ? Outcome.WIN1 : Outcome.WIN2;
				} else if (player1 == Hand.PAPER) {
					return player2 == Hand.ROCK ? Outcome.WIN1 : Outcome.WIN2;
				} else if (player1 == Hand.SCISSORS) {
					return player2 == Hand.ROCK ? Outcome.WIN2 : Outcome.WIN1;
				}
			}
			return null;
		}

		public int getPoints1() {
			Outcome o = getOutcome();
			return o.getPoints1() + player1.getPoints();
		}

		public int getPoints2() {
			Outcome o = getOutcome();
			return o.getPoints2() + player2.getPoints();
		}

		@Override
		public String toString() {
			return player1 + " " + player2 + " => " + getOutcome() + " [" + getPoints1() + ", " + getPoints2() + "]";
		}

	}

	public static final Map<String, Hand> Player1Map = Map.<String, Hand>of("A", Hand.ROCK, "B", Hand.PAPER, "C", Hand.SCISSORS);
	public static final Map<String, Hand> Player2Map = Map.<String, Hand>of("X", Hand.ROCK, "Y", Hand.PAPER, "Z", Hand.SCISSORS);

	public static final Map<String, Outcome> OutcomeMap = Map.<String, Outcome>of("X", Outcome.WIN1, "Y", Outcome.DRAW, "Z", Outcome.WIN2);

	public static Game parse(String s) {
		String[] c = s.split("\\s+");
		Hand x1 = Player1Map.get(c[0]);
		Hand x2 = Player2Map.get(c[1]);
		return new Game(x1, x2);
	}

	public static Game parse2(String s) {
		String[] c = s.split("\\s+");
		Hand x1 = Player1Map.get(c[0]);
		Outcome o = OutcomeMap.get(c[1]);
		return Game.of(x1, o);
	}

	@Override
	public void solve() throws Exception {
		String fileName = getDefaultInputFileName();

		logResult(1, getStringStream(fileName).map(Task2::parse).map(Game::getPoints2).reduce(Integer::sum).get());
		logResult(2, getStringStream(fileName).map(Task2::parse2).map(Game::getPoints2).reduce(Integer::sum).get());
	}


	public static void main(String[] args) throws Exception {
		new Task2().run();
	}

}
