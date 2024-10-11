package cz.kobul.aoc2023;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * https://adventofcode.com/2023/day/5
 * start: 6:00 
 * end: 8:29
 */
public class Task5 extends Aoc2023 {

    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();

        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }

    record Range(long from, long to) {}
    
    enum MatchType {
        LEFT_O,
        IN,
        RIGHT_O,
        OVER,
        OUT;
    }
    
    record Pair(List<Range> src, List<Range> converted) {}
    
    record CnvRange(long dest, long src, long len) {
        boolean match(long srcNo) {
            return srcNo >= src && srcNo < src + len;
        }
        long cnv(long srcNo) {
            long off = srcNo - src;
            return dest + off;
        }
        MatchType match(Range r) {
            if (match(r.from) && match(r.to)) {
                return MatchType.IN;
            } else if (match(r.from) && !match(r.to)) {
                return MatchType.RIGHT_O;
            } else if (!match(r.from) && match(r.to)) {
                return MatchType.LEFT_O;
            } else if (src > r.from && src + len < r.to) {
                return MatchType.OVER;
            }
            return MatchType.OUT;
        }
        
        void cnv(Pair ranges) {
            List<Range> newSrc = new ArrayList<>();
            for (Range r : ranges.src) {
                MatchType m = match(r);
                if (m == MatchType.OUT) {
                    newSrc.add(r);
                } else if (m == MatchType.IN) {
                    ranges.converted.add(new Range(cnv(r.from), cnv(r.to)));
                } else if (m == MatchType.OVER) {
                    ranges.converted.add(new Range(cnv(src), cnv(src + len -1)));
                    newSrc.add(new Range(r.from, src-1));
                    newSrc.add(new Range(src + len, r.to));                    
                } else if (m == MatchType.LEFT_O) {
                    ranges.converted.add(new Range(cnv(src), cnv(r.to)));
                    newSrc.add(new Range(r.from, src-1));
                } else if (m == MatchType.RIGHT_O) {
                    ranges.converted.add(new Range(cnv(r.from), cnv(src + len - 1)));
                    newSrc.add(new Range(src + len, r.to));
                }
            }            
            ranges.src.clear();
            ranges.src.addAll(newSrc);
        }
        
        public String toStr() {
            return "[(" + src + "-" + (src+len-1) + ")=>" + (dest-src) + "]";
        }
        
    }
    
    record Mp(String type, List<CnvRange> ranges) {
        long convert(long src) {
            for (CnvRange range : ranges) {
                if (range.match(src)) {
                    return range.cnv(src);
                }
            }
            return src;
        }
        List<Range> convert(List<Range> src) {
            Pair pair = new Pair(new ArrayList<>(src), new ArrayList<>());
            for (CnvRange range : ranges) {
                range.cnv(pair);
            }            
            
            List<Range> result = new ArrayList<>();
            result.addAll(pair.src);
            result.addAll(pair.converted);
            return List.copyOf(result);
        }
        
        public String toStr() {
            StringBuilder result = new StringBuilder();
            result.append(type);
            for (CnvRange r : ranges) {
                result.append(r.toStr());
            }
            return result.toString();
        }

    }
    
    public long cnv(long seed, List<Mp> maps) {
        long result = seed;
        for (Mp mp  : maps) {
            result = mp.convert(result);
        }
        return result;
    }

    public List<Range> cnv(Range seed, List<Mp> maps) {
        List<Range> result = List.of(seed);
        for (Mp mp  : maps) {
//            System.out.println(result);
//            System.out.println(mp.toStr());
            result = mp.convert(result);
//            System.out.println(result);
//            System.out.println("-----");
        }
        return result;
    }
    

    public Long cnvbf(Range seed, List<Mp> maps) {
        long min = Long.MAX_VALUE; 
        for (long s = seed.from; s<= seed.to; s++) {
            min = Math.min(cnv(s, maps), min);
        }
        return min;
    }
    

    
    public Long solve1(List<String> lines) {
        List<Long> seeds = toLongList(lines.get(0));
        
        List<Mp> maps = new ArrayList<>();
        
        String name = "";
        List<CnvRange> ranges = new ArrayList<>();
        int line = 2;
        while (line < lines.size()) {
            String ln  = lines.get(line);
            List<Long> nos = toLongList(ln);
            if (nos.size() == 3) {
                ranges.add(new CnvRange(nos.get(0), nos.get(1), nos.get(2)));
            } else if (ln.trim().length() == 0 && ranges.size() > 0) {
                    maps.add(new Mp(name, List.copyOf(ranges)));
                    ranges.clear();
            } else {
                name = ln;
            }
            line++;
        }
        if (ranges.size() > 0) {
            maps.add(new Mp(name, List.copyOf(ranges)));
            ranges.clear();
        }
        
        return seeds.stream().map(s -> cnv(s, maps)).reduce(Long::min).get();        
    } 
        
    /** bruteforce pristup, bezi cca minutu */
    public Long solve2bf(List<String> lines) {
        List<Range> seeds = new ArrayList<>();
        List<Long> seedsR = toLongList(lines.get(0));
        for (int i = 0; i < seedsR.size() / 2; i++) {
            long from = seedsR.get(i * 2);
            long cnt = seedsR.get(i * 2 + 1);
            seeds.add(new Range(from, from+ cnt -1));
        }
        
        List<Mp> maps = new ArrayList<>();
        
        String name = "";
        List<CnvRange> ranges = new ArrayList<>();
        int line = 2;
        while (line < lines.size()) {
            String ln  = lines.get(line);
            List<Long> nos = toLongList(ln);
            if (nos.size() == 3) {
                ranges.add(new CnvRange(nos.get(0), nos.get(1), nos.get(2)));
            } else if (ln.trim().length() == 0 && ranges.size() > 0) {
                    maps.add(new Mp(name, List.copyOf(ranges)));
                    ranges.clear();
            } else {
                name = ln;
            }
            line++;
        }
        if (ranges.size() > 0) {
            maps.add(new Mp(name, List.copyOf(ranges)));
            ranges.clear();
        }
        
        return seeds.parallelStream().map(s -> cnvbf(s, maps)).reduce(Long::min).get();        
    } 

    /** hezci pristup pres pruniky intervalu */
    public Long solve2(List<String> lines) {
        List<Range> seeds = new ArrayList<>();
        List<Long> seedsR = toLongList(lines.get(0));
        for (int i = 0; i < seedsR.size() / 2; i++) {
            long from = seedsR.get(i * 2);
            long cnt = seedsR.get(i * 2 + 1);
            seeds.add(new Range(from, from+ cnt -1));
        }      
        
        List<Mp> maps = new ArrayList<>();
        
        String name = "";
        List<CnvRange> ranges = new ArrayList<>();
        int line = 2;
        while (line < lines.size()) {
            String ln  = lines.get(line);
            List<Long> nos = toLongList(ln);
            if (nos.size() == 3) {
                ranges.add(new CnvRange(nos.get(0), nos.get(1), nos.get(2)));
            } else if (ln.trim().length() == 0 && ranges.size() > 0) {
                    maps.add(new Mp(name, List.copyOf(ranges)));
                    ranges.clear();
            } else {
                name = ln;
            }
            line++;
        }
        if (ranges.size() > 0) {
            maps.add(new Mp(name, List.copyOf(ranges)));
            ranges.clear();
        }
        
        return seeds.stream().map(s -> cnv(s, maps)).flatMap(Collection::stream).map(Range::from).reduce(Long::min).get();        

    }   

    public static void main(String[] args) throws Exception {
        new Task5().run();
    }

}
