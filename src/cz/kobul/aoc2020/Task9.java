package cz.kobul.aoc2020;

import java.util.List;

/**
 * https://adventofcode.com/2020/day/9
 * start: 10:46
 * end: 11:01
 */
public class Task9 extends Aoc2020 {

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }

    public boolean test(int testIndex, long[] nums) {
        long testnum = nums[testIndex];
        for (int i = testIndex-25; i < testIndex; i++) {
            for (int j =i; j < testIndex; j++) {
                if (i != j) {
                    if (testnum == nums[i] + nums[j]) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public Object solve1(List<String> lines) {
        long[] nums = new long[lines.size()];
        int idx = 0;
        for (String l : lines) {
            nums[idx++] = Long.parseLong(l);
        }
        return nums[findInvalidNumberIndex(nums)];
    }

    protected int findInvalidNumberIndex(long[] nums) {
        for (int i = 25; i < nums.length; i++) {
            if (!test(i, nums)) {
                return i;
            }   
        }
        return -1;
    }
    
    public Object solve2(List<String> lines) {
        long[] nums = new long[lines.size()];
        int idx = 0;
        for (String l : lines) {
            nums[idx++] = Long.parseLong(l);
        }
        int invalidIdx = findInvalidNumberIndex(nums);
        long invalid = nums[invalidIdx];
        for (int i = 0; i < invalidIdx; i++) {
            long sum = 0;
            int j = i;
            while (sum < invalid) {
                sum += nums[j];
                j++;
            }
            if (sum == invalid) {
                return nums[i] + nums[j-1];
            }
        }
        return null;
    }	
    
    public static void main(String[] args) throws Exception {
        new Task9().run();
    }

}
