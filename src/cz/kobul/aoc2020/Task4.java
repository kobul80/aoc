package cz.kobul.aoc2020;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * https://adventofcode.com/2020/day/4
 * start: 13:24
 * end: 13:48
 */
public class Task4 extends Aoc2020 {

    
    
    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();
        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }

    public Object solve1(List<String> lines) {
        Set<String> req = Set.of("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid");
        Set<String> parts = newSet();
        int valid = 0;
        for (String line : lines) {
            if (line.isBlank()) {
                if (parts.containsAll(req)) {
                    valid++;
                }
                parts.clear();
            } else {
                for (String p : line.split(" ")) {
                    parts.add(p.split(":")[0]);
                }
            }
        }
        if (parts.equals(req)) {
            valid++;
        }        
        return valid;
    }
    
    record Info(String type, String value) {}

    public boolean validate(Map<String, String> infos) {
        try {

//            byr (Birth Year) - four digits; at least 1920 and at most 2002.
            String byr =  infos.get("byr");
            if (byr == null || byr.length() != 4 || Integer.parseInt(byr) < 1920 || Integer.parseInt(byr) > 2002) {
                return false;
            }            

//            iyr (Issue Year) - four digits; at least 2010 and at most 2020.
            String iyr =  infos.get("iyr");
            if (iyr == null || iyr.length() != 4 || Integer.parseInt(iyr) < 2010 || Integer.parseInt(iyr) > 2020) {
                return false;
            }          

//            eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
            String eyr = infos.get("eyr");
            if (eyr == null || eyr.length() != 4 || Integer.parseInt(eyr) < 2020 || Integer.parseInt(eyr) > 2030) {
                return false;
            }

//            hgt (Height) - a number followed by either cm or in:
//                If cm, the number must be at least 150 and at most 193.
//                If in, the number must be at least 59 and at most 76.
            String hgt = infos.get("hgt");
            if (hgt == null || !validhgt(hgt)) {
                return false;
            }
            
//            hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
            String hcl = infos.get("hcl");
            if (hcl == null 
                    || (!hcl.startsWith("#")) 
                    || (!(hcl.length() == 7)) 
                    || Integer.parseInt(hcl.substring(1), 16) < 0) {
                return false;
            }
            
            //ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
            String ecl = infos.get("ecl");
            Set<String> validEcl = Set.of("amb","blu","brn","gry","grn","hzl","oth");
            if (ecl == null || !validEcl.contains(ecl)) {
                return false;
            }

            // pid (Passport ID) - a nine-digit number, including leading zeroes.
            String pid = infos.get("pid");
            if (pid == null || pid.length() != 9 || Long.parseLong(pid) < 0) {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }        
        return true;
    }
    
    private boolean validhgt(String hgt) {
//      hgt (Height) - a number followed by either cm or in:
//      If cm, the number must be at least 150 and at most 193.
//      If in, the number must be at least 59 and at most 76.
        try {
            int no = Integer.parseInt(hgt.substring(0, hgt.length() - 2));
            
            if (hgt.endsWith("cm")) {
                return no >= 150 && no <= 193;
            } else if (hgt.endsWith("in")) {
                return no >= 59 && no <= 76;
            }
            return false;            
        } catch (Exception ex) {
            return false;
        }
        
    }

    public Object solve2(List<String> lines) {
        Map<String, String> infos = newMap();
        int valid = 0;
        for (String line : lines) {
            if (line.isBlank()) {
                if (validate(infos)) {
                    valid++;
                }
                infos.clear();
            } else {
                for (String p : line.split(" ")) {
                    infos.put(p.split(":")[0], p.split(":")[1]);
                }
            }
        }
        if (validate(infos)) {
            valid++;
        }
        return valid;
    }	
    
    public static void main(String[] args) throws Exception {
        new Task4().run();
    }

}
