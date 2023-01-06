package cz.kobul.aoc2022;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * https://adventofcode.com/2022/day/17
 */
public class Task17 extends Aoc {

    char[][] shape1 = new char[][] {{'0', '0','0','0'}};

    char[][] shape2 = new char[][] {
        {' ', 'A',' '},
        {'A', 'A','A'},
        {' ', 'A',' '}
        };

        char[][] shape3 = new char[][] {
            {'B', 'B','B'},
            {' ', ' ','B'},
            {' ', ' ','B'}
        };

        char[][] shape4 = new char[][] {
            {'C'},
            {'C'},
            {'C'},
            {'C'}
        };

        char[][] shape5 = new char[][] {
            {'D','D'},
            {'D','D'}
        };

    List<char[][]> shapes = List.of(
            shape1, shape2, shape3, shape4, shape5
            );


    enum Move {
        LEFT, RIGHT;
    }
    
    record State(long rock, long topHeight, int rockIndex) {}
    
    record Result(long topHeight, List<State> statesAtMove0) {}

	protected Result solveImpl(List<Move> moves, int rockCount, int rows) {
		Map2d map = new Map2d(rows+2, 10);
        map.line(new Pos(0, -1), new Pos(rows, -1), '|');
        map.line(new Pos(0, 7), new Pos(rows, 7), '|');
        map.line(new Pos(-1, -1), new Pos(-1, 7), '-');
        int currentTop = 0;
        int rockIndex = 0;

        int moveIndex = 0;
        int currentRock =0;

        List<State> statesAtMove0 = new ArrayList<>();
        
        while (currentRock < rockCount) {
            currentRock++;
            char[][] shape = shapes.get(rockIndex);
            rockIndex = (rockIndex + 1) % shapes.size();

            Pos p = new Pos(currentTop + 3, 2);

            boolean canMove = true;
            while (canMove) {

                if (moveIndex == 0 && currentTop > 0) {
                	statesAtMove0.add(new State(currentRock, currentTop, rockIndex));
                }

                Move move = moves.get(moveIndex);
                moveIndex = (moveIndex + 1) % moves.size();
                Pos p1 = move == Move.LEFT ? p.left() : p.right();
                if (!map.conflictSprite(p1, shape)) {
                    // muzu se pohnout vlevo/vpravo
                    p = p1;
                }

                Pos p2 = p.up();
                if (!map.conflictSprite(p2, shape)) {
                    p = p2;
                } else {
                    canMove = false;
                    map.drawSprite(p, shape);
                    currentTop = Math.max(currentTop, p.row() + shape.length);
                }
            }
        }
        return new Result(currentTop, statesAtMove0);
	}
    
    protected Object solve1(List<Move> moves) {
    	int rockCount = 2022;
        int rows = 500000;
        return solveImpl(moves, rockCount, rows).topHeight;
    }
    
    protected boolean checkRepeatSize(List<Integer> rocks, int repeatSize) {
		// check repetition of size repeatSize
		for (int k = 0; k < repeatSize; k++) {
			Integer check = rocks.get(k);
			for (int j = 1; j < (rocks.size() / repeatSize); j++) {
				int index = j * repeatSize + k;
				if (!rocks.get(index).equals(check)) {
					return false;
				}
			}
		}
		return true;
	}
    
    protected int foundRepetitionCnt(List<Integer> rocks) {
    	for (int i = 1; i < rocks.size() - 1; i++) {
    		if (checkRepeatSize(rocks, i)) {
    			return i;
    		}
    	}
    	return -1;
    }

    
	protected boolean checkStartSize(Result r, int repCnt, int startSkip) {
		int i1 = startSkip;
		int i2 = startSkip + repCnt;
		long diffRock = r.statesAtMove0.get(i2).rock -r.statesAtMove0.get(i1).rock;   
		long diffHeight = r.statesAtMove0.get(i2).topHeight -r.statesAtMove0.get(i1).topHeight;   
		for (int j = 1; j < 4; j++) {
			i1 += repCnt;
			i2 += repCnt;
			long diffRock1 = r.statesAtMove0.get(i2).rock -r.statesAtMove0.get(i1).rock;   
			long diffHeight1 = r.statesAtMove0.get(i2).topHeight -r.statesAtMove0.get(i1).topHeight;   
			if (diffRock != diffRock1 || diffHeight != diffHeight1) {
				return false;
			}    				
		}
		return true;
	}

	protected int findStartSize(Result r, int repCnt) {
    	if (repCnt > 1) {
    		// find start size
    		for (int i = 1; i < repCnt; i++) {
    			if (checkStartSize(r, repCnt, i)) {
    				return i;
    			}
    		}
    		throw new IllegalStateException("Unable to find start segment size");
    	}
    	return 0;
	}

	
    protected Object solve2(List<Move> moves) {
    	long rockCount = 1000000000000L;
    	
    	// make test run to find repetition
    	Result r = solveImpl(moves, 10000, 500000);

    	List<Integer> rockRepetition = r.statesAtMove0.stream().map(State::rockIndex).toList();

    	// find rock repetition schema after first moves repeat
    	int repCnt = foundRepetitionCnt(rockRepetition);
    
    	// find start segment
    	int startSize = findStartSize(r, repCnt);
    	
    	long firstPartRocks = r.statesAtMove0.get(startSize).rock;
    	long firstPartHeight = r.statesAtMove0.get(startSize).topHeight;
    	
    	long repeatingRocks = r.statesAtMove0.get(repCnt + startSize).rock - firstPartRocks;
    	long repeatingHeight = r.statesAtMove0.get(repCnt + startSize).topHeight - firstPartHeight;

    	long remainingRocks = rockCount;
    	long totalHeight = 0;
    	// repeating part
    	long repPartCnt = (remainingRocks - firstPartRocks) / repeatingRocks;
    	remainingRocks -= repPartCnt * repeatingRocks;
    	totalHeight += repPartCnt * repeatingHeight;

    	// first part and last part - simulate firstPart+last part of repeating part
    	Result r1 = solveImpl(moves, (int) remainingRocks, 500000);
    	totalHeight += r1.topHeight;
    	
    	return totalHeight;
    }

	protected List<Move> parseMoves(String s) throws IOException {
		List<Move> moves = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '<') {
                moves.add(Move.LEFT);
            } else if (s.charAt(i) == '>') {
                moves.add(Move.RIGHT);
            } else {
                throw new IllegalStateException("Unknown move " + s.charAt(i));
            }
        }
        return moves;
	}  
    
    public void solve() throws Exception {
		String fileContent = getStringStream(getDefaultInputFileName()).findFirst().get();

        logResult(1, solve1(parseMoves(fileContent)));
        logResult(2, solve2(parseMoves(fileContent)));
    }

    public static void main(String[] args) throws Exception {
        new Task17().run();
    }

}
