package cz.kobul.aoc2023;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * https://adventofcode.com/2023/day/3
 * start: 7:15 
 * end: 7:58
 */
public class Task3 extends Aoc2023 {
    
    
	@Override
	public void solve() throws Exception {
		String fileName = getDefaultInputFileName();

		logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
	}
	
	
	public Long solve1(List<String> lines) {
	    boolean[][] used = new boolean[lines.size()][lines.get(0).length()];
	    
	    List<Long> result = new ArrayList<>();
	    
	    for (int l = 0; l < lines.size(); l++) {
	        String line= lines.get(l); 
	        for (int x = 0; x < line.length(); x++) {
	            char ch = line.charAt(x);
	            if (!Character.isDigit(ch) && ch != '.') {
	                // mam symbol
	                // podivam se do vsech stran, jestli tam neni cislo
	                List<Long> nos = getNumbers(l, x, lines, used);
	                System.out.println("(" + l + "," + x + "): " + nos);
	                result.addAll(nos);
	            }
	        }
	    }
	    
	    return result.stream().reduce(Long::sum).get();
	}	

	   public Long solve2(List<String> lines) {
	        boolean[][] used = new boolean[lines.size()][lines.get(0).length()];
	        
	        List<Long> result = new ArrayList<>();
	        
	        for (int l = 0; l < lines.size(); l++) {
	            String line= lines.get(l); 
	            for (int x = 0; x < line.length(); x++) {
	                char ch = line.charAt(x);
	                if (ch == '*') {
	                    // mam symbol
	                    // podivam se do vsech stran, jestli tam neni cislo
	                    List<Long> nos = getNumbers(l, x, lines, used);
	                    System.out.println("(" + l + "," + x + "): " + nos);
	                    if (nos.size() == 2) {
	                        result.add(nos.get(0) * nos.get(1));	                        
	                    }
	                }
	            }
	        }
	        
	        return result.stream().reduce(Long::sum).get();
	    }   


	
	public List<Long> getNumbers(int row, int col, List<String> lines, boolean[][] used) {        
	    int rowMax = used.length - 1;
	    int colMax = used[0].length - 1;

        List<Long> numbers = new ArrayList<>();
        Pos pos = new Pos(row, col);
       
        if (row > 0 && col > 0) {
            // nahore vlevo
            numbers.add(getNumber(row-1, col-1, lines, used));
        }
        if (row > 0) {
            // nahore
            numbers.add(getNumber(row-1, col, lines, used));
        }
        if (row > 0 && col < colMax) {
            // nahore vpravo
            numbers.add(getNumber(row-1, col+1, lines, used));
        }
        if (col > 0) {
            // vlevo
            numbers.add(getNumber(row, col-1, lines, used));
        }
        if (col < colMax) {
            // vpravo
            numbers.add(getNumber(row, col+1, lines, used));
        }
        if (row < rowMax && col > 0) {
            // dole vlevo
            numbers.add(getNumber(row+1, col-1, lines, used));
        }
        if (row < rowMax) {
            // dole
            numbers.add(getNumber(row+1, col, lines, used));
        }
        if (row < rowMax && col < colMax) {
            // dole vpravo
            numbers.add(getNumber(row+1, col+1, lines, used));
        }
        return numbers.stream().filter(Objects::nonNull).toList();
	}
	
	public Long getNumber(int row, int col, List<String> lines, boolean[][] used) {
	    String line = lines.get(row);
	    char ch = line.charAt(col);
	    if (Character.isDigit(ch) && !used[row][col]) {
	     // mam cislo, musim ho najit cele
	        return getNumberImpl(row, col, line, used);
	    }
	    return null;
	}
	
	public Long getNumberImpl(int row, int col, String line, boolean[][] used) {
	    int curr = col;
	    // najdu zacatek
	    while (curr > 0 && Character.isDigit(line.charAt(curr - 1))) {
	        curr--;
	    }

	    String n = "";
	    while (curr < line.length() && Character.isDigit(line.charAt(curr))) {
	        n+=line.charAt(curr);
	        used[row][curr] = true;
	        curr++;
	    }
	    return Long.valueOf(n);
	}
	
	
	public Integer parse2(String s) {
	    return null;
	}
	    

	public static void main(String[] args) throws Exception {
		new Task3().run();
	}

}
