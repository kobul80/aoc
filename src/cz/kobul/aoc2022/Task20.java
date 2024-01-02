package cz.kobul.aoc2022;

import java.util.ArrayList;
import java.util.List;

/**
 * https://adventofcode.com/2022/day/20
 * start 6:00 end 8:34 lamismus!
 */
public class Task20 extends Aoc2022 {

    private int indexOf(int[] a, int i) {
        for (int x = 0; x < a.length; x++) {
            if (a[x] == i) {
                return x;
            }
        }
        return -1;
    }


    protected Object solve1() {
        int numberCount = numbers.size();
        List<Long> nl = new ArrayList<>(numbers);

        int[] origIdx = new int[nl.size()];
        for (int i = 0; i < origIdx.length; i++) {
            origIdx[i] = i;
        }

        for (int idx = 0; idx < numberCount; idx++) {
            int index = indexOf(origIdx, idx);
            Long l = nl.get(index);
            int newIndex = index + (int) l.longValue();
            if (newIndex < 0) {
                newIndex = newIndex % (numberCount -1) + (numberCount - 1);
            }
            while (newIndex >= numberCount) {
                newIndex = newIndex % (numberCount - 1);
            }
            if (newIndex > index) {
                if (newIndex >= nl.size()) {
                    System.out.println("Error new Index " + newIndex);
                }
                for (int i = index; i < newIndex; i++) {
                    nl.set(i, nl.get(i + 1));
                    origIdx[i] = origIdx[i + 1];
                }
            } else if (newIndex < index) {
                for (int i = index ; i > newIndex; i--) {
                    nl.set(i, nl.get(i - 1));
                    origIdx[i] = origIdx[i - 1];
                }

            }
            origIdx[newIndex] = idx;
            nl.set(newIndex, l);
        }

        int i0 = nl.indexOf(0L);
        long l1 = nl.get((i0 + 1000) % nl.size());
        long l2 = nl.get((i0 + 2000) % nl.size());
        long l3 = nl.get((i0 + 3000) % nl.size());

		return l1 + l2 + l3;
    }

    protected Object solve2() {
        long numberCount = numbers.size();
        List<Long> nl = new ArrayList<>(numbers);
        for (int idx = 0; idx < numberCount; idx++) {
            nl.set(idx, nl.get(idx) * 811589153L);
        }

        int[] origIdx = new int[nl.size()];
        for (int i = 0; i < origIdx.length; i++) {
            origIdx[i] = i;
        }

        for (int r = 0; r < 10; r++) {
            for (int idx = 0; idx < numberCount; idx++) {
                int index = indexOf(origIdx, idx);
                if (index == -1) {
                    System.out.println("ERROR @ idx:" + idx + " r:" + r);
                }
                Long l = nl.get(index);

                long newIndex = index + l.longValue();
                if (newIndex < 0) {
                    newIndex = newIndex % (numberCount -1) + (numberCount - 1);
                }
                while (newIndex >= numberCount) {
                    newIndex = newIndex % (numberCount - 1);
                }
                if (newIndex > index) {
                    if (newIndex >= nl.size()) {
                        System.out.println("Error new Index " + newIndex);
                    }
                    for (int i = index; i < newIndex; i++) {
                        nl.set(i, nl.get(i + 1));
                        origIdx[i] = origIdx[i + 1];
                    }
                } else if (newIndex < index) {
                    for (int i = index ; i > newIndex; i--) {
                        nl.set(i, nl.get(i - 1));
                        origIdx[i] = origIdx[i - 1];
                    }

                }
                origIdx[(int)newIndex] = idx;
                nl.set((int)newIndex, l);
            }
        }

        int i0 = nl.indexOf(0L);
        long l1 = nl.get((i0 + 1000) % nl.size());
        long l2 = nl.get((i0 + 2000) % nl.size());
        long l3 = nl.get((i0 + 3000) % nl.size());

		return l1 + l2 + l3;
    }

    List<Long> numbers;
    public void solve() throws Exception {
        numbers = getLongStream(getDefaultInputFileName()).toList();

        logResult(1, solve1());
        logResult(2, solve2());
    }

    public static void main(String[] args) throws Exception {
        new Task20().run();
    }

}
