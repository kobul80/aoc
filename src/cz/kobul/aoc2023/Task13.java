package cz.kobul.aoc2023;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * https://adventofcode.com/2023/day/13
 * start: 6:00
 * end: 6:28
 */
public class Task13 extends Aoc2023 {


    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }

    public Long solve1(List<String> lines) {
        List<Map2d> maps = parseMaps(lines);
        
        long result = 0;
        for (Map2d map : maps) {
            long r = solve(map);
            result+=r;
        } 
        return result;
   }

    public Long solve2(List<String> lines) {
        List<Map2d> maps = parseMaps(lines);
        
        long result = 0;
        for (Map2d map : maps) {
            long r = solve2(map);
            result+=r;
        } 
        return result;
    }

    protected List<Map2d> parseMaps(List<String> lines) {
        List<Map2d> maps = new ArrayList<>();
         List<String> buf = new ArrayList<>();
         for (int i = 0; i < lines.size(); i++) {
             String l = lines.get(i);
             if (l.trim().isEmpty()) {
                 maps.add(new Map2d(buf));
                 buf.clear();
             } else {
                 buf.add(l);
             }
         }
         if (!buf.isEmpty()) {
             maps.add(new Map2d(buf));             
         }
        return maps;
    }

       
    private int checkReflectionRow(int row, Map2d map) {
        int row1 = row;
        int row2 = row + 1;
        int res = 0;
        while (row1 >= 0 && row2 < map.rows()) {
            for (int c = 0; c < map.cols(); c++) {
                if (map.get(new Pos(row1, c)) != map.get(new Pos(row2, c))) {
                    res++;
                }
            }            
            row1--;
            row2++;
        }
        return res;
    }

    private int checkReflectionCol(int col, Map2d map) {
        int col1 = col;
        int col2 = col + 1;
        int res = 0;
        while (col1 >= 0 && col2 < map.cols()) {
            for (int r = 0; r < map.rows(); r++) {
                if (map.get(new Pos(r, col1)) != map.get(new Pos(r, col2))) {
                    res++;
                }
            }            
            col1--;
            col2++;
        }
        return res;
    }
     

    private long solve(Map2d map) {
        for (int r = 0; r < map.rows() - 1; r++) {
            if (checkReflectionRow(r, map) == 0) {
                return (r + 1) * 100;
            }
        }
        for (int c = 0; c < map.cols() - 1; c++) {
            if (checkReflectionCol(c, map) == 0) {
                return (c + 1);
            }
        }
        map.print();
        throw new IllegalStateException();
    }

    private long solve2(Map2d map) {
        for (int r = 0; r < map.rows() - 1; r++) {
            if (checkReflectionRow(r, map) == 1) {
                return (r + 1) * 100;
            }
        }
        for (int c = 0; c < map.cols() - 1; c++) {
            if (checkReflectionCol(c, map) == 1) {
                return (c + 1);
            }
        }
        map.print();
        throw new IllegalStateException();      
    }


    
    public static void main(String[] args) throws Exception {
        new Task13().run();
    }
    
}
