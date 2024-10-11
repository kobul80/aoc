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
 * https://adventofcode.com/2023/day/12
 * start: 6:00
 * end: 19:35 :'(
 */
public class Task12 extends Aoc2023 {


    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }
    
    public boolean check(char[] l, List<Integer> spr) {
        int curr = 0;
        int sprIdx = 0;
        for (int i = 0; i < l.length; i++) {
            if (l[i] == '#') {
                curr++;
            } else if(l[i] == '.' && curr > 0) {
                if (sprIdx >= spr.size()) {
                    return false;
                }
                int reqSize = spr.get(sprIdx);
                if (curr != reqSize) {
                    return false;
                }
                sprIdx++;
                curr = 0;
            }
        }
        return ((sprIdx >= spr.size() && curr==0) || (sprIdx == spr.size()-1 && curr == spr.get(spr.size() - 1)));
    }
    
    public Long solve1(List<String> lines) {
        long result = 0;
        int idx = 0;
        for (String line : lines) {
            String[] l = line.split(" ");
            List<Integer> spr = toIntegerList(l[1]);
            long cmb = solve(idx, "", l[0], spr);
            result += cmb;
            idx++;
        }
        
        return result;
    }
      
     record PartialInput(String s, List<Integer> spr) {}
     
     // ukladam si mezivysledky vypoctu, abych nezkousel dokolecka stejne kombinace
     Map<PartialInput, Long> partialValues = new HashMap<>(); 
    
     private long solve(int idx, String startStr, String s, List<Integer> spr) {
         if (spr.size() == 0) {
             return 1;
         }
         PartialInput inp = new PartialInput(s, spr);
         
         if (partialValues.containsKey(inp)) {
             return partialValues.get(inp);
         }
         
         int len = s.length(); 
         int sprSum = spr.stream().reduce(Integer::sum).get();
         int voidCnt = len - sprSum;
         int positions = voidCnt+1; 
         int count = spr.size(); 
         long result = 0;
         // pozice, kam muzu 'strcit' prvni stream mezi mezery
         // pak vygeneruju pocatecni cast a oriznu o ni zadani a zbytek udelam rekurzivne
         for (int p1 = 0; p1 < positions-count+1; p1++) {
             String start = fill('.', p1) + fill('#', spr.get(0));
             if (spr.size() > 1) {
                 start += '.';
             } else {
                 start += fill('.', s.length() - start.length());
             }
             if (matches(s, start)) {
                 result += solve(idx, startStr + start, s.substring(start.length()), spr.subList(1, spr.size()));
             }
         }
         partialValues.put(inp, result);
         return result;
    }

     /**
      * zkontroluje, ze zacatek vygenerovaneho odpovida zacatku v zadani
      * @param s zacatek ze zadani se znaky '?'
      * @param start vygenerovany zacatek
      * @return true, pokud vygenerovany zacatek odpovida zadani (bud se shoduji znaky, nebo je v zadani ?)
      */
     private boolean matches(String s, String start) {
         for (int i = 0; i < start.length(); i++) {
             if (s.charAt(i) != '?' &&  s.charAt(i) != start.charAt(i)) {
                 return false;
             }
         }
         return true;
     }

    public BigDecimal solve2(List<String> lines) {
        BigDecimal result = BigDecimal.ZERO;
        int idx = 0;
        for (String line : lines) {
            String[] l = line.split(" ");
            List<Integer> spr = toIntegerList(l[1]);
            for (int i = 0; i < 4; i++) {
                spr.addAll(toIntegerList(l[1]));
            }
            String ln = l[0] + '?' + l[0] + '?' +l[0] + '?' +l[0] + '?' +l[0];
            final int idxf = idx;
            long comb = solve(idxf, "", ln, spr);
            result = result.add(BigDecimal.valueOf(comb));
            idx++;                       
        }
        return result;
    }
    
//    [P1] 7195
//    [P2] 33992866292225
//    962ms
     
    public static void main(String[] args) throws Exception {
        new Task12().run();
    }
    
}
