package cz.kobul.aoc2022;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://adventofcode.com/2022/day/19
 * start 7:00 end 18:00
 */
public class Task19 extends Aoc2022 {

    enum Res {
        ORE,
        CLAY,
        OBSIDIAN,
        GEODE;
    }

    record Am(int am, Res res) {}

    record Robot(Res produces, Resources costs) {}

    record Blueprint(Map<Res, Robot> robots) {}

    record Resources(int ore, int clay, int obsidian, int geode) {
        int get(Res res) {
            return switch (res) {
            case ORE -> ore;
            case CLAY -> clay;
            case OBSIDIAN -> obsidian;
            case GEODE -> geode;
            };
        }

        Resources add(Res res, int amount) {
            return switch (res) {
            case ORE -> new Resources(ore + amount, clay, obsidian, geode);
            case CLAY -> new Resources(ore, clay + amount, obsidian, geode);
            case OBSIDIAN -> new Resources(ore, clay, obsidian + amount, geode);
            case GEODE -> new Resources(ore, clay, obsidian, geode + amount);
            };
        }

        Resources add(Resources r) {
            return new Resources(ore + r.ore, clay + r.clay, obsidian + r.obsidian, geode + r.geode);
        }

        Resources consume(Am am) {
            int or = get(am.res);
            if (or < am.am) {
                return null;
            }
            return add(am.res, -am.am);
        }

        Resources consume(Resources r) {
            return new Resources(ore - r.ore, clay - r.clay, obsidian - r.obsidian, geode - r.geode);
        }

        boolean has(Am am) {
            return get(am.res) >= am.am;
        }

        boolean has(Resources res) {
            return (ore >= res.ore && clay >= res.clay && obsidian >= res.obsidian && geode >= res.geode);
        }

        Resources produce(Am am) {
            return add(am.res, am.am);
        }

        Resources produce(Res res) {
            return add(res, 1);
        }


    }

    class Simulation {
        Blueprint blue;
        Resources robots;

//        Map<Res, List<Robot>> robots;
        Resources resources;
        final int time;

        public Simulation(Blueprint blue, int time) {
            this.time = time;
            this.blue = blue;
            resources = new Resources(0, 0, 0, 0);
            robots = new Resources(1, 0, 0, 0);
        }

        private Simulation(Blueprint blue, int time, Resources robots, Resources resources) {
            this.blue = blue;
            this.time = time;
            this.robots = robots;
            this.resources = resources;
        }


        public Simulation oneMinute(Robot buyRobots) {
            if (time == 0) {
                throw new IllegalStateException("No time left");
            }
            int newTime = time-1;
            Resources newRes = resources;
            Resources newRob = robots;


            // postavit roboty
            if (buyRobots != null) {
                newRes = newRes.consume(buyRobots.costs);
                newRob = newRob.produce(buyRobots.produces);
            }

            // vyprodukovat suroviny
            newRes = newRes.add(robots);

            return new Simulation(blue, newTime, newRob, newRes);
        }

        List<Robot> allRobotsCanBuy() {
            Resources remain = resources;
            List<Robot> result = new ArrayList<>();
            for (Robot robot : blue.robots.values()) {
                if (remain.has(robot.costs)) {
                    result.add(robot);
                }
            }
            result.add(null);
            return result;
        }

        @Override
        public final String toString() {
            return resources.toString();
        }

    }

    protected Object solve1() {
        return null;
    }

    protected Object solve2() {
        return null;
    }

    static Pattern p = Pattern.compile(".*Each ore robot costs ([0-9]+) ore. Each clay robot costs ([0-9]+) ore. Each obsidian robot costs ([0-9]+) ore and ([0-9]+) clay. Each geode robot costs ([0-9]+) ore and ([0-9]+) obsidian.");

    static Blueprint parse(String input) {
        Matcher m = p.matcher(input);
        if (m.matches()) {
            Map<Res, Robot> robots = new LinkedHashMap<>();
            robots.put(Res.GEODE, new Robot(Res.GEODE, new Resources(Integer.parseInt(m.group(5)), 0, Integer.parseInt(m.group(6)), 0)));
            robots.put(Res.OBSIDIAN, new Robot(Res.OBSIDIAN, new Resources(Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)), 0, 0)));
            robots.put(Res.CLAY, new Robot(Res.CLAY, new Resources(Integer.parseInt(m.group(2)), 0, 0, 0)));
            robots.put(Res.ORE, new Robot(Res.ORE, new Resources(Integer.parseInt(m.group(1)), 0, 0, 0)));
            return new Blueprint(robots);
        }
        throw new IllegalStateException("Unable to parse input '" +input + "'");
    }

    class Simulator {
        Blueprint bp;

        public Simulator(Blueprint bp) {
            this.bp = bp;
        }

        Simulation bestSim = null;
        Map<Integer, Simulation> bestSims = new HashMap<>();

        public void simulate(Simulation sim, List<Res> buys) {
            int t = sim.time;
            if (t > 0) {
                Simulation best = bestSims.get(t);
                if (best == null) {
                    bestSims.put(t, sim);
                } else {
                    if (best.resources.geode > sim.resources.geode) {
                        return;
                    } else {
                        bestSims.put(t, sim);
                    }
                }
            }
            if (sim.time == 0) {
                if (bestSim == null || bestSim.resources.geode < sim.resources.geode) {
                    bestSim = sim;
                    System.out.println(bestSim);
                    System.out.println(buys);
                }
                return;
            }
            List<Robot> allCanBuy = sim.allRobotsCanBuy();
            for (Robot oneBuy : allCanBuy) {
                //path + (24-sim.time+1) + toStr(oneBuy)
                List<Res> newBuys = new ArrayList<>(buys);
                newBuys.add(oneBuy != null ? oneBuy.produces : null);
                simulate(sim.oneMinute(oneBuy), newBuys);
            }
        }

        public Simulation simulate(int min) {
            Simulation sim = new Simulation(bp, min);
            simulate(sim, new ArrayList<>());
            return bestSim;
        }

    }


    public void solve() throws Exception {
        String fileName = FN_PREFIX + "19/input.txt";

        List<Blueprint> bps = getStringStream(fileName).map(Task19::parse).toList();

        System.out.println(bps);

        int result = 0;

        ExecutorService ex = Executors.newFixedThreadPool(12);

        List<Future<Simulation>> bestSimulations = new ArrayList<>();

        for (int i =0; i < bps.size(); i++) {
            final int b = i + 1;
//            System.out.println("Blueprint " + i);
            final Blueprint bp = bps.get(i);
            bestSimulations.add(ex.submit(new Callable<Simulation>() {
                @Override
                public Simulation call() throws Exception {
//                    System.out.println(Thread.currentThread().getName() + " processing blueprint " + b);
                    return new Simulator(bp).simulate(32);
                }
            }));
        }

        //
        for (int i =0; i < bestSimulations.size(); i++) {
            Simulation best = bestSimulations.get(i).get();
            System.out.println("Best sim of blueprint " + (i+1) + " " + best);
            result += (i + 1) * best.resources.geode;
        }

      logResult(1, result);
//        logResult(2, solve2(parseValves(lines)));
      ex.shutdown();
    }

    public static void main(String[] args) throws Exception {
        new Task19().run();
    }

}
