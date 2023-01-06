package cz.kobul.aoc2022;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * https://adventofcode.com/2022/day/7
 * start 6:01, end 6:35
 */
public class Task7 extends Aoc {

    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();

        List<String> lines = Files.readAllLines(Paths.get(fileName));

        List<String> currentDir = new ArrayList<>();
        List<String> files = new ArrayList<>();
        List<String> dirs = new ArrayList<>();

        Map<String, Long> fileSizes = new HashMap<>();
        Map<String, Long> dirSizes = new HashMap<>();

        for (String line : lines) {
            if (line.startsWith("$ ls")) {
                // ls dir
//                System.out.println("ls");
            } else if (line.startsWith("$ cd")) {
                String dir = line.substring(5);
//                System.out.println("cd " + line);
                if ("/".equals(dir)) {
                    currentDir.clear();
                } else if ("..".equals(dir)) {
                    currentDir.remove(currentDir.size() - 1);
                } else {
                    currentDir.add(dir);
                }
            } else {
                // file/dir in directory dirs:
                String fullDir = currentDir.stream().collect(Collectors.joining("/"));

                String[] row = line.split(" ");
                String fileDirName = fullDir + "/" + row[0];
                if ("dir".equals(row[0])) {
                    dirs.add(fileDirName);
                } else {
                    files.add(fileDirName);
                    long length = Long.parseLong(row[0]);
                    fileSizes.put(fileDirName, length);

                    String cd = "";
                    dirSizes.compute(cd, (d, size) -> size == null ? Long.valueOf(length) : size.longValue() + length);
                    for (String dir: currentDir) {
                        cd += "/" + dir;
                        dirSizes.compute(cd, (d, size) -> size == null ? Long.valueOf(length) : size.longValue() + length);
                    }
                }
            }
        }

        logResult(1, dirSizes.values().stream().filter(k -> k < 100000).collect(Collectors.summarizingLong(Long::longValue)).getSum());

        long neededFreeSpace = 30000000 - (70000000 - dirSizes.get(""));

        logResult(2, dirSizes.values().stream().filter(k -> k >= neededFreeSpace).sorted().findFirst().get());

    }

    public static void main(String[] args) throws Exception {
        new Task7().solve();
    }

}
