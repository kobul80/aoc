package cz.kobul.aoc2022;

import java.util.List;
import java.util.Objects;

/**
 * https://adventofcode.com/2022/day/25
 * start 9:00 end 9:50
 */
public class Task25 extends Aoc2022 {

	static final List<Character> CNV = List.of('=', '-', '0', '1', '2');

    static long from(String inp) {
        long multiplicand = 1;
        long result = 0;
        for (int i = inp.length() - 1; i >= 0; i--) {
        	char ch = inp.charAt(i);
            int index = CNV.indexOf(ch);
            if (index == -1) {
            	throw new IllegalStateException("Illegal character " + ch);
            };
            result += multiplicand * (index - 2);

            multiplicand *= 5;
        }
        return result;
    }

    static String toSnafu(long inp) {
    	if (inp == 0) { return "0"; }
    	StringBuilder result = new StringBuilder();
    	long rest = inp;
    	while (rest > 0) {
    		int cif = (int)( rest % 5);
    		if (cif > 2) {
    			cif = cif - 5;
    			rest += 5;
    		}
    		result.insert(0, CNV.get(cif+2));
    		rest = rest / 5;
    	}
    	return result.toString();
    }


    public void solve() throws Exception {
        logResult(1, toSnafu(getStringStream(getDefaultInputFileName()).map(Task25::from).reduce(Long::sum).get()));
    }

    public static void assertEq(String expected, String actual) {
    	if (!Objects.equals(expected, actual)) {
    		throw new AssertionError("Expected: '" + expected + "' actual '" + actual + "'");
    	}
    }
    
    public static void main(String[] args) throws Exception {
        new Task25().run();
    }

 }
