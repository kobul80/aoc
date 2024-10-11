package cz.kobul.aoc2020;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
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
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cz.kobul.aoc2020.Aoc2020.Pos;

/**
 * Support class for AOC problem solving
 */
public abstract class Aoc2020 {
	
	public static final String FN_PREFIX = "res/cz/kobul/aoc2020/";

	/** name for logging file - logs time and results */
	public static final String LOG_FILE_NAME = FN_PREFIX + "log.txt";

    protected String getDefaultInputFileName() {
		return FN_PREFIX + getTaskNo() + "/input.txt";
	}

    protected String getDefaultTestFileName() {
		return FN_PREFIX + getTaskNo() + "/test.txt";
	}

	protected int getTaskNo() {
		return toIntegerList(getClass().getSimpleName()).get(0);
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
    
    protected static List<Integer> range(int from, int to) {
        List<Integer> result = new ArrayList<>();
        if (from < to) {
            for (int i = from; i <= to; i++) {
                result.add(i);
            }
        } else {
            for (int i = from; i >= to; i--) {
                result.add(i);
            }            
        }
        return result;
    }
    
    protected static <T extends Object> ArrayList<T> add(List<T> l, T t) {
        ArrayList<T> result = new ArrayList<>(l.size() + 1);
        result.addAll(l);
        result.add(t);
        return result;
    }

    protected static <T extends Object> LinkedList<T> add(LinkedList<T> l, T t) {
        LinkedList<T> result = new LinkedList<>();
        result.addAll(l);
        result.add(t);
        return result;
    }


    protected static <T extends Object> LinkedHashSet<T> add(Set<T> s, T t) {
        LinkedHashSet<T> result = new LinkedHashSet<>(s.size() + 1);
        result.addAll(s);
        result.add(t);
        return result;
    }

    protected static <T extends Object> LinkedHashSet<T> add(Set<T> s1, Set<T> s2) {
        LinkedHashSet<T> result = new LinkedHashSet<>(s1.size() + s2.size());
        result.addAll(s1);
        result.addAll(s2);
        return result;
    }


    protected static <T extends Object> ArrayList<T> remove(List<T> l1, T t) {
        return removeAll(l1, List.of(t));
    }

    protected static <T extends Object> ArrayList<T> removeFirst(List<T> l1) {
        ArrayList<T> result = new ArrayList<T>();
        for (int i = 1; i < l1.size(); i++) {
            result.add(l1.get(i));
        }
        return result;
    }
    
    protected static <T extends Object> LinkedHashSet<T> remove(Set<T> l1, T t) {
        return removeAll(l1, Set.of(t));
    }

    protected static <T extends Object> ArrayList<T> removeAll(List<T> l1, Collection<T> l2) {
        LinkedHashSet<T> r = new LinkedHashSet<>(l1);
        r.removeAll(l2);
        return new ArrayList<>(r);
    }

    protected static <T extends Object> LinkedHashSet<T> removeAll(Set<T> l1, Collection<T> l2) {
        LinkedHashSet<T> r = new LinkedHashSet<>(l1);
        r.removeAll(l2);
        return r;
    }

    protected static <T extends Object> ArrayList<T> retainAll(List<T> l1, Collection<T> l2) {
        LinkedHashSet<T> r = new LinkedHashSet<>(l1);
        r.retainAll(l2);
        return new ArrayList<>(r);
    }

    protected static <T extends Object> LinkedHashSet<T> retainAll(Set<T> l1, Collection<T> l2) {
        LinkedHashSet<T> r = new LinkedHashSet<>(l1);
        r.retainAll(l2);
        return r;
    }


    protected static <T extends Comparable<T>> T min(Stream<T> numbers) {
    	return numbers.min(Comparator.naturalOrder()).orElse(null);
    }
    
    protected static <T extends Comparable<T>> T max(Stream<T> numbers) {
    	return numbers.max(Comparator.naturalOrder()).orElse(null);
    }
    
    protected static int min(int ... numbers) {
        int min = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            int no = numbers[i];
            if (no < min) {
                min = no; 
            }
        }
        return min;
    }

    protected static int max(int ... numbers) {
    	int max = numbers[0];
    	for (int i = 1; i < numbers.length; i++) {
    	    int no = numbers[i];
    	    if (no > max) {
    	        max = no; 
    	    }
    	}
    	return max;
    }
    
    protected static long min(long ... numbers) {
    	return min(Arrays.stream(numbers).boxed());
    }
    
    /**  
     * @return pozici, ktera odpovida nejnizsim souradnicim vsech uvedenych pozic (pos(min(row),min(col))
     * POZOR! nemusi vracet zadnou z uvedenych pozic, jedna se jakoby o 'levy horni roh' obdelniku, do ktereho padnou vsechny pozice pos
     */
    protected static Pos min(Pos ... pos) {
        int minRow = pos[0].row();
        int minCol = pos[0].col();
        for (int i = 1; i < pos.length; i++) {
            minRow = min(minRow, pos[i].row());
            minCol = min(minCol, pos[i].col());
        }
        return new Pos(minRow, minCol);
    }

    /**  
     * @return pozici, ktera odpovida nejvyssim souradnicim vsech uvedenych pozic (pos(min(row),min(col))
     * POZOR! nemusi vracet zadnou z uvedenych pozic, jedna se jakoby o 'pravy dolni roh' obdelniku, do ktereho padnou vsechny pozice pos
     */
    protected static Pos max(Pos ... pos) {
        int maxRow = pos[0].row();
        int maxCol = pos[0].col();
        for (int i = 1; i < pos.length; i++) {
            maxRow = max(maxRow, pos[i].row());
            maxCol = max(maxCol, pos[i].col());
        }
        return new Pos(maxRow, maxCol);
    }
    
    protected static long max(long ... numbers) {
    	return max(Arrays.stream(numbers).boxed());
    }
    
    protected static <T extends Comparable<T>> T min(Collection<T> numbers) {
        return min(numbers.stream());
    }
    
    protected static String lpad(long number, int positions) {
        return lpad(Long.toString(number), positions, ' ');
    }

    protected static String rpad(long number, int positions) {
        return rpad(Long.toString(number), positions, ' ');
    }

    protected static String lpad(Object o, int positions) {
        return lpad(o.toString(), positions, ' ');
    }

    protected static String rpad(Object o, int positions) {
        return rpad(o.toString(), positions, ' ');
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
                        Collectors.groupingBy(x -> x == null ? counter.incrementAndGet() : counter.get(), Collectors.reducing(Aoc2020::sumLong)))
                .values().stream().map(Optional::get);
    }

    public static Stream<String> getStringStream(String fileName) throws IOException {
        return Files.lines(Paths.get(fileName));
    }

    public static Stream<Long> getLongStream(String fileName) throws IOException {
        return Files.lines(Paths.get(fileName)).map(Aoc2020::strToLong);
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

    public static List<Integer> toIntegerList(String row) {
        List<Integer> result = new ArrayList<>();

        Matcher matcher = INTEGER_PATTERN.matcher(row);

        while (matcher.find()) {
            result.add(Integer.valueOf(matcher.group()));
        }

        return result;
    }

    public static int[] toIntArray(List<Integer> integers) {
        return integers.stream().mapToInt(Integer::intValue).toArray();
    }

    public static long[] toLongArray(List<Long> longs) {
        return longs.stream().mapToLong(Long::longValue).toArray();
    }
    
    public static int[] toIntArray(String row) {
        return toIntArray(toIntegerList(row));
    }

    public static long[] toLongArray(String row) {
        return toLongArray(toLongList(row));
    }


    public static List<Integer> getDigitsFromString(String row) {
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < row.length(); i++) {
            if (Character.isDigit(row.charAt(i))) {
                result.add(((int) row.charAt(i)) - 48);
            }
        }
        return result;
    }
    
    public static List<Long> toLongList(String row) {
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

    public static boolean inInterval(int i, int from, int to) {
        return i >= from && i <= to;
    }

    public static boolean inInterval(long i, long from, long to) {
        return i >= from && i <= to;
    }
    
    record Pos(int row, int col) {
        private static final Pos DEFAULT = new Pos(0, 0);
        public static Pos of() {
            return DEFAULT;
        }
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
        Pos up(int cnt) { return new Pos(row-cnt, col); }
        Pos down(int cnt) { return new Pos(row+cnt, col); }
        Pos left(int cnt) { return new Pos(row, col-cnt); }
        Pos right(int cnt) { return new Pos(row, col+cnt); }
        Pos move(Pos offset) { return new Pos(row + offset.row, col + offset.col); }
        Pos move(Dir dir) {
            return switch (dir) {
            case RIGHT -> right();
            case DOWN -> down();
            case LEFT -> left();
            case UP -> up();
            };
        }
        Pos move(Dir dir, int cnt) {
            return switch (dir) {
            case RIGHT -> right(cnt);
            case DOWN -> down(cnt);
            case LEFT -> left(cnt);
            case UP -> up(cnt);
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
        boolean in(int minRow, int minCol, int maxRow, int maxCol) {
            return row >= minRow && row <= maxRow && col >= minCol && col <= maxCol;
        }
        public boolean touch(Pos pos2) {
            return inInterval(pos2.col, col-1, col+1) && inInterval(pos2.row, row-1, row+1);
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
        public Pos rot(Rot r) {
            return switch (r) {
                case L -> new Pos(-col, row);
                case R -> new Pos(col, -row);
            };
        }
    }

    enum Dir3 {
        LEFT,
        RIGHT,
        UP,
        DOWN,
        FRONT,
        BACK;
    }
    
    /** x je doleva, doprava, y je vpred, vzad a z je nahoru, dolu */
    record Pos3(int x, int y, int z) {
        static Pos3 of(int[] v, int startIndex) {
            return new Pos3(v[startIndex], v[startIndex+1], v[startIndex+2]);
        }
        Pos3 up(int cnt) { return new Pos3(x, y, z+cnt); }
        Pos3 down(int cnt) { return new Pos3(x, y, z-cnt); }
        Pos3 left(int cnt) { return new Pos3(x-cnt,y,z); }
        Pos3 right(int cnt) { return new Pos3(x+cnt,y,z); }
        Pos3 front(int cnt) { return new Pos3(x, y-1, z); }
        Pos3 back(int cnt) { return new Pos3(x, y+1, z); }

        Pos3 up() { return up(1); }
        Pos3 down() { return down(1); }
        Pos3 left() { return left(1); }
        Pos3 right() { return right(1); }
        Pos3 front() { return front(1); }
        Pos3 back() { return back(1); }

        Pos3 move(Pos3 off) { return new Pos3(x + off.x, y + off.y, z + off.z); }
        Pos3 move(Dir3 dir) {
            return switch (dir) {
            case RIGHT -> right();
            case DOWN -> down();
            case LEFT -> left();
            case UP -> up();
            case FRONT -> front();
            case BACK -> back();
            };
        }        
    }

    record Cube(Pos3 p1, Pos3 p2) {
        Cube up(int cnt) { return new Cube(p1.up(cnt), p2.up(cnt)); }
        Cube down(int cnt) { return new Cube(p1.down(cnt), p2.down(cnt)); }
        Cube left(int cnt) { return new Cube(p1.left(cnt), p2.left(cnt)); }
        Cube right(int cnt) { return new Cube(p1.right(cnt), p2.right(cnt)); }
        Cube front(int cnt) { return new Cube(p1.front(cnt), p2.front(cnt)); }
        Cube back(int cnt) { return new Cube(p1.back(cnt), p2.back(cnt)); }
        Cube up() { return up(1); }
        Cube down() { return down(1); }
        Cube left() { return left(1); }
        Cube right() { return right(1); }
        Cube front() { return front(1); }
        Cube back() { return back(1); }
        Cube move(Pos3 off) { return new Cube(p1.move(off), p2.move(off)); }
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
        
        public Dir back() {
            return switch (this) {
            case RIGHT -> LEFT;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case UP -> DOWN;
            };
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

        public Map2d(char[][] map) {
            this.map = map;
        }

        public Map2d(char[][] map, Pos defaultRef) {
            this.map = map;
            this.ref = defaultRef;
        }


        public Map2d(Pos defaultRef) {
            map = new char[1][1];
            ref = defaultRef;
        }

        public Map2d(List<String> lines) {
            int rowCnt = (lines == null || lines.isEmpty()) ? 0 : lines.size();
            
            map = new char[lines.size()][rowCnt > 0 ? lines.get(0).length() : 0];
            for (int r = 0; r < rowCnt; r++) {
                for (int c = 0; c < lines.get(r).length(); c++) {
                    char ch = lines.get(r).charAt(c);
                    Pos pos = new Pos(r, c);
                    point(pos, ch);
                }
            }
        }

        public boolean isNotIn(Pos _pos) {
            return (_pos.row < minRow() || _pos.row >= maxRowPP() || _pos.col < minCol() || _pos.col >= maxColPP());
        }
        
        public boolean isIn(Pos _pos) {
            return !isNotIn(_pos);
        }

        
        public char get(Pos _pos) {
            if (isNotIn(_pos)) {
                return VOID;
            }
            Pos pos = cnv(_pos);
            return map[pos.row][pos.col];
        }

        public List<Character> get(Pos ... _pos) {
            List<Character> result = newList();
            for (Pos p : _pos) {
                result.add(get(p));
            }
            return result;
        }

        public List<Character> get(Collection<Pos> _pos) {
            return get(_pos.toArray(new Pos[0]));
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
        
        public String row(int _row) {
            return new String(map[cnvRow(_row)]);
        }
        
        public Set<Character> rowChars(int _row) {
            Set<Character> result = newSet();
            for (char ch : map[cnvRow(_row)]) {
                result.add(ch);
            }
            return result;
        }

        public String col(int _col) {
            StringBuilder result = new StringBuilder();
            int col = cnvCol(_col);
            for (int r = 0; r < rows(); r++) {
                result.append(map[r][col]);
            }
            return result.toString();
        }        

        public Set<Character> colChars(int _col) {
            Set<Character> result = newSet();
            int col = cnvCol(_col);
            for (int r = 0; r < rows(); r++) {
                result.add(map[r][col]);
            }
            return result;
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

        public void rect(Pos _s, Pos _e, char ch) {
            ensurePos(_s);
            ensurePos(_e);
            Pos s = cnv(_s);
            Pos e = cnv(_e);
            for (int r = s.row; r<= e.row; r++) {
                for (int c = s.col; c<= e.col; c++) {
                    map[r][c] = ch;
                }                
            }
        }

        public void points(Collection<Pos> _pos, char ch) {
            for (Pos p : _pos) {
                point(p, ch);
            }
        }

        private Pos cnv(Pos from) {
            return from.move(ref.inv());
        }

        private int cnvRow(int row) {
            return row + (ref.inv().row());
        }

        private int cnvCol(int col) {
            return col + (ref.inv().col());
        }


        /**
         * Vyplni prostor od pozice from znakem fillWith
         * @param from
         * @param fillWith
         */
        public void floodFill(Pos from, char fillWith) {
            char voidChar = get(from);
            point(from, fillWith);
            if (get(from.left()) == voidChar) {
                floodFill(from.left(), fillWith);
            }
            if (get(from.right()) == voidChar) {
                floodFill(from.right(), fillWith);
            }
            if (get(from.up()) == voidChar) {
                floodFill(from.up(), fillWith);
            }
            if (get(from.down()) == voidChar) {
                floodFill(from.down(), fillWith);
            }
        }
        
        /**
         * @param ch
         * @return pozice pocet znaku 'ch' v mape
         */
        public List<Pos> getPositions(char ch) {
            List<Pos> result = new ArrayList<>();
            for (int r = 0; r < rows(); r++) {
                for (int c = 0; c < cols(); c++) {
                    if (map[r][c] == ch) {
                        result.add(new Pos(r + ref.row, c + ref.col));
                    }
                }
            }
            return result;
        }

        /**
         * @param ch
         * @return pozice pocet znaku 'ch' v mape
         */
        public List<Pos> getPositions(char ... ch) {
            Set<Character> chars = newSet();
            for (char c : ch) {
                chars.add(c);
            }
            List<Pos> result = new ArrayList<>();
            for (int r = 0; r < rows(); r++) {
                for (int c = 0; c < cols(); c++) {
                    if (chars.contains(map[r][c])) {
                        result.add(new Pos(r + ref.row, c + ref.col));
                    }
                }
            }
            return result;
        }


        /**
         * @param ch
         * @return pocet znaku 'ch' v mape
         */
        public long count(char ch) {
            int sum = 0;
            for (int r = 0; r < rows(); r++) {
                for (int c = 0; c < cols(); c++) {
                    if (map[r][c] == ch) {
                        sum++;
                    }
                }
            }
            return sum;
        }

        public Map2d subMap(Pos start, int rows, int cols) {
            char[][] newMap = new char[rows][cols];
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    char orig = map[r + start.row][c + start.col];
                    newMap[r][c] = orig;                        
                }                
            }
            return new Map2d(newMap);            
        }
        
        public Map2d duplicate() {
            char[][] newMap = new char[rows()][];
            for (int r = 0; r < rows(); r++) {
                newMap[r] = Arrays.copyOf(map[r], cols());
            }
            return new Map2d(newMap, ref);
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

    public static long lcm(long ... a) {
        long result = a[0];
        for (int i = 1; i < a.length; i++) {
            result = lcm(result, a[i]);
        }
        return result;
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

    public static <T> Collection<List<T>> partition(Collection<T> collection, int chunkSize) {
        final AtomicInteger index = new AtomicInteger();
        return collection.stream()
            .map(v -> new SimpleImmutableEntry<>(index.getAndIncrement() / chunkSize, v))
            // LinkedHashMap is used here just to preserve order
            .collect(Collectors.groupingBy(Entry::getKey, LinkedHashMap::new, Collectors.mapping(Entry::getValue, Collectors.toList())))
            .values();
     }

    static record Cycle<T extends Number>(int startPos, List<T> cyclicNumbers) {}
    
    public static <T extends Number> Cycle<T> findMaxCycle(List<T> numbers) {
        int maxCycleLength = -1;
        int maxCycleStart = -1;
        for (int cl = 2; cl < numbers.size() / 2; cl++) {
            int cycleStart = findCycle(cl, numbers);
            if (cycleStart != -1) {
                maxCycleLength = cl;
                maxCycleStart = cycleStart;
            }
        }
        return maxCycleStart == -1 ? null : new Cycle<T>(maxCycleStart, List.copyOf(numbers.subList(maxCycleStart, maxCycleStart + maxCycleLength)));
    }
    
    private static int findCycle(int cl, List<? extends Number> numbers) {
        for (int sp = 0; sp < numbers.size() - (cl * 2); sp++) {
            // overuji, zda cyklus delky cl zacina na pozici sp
            if (testCycle(sp, cl, numbers)) {
                return sp;
            }
        }        
        return -1;
    }
    
    protected static <T extends Object> List<T> newList() {
        return new ArrayList<>();
    }

    protected static <T extends Object> LinkedList<T> newLinkedList() {
        return new LinkedList<>();
    }

    protected static <T extends Object> LinkedList<T> newLinkedList(T content) {
        return new LinkedList<>(List.of(content));
    }


    protected static <T extends Object, K extends Object> Map<T,K> newMap() {
        return new LinkedHashMap<T,K>();
    }
    
    protected static <T extends Object> Set<T> newSet() {
        return new LinkedHashSet<T>();
    }    

    // pocitam plochu polygonu
    // https://en.wikipedia.org/wiki/Shoelace_formula
    // https://en.wikipedia.org/wiki/Pick%27s_theorem
    /**
     * Spocita plochu polygonu, pokud points tvori spojity cyklus v diskretnim prostoru.
     * Pocitame s tim, ze cary jsou jen pod uhlem 90st (nahoru, dolu, vlevo, vpravo)
     * a ze pocatecni bod je stejny jako koncovy
     * @param points
     * @return
     */
    public static long polyArea(List<Pos> points) {
        long area = 0;
        long boundary = 0;
        
        for (int i = 1; i < points.size(); i++) {
            Pos p1 = points.get(i-1);
            Pos p2 = points.get(i);
            area += ((long)p1.row() * p2.col()) - ((long)p1.col() * p2.row());
            boundary += p1.mdist(p2);
        }
        return Math.abs(area) / 2 + (boundary / 2) + 1;        
    }

    
    private static boolean testCycle(int sp, int cl, List<? extends Number> numbers) {
        for (int i = 0; i < cl; i++) {
            int index = sp + i;
            Number actual = numbers.get(index); 
            while (index < numbers.size()) {
                index += cl;
                if (index < numbers.size() && numbers.get(index).doubleValue() != actual.doubleValue()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public abstract void solve() throws Exception;
    
    public void run() throws Exception {
        long nanos = System.nanoTime();
        solve();
        System.out.println(nanoTimeToStringSince(nanos));
    }
    
    public static void createTasks() throws Exception {
        String file = Files.readString(Path.of("src/cz/kobul/aoc2020/TaskEmpty.java"));
        for (int i = 1; i <= 25; i++) {
            Files.writeString(Path.of("src/cz/kobul/aoc2020/Task" + i + ".java"), file.replace("Empty", Integer.toString(i)));
        }
    }
        
}
