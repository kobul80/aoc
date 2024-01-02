package cz.kobul.aoc2022;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * https://adventofcode.com/2022/day/13
 * start 8:45, end 12:26
 */
public class Task13 extends Aoc2022 {

    enum Eq {
        LESS, SAME, MORE;
    }

    class Item implements Comparable<Item> {
        Item parent;
        Integer value = null;

        List<Item> values = new ArrayList<>();

        public Item(Item parent) {
            this.parent = parent;
        }

        public Item(Item parent, int value) {
            this.parent = parent;
            this.value = value;
        }


        void addItem(Item i) {
            values.add(i);
        }

        Eq cmp(Item i2) {
            if (value != null) {
                // jsem cislo
                if (i2.value != null) {
                 // provonavam hodnoty
                    if (value < i2.value) {
                        return Eq.LESS;
                    } else if (i2.value < value) {
                        return Eq.MORE;
                    } else {
                        return Eq.SAME;
                    }
                } else {
                    Item it = new Item(parent);
                    it.addItem(new Item(it, value));
                    return it.cmp(i2);
                }
            } else {
                // jsem list
                if (i2.values.isEmpty()) {
                    if (i2.value != null) {
                        // druha strana je cislo
                        Item item2= new Item(i2.parent);
                        item2.addItem(i2);
                        i2 = item2;
                    }
                }

                for (int i = 0; i < values.size(); i++) {
                    Item it1 = values.get(i);
                    if (i2.values.size() < i + 1) {
                        return Eq.MORE;
                    }
                    Item it2 = i2.values.get(i);
                    Eq result = it1.cmp(it2);
                    if (result != Eq.SAME) {
                        return result;
                    }
                }
                if (values.size() < i2.values.size()) {
                    return Eq.LESS;
                }
                return Eq.SAME;
            }
        }

        @Override
        public int compareTo(Item o) {
            Eq cmp = this.cmp(o);
            return cmp == Eq.SAME ? 0 : cmp == Eq.LESS ? -1 : 1;
        }


        public int getValue() {
            return value;
        }
        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return (value != null ? value.toString() : values.toString()).replace(" ", "");
        }

    }

    Item parse(String input) {
        int level = 0;
        Item current = null;
        Item root = null;
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (ch == '[') {
                level++;
                if (current == null) {
                    current = new Item(null);
                    root = current;
                } else {
                    Item newItem = new Item(current);
                    current.addItem(newItem);
                    current = newItem;
                }
            } else if (ch == ']') {
                current = current.parent;
            } else if (ch >= '0' && ch <= '9') {
                String no = "" + ch;
                while (input.charAt(i+1) >= '0' && input.charAt(i+1) <= '9') {
                    no += input.charAt(i+1);
                    i++;
                }
                current.addItem(new Item(current, Integer.parseInt(no)));
            }
        }
        if (!root.toString().equals(input)) {
            throw new IllegalArgumentException("Wrong parse: " + input + " vs " + root);
        }
        return root;
    }

    record ItemPair(Item i1, Item i2) {}
    
    List<ItemPair> pairs = new ArrayList<>();

    Eq compare(Item item1, Item item2) {
        return item1.cmp(item2);
    }

    public void solve() throws Exception {
        List<String> lines = readFileToListString(getDefaultInputFileName());
        for (Iterator<String> it = lines.iterator(); it.hasNext();) {
            String line1 = it.next();
            if (!line1.isEmpty()) {
                String line2 = it.next();
                pairs.add(new ItemPair(parse(line1), parse(line2)));
            }
        }

        logResult(1, solve1());
        logResult(2, solve2());
    }

    protected Object solve1() {
        int res = 0;

        for (int i =0 ; i < pairs.size(); i++) {
            ItemPair pair = pairs.get(i);
            Eq result = compare(pair.i1(), pair.i2());
            if (result == Eq.LESS) {
                res += (i + 1);
            }
        }

        return res;
    }

    protected Object solve2() {
        Item splitter1 = parse("[[2]]");
        Item splitter2 = parse("[[6]]");
        List<Item> allItems = new ArrayList<>();
        allItems.add(splitter1);
        allItems.add(splitter2);
        for (ItemPair pair : pairs) {
            allItems.add(pair.i1());
            allItems.add(pair.i2());
        }
        List<Item> sorted = allItems.stream().sorted().toList();
        int index1 = sorted.indexOf(splitter1) + 1;
        int index2 = sorted.indexOf(splitter2) + 1;
        return index1 * index2;
    }

    public static void main(String[] args) throws Exception {
        new Task13().run();
    }

}

