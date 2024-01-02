package cz.kobul.aoc2022;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://adventofcode.com/2022/day/22
 * start 6:30 end 16:20 (s velkyma prestavkama :-) )
 */
public class Task22 extends Aoc2022 {

    protected Object solve1(Input input) {
        Dir dir = Dir.RIGHT;
        Pos p = new Pos(0, 0);
        while (input.map.get(p) != '.') {
            p = p.right();
        }

        for (Ins in : input.instructions) {
            // move
            for (int i = 0; i < in.cnt; i++) {
                Pos np = input.map.wrapMove(p, dir);
                if (input.map.get(np) == '#') {
                    break;
                }
                p = np;
            }
            if (in.rot != null) {
                dir = dir.rot(in.rot);
            }
        }
        int res1 = (p.row() + 1) * 1000 + (p.col() + 1) * 4 + dir.ordinal();
        return res1;
    }

    protected Object solve2(Input input) {
    	// for test and input files there is different layout - thus different hardcoded transitions
        PreparedData prep = input.map.rows() > 12 ? prepareDataInput(input) : prepareDataTest(input);

        Dir dir = Dir.RIGHT;
        Pos p = new Pos(0, 0);
        Side s = prep.sides.get(new NoDir(1, Dir.UP));
        while (s.get(p) != '.') {
            p = p.right();
        }

        for (Ins in : input.instructions) {
            // move
            for (int i = 0; i < in.cnt; i++) {
                NewMove np = wrapMoveSide(prep, s, p, dir);
                if (np.side().get(np.pos()) == '#') {
                    break;
                }
                p = np.pos();
                s = np.side();
                dir = np.dir();
            }
            if (in.rot != null) {
                dir = dir.rot(in.rot);
            }
        }

        int res2 = (s.ref.row() + p.row() + 1) * 1000 + (s.ref.col() + p.col() + 1) * 4 + dir.ordinal();
        return res2;
    }

    Pattern P = Pattern.compile("([0-9]+)([LR])?");

    record Ins (int cnt, Rot rot) {}


    record MapCh(char[][] map, int rowLen) {
        int rows() {
            return map.length;
        }

        int cols() {
            return rowLen;
        }

        char get(Pos p) {
            return map[p.row()][p.col()];
        }

        Pos wrapMove1(Pos p, Dir d) {
            Pos p1 = p.move(d);
            if (p1.row() >= rows()) {
                p1 = new Pos(0, p1.col());
            } else if (p1.row() < 0) {
                p1 = new Pos(rows() -1, p1.col());
            } else if (p1.col() >= cols()) {
                p1 = new Pos(p1.row(), 0);
            } else if (p1.col() < 0) {
                p1 = new Pos(p1.row(), cols() - 1);
            }
            return p1;
        }

        Pos wrapMove(Pos p, Dir d) {
            Pos p1 = wrapMove1(p, d);
            while (get(p1) != '.' && get(p1) != '#') {
                p1 = wrapMove1(p1, d);
            }
            return p1;
        }
        
        char[][] subMap(Pos from, int cubeSize) {
            char[][] res = new char[cubeSize][cubeSize];
            for (int r = 0; r < cubeSize; r++) {
                for (int c = 0; c < cubeSize; c++) {
                    res[r][c] = map[r + from.row()][c + from.col()];
                }
            }
            return res;
        }
        
        Side createSide(int index, int row, int col, int cubeSize) {
            Pos p = new Pos(row, col);
            return new Side(index, Dir.UP, subMap(p, cubeSize), p);
        }
	
    }

    static class Side {

        int no;
        Dir dir;
        char[][] map;
        Pos ref;

        public Side(int no, Dir dir, char[][] map, Pos ref) {
			super();
			this.no = no;
			this.dir = dir;
			this.map = map;
			this.ref = ref;
		}

		int rows() {
            return map.length;
        }

        int cols() {
            return map[0].length;
        }

        NoDir getNoDir() {
            return new NoDir(no, dir);
        }

        char get(Pos p) {
            return map[p.row()][p.col()];
        }

        Side rotateL() {
            char[][] newMap = new char[rows()][cols()];
            for (int r = 0; r < rows(); r++) {
                for (int c = 0; c < cols(); c++) {
                    int newRow = (rows() -1 ) - c;
                    int newCol = r;
                    newMap[newRow][newCol] = map[r][c];
                }
            }
            return new Side(no, dir.rot(Rot.L), newMap, ref);
        }

        Pos rotatePosL(Pos p) {
            int newRow = (rows() -1 ) - p.col();
            int newCol = p.row();
            return new Pos(newRow, newCol);
        }

        Side rotate(Dir newDir) {
            Side newSide = this;
            while (newSide.dir != newDir) {
                newSide = newSide.rotateL();
            }
            return newSide;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Side)) {
                return false;
            }
            Side oth = (Side) obj;
            return oth.dir == dir && oth.no == no;
        }

        @Override
        public int hashCode() {
            return Objects.hash(dir, no);
        }

        @Override
        public String toString() {
            return no + " " + dir + "\n" + charCharArrayToString(map);
        }

    }

    record NoDir (int no, Dir dir) {}

    record NoDirDir (int no, Dir dir, Dir where) {}

    record Input(MapCh map, List<Ins> instructions) {}
    
    record PreparedData(Map<NoDir, Side> sides, Map<NoDirDir, NoDir> steps) {}
    
    public Input parse(List<String> input) {
    	int mapRows = 0;
    	int rowLen = 0;
    	while (input.get(mapRows).trim().length() > 0) {
    		rowLen = Math.max(input.get(mapRows).length(), rowLen);
    		mapRows++;
    	}
    	
        char[][] map = new char[mapRows][];
        for (int r = 0; r< mapRows; r++) {
            char[] a = new char[rowLen];
            map[r] = a;
            Arrays.fill(a, ' ');
            String row = input.get(r);
            for (int c = 0; c < row.length(); c++) {
                a[c] = row.charAt(c);
            }
        }
        
        List<Ins> instr = new ArrayList<>();
        
        String insStr = input.get(mapRows + 1);
        Matcher m = P.matcher(insStr);
        while (m.find()) {
            Rot r = null;
            if (m.group(2) != null) {
                r = Rot.valueOf(m.group(2));
            }
            instr.add(new Ins(Integer.parseInt(m.group(1)), r));
        }
    	return new Input(new MapCh(map, rowLen), instr);
    }
    
    protected PreparedData prepareDataTest(Input input) {
        int cubeSize = 4;
        List<Side> sds = new ArrayList<>();
        sds.add(input.map.createSide(1, 0, 8, cubeSize));
        sds.add(input.map.createSide(2, 4, 0, cubeSize));
        sds.add(input.map.createSide(3, 4, 4, cubeSize));
        sds.add(input.map.createSide(4, 4, 8, cubeSize));
        sds.add(input.map.createSide(5, 8, 8, cubeSize));
        sds.add(input.map.createSide(6, 8, 12, cubeSize));

        Map<NoDir, Side> sides = new HashMap<>();
        
        for (Dir dir : Dir.values()) {
            for (Side side : sds) {
                Side newSide = side.rotate(dir);
                sides.put(newSide.getNoDir(), newSide);
            }
        }

        Map<NoDirDir, NoDir> basicSteps = new HashMap<>();
        // this is hardcoded for specific input of test example
        basicSteps.put(new NoDirDir(1, Dir.UP, Dir.RIGHT), new NoDir(6, Dir.DOWN));
        basicSteps.put(new NoDirDir(1, Dir.UP, Dir.UP), new NoDir(2, Dir.DOWN));
        basicSteps.put(new NoDirDir(1, Dir.UP, Dir.DOWN), new NoDir(4, Dir.UP));
        basicSteps.put(new NoDirDir(1, Dir.UP, Dir.LEFT), new NoDir(3, Dir.RIGHT));

        basicSteps.put(new NoDirDir(2, Dir.UP, Dir.RIGHT), new NoDir(3, Dir.UP));
        basicSteps.put(new NoDirDir(2, Dir.UP, Dir.UP), new NoDir(1, Dir.DOWN));
        basicSteps.put(new NoDirDir(2, Dir.UP, Dir.DOWN), new NoDir(5, Dir.DOWN));
        basicSteps.put(new NoDirDir(2, Dir.UP, Dir.LEFT), new NoDir(6, Dir.LEFT));

        basicSteps.put(new NoDirDir(3, Dir.UP, Dir.RIGHT), new NoDir(4, Dir.UP));
        basicSteps.put(new NoDirDir(3, Dir.UP, Dir.UP), new NoDir(1, Dir.LEFT));
        basicSteps.put(new NoDirDir(3, Dir.UP, Dir.DOWN), new NoDir(5, Dir.RIGHT));
        basicSteps.put(new NoDirDir(3, Dir.UP, Dir.LEFT), new NoDir(2, Dir.UP));

        basicSteps.put(new NoDirDir(4, Dir.UP, Dir.RIGHT), new NoDir(6, Dir.LEFT));
        basicSteps.put(new NoDirDir(4, Dir.UP, Dir.UP), new NoDir(1, Dir.UP));
        basicSteps.put(new NoDirDir(4, Dir.UP, Dir.DOWN), new NoDir(5, Dir.UP));
        basicSteps.put(new NoDirDir(4, Dir.UP, Dir.LEFT), new NoDir(3, Dir.LEFT));

        basicSteps.put(new NoDirDir(5, Dir.UP, Dir.RIGHT), new NoDir(6, Dir.UP));
        basicSteps.put(new NoDirDir(5, Dir.UP, Dir.UP), new NoDir(4, Dir.UP));
        basicSteps.put(new NoDirDir(5, Dir.UP, Dir.DOWN), new NoDir(2, Dir.DOWN));
        basicSteps.put(new NoDirDir(5, Dir.UP, Dir.LEFT), new NoDir(3, Dir.LEFT));

        basicSteps.put(new NoDirDir(6, Dir.UP, Dir.RIGHT), new NoDir(1, Dir.DOWN));
        basicSteps.put(new NoDirDir(6, Dir.UP, Dir.UP), new NoDir(4, Dir.RIGHT));
        basicSteps.put(new NoDirDir(6, Dir.UP, Dir.DOWN), new NoDir(2, Dir.RIGHT));
        basicSteps.put(new NoDirDir(6, Dir.UP, Dir.LEFT), new NoDir(5, Dir.UP));

        Map<NoDirDir, NoDir> steps = new HashMap<>();
        steps.putAll(basicSteps);
        return new PreparedData(sides, steps);
    }


    protected PreparedData prepareDataInput(Input input) {
        List<Side> sds = new ArrayList<>();
        int cubeSize = 50;
        sds.add(input.map.createSide(1, 0, 50, cubeSize));
        sds.add(input.map.createSide(2, 0, 100, cubeSize));
        sds.add(input.map.createSide(3, 50, 50, cubeSize));
        sds.add(input.map.createSide(4, 100, 0, cubeSize));
        sds.add(input.map.createSide(5, 100, 50, cubeSize));
        sds.add(input.map.createSide(6, 150, 0, cubeSize));

        Map<NoDir, Side> sides = new HashMap<>();

        for (Dir dir : Dir.values()) {
            for (Side side : sds) {
                Side newSide = side.rotate(dir);
                sides.put(newSide.getNoDir(), newSide);
            }
        }

        Map<NoDirDir, NoDir> basicSteps = new HashMap<>();
        // this is hardcoded for specific input of input example
        basicSteps.put(new NoDirDir(1, Dir.UP, Dir.RIGHT), new NoDir(2, Dir.UP));
        basicSteps.put(new NoDirDir(1, Dir.UP, Dir.UP), new NoDir(6, Dir.LEFT));
        basicSteps.put(new NoDirDir(1, Dir.UP, Dir.DOWN), new NoDir(3, Dir.UP));
        basicSteps.put(new NoDirDir(1, Dir.UP, Dir.LEFT), new NoDir(4, Dir.DOWN));

        basicSteps.put(new NoDirDir(2, Dir.UP, Dir.RIGHT), new NoDir(5, Dir.DOWN));
        basicSteps.put(new NoDirDir(2, Dir.UP, Dir.UP), new NoDir(6, Dir.UP));
        basicSteps.put(new NoDirDir(2, Dir.UP, Dir.DOWN), new NoDir(3, Dir.LEFT));
        basicSteps.put(new NoDirDir(2, Dir.UP, Dir.LEFT), new NoDir(1, Dir.UP));

        basicSteps.put(new NoDirDir(3, Dir.UP, Dir.RIGHT), new NoDir(2, Dir.RIGHT));
        basicSteps.put(new NoDirDir(3, Dir.UP, Dir.UP), new NoDir(1, Dir.UP));
        basicSteps.put(new NoDirDir(3, Dir.UP, Dir.DOWN), new NoDir(5, Dir.UP));
        basicSteps.put(new NoDirDir(3, Dir.UP, Dir.LEFT), new NoDir(4, Dir.RIGHT));

        basicSteps.put(new NoDirDir(4, Dir.UP, Dir.RIGHT), new NoDir(5, Dir.UP));
        basicSteps.put(new NoDirDir(4, Dir.UP, Dir.UP), new NoDir(3, Dir.LEFT));
        basicSteps.put(new NoDirDir(4, Dir.UP, Dir.DOWN), new NoDir(6, Dir.UP));
        basicSteps.put(new NoDirDir(4, Dir.UP, Dir.LEFT), new NoDir(1, Dir.DOWN));

        basicSteps.put(new NoDirDir(5, Dir.UP, Dir.RIGHT), new NoDir(2, Dir.DOWN));
        basicSteps.put(new NoDirDir(5, Dir.UP, Dir.UP), new NoDir(3, Dir.UP));
        basicSteps.put(new NoDirDir(5, Dir.UP, Dir.DOWN), new NoDir(6, Dir.LEFT));
        basicSteps.put(new NoDirDir(5, Dir.UP, Dir.LEFT), new NoDir(4, Dir.UP));

        basicSteps.put(new NoDirDir(6, Dir.UP, Dir.RIGHT), new NoDir(5, Dir.RIGHT));
        basicSteps.put(new NoDirDir(6, Dir.UP, Dir.UP), new NoDir(4, Dir.UP));
        basicSteps.put(new NoDirDir(6, Dir.UP, Dir.DOWN), new NoDir(2, Dir.UP));
        basicSteps.put(new NoDirDir(6, Dir.UP, Dir.LEFT), new NoDir(1, Dir.RIGHT));

        Map<NoDirDir, NoDir> steps = new HashMap<>();
        steps.putAll(basicSteps);
        return new PreparedData(sides, steps);
    }

    record NewMove(Side side, Pos pos, Dir dir) {}

    private NewMove upify(Side s, Pos p, Dir d) {
        while (s.dir != Dir.UP) {
            s = s.rotateL();
            p = s.rotatePosL(p);
            d = d.rot(Rot.L);
        }
        return new NewMove(s, p, d);
    }

    private NewMove wrapMoveSide(PreparedData prep, Side s, Pos p, Dir dir) {
        Pos p2 = p.move(dir);
        if (p2.row() < 0) {
            // nahoru
            NoDirDir move = new NoDirDir(s.no, s.dir, Dir.UP);
            Side newSide = prep.sides.get(prep.steps.get(move));
            p2 = new Pos(newSide.rows() - 1, p2.col());
            return upify(newSide, p2, dir);
        } else if (p2.col() < 0) {
            // doleva
            NoDirDir move = new NoDirDir(s.no, s.dir, Dir.LEFT);
            Side newSide = prep.sides.get(prep.steps.get(move));
            p2 = new Pos(p2.row(), newSide.cols() - 1);
            return upify(newSide, p2, dir);
        } else if (p2.row() >= s.rows()) {
            // dolu
            NoDirDir move = new NoDirDir(s.no, s.dir, Dir.DOWN);
            Side newSide = prep.sides.get(prep.steps.get(move));
            p2 = new Pos(0, p2.col());
            return upify(newSide, p2, dir);
        } else if (p2.col() >= s.cols()) {
            // vpravo
            NoDirDir move = new NoDirDir(s.no, s.dir, Dir.RIGHT);
            Side newSide = prep.sides.get(prep.steps.get(move));
            p2 = new Pos(p2.row(), 0);
            return upify(newSide, p2, dir);
        }
        return new NewMove(s, p2, dir);
    }

    public void solve() throws Exception {
        List<String> inputList = readFileToListString(getDefaultInputFileName());
      
        logResult(1, solve1(parse(inputList)));
        logResult(1, solve2(parse(inputList)));
    }
    
    public static void main(String[] args) throws Exception {
        new Task22().run();
    }

}
