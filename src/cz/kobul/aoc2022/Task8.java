package cz.kobul.aoc2022;

/**
 * https://adventofcode.com/2022/day/8
 * start 6:00, end 6:34
 */
public class Task8 extends Aoc {

    int[][] trees;

    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();

        trees = readFileToIntIntArray(fileName);

        logResult(1, solve1());
        logResult(2, solve2());
    }

    protected Object solve1() {
        int total = 0;
        for (int r = 0; r < trees.length; r++) {
            for (int c = 0; c < trees[0].length; c++) {
                if (isVisible(r, c)) {
                    total ++;
                }
            }
        }
        return total;
    }

    protected Object solve2() {
        long max = 0;
        for (int r = 0; r < trees.length; r++) {
            for (int c = 0; c < trees[0].length; c++) {
                long sc = scenicScore(r, c);
                if (sc > max) {
                    max = sc;
                }
            }
        }
        return max;
    }

    private boolean isVisible(int r, int c) {
        return (r == 0) || (c == 0) || (r == trees.length - 1) || (c == trees[0].length - 1) ||
                visibleLeft(r, c) || visibleRight(r, c) || visibleTop(r, c) || visibleBottom(r, c);
    }

    private boolean visibleLeft(int r, int c) {
        int h = trees[r][c];
        for (int i = c -1; i >= 0; i--) {
            if (trees[r][i] >= h) {
                return false;
            }
        }
        return true;
    }

    private boolean visibleRight(int r, int c) {
        int h = trees[r][c];
        for (int i = c + 1; i < trees[0].length; i++) {
            if (trees[r][i] >= h) {
                return false;
            }
        }
        return true;

    }

    private boolean visibleTop(int r, int c) {
        int h = trees[r][c];
        for (int i = r -1; i >= 0; i--) {
            if (trees[i][c] >= h) {
                return false;
            }
        }
        return true;
    }

    private boolean visibleBottom(int r, int c) {
        int h = trees[r][c];
        for (int i = r + 1; i < trees.length; i++) {
            if (trees[i][c] >= h) {
                return false;
            }
        }
        return true;
    }

    private long scenicScore(int r, int c) {
        return scenicLeft(r, c) * scenicRight(r, c) * scenicTop(r, c) * scenicBottom(r, c);
    }

    private long scenicLeft(int r, int c) {
        if (c == 0) { return 0; }
        int h = trees[r][c];
        long score = 0;
        for (int i = c -1; i >= 0; i--) {
            if (trees[r][i] >= h) {
                return score + 1;
            }
            score++;
        }
        return score;
    }

    private long scenicRight(int r, int c) {
        if (c >= trees[0].length) { return 0; }
        int h = trees[r][c];
        long score = 0;
        for (int i = c + 1; i < trees[0].length; i++) {
            if (trees[r][i] >= h) {
                return score + 1;
            }
            score++;
        }
        return score;

    }

    private long scenicTop(int r, int c) {
        if (r == 0) { return 0; }
        int h = trees[r][c];
        long score = 0;
        for (int i = r -1; i >= 0; i--) {
            if (trees[i][c] >= h) {
                return score +1;
            }
            score++;
        }
        return score;
    }

    private long scenicBottom(int r, int c) {
        if (r >= trees.length) { return 0; }
        int h = trees[r][c];
        long score = 0;
        for (int i = r + 1; i < trees.length; i++) {
            if (trees[i][c] >= h) {
                return score +1;
            }
            score++;
        }
        return score;
    }

    public static void main(String[] args) throws Exception {
        new Task8().run();
    }

}
