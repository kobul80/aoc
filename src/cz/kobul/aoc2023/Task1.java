package cz.kobul.aoc2023;

import java.util.ArrayList;
import java.util.List;

/**
 * https://adventofcode.com/2023/day/1
 * start: 14:11
 * end: 14:39
 */
public class Task1 extends Aoc2023 {

	@Override
	public void solve() throws Exception {
		String fileName = getDefaultInputFileName();
		logResult(1, getStringStream(fileName).map(Task1::process).reduce(Long::sum).get());
        logResult(2, getStringStream(fileName).map(Task1::process2).reduce(Long::sum).get());
		
//		logResult(1, groupBySummingLongs(getLongStream(fileName)).sorted(Comparator.reverseOrder()).findFirst().get());
//		logResult(2, groupBySummingLongs(getLongStream(fileName)).sorted(Comparator.reverseOrder()).limit(3).reduce(Long::sum).get());			
	}

	public static final Long process(String s) {
	    List<Integer> l = getDigitsFromString(s);
	    return (long) ((l.get(0) * 10) + l.get(l.size() -1));
	}

	   public static final Long process2(String s) {
	        List<Integer> l = getDigitsFromString2(s);
	        return (long) ((l.get(0) * 10) + l.get(l.size() -1));
	    }
	    
    static List<String> digits = List.of("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine");
    
    public static final List<Integer> getDigitsFromString2(String s) {
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i))) {
                result.add(((int) s.charAt(i)) - 48);
            } else {
                String sub = s.substring(i);
                for (int j = 0; j < digits.size(); j++) {
                    if (sub.length() >= digits.get(j).length() && sub.startsWith(digits.get(j))) {
                        result.add(j);
                        break;
                    }
                }
            }
        }
        return result;
    }
    
	
	public static void main(String[] args) throws Exception {
//	    getStringStream(getDigitsFromString2(FN_PREFIX)).map(Task1::process).reduce(Long::sum).get()
//	    System.out.println(getDigitsFromString("1fsd54asd56a8d97ffaa99"));
		new Task1().run();
	}

}
