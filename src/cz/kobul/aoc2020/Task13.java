package cz.kobul.aoc2020;

import java.util.List;

/**
 * https://adventofcode.com/2020/day/13
 * start: 9:28
 * end: 11:31
 */
public class Task13 extends Aoc2020 {

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }

    public Object solve1(List<String> lines) {
        int time = Integer.parseInt(lines.get(0));
        List<Integer> buses = toIntegerList(lines.get(1));
        buses.sort(Integer::compare);
        int minWait = Integer.MAX_VALUE;
        int busNo = 0;
        for (int bus : buses) {
            int w = bus - (time % bus);
            if (w < minWait) {
                busNo = bus;
                minWait = w;
            }
        }
        return (long)busNo * minWait;
    }

    record Bus(int id, int offset) {
    }
    
    boolean test(List<Bus> buses, long t) {
        for (Bus b : buses) {
            if (((t + b.offset) % b.id) != 0) {
                return false;
            }
        }
        return true;
    }

    public long findT(List<Bus> buses, long startt, long step) {
        long t = startt;
        while (test(buses, t) == false) {
            t += step;
        }        
        return t;
    }
    
    public Object solve2(List<String> lines) {
        List<Bus> buses = newList();
        String[]s = lines.get(1).split(",");
        int off = 0;
        for (String n : s) {
            if (!"x".equals(n)) {
                int bus = Integer.parseInt(n);
                buses.add(new Bus(bus, off % bus));
            }
            off++;
        }
        buses.sort((a,b) -> Integer.compare(a.id, b.id));

        // pro dvojici vzdycky najdu hrubou silou ten spravny cas
        // pak vzdycky plati, ze dana konstalace nastava po kazdych lcm(a.off,b.off) krocich
        // takze kdyz hledam hrubou silou dalsi, zacinam v case t a skacu po lcm krocich
        long t = findT(buses.subList(0, 2), 1, 1);
        long m = lcm(buses.get(0).id, buses.get(1).id);
        for (int i = 3; i <= buses.size(); i++) {
            t = findT(buses.subList(0, i), t, m);
            m = lcm(m, buses.get(i-1).id());
        }
        return t;
    }	
    
    public static void main(String[] args) throws Exception {
        new Task13().run();
    }

}
