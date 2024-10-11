package cz.kobul.aoc2023;

import java.util.ArrayList;
import java.util.List;

/**
 * https://adventofcode.com/2023/day/2
 * start: 21:18
 * end: 21:40
 */
public class Task2 extends Aoc2023 {
    
    record ColoredCubes(int red, int green, int blue) {
        boolean possible(ColoredCubes c) {
            return red <= c.red && green <= c.green && blue <= c.blue;
        }
        
        long mult() {
            return (long) red * green * blue;
        }
    };
    
    static final ColoredCubes MAX = new ColoredCubes(12, 13, 14);
    
    record Game(int index, List<ColoredCubes> turns) {
        boolean possible(ColoredCubes c) {
            for (ColoredCubes cubes : turns) {
                if (!cubes.possible(c)) {
                    return false;
                }
            }
            return true;
        }
        
        ColoredCubes min() {
            int red = 0;
            int green = 0;
            int blue = 0;
            for (ColoredCubes cubes : turns) {
                red = Math.max(red, cubes.red);
                green = Math.max(green, cubes.green);
                blue = Math.max(blue, cubes.blue);
            }
            return new ColoredCubes(red, green, blue);
        }
    }
    
//    Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
//    Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
//    Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
//    Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
//    Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
    
	@Override
	public void solve() throws Exception {
		String fileName = getDefaultInputFileName();

		logResult(1, getStringStream(fileName).map(this::parse).filter(this::test).map(Game::index).reduce(Integer::sum).get());
		logResult(2, getStringStream(fileName).map(this::parse).map(Game::min).map(ColoredCubes::mult).reduce(Long::sum).get());
	}
	
	public boolean test(Game g) {
	    return g.possible(MAX);
	}
	
	public Game parse(String s) {
	    String[] s1 = s.split(":\\s*");
	    int game = Integer.parseInt(s1[0].split(" ")[1]);
	    String[] gs = s1[1].split(";\\s*");
	    List<ColoredCubes> turns = new ArrayList<>();
	    for (String g : gs) {
	        String[] cc = g.split(",\\s*");
	        int blue = 0;
	        int red = 0;
	        int green = 0;
	        for (String cube : cc) {
	            String[] cb = cube.split(" ");
	            int value = Integer.parseInt(cb[0]);
	            if ("red".equals(cb[1].trim())) {
	                red = value;
	            } else if ("green".equals(cb[1].trim())) {
	                green = value;
	            } if ("blue".equals(cb[1].trim())) {
	                blue = value;
	            }
	        }
	        turns.add(new ColoredCubes(red, green, blue));
	    }
	    return new Game(game, turns);
	}
	

	public Integer parse2(String s) {
	    return null;
	}
	    

	public static void main(String[] args) throws Exception {
		new Task2().run();
	}

}
