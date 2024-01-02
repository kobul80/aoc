package cz.kobul.aoc2022;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * https://adventofcode.com/2022/day/18
 * start 8:00 end 9:57
 */
public class Task18 extends Aoc2022 {

    enum Side {
        FRONT,
        BACK,
        TOP,
        BOTTOM,
        LEFT,
        RIGHT;
        public Side inv() {
            return switch (this) {
            case FRONT -> BACK;
            case BACK -> FRONT;
            case TOP -> BOTTOM;
            case BOTTOM -> TOP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            };
        }
    }

    record Cube(int x, int y, int z) {
        public static Cube of(String input) {
            String[] in = input.split(",");
            return new Cube(Integer.parseInt(in[0]), Integer.parseInt(in[1]), Integer.parseInt(in[2]));
        }

        Side connected(Cube oth) {
            if (oth.x == x && oth.y == y) {
                if (oth.z == z-1) {
                    return Side.FRONT;
                }
                if (oth.z == z+1) {
                    return Side.BACK;
                }
            } else if (oth.x == x && oth.z == z) {
                if (oth.y == y-1) {
                    return Side.TOP;
                }
                if (oth.y == y+1) {
                    return Side.BOTTOM;
                }
            } else if (oth.y == y && oth.z == z) {
                if (oth.x == x+1) {
                    return Side.RIGHT;
                }
                if (oth.x == x-1) {
                    return Side.LEFT;
                }
            }
            return null;
        }

        Cube move(Side s) {
            return switch (s) {
            case LEFT -> new Cube(x -1, y, z);
            case RIGHT -> new Cube(x +1, y, z);
            case TOP -> new Cube(x , y + 1, z);
            case BOTTOM -> new Cube(x , y -1, z);
            case FRONT -> new Cube(x, y, z - 1);
            case BACK -> new Cube(x, y, z + 1);
            };
        }

        boolean isConnected(Cube oth) {
            return connected(oth) != null;
        }

    }

    class CubeC {
        final Cube c;
        EnumSet<Side> connected = EnumSet.noneOf(Side.class);
        EnumSet<Side> outside = EnumSet.noneOf(Side.class);

        public CubeC(Cube c) {
            this.c = c;
        }

        public void check(CubeC oth) {
            Side conn2 = c.connected(oth.c);
            if (conn2 != null) {
                connected.add(conn2);
            }
        }

        public Cube c() {
            return c;
        }
    }

    record Bounds(int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
    	boolean out(Cube pos) {
    		return pos.x < minX || pos.x > maxX || pos.y < minY || pos.y > maxY || pos.z < minZ || pos.z > maxZ;
    	}
    	
    }

    Set<Cube> visited = new HashSet<>();

    void floodFill(Map<Cube, CubeC> cubes, Cube pos, Side from, Bounds bounds) {
        // jsem na hrane
        if (bounds.out(pos)) {
            return;
        }
        CubeC found = cubes.get(pos);
        if (found != null) {
            // nasel jsem kostku, obarvim stranu, odkud jsem prisel a koncim
            if (from != null) {
                found.outside.add(from);
            }
            return;
        }

        // tady uz jsem byl
        if (visited.contains(pos)) {
            return;
        }
        visited.add(pos);

        floodFill(cubes, pos.move(Side.TOP), Side.BOTTOM, bounds);
        floodFill(cubes, pos.move(Side.BOTTOM), Side.TOP, bounds);
        floodFill(cubes, pos.move(Side.LEFT), Side.RIGHT, bounds);
        floodFill(cubes, pos.move(Side.RIGHT), Side.LEFT, bounds);
        floodFill(cubes, pos.move(Side.FRONT), Side.BACK, bounds);
        floodFill(cubes, pos.move(Side.BACK), Side.FRONT, bounds);
    }

    protected Object solve1(List<CubeC> cubes) {
    	for (int i = 0; i < cubes.size(); i++) {
            for (int j = 0; j < cubes.size(); j++) {
                CubeC c1 = cubes.get(i);
                CubeC c2 = cubes.get(j);
                if (i != j) {
                    c1.check(c2);
                }
            }
        }

        long sum = 0;
        for (CubeC c : cubes) {
            sum += (6 - c.connected.size());
        }

        return sum;
    }

    protected Object solve2(List<CubeC> cubes) {
        Map<Cube, CubeC> cubesMap = cubes.stream().collect(Collectors.toMap(CubeC::c, Function.identity()));

        int minX = min(cubes.stream().map(c -> c.c.x)) - 1;
        int maxX = max(cubes.stream().map(c -> c.c.x)) + 1;
        int minY = min(cubes.stream().map(c -> c.c.y)) - 1;
        int maxY = max(cubes.stream().map(c -> c.c.y)) + 1;
        int minZ = min(cubes.stream().map(c -> c.c.z)) - 1;
        int maxZ = max(cubes.stream().map(c -> c.c.z)) + 1;

        Bounds bounds = new Bounds(minX, maxX, minY, maxY, minZ, maxZ);
        
        floodFill(cubesMap, new Cube(minX, minY, minZ), null, bounds);

        long sum = 0;
        for (CubeC c : cubesMap.values()) {
            sum += (c.outside.size());
        }
        return sum;
    }
    
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        
        logResult(1, solve1(getStringStream(fileName).map(Cube::of).map(CubeC::new).toList()));
        logResult(2, solve2(getStringStream(fileName).map(Cube::of).map(CubeC::new).toList()));
    }

    public static void main(String[] args) throws Exception {
        new Task18().run();
    }

}
