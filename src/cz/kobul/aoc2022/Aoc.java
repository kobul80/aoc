package cz.kobul.aoc2022;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Support class for AOC problem solving
 */
public abstract class Aoc {
	
	public static final String FN_PREFIX = "res/cz/kobul/aoc2022/";

	/** name for logging file - logs time and results */
	public static final String LOG_FILE_NAME = FN_PREFIX + "log.txt";

    protected String getDefaultInputFileName() {
		return FN_PREFIX + getTaskNo() + "/input.txt";
	}

    protected String getDefaultTestFileName() {
		return FN_PREFIX + getTaskNo() + "/test.txt";
	}

	protected int getTaskNo() {
		return Integer.parseInt(getClass().getSimpleName().replace("Task", ""));
	}

    public static final Pattern INTEGER_PATTERN = Pattern.compile("-?\\d+");

    record Couple(Object key, Object value) {}
    
    /**
     * Returns string containing given count of character.
     * @param character filling character
     * @param count count character repeat
     * @return string containing <code>count</code> charachters <code>character</code>
     */
    public static String fill(final char character, final int count) {
        if (count > 0) {
            StringBuilder result = new StringBuilder(count);
            for (int i = 0; i < count; i++) {
                result.append(character);
            }
            return result.toString();
        }
        return "";
    }
    
    /**
     * Makes left pad (before string) by specified character to given length.
     * @param string input string
     * @param positions required length
     * @param padChar char to create pad
     * @return input string with left pad and required length
     */
    public static String lpad(final String string, final int positions, final char padChar) {
        if (positions > 0) {
            return lpad(string, positions, padChar, new StringBuilder(positions)).toString();
        }
        return string;
    }

    /**
     * Doplni <code>string</code> zlava znakom <code>padChar</code> na definovany pocet znakov <code>positions<code>
     * @param string retazec tory chceme zlava doplnit
     * @param positions maximalna pozadovana dlzka
     * @param padChar znak ktorym doplnujeme
     * @return builder ktory prisiel do metody ako argument <code>sb</code>
     */
    public static StringBuilder lpad(final String string, final int positions, final char padChar, StringBuilder sb) {
        final String uString = string == null ? "" : string;
        if (positions > 0) {
            for (int i = 0; i < (positions - uString.length()); i++) {
                sb.append(padChar);
            }
        }
        sb.append(uString);
        return sb;
    }

    <T extends Comparable<T>> T min(Stream<T> numbers) {
    	return numbers.min(Comparator.naturalOrder()).orElse(null);
    }
    
    <T extends Comparable<T>> T max(Stream<T> numbers) {
    	return numbers.max(Comparator.naturalOrder()).orElse(null);
    }
    
    int min(int ... numbers) {
    	return min(Arrays.stream(numbers).boxed());
    }

    int max(int ... numbers) {
    	return max(Arrays.stream(numbers).boxed());
    }
    
    long min(long ... numbers) {
    	return min(Arrays.stream(numbers).boxed());
    }

    long max(long ... numbers) {
    	return max(Arrays.stream(numbers).boxed());
    }
    

    /**
     * Makes right pad (after string) by specified character to given length.
     * @param string input string
     * @param positions required length
     * @param padChar char to create pad
     * @return input string with right pad and required length
     */
    public static String rpad(final String string, final int positions, final char padChar) {
        if (positions > 0) {
            final String uString = string == null ? "" : string;
            StringBuilder result = new StringBuilder(positions);
            result.append(uString);
            for (int i = 0; i < (positions - uString.length()); i++) {
                result.append(padChar);
            }
            return result.toString();
        }
        return string;
    }

    
    /**
     * @param values libovolne mnozstvi objektu
     * @return prvni z objektu, ktery neni null
     */
    @SafeVarargs
    public static <T> T nvl(final T... values) {
        // noinspection Convert2streamapi
        for (final T value : values) {
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    /**
     * @param values libovolne mnozstvi intu
     * @return prvni z objektu, ktery je nenulovy
     */
    public static int nvlZero(final int... values) {
        // noinspection Convert2streamapi
        for (final int value : values) {
            if (value != 0) {
                return value;
            }
        }
        return 0;
    }
    
    public static Long strToLong(String input) {
        return input.trim().length() > 0 ? Long.valueOf(input.trim()) : null;
    }

    public static final Long sumLong(Long l1, Long l2) {
        return nvl(l1, Long.valueOf(0)) + nvl(l2, Long.valueOf(0));
    }

    public static Stream<Long> groupBySummingLongs(Stream<Long> input) {
        AtomicLong counter = new AtomicLong();

        return
                input.collect(
                        Collectors.groupingBy(x -> x == null ? counter.incrementAndGet() : counter.get(), Collectors.reducing(Aoc::sumLong)))
                .values().stream().map(Optional::get);
    }

    public static Stream<String> getStringStream(String fileName) throws IOException {
        return Files.lines(Paths.get(fileName));
    }

    public static Stream<Long> getLongStream(String fileName) throws IOException {
        return Files.lines(Paths.get(fileName)).map(Aoc::strToLong);
    }

    public void logResult(int part, Object result) {
        if (result != null && result.toString().length() > 0) {
            log("[P" + part + "] " + result);
        }
    }

    /** formatter for logging */
    DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss,SSS");
    
    public void log(String message) {
    	LocalDateTime dt = LocalDateTime.now();
        String full = "[" + getClass().getSimpleName() + "] " + DTF.format(dt) + " " + message;
        System.out.println(message);
        try (FileWriter fw = new FileWriter(LOG_FILE_NAME, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
            out.println(full);
        } catch (IOException e) {
            System.err.println("Unable to write to log file " + e.getMessage());
        }
    }

    public int[][] readFileToIntIntArray(String fileName) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        int cnt = lines.get(0).length();

        int[][] result = new int[lines.size()][cnt];
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            for (int col = 0; col < line.length(); col++) {
                result[row][col] = (line.charAt(col) - 48);
            }
        }
        return result;
    }

    public List<String> readFileToListString(String fileName) throws IOException {
        return Files.readAllLines(Paths.get(fileName));
    }

    public static void printIntIntArray(int[][] array) {
        for (int r = 0; r < array.length; r++) {
            for (int c = 0; c < array[0].length; c++) {
                System.out.print(array[r][c] + " ");
            }
            System.out.println();
        }
    }

    public static void printCharCharArray(char[][] array) {
        printCharCharArray(array, 0, 0, array.length, array[0].length);
    }

    public static void printCharCharArray(char[][] array, int fromRow, int fromCol) {
        printCharCharArray(array, fromRow, fromCol, array.length, array[0].length);
    }

    public static void printCharCharArray(char[][] array, int fromRow, int fromCol, int toRow, int toCol) {
        for (int r = fromRow; r < toRow; r++) {
            StringBuilder row = new StringBuilder();
            for (int c = fromCol; c < toCol; c++) {
                row.append(array[r][c]);
            }
            System.out.println(row);
        }
    }


    public static String charCharArrayToString(char[][] array) {
        return charCharArrayToString(array, 0, 0, array.length, array[0].length);
    }

    public static String charCharArrayToString(char[][] array, int fromRow, int fromCol, int toRow, int toCol) {
        StringBuilder result = new StringBuilder();
        for (int r = fromRow; r < toRow; r++) {
            for (int c = fromCol; c < toCol; c++) {
                result.append(array[r][c]);
            }
            result.append("\n");
        }
        return result.toString();
    }



    public static void fill(char[][] array, char ch) {
        for (int r = 0; r < array.length; r++) {
            Arrays.fill(array[r], ch);
        }
    }

    public static List<Integer> getIntegersFromString(String row) {
        List<Integer> result = new ArrayList<>();

        Matcher matcher = INTEGER_PATTERN.matcher(row);

        while (matcher.find()) {
            result.add(Integer.valueOf(matcher.group()));
        }

        return result;
    }

    public static List<Long> getLongsFromString(String row) {
        List<Long> result = new ArrayList<>();

        Matcher matcher = INTEGER_PATTERN.matcher(row);

        while (matcher.find()) {
            result.add(Long.valueOf(matcher.group()));
        }

        return result;
    }

    public class Graph<T extends Object> {

        private Set<Node<T>> nodes = new HashSet<>();

        public void addNode(Node<T> nodeA) {
            nodes.add(nodeA);
        }

        public <T extends Object> void calculateShortestPathFromSource(Node<T> source) {
            source.setDistance(0);

            Set<Node<T>> settledNodes = new HashSet<>();
            Set<Node<T>> unsettledNodes = new HashSet<>();

            unsettledNodes.add(source);

            while (unsettledNodes.size() != 0) {
                Node<T> currentNode = getLowestDistanceNode(unsettledNodes);
                unsettledNodes.remove(currentNode);
                for (Entry<Node<T>, Integer> adjacencyPair:
                  currentNode.getAdjacentNodes().entrySet()) {
                    Node<T> adjacentNode = adjacencyPair.getKey();
                    Integer edgeWeight = adjacencyPair.getValue();
                    if (!settledNodes.contains(adjacentNode)) {
                        calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                        unsettledNodes.add(adjacentNode);
                    }
                }
                settledNodes.add(currentNode);
            }
        }
        
        public Set<Node<T>> getNodes() {
			return nodes;
		}
        
        public void setNodes(Set<Node<T>> nodes) {
			this.nodes = nodes;
		}

    }

    public class Node<T> {

        private T val;

        private List<Node<T>> shortestPath = new LinkedList<>();

        private Integer distance = Integer.MAX_VALUE;

        Map<Node<T>, Integer> adjacentNodes = new HashMap<>();

        public void addDestination(Node<T> destination, int distance) {
            adjacentNodes.put(destination, distance);
        }

        public Map<Node<T>, Integer> getAdjacentNodes() {
			return adjacentNodes;
		}

		public Node(T val) {
            this.val = val;
        }

		public Integer getDistance() {
			return distance;
		}

		public void setDistance(Integer distance) {
			this.distance = distance;
		}

		public List<Node<T>> getShortestPath() {
			return shortestPath;
		}
		
		public void setShortestPath(List<Node<T>> shortestPath) {
			this.shortestPath = shortestPath;
		}
		
		public T getVal() {
			return val;
		}
		
    }

    private static <T extends Object> void calculateMinimumDistance(Node<T> evaluationNode,
            Integer edgeWeigh, Node<T> sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Node<T>> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

    private static <T extends Object> Node<T> getLowestDistanceNode(Set < Node<T> > unsettledNodes) {
        Node<T> lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Node<T> node: unsettledNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    public static boolean in(int i, int from, int to) {
        return i >= from && i <= to;
    }

    record Pos(int row, int col) {
        public static Pos ofColRow(String pos) {
            String[] posS = pos.split(",");
            return new Pos(Integer.parseInt(posS[1]), Integer.parseInt(posS[0]));
        }
        public static Pos ofRowCol(String pos) {
            String[] posS = pos.split(",");
            return new Pos(Integer.parseInt(posS[0]), Integer.parseInt(posS[1]));
        }

        Pos up() { return new Pos(row-1, col); }
        Pos upLeft() { return new Pos(row-1, col-1); }
        Pos upRight() { return new Pos(row-1, col+1); }
        Pos down() { return new Pos(row+1, col); }
        Pos downLeft() { return new Pos(row +1, col -1); }
        Pos left() { return new Pos(row, col-1); }
        Pos right() { return new Pos(row, col+1); }
        Pos downRight() { return new Pos(row +1, col +1); }
        Pos move(Pos offset) { return new Pos(row + offset.row, col + offset.col); }
        Pos move(Dir dir) {
            return switch (dir) {
            case RIGHT -> right();
            case DOWN -> down();
            case LEFT -> left();
            case UP -> up();
            };
        }
        Pos move(DirFull dir) {
            return switch (dir) {
            case RIGHT -> right();
            case DOWN -> down();
            case LEFT -> left();
            case UP -> up();
            case DOWN_LEFT -> downLeft();
            case DOWN_RIGHT -> downRight();
            case UP_LEFT -> upLeft();
            case UP_RIGHT -> upRight();
            };
        }
        Pos inv() { return new Pos(-row, -col); }
        int mdist(Pos pos2) { return Math.abs(row - pos2.row) + Math.abs(col - pos2.col); }
        public boolean touch(Pos pos2) {
            return in(pos2.col, col-1, col+1) && in(pos2.row, row-1, row+1);
        }
        public Pos moveToTouch(Pos pos) {
            if (pos.col == col) {
                return (pos.row > row) ? new Pos(row + 1, col) : new Pos(row - 1, col);
            } else if (pos.row == row) {
                return (pos.col > col) ? new Pos(row, col + 1) : new Pos(row, col - 1);
            } else {
                if (pos.col > col) {
                    return (pos.row > row) ? new Pos(row + 1, col + 1) : new Pos(row - 1, col + 1);
                } else {
                    return (pos.row > row) ? new Pos(row + 1, col - 1) : new Pos(row - 1, col - 1);
                }
            }
        }
        
    }

    enum Rot {
        L, R;
    }

    enum Dir {
        RIGHT,
        DOWN,
        LEFT,
        UP;

        public Dir rot(Rot rot) {
            int newO = (ordinal() + (rot == Rot.R ? 1 : -1)) % 4;
            if (newO == -1) {
                newO = Dir.values().length -1;
            }
            return Dir.values()[newO];
        }
        
        public static Dir of(String nameSubStr) {
        	for (Dir d : Dir.values()) {
        		if (d.name().startsWith(nameSubStr)) {
        			return d;
        		}
        	}
        	return null;
        }
    }

    enum DirFull {
        RIGHT,
        DOWN,
        LEFT,
        UP,
        UP_LEFT,
        UP_RIGHT,
        DOWN_LEFT,
        DOWN_RIGHT;
    }


    static class Map2d {

        char[][] map;

        static final char VOID = ' ';

        Pos ref = new Pos(0, 0);

        public Map2d() {
            map = new char[1][1];
        }

        public Map2d(int rows, int cols) {
            map = new char[rows][cols];
            fill(map, VOID);
        }

        public Map2d(Pos defaultRef) {
            map = new char[1][1];
            ref = defaultRef;
        }


        public char get(Pos _pos) {
            if (_pos.row < minRow() || _pos.row >= maxRowPP() || _pos.col < minCol() || _pos.col >= maxColPP()) {
                return VOID;
            }
            Pos pos = cnv(_pos);
            return map[pos.row][pos.col];
        }

        /**
         * @return maximalni hodnotu radku + 1
         */
        public int maxRowPP() {
            return rows() + ref.row;
        }

        /**
         * @return maximalni hodnotu sloupce + 1
         */
        public int maxColPP() {
            return cols() + ref.col;
        }

        public int rows() {
            return map.length;
        }

        public int cols() {
            return map.length == 0 ? 0 : map[0].length;
        }

        public int minRow() {
            return ref.row;
        }

        public int minCol() {
            return ref.col;
        }

        public void addSpaceLeftTop(int rows, int cols) {
            if (rows > 0 || cols > 0) {
                char[][] newMap = new char[rows() + rows][cols() + cols];
                int row = 0;
                while (row < rows) {
                    // pridam radky nahoru
                    Arrays.fill(newMap[row], VOID);
                    row++;
                }
                // kopiruju radky puvodniho pole do noveho
                while (row < (newMap.length)) {
                    if (cols > 0) {
                        Arrays.fill(newMap[row], 0, cols, VOID);
                    }
                    System.arraycopy(map[row-rows], 0, newMap[row], cols, map[row-rows].length);
                    row++;
                }
                map = newMap;
            }
        }

        public void addSpaceRightBottom(int rows, int cols) {
            if (rows > 0 || cols > 0) {
                char[][] newMap = new char[rows() + rows][cols() + cols];
                int row = 0;
                // kopiruju radky puvodniho pole do noveho
                while (row < (map.length)) {
                    if (cols > 0) {
                        Arrays.fill(newMap[row], cols(), cols() + cols, VOID);
                    }
                    System.arraycopy(map[row], 0, newMap[row], 0, map[row].length);
                    row++;
                }
                while (row < (map.length + rows)) {
                    // pridam radky dolu
                    Arrays.fill(newMap[row], VOID);
                    row++;
                }
                map = newMap;
            }
        }

        /**
         * Pozsiri velikost interni mapy tak, aby se tam dala umistit dana pozice
         * Pripadne posune referencni bod
         */
        public void ensurePos(Pos pos) {
            if (pos.row < minRow() || pos.col < minCol()) {
                // pridavam doleva nahoru
                Pos moveRef = new Pos(Math.min(0, pos.row-minRow()), Math.min(0, pos.col-minCol()));
                ref = ref.move(moveRef);
                addSpaceLeftTop(Math.max(0, Math.abs(moveRef.row)), Math.max(0,  Math.abs(moveRef.col)));
            }
            if (pos.row > maxRowPP() || pos.col > maxColPP()) {
                // pridavam doprava dolu
                addSpaceRightBottom(Math.max(0, (pos.row + 1) - maxRowPP()), Math.max(0, (pos.col + 1) - maxColPP()));
            }
        }

        public boolean isVoid(Pos pos) {
            return get(pos) == VOID;
        }

        public void line(Pos _from, Pos _to, char filler) {
            ensurePos(_from);
            ensurePos(_to);
            Pos from = cnv(_from);
            Pos to = cnv(_to);
            if (from.row == to.row) {
                if (from.col < to.col) {
                    for (int col = from.col; col <= to.col; col++) {
                        map[from.row][col] = filler;
                    }
                } else {
                    for (int col = to.col; col <= from.col; col++) {
                        map[from.row][col] = filler;
                    }
                }
            } else if (from.col == to.col) {
                if (from.row < to.row) {
                    for (int row = from.row; row <= to.row; row++) {
                        map[row][from.col] = filler;
                    }
                } else {
                    for (int row = to.row; row <= from.row; row++) {
                        map[row][from.col] = filler;
                    }
                }
            } else {
                throw new IllegalArgumentException("Not straight row " + from + " -> " + to);
            }
        }

        public void drawSprite(Pos _pos, char[][] ch) {
            if (ch == null || ch.length == 0) {
                return;
            }
            ensurePos(_pos);
            ensurePos(_pos.move(new Pos(ch.length, ch[0].length)));
            Pos pos = cnv(_pos);
            for (int r = 0; r < ch.length; r ++) {
                for (int c = 0; c < ch[0].length; c++) {
                    if (ch[r][c] != VOID) {
                        map[pos.row + r][pos.col + c] = ch[r][c];
                    }
                }
            }
        }

        public void clearSprite(Pos _pos, char[][] ch) {
            if (ch == null || ch.length == 0) {
                return;
            }
            ensurePos(_pos);
            ensurePos(_pos.move(new Pos(ch.length, ch[0].length)));
            Pos pos = cnv(_pos);
            for (int r = 0; r < ch.length; r ++) {
                for (int c = 0; c < ch[0].length; c++) {
                    if (ch[r][c] != VOID) {
                        map[pos.row + r][pos.col + c] = VOID;
                    }
                }
            }
        }


        public boolean conflictSprite(Pos _pos, char[][] ch) {
            if (ch == null || ch.length == 0) {
                return false;
            }
            ensurePos(_pos);
            ensurePos(_pos.move(new Pos(ch.length, ch[0].length)));
            Pos pos = cnv(_pos);
            for (int r = 0; r < ch.length; r ++) {
                for (int c = 0; c < ch[0].length; c++) {
                    if (ch[r][c] != VOID && map[pos.row + r][pos.col + c] != VOID) {
                        return true;
                    }
                }
            }
            return false;
        }


        public void point(Pos _pos, char ch) {
            ensurePos(_pos);
            Pos pos = cnv(_pos);
            map[pos.row][pos.col] = ch;
        }

        private Pos cnv(Pos from) {
            return from.move(ref.inv());
        }

        public void print() {
            int padPos = (int) Math.log10(Math.max(Math.abs(minRow()), Math.abs(maxRowPP()))) + 1;
            int vPosLines = (int) Math.log10(Math.max(Math.abs(minCol()), Math.abs(maxColPP()))) + 1;
            StringBuilder[] cRows = new StringBuilder[vPosLines];
            for (int i =0; i < cRows.length; i++) {
                cRows[i] = new StringBuilder(cols()).append(fill(' ', padPos + 1));
            }
            for (int i = 0; i < cols(); i++) {
                String val = lpad(Integer.toString(i + ref.col), vPosLines, ' ');
                for (int j = 0; j < val.length(); j++) {
                    cRows[j].append(val.charAt(j));
                }
            }
            for (int i =0; i < cRows.length; i++) {
                System.out.println(cRows[i]);
            }
            System.out.println(fill('-', cols() + padPos + 1));
            for (int r = 0; r < rows(); r++) {
                StringBuilder row = new StringBuilder();
                row.append(lpad(Integer.toString(r + ref.row), padPos, ' '));
                row.append('|');
                for (int c = 0; c < cols(); c++) {
                    row.append(map[r][c]);
                }
                System.out.println(row);
            }
        }

    }

    /**
     * Nejmensi spolecny nasobek cisel "a" a "b"
     * @param a cislo "a"
     * @param b cislo "b"
     * @return lcm(a, b)
     * @autor Thomas (www.adamjak.net)
     */
    public static long lcm(long a, long b)
    {
        if (a == 0 || b == 0)
        {
            return 0;
        }
        return (a * b) / gcd(a, b);
    }

    /**
     * Nejvetsi spolecny delitel cisel "a" a "b"
     * @param a cislo "a"
     * @param b cislo "b"
     * @return gcd(a, b)
     * @autor Thomas (www.adamjak.net)
     */
    public static long gcd(long a, long b)
    {
        if (a < 1 || b < 1)
        {
            throw new IllegalArgumentException("a or b is less than 1");
        }
        long r = 0;
        do
        {
            r = a % b;
            a = b;
            b = r;
        } while (b != 0);
        return a;
    }

    /**
     * Returns time in milliseconds that passed since given timestamp,
     * written in string format: 2d:3h:1m:0s:100ms.
     * <br/>
     * Vraci dobu trvani od "time" do ted, zapsanou jako napr. 2d:3h:1m:0s:100ms.
     * <br/>
     * Typicke pouziti:
     * <br/>
     *      final long time = currentTimestampProvider.curentTimeMillis(); <br/>
     *      // --- nejaky kod, u ktereho chceme merit, jak dlouho bezi <br/>
     *      debugLogger("Operation took: " + AdvancedCalendar.msTimeToStringSince(time));
     *
     * @param time pocatecni cas, jako timestamp v ms
     * @return Stringova reprezentace doby trvani od "time" do ted.
     */
    public static String msTimeToStringSince(final long time) {
        return msTimeToString(System.currentTimeMillis() - time, true);
    }

    /**
     * Vraci dobu trvani od "time" do ted, zapsanou jako napr. 2d:3h:1m:0s:100,xxxxxms.
     * Typicke pouziti:
     *      final long time = System.nanoTime();
     *      // --- nejaky kod, u ktereho chceme merit, jak dlouho bezi
     *      debugLogger("Operation took: " + AdvancedCalendar.nanoTimeToStringSince(time));
     * @param nanos pocatecni cas, jako timestamp v nanos
     * @return Stringova reprezentace doby trvani od "time" do ted.
     */
    public static String nanoTimeToStringSince(final long nanos) {
        return nanoTimeToString(System.nanoTime() - nanos);
    }

    /**
     * Returns time in milliseconds written in string format: 2d:3h:1m:0s:100ms.
     */
    public static String msTimeToString(final long time) {
        return msTimeToString(time, true);
    }

    /**
     * Returns time in milliseconds written in string format: 2d:3h:1m:0s:100ms.
     *
     * @param time time in milliseconds
     * @param miliseconds true if print milliseconds in return string
     */
    public static String msTimeToString(long time, final boolean miliseconds) {
        return msTimeToString(time, true, miliseconds);
    }

    /**
     * Returns time in milliseconds written in string format: 2d:3h:1m:0s:100ms.
     *
     * @param time time in milliseconds
     * @param seconds true, if print seconds in return string
     * @param miliseconds true if print milliseconds in return string
     */
    public static String msTimeToString(long time, final boolean seconds, final boolean miliseconds) {
        final StringBuilder result = new StringBuilder(20);
        if (miliseconds) {
            result.append(time % 1000).append("ms");
        }
        if ((time /= 1000) > 0) {
            if (seconds) {
                result.insert(0, miliseconds ? ":" : "").insert(0, "s").insert(0, time % 60);
            }
            if ((time /= 60) > 0) {
                result.insert(0, seconds ? ":" : "").insert(0, "m").insert(0, time % 60);
                if ((time /= 60) > 0) {
                    result.insert(0, "h:").insert(0, time % 24);
                    if ((time /= 24) > 0) {
                        result.insert(0, "d:").insert(0, time);
                    }
                }
            }
        }
        return result.toString();
    }
    
    /** pocet dni v tydnu */
    public static final int DAYS_IN_WEEK = 7;
    /** pocet hodin ve dni */
    public static final int HOURS_IN_DAY = 24;
    /** pocet dni v roce */
    public static final int DAYS_IN_YEAR = 365;
    /** pocet dni v prestupnem roce */
    public static final int DAYS_IN_LEAP_YEAR = 366;
    /** pocet vterin v minute */
    public static final int SECONDS_IN_MINUTE = 60;
    /** pocet milisekund ve vterine */
    public static final int MS_IN_SECOND = 1000;
    /** pocet nanosekund v milisekunde */
    public static final int NANOS_IN_MS = 1000000;
    /** pocet nanosekund v sekunde */
    public static final int NANOS_IN_SECOND = NANOS_IN_MS * MS_IN_SECOND;
    /** pocet minut v hodine */
    public static final int MINUTES_IN_HOUR = SECONDS_IN_MINUTE;
    /** pocet minut ve dni */
    public static final int MINUTES_IN_DAY = MINUTES_IN_HOUR * HOURS_IN_DAY;
    /** pocet vterin v hodine */
    public static final int SECONDS_IN_HOUR = MINUTES_IN_HOUR * SECONDS_IN_MINUTE;
    /** pocet vterin ve dni */
    public static final int SECONDS_IN_DAY = HOURS_IN_DAY * SECONDS_IN_HOUR;
    /** pocet vterin v tydnu */
    public static final int SECONDS_IN_WEEK = SECONDS_IN_DAY * DAYS_IN_WEEK;

    /** Pocet milisekund ve dni */
    public static final int MS_IN_DAY = SECONDS_IN_DAY * MS_IN_SECOND;

    
    /**
     * Convert {@code number} to float and shift decimal point for number of position given by {@code positions}.
     * Example: formatIntAsDouble(12345, 2) returns "123.45"
     *
     * @param number
     * @param positions number of positions
     * @return string representation if float
     */
    public static String formatIntAsDouble(final long number, final int positions) {
        final long number0 = Math.abs(number);
        final int factor = (int) Math.pow(10, positions);
        return (number < 0 ? "-" : "") + (number0 / factor) + (positions > 0 ? '.' + String.valueOf(factor + (number0 % factor)).substring(1) : "");
    }

    /**
     * Returns time in nanoseconds written in string format: 2d:3h:1m:0s:100,xxxxxxms.
     */
    public static String nanoTimeToString(final long nanoTime) {
        if (nanoTime == 0) {
            return "0ms";
        }
        long msTime = nanoTime / NANOS_IN_MS;
        long nanos = nanoTime % NANOS_IN_MS;
        String result = msTimeToString(msTime, false);
        int positions = msTime == 0 ? nanos < 100 ? 6 : nanos < 1000 ? 5 : nanos < 10000 ? 4 : 3 : msTime < 10 ? 2 : msTime < 100 ? 1 : 0;
        long msAndNanos = (nanoTime % NANOS_IN_SECOND) / (long) Math.pow(10, 6.0 - positions);
        return result + formatIntAsDouble(msAndNanos, positions) + "ms";
    }

    public <T> Collection<List<T>> partition(Collection<T> collection, int chunkSize) {
        final AtomicInteger index = new AtomicInteger();
        return collection.stream()
            .map(v -> new SimpleImmutableEntry<>(index.getAndIncrement() / chunkSize, v))
            // LinkedHashMap is used here just to preserve order
            .collect(Collectors.groupingBy(Entry::getKey, LinkedHashMap::new, Collectors.mapping(Entry::getValue, Collectors.toList())))
            .values();
     }

    public abstract void solve() throws Exception;
    
    public void run() throws Exception {
        long nanos = System.nanoTime();
        solve();
        System.out.println(nanoTimeToStringSince(nanos));
    }
}
