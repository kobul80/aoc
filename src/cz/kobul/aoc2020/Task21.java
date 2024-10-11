package cz.kobul.aoc2020;

import java.util.List;

/**
 * https://adventofcode.com/2020/day/21
 * start: 
 * end: 
 */
public class Task21 extends Aoc2020 {

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }


    public Object solve1(List<String> lines) {
        return null;
    }

    public Object solve2(List<String> lines) {
        return null;
    }	
    
    public static void main(String[] args) throws Exception {
        new Task21().run();
    }

}
