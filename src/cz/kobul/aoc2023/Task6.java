package cz.kobul.aoc2023;

import java.util.ArrayList;
import java.util.List;

/**
 * https://adventofcode.com/2023/day/6
 * start: 6:00
 * end: 6:12
 */
public class Task6 extends Aoc2023 {

    record Race(long time, long record) {
        long run(long precharge) {
            if (precharge > time) {
                throw new IllegalStateException();
            }
            return precharge * (time - precharge);
        }
        boolean win(long precharge) {
            return run(precharge) > record;
        }
    }

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }
   
    public Long solve1(List<String> lines) {
        List<Long> no1 = toLongList(lines.get(0));
        List<Long> no2 = toLongList(lines.get(1));
        List<Race> races = new ArrayList<>();
        for (int i = 0; i < no1.size(); i++) {
            races.add(new Race(no1.get(i), no2.get(i)));
        }
        return solve(races);
    }
    
    public Long solve2(List<String> lines) {
        Long no1 = Long.parseLong(lines.get(0).replaceAll("[^0-9]", ""));
        Long no2 = Long.parseLong(lines.get(1).replaceAll("[^0-9]", ""));
        return solve(List.of(new Race(no1, no2)));
    }
    
    public Long solve(List<Race> races) {
        long result = 1;
        for (Race race : races) {
            long win = 0;
            for (long t = 0; t <= race.time; t++) {
                if (race.win(t)) {
                    win++;
                }
            }
            result *= win;
        }
        return result;
    }     

    public static void main(String[] args) throws Exception {
        new Task6().run();
    }

}
