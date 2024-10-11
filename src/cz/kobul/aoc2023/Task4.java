package cz.kobul.aoc2023;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * https://adventofcode.com/2023/day/4
 * start: 8:02 
 * end: 8:24
 */
public class Task4 extends Aoc2023 {


    @Override
    public void solve() throws Exception {
        String fileName = getDefaultInputFileName();

        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }

    public Long solve1(List<String> lines) {

        //        Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53

        long sum = 0;
        for (String line : lines) {
            String[] l = line.split("\\|");
            String[] l0 = l[0].split(":");

            Set<Integer> winning = new HashSet<>(toIntegerList(l0[1]));
            Set<Integer> all = new HashSet<>(toIntegerList(l[1]));
            all.retainAll(winning);
            int win = all.size();
            if (win > 0) {
                sum += Math.pow(2, win - 1);
            }
        }
        return sum;
    }	

    class Card{
        final int no;
        long copies;
        final int wins;
        
        public Card(int no, int wins) {
            this.no = no;
            this.wins = wins;
        }
    }
    
    public Long solve2(List<String> lines) {
        List<Card> cards = new ArrayList<>();
        int no = 1;
        for (String line : lines) {
            String[] l = line.split("\\|");
            String[] l0 = l[0].split(":");

            Set<Integer> winning = new HashSet<>(toIntegerList(l0[1]));
            Set<Integer> all = new HashSet<>(toIntegerList(l[1]));
            all.retainAll(winning);
            
            cards.add(new Card(no, all.size()));
            no++;
        }

        for (int c = 0; c < cards.size(); c++) {
            Card card = cards.get(c);
            long winsIncr = card.copies + 1;
            for (int j = 0; j < card.wins; j++) {
                int index = c + j + 1;
                if (index < cards.size()) {
                    cards.get(index).copies += winsIncr;                    
                }
            }
        }
        
        return cards.stream().map(c -> c.copies + 1).reduce(Long::sum).get();
    }   

    public static void main(String[] args) throws Exception {
        new Task4().run();
    }

}
