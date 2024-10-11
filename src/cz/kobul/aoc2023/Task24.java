package cz.kobul.aoc2023;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;


/**
 * https://adventofcode.com/2023/day/24
 * start: 8:00
 * end: 
 */
public class Task24 extends Aoc2023 {

    @Override
    public void solve() throws Exception {
//        String fileName = getDefaultTestFileName();
                String fileName = getDefaultInputFileName();

//        logResult(1, solve1(readFileToListString(fileName)));
        logResult(2, solve2(readFileToListString(fileName)));
    }     

    static long max = 10L;
    
    /** x je doleva, doprava, y je vpred, vzad a z je nahoru, dolu */
    static class Pos3L {
        long x; long y; long z;

        public Pos3L(long x, long y, long z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        @Override
        public String toString() {
            return "{" + x + ", " + y + ", " + z + "}";
        }
        
    }

    /** x je doleva, doprava, y je vpred, vzad a z je nahoru, dolu */
    static class Pos3d {
        double x; double y; double z;

        public Pos3d(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        @Override
        public String toString() {
            return "{" + x + ", " + y + ", " + z + "}";
        }
        
    }

    record Hail(Pos3L start, Pos3L speed) {
        public Hail normalize() {
            long coef = -(start.x / speed.x);
            return new Hail(new Pos3L(speed.x * coef + start.x, speed.y * coef + start.y, speed.z * coef + start.z), speed);
        }
    }

    record Haild(Pos3d start, Pos3d speed) {
    }
    

    public static boolean intersecta(long box1a1, long box1a2, long box2a1, long box2a2) {
        long min1 = Math.min(box1a1, box1a2);
        long min2 = Math.min(box2a1, box2a2);
        long max1 = Math.max(box1a1, box1a2);
        long max2 = Math.max(box2a1, box2a2);
        
        return !(min2 > max1 || min1 > max2);
    }
    
    public static boolean intersect(Rect r1, Rect r2) {
        return intersecta(r1.x1, r1.x2, r2.x1, r2.x2)
                || intersecta(r1.y1, r1.y2, r2.y1, r2.y2);
    }
    
    record Rect(long x1, long y1, long x2, long y2) {
    }
    
    record Inter(double x, double y) {}
    
    public Inter interxy(Hail h1, Hail h2) {
        double k1 = (double)h1.speed.y/h1.speed.x;
        double k2 = (double)h2.speed.y/h2.speed.x;
        
        double a = k1;
        double c = ((double)h1.start.y - (h1.start.x * k1));
        
        double b = k2;
        double d = ((double)h2.start.y - (h2.start.x * k2));
        
        double x=(d-c)/(a-b);
        double y= a * (x) + c; 
        
        return new Inter(x, y);
    }

    public Inter interyz(Hail h1, Hail h2) {
        double k1 = (double)h1.speed.z/h1.speed.y;
        double k2 = (double)h2.speed.z/h2.speed.y;
        
        double a = k1;
        double c = ((double)h1.start.z - (h1.start.y * k1));
        
        double b = k2;
        double d = ((double)h2.start.z - (h2.start.y * k2));
        
        double y=(d-c)/(a-b);
        double z= a * (y) + c; 
        
        return new Inter(y, z);
    }

    public Inter interxz(Hail h1, Hail h2) {
        double k1 = (double)h1.speed.z/h1.speed.x;
        double k2 = (double)h2.speed.z/h2.speed.x;
        
        double a = k1;
        double c = ((double)h1.start.z - (h1.start.x * k1));
        
        double b = k2;
        double d = ((double)h2.start.z - (h2.start.x * k2));
        
        double x=(d-c)/(a-b);
        double z= a * (x) + c; 
        
        return new Inter(x, z);
    }

    
    
    public Inter interxy(Haild h1, Haild h2) {
        double k1 = (double)h1.speed.y/h1.speed.x;
        double k2 = (double)h2.speed.y/h2.speed.x;
        
        double a = k1;
        double c = ((double)h1.start.y - (h1.start.x * k1));
        
        double b = k2;
        double d = ((double)h2.start.y - (h2.start.x * k2));
        
        double x=(d-c)/(a-b);
        double y= a * (x) + c; 
        
        return new Inter(x, y);
    }
    
    public Inter interxz(Haild h1, Haild h2) {
        double k1 = (double)h1.speed.z/h1.speed.x;
        double k2 = (double)h2.speed.z/h2.speed.x;
        
        double a = k1;
        double c = ((double)h1.start.z - (h1.start.x * k1));
        
        double b = k2;
        double d = ((double)h2.start.z - (h2.start.x * k2));
        
        double x=(d-c)/(a-b);
        double z= a * (x) + c; 
        
        return new Inter(x, z);
    }
    
    public Inter interyz(Haild h1, Haild h2) {
        double k1 = (double)h1.speed.z/h1.speed.y;
        double k2 = (double)h2.speed.z/h2.speed.y;
        
        double a = k1;
        double c = ((double)h1.start.z - (h1.start.y * k1));
        
        double b = k2;
        double d = ((double)h2.start.z - (h2.start.y * k2));
        
        double y=(d-c)/(a-b);
        double z= a * (y) + c; 
        
        return new Inter(y, z);
    }
    

    public Object solve1(List<String> lines) {
        LinkedList<Hail> hails = newLinkedList();
        for (String l : lines) {
            long[] i = toLongArray(l);
            hails.add(new Hail(new Pos3L((long)i[0], (long)i[1], (long)i[2]), new Pos3L((long)i[3], (long)i[4], (long)i[5])));
        }
        
        double xmin = 200000000000000L;
        double xmax = 400000000000000L;

        double ymin = 200000000000000L;
        double ymax = 400000000000000L;

        long sum = 0;
        for (int i1 = 0; i1 < hails.size(); i1++) {
            for (int i2 = i1 + 1; i2 < hails.size(); i2++) {
                Hail h1 = hails.get(i1);
                Hail h2 = hails.get(i2);

                Inter in = interxy(h1, h2);
                System.out.println("A " + h1);
                System.out.println("B " + h2);
                System.out.println(in);
                double t1 = (double)(in.x - h1.start.x)/h1.speed.x;
                double t2 = (double)(in.x - h2.start.x)/h2.speed.x;
                
                if (t1 > 0 && t2 > 0) {                    
                    if (in.x >= xmin && in.x <= xmax && in.y >= ymin && in.y <= ymax) {
                        sum++;
//                        System.out.println("Hailstones' paths will cross inside test area @ " + in);
                    } else {
//                        System.out.println("Hailstones' paths will cross outside test area @ " + in);                        
                    }
                } 
//            else {
//                    System.out.println("Hailstones' paths will not cross");                    
//                }
                
                
                
//                if (t1 == t2) {
//                    System.out.println("Hailstones cross @ " + (h1.start.x() + t1 * h1.speed.x()) + "," + (h1.start.y() + t1 * h2.speed.y()));
//                } else {
//                    System.out.println("A " + h1);
//                    System.out.println("B " + h2);
//                    System.out.println("Hailstones never cross");                    
//                }
            }
        }
        return sum;
    }

//    public Inter inter(Hail h1) {
//        double k1 = (double)h1.speed.y/h1.speed.x;
//        
//        double a = k1;
//        double c = ((double)h1.start.y - (h1.start.x * k1));
//        
//        double b = k2;
//        double d = ((double)h2.start.y - (h2.start.x * k2));
//        
//        System.out.println();
//        
//        double x=(d-c)/(a-b);
//        double y= a * (x) + c; 
//        
//        return new Inter(x, y);
//    }
//    
//
    
    public String inv(long no) {
        long in = no * -1;
        return in > 0 ? "+" + in : "" + in;
    }

    public String no(long no) {
        long in = no;
        return in > 0 ? "+" + in : "" + in;
    }
    

    static BigDecimal countt(Hail h, long x, long a) {
        if (h.speed.x != a) {
            return new BigDecimal(x - h.start.x).divide(new BigDecimal(h.speed.x - a), 4, RoundingMode.HALF_UP);            
        }
        return BigDecimal.ZERO;        
    }
    
    record PairLong(long l1, long l2) {}
    
    
    
    
    public Object solve2(List<String> lines) {
        LinkedList<Hail> hails = newLinkedList();
        for (String l : lines) {
            long[] i = toLongArray(l);
            hails.add(new Hail(new Pos3L((long)i[0], (long)i[1], (long)i[2]), new Pos3L((long)i[3], (long)i[4], (long)i[5])));
        }
        
//        double sumx = 0;
//        int cnt = 0;
//        for (int i1 = 0; i1 < hails.size(); i1++) {
//            for (int i2 = i1 + 1; i2 < hails.size(); i2++) {
//                Hail h1 = hails.get(i1);
//                Hail h2 = hails.get(i2);
//
//                
//                
////                int 
//                
//                Inter inxy = interxy(h1, h2);
//                Inter inxz = interxz(h1, h2);
//                Inter inyz = interyz(h1, h2);
//                
//                System.out.println("xy " + inxy);
//                System.out.println("xz " + inxz);
//                System.out.println("yz " + inyz);
//                if (!Double.isInfinite(inxy.x) && !Double.isNaN(inxy.x)) {
//                    sumx += inxy.x;
//                    cnt++;                    
//                }
//                if (!Double.isInfinite(inxz.x) && !Double.isNaN(inxz.x)) {
//                    sumx += inxz.x;
//                    cnt++;                    
//                }
//
//                
////                double t1 = (double)(in.x - h1.start.x)/h1.speed.x;
////                double t2 = (double)(in.x - h2.start.x)/h2.speed.x;
//                
//            }
//        }
        
//        System.out.println(sumx / cnt);
        
        countApx1(hails);
        
//        countBf(hails);
//        
        countRot(hails);
        
//        for (long a = -100; a < 100; a++) {
//            for (long x = -100; x < 100; x++) {
//                Set<BigDecimal> ts = newSet();
//                for (Hail h : hails) {
//                    BigDecimal t = countt(h, x, a);
////                    y = 
//                    if (t.compareTo(BigDecimal.ZERO) > 0 && t.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
//                        System.out.println(t);                        
//                    }
////                    if (t.re) {
////                        ts.add(t);                        
////                    } else {
////                        System.out.println(t);
////                        break;
////                    }
////                    if (x0s.size() > 1) {
////                        break;
////                    }
//                }
////                if (x0s.size() == 1) {
////                    System.out.println("x0 = " + x0s.iterator().next());
////                } else {
//                if (ts.size() == hails.size()) {
//                    System.out.println(ts + " " + a + " " + x);
//                }
//            }                
//        }

//      Set<PairLong> ab = newSet();
//      Set<PairLong> ac = newSet();
//      Set<PairLong> bc = newSet();
//        
//      for (Hail h : hails) {
//          ab.add(new PairLong(h.speed.x, h.speed.y));
//          ac.add(new PairLong(h.speed.x, h.speed.z));
//          bc.add(new PairLong(h.speed.y, h.speed.z));
//      }
//      
//      long cnt = 0;
//      for (int a = -500; a < 500; a++) {
//          for (int b = -500; b < 500; b++) {
//              for (int c = -500; c < 500; c++) {
//                  if (!ab.contains(new PairLong(a, b))
//                          && !ac.contains(new PairLong(a, c))
//                          && !bc.contains(new PairLong(b, c))) {
//                      cnt++;
////                      System.out.println(a + " " + b + " " + c);
//                  }
//              }
//          }
//      }
//      System.out.println(cnt);
      
//      char unk = 't';
//      for (Hail h : hails.subList(0, 3)) {
//
//            System.out.println("a=(" + h.start.x + "-x)/" +unk +" " + no(h.speed.x) + " ");
//            System.out.println("b=(" + h.start.y + "-y)/" +unk +" " + no(h.speed.y) + " ");
//            System.out.println("c=(" + h.start.z + "-z)/" +unk +" " + no(h.speed.z) + " ");
//            unk++;
//      }
//            
//            System.out.println("a=(x"+i+"-x)/t" +i +"+ a" + i + " &&");
//            System.out.println("b=(y"+i+"-y)/t" +i +"+ b" + i + " &&");
//            System.out.println("c=(z"+i+"-z)/t" +i +"+ c" + i + " &&");

//            for (long j = 0; j < 1000; j++) {
//                
//            }
//            Hail h = hail.normalize();
//            System.out.println(h.start + " + t" + i + " * " + h.speed + " = (x, y, z) + t" + (i) + " * (a,b,c) && " );
//            System.out.println(h.start + " + t" + i + " " + h.speed + " = {x, y, z} + t" + (i+1) + " {a, b, c}, " );
//            i+=1;
//        }
        return null;
        
    }

    private void countRot(LinkedList<Hail> hails) {
        List<Haild> rotated = new ArrayList<>();

        long a = -3;
        long b = 1;
//        double sina = getSin90(a);
//        double cosa = getCos90(a);
//        double sinb = getSin90(b);
//        double cosb = getCos90(b);
        Pos3d how = new Pos3d(-3, 1, 2);
        
        Hail line = new Hail(new Pos3L(354954946036320L, 318916597757112L, 112745502066835L), new Pos3L(-117, -69, 281));
        
        for (Hail h : hails) {
            Hail h1 = h;
            Hail h2 = line;
            
            long x1 = h1.start.x;
            long y1 = h1.start.y;
            long z1 = h1.start.z;

            long x2 = h2.start.x;
            long y2 = h2.start.y;
            long z2 = h2.start.z;

            long a1 = h1.speed.x;
            long b1 = h1.speed.y;
            long c1 = h1.speed.z;

            long a2 = h2.speed.x;
            long b2 = h2.speed.y;
            long c2 = h2.speed.z;
            
//            x1 + a1 * t == x2 + a2 * t;
            
            if (a1 != a2 && b1 != b2 && c1 != c2) {
                long t1 = (x2 - x1) / (a1 - a2);
                long t2 = (y2 - y1) / (b1 - b2);
                long t3 = (z2 - z1) / (c1 - c2);
                        
                System.out.println(t1 + " " + t2 + " " + t3);                
            } else {
                System.out.println("No");
//                System.out.println(t1 + " " + t2 + " " + t3);                                
            }
            
                    
//            double a = (xp2-xp1)/(t2-t1);
//            double b = (yp2-yp1)/(t2-t1);
//            double c = (zp2-zp1)/(t2-t1);
//            
//            double x = xp1 - (a * t1);
//            double y = yp1 - (b * t1);
//            double z = zp1 - (c * t1);
//            
//            double t31 = (double)(x3 - x)/(a - a3);
//            double t32 = (double)(y3 - y)/(b - b3);
//            double t33 = (double)(z3 - z)/(c - c3);
            
//            Inter inter = int
            //roty(h, sinb, cosb)
//            Hail m = new Hail(new Pos3L(h.start.x - 24, h.start.y - 13, h.start.z - 10), h.speed);
//            roty
//            System.out.println(rotx(h.speed, getSin((double)how.y/how.z), getCos((double)how.y/how.z)));
//            System.out.println(rot(h.speed, how));
//            Pos3d p2 = rotz(p1, getSin(h.speed.y/h.speed.z), getCos(h.speed.y/h.speed.z));

//            System.out.println(ro);
//            rotated.add(rot(h, how));
        }
        
        
//        for (int i1 = 0; i1 < rotated.size(); i1++) {
//            for (int i2 = i1 + 1; i2 < rotated.size(); i2++) {
//                Haild h1 = rotated.get(i1);
//                Haild h2 = rotated.get(i2);
//
//                Inter in = interxy(h1, h2);
//                System.out.println("A " + h1);
//                System.out.println("B " + h2);
//                System.out.println(interxy(h1, h2));
//                System.out.println(interxz(h1, h2));
//                System.out.println(interyz(h1, h2));
//            }
//        }
        
    }

    record Diff(double diff, long t1, long t2) {}
    
    
    protected void countApx1(LinkedList<Hail> hails) {
        long t1min = 2;
        long t1max = 999999999998L;
        long t2min = 1;
        long t2max = 999999999999L;
        
        while ((t1max - t1min) > 1) {
            long t1mid = (t1max + t1min) /2;
            long t2mid = (t2max + t2min) /2;
            
            if (t1mid == t2mid) {
                t1mid++;
            }
            if (t1min == t2min) {
                t1min--;
            }
            if (t1max == t2max) {
                t1max++;
            }
            
            
            System.out.println(t1mid + " " + " " + t1min + " " + t1max + " " + t2min + " " + t2max);
            
            List<Diff> diffs = new ArrayList<>();
            diffs.add(getDiff(t1min, t2min, hails));
            diffs.add(getDiff(t1mid, t2min, hails));
            diffs.add(getDiff(t1max, t2min, hails));
            diffs.add(getDiff(t1min, t2mid, hails));
            diffs.add(getDiff(t1mid, t2mid, hails));
            diffs.add(getDiff(t1max, t2mid, hails));
            diffs.add(getDiff(t1min, t2max, hails));
            diffs.add(getDiff(t1mid, t2max, hails));
            diffs.add(getDiff(t1max, t2max, hails));
            
            Diff d = diffs.stream().filter(Objects::nonNull).sorted(Comparator.comparing(Diff::diff)).findFirst().get();
            
//            diffs.sort(Comparator.comparing(Diff::diff));
//            
//            Diff d = diffs.get(0);
            
            if (d.diff == 0) {
                System.out.println(d.t1 + " " + d.t2);
                return;
            }
            
            if (d.t1 == t1min) {
                t1max = t1mid;
            } else {
                t1min = t1mid;
            }
            if (d.t2 == t2min) {
                t2max = t2mid;
            } else {
                t2min = t2mid;
            }
        }

        long t1 = (t1max + t1min) / 2;
        long t2 = (t2max + t2min) / 2;
        System.out.println(t1 + " " + t2);
        
//        long t1 = (t1max + t1min) / 2;
//        
//        while ((t2max - t2min) > 1) {
//            System.out.println("T1 = " + t1);
////            long t1mid = (t1max + t1min) /2;
//            long t2mid = (t2max + t2min) /2;
//            
//            System.out.println(t2mid + " " + t1 + " " + t2min + " " + t2max);
//            
//            if (t1 == t2min) {
//                System.out.println(t1 + " " +t2min);
//                System.out.println(t1 + " " +t2max);
//            }
//            
//            List<Diff> diffs = new ArrayList<>();
//            diffs.add(getDiff(t1, t2min, hails));
//            diffs.add(getDiff(t1, t2max, hails));
////            diffs.add(getDiff(t1min, t2max, hails));
////            diffs.add(getDiff(t1max, t2max, hails));
//            
//            diffs.sort(Comparator.comparing(Diff::diff));
//            
//            Diff d = diffs.get(0);
//            
//            if (d.diff == 0) {
//                System.out.println(d.t1 + " " + d.t2);
//                return;
//            }
//            
////            if (d.t1 == t1min) {
////                t1max = t1mid;
////            } else {
////                t1min = t1mid;
////            }
//            if (d.t2 == t2min) {
//                t2max = t2mid;
//            } else {
//                t2min = t2mid;
//            }
//        }

    }

    
    
    protected void countApx(LinkedList<Hail> hails) {
        long t1min = 1;
        long t1max = 1000000000001L;
        long t2min = 0;
        long t2max = 1000000000000L;
        
        while ((t1max - t1min) > 1) {
            long t1mid = (t1max + t1min) /2;
//            long t2mid = (t2max + t2min) /2;
            
            System.out.println(t1mid + " " + " " + t1min + " " + t1max + " " + t2min + " " + t2max);
            
            List<Diff> diffs = new ArrayList<>();
            diffs.add(getDiff(t1min, t2min, hails));
            diffs.add(getDiff(t1max, t2min, hails));
//            diffs.add(getDiff(t1min, t2max, hails));
//            diffs.add(getDiff(t1max, t2max, hails));
            
            diffs.sort(Comparator.comparing(Diff::diff));
            
            Diff d = diffs.get(0);
            
            if (d.diff == 0) {
                System.out.println(d.t1 + " " + d.t2);
                return;
            }
            
            if (d.t1 == t1min) {
                t1max = t1mid;
            } else {
                t1min = t1mid;
            }
//            if (d.t2 == t2min) {
//                t2max = t2mid;
//            } else {
//                t2min = t2mid;
//            }
        }

        long t1 = (t1max + t1min) / 2;
        
        while ((t2max - t2min) > 1) {
            System.out.println("T1 = " + t1);
//            long t1mid = (t1max + t1min) /2;
            long t2mid = (t2max + t2min) /2;
            
            System.out.println(t2mid + " " + t1 + " " + t2min + " " + t2max);
            
            if (t1 == t2min) {
                System.out.println(t1 + " " +t2min);
                System.out.println(t1 + " " +t2max);
            }
            
            List<Diff> diffs = new ArrayList<>();
            diffs.add(getDiff(t1, t2min, hails));
            diffs.add(getDiff(t1, t2max, hails));
//            diffs.add(getDiff(t1min, t2max, hails));
//            diffs.add(getDiff(t1max, t2max, hails));
            
            diffs.sort(Comparator.comparing(Diff::diff));
            
            Diff d = diffs.get(0);
            
            if (d.diff == 0) {
                System.out.println(d.t1 + " " + d.t2);
                return;
            }
            
//            if (d.t1 == t1min) {
//                t1max = t1mid;
//            } else {
//                t1min = t1mid;
//            }
            if (d.t2 == t2min) {
                t2max = t2mid;
            } else {
                t2min = t2mid;
            }
        }

    }

    
    protected void countBf(LinkedList<Hail> hails) {
        int i = 1;
        for (long t1 = 0; t1 < 10000; t1++) {
            for (long t2 = 0; t2 < 10000; t2++) {
                if (t1 != t2) {
                    Hail h1 = hails.get(0);
                    Hail h2 = hails.get(1);
                    Hail h3 = hails.get(2);

                    long x1 = h1.start.x;
                    long y1 = h1.start.y;
                    long z1 = h1.start.z;

                    long x2 = h2.start.x;
                    long y2 = h2.start.y;
                    long z2 = h2.start.z;

                    long x3 = h3.start.x;
                    long y3 = h3.start.y;
                    long z3 = h3.start.z;


                    long a1 = h1.speed.x;
                    long b1 = h1.speed.y;
                    long c1 = h1.speed.z;

                    long a2 = h2.speed.x;
                    long b2 = h2.speed.y;
                    long c2 = h2.speed.z;

                    long a3 = h3.speed.x;
                    long b3 = h3.speed.y;
                    long c3 = h3.speed.z;

                    long xp1 = x1 + t1 * a1;
                    long yp1 = y1 + t1 * b1;
                    long zp1 = z1 + t1 * c1;

                    long xp2 = x2 + t2 * a2;
                    long yp2 = y2 + t2 * b2;
                    long zp2 = z2 + t2 * c2;

                    double a = (xp2-xp1)/(t2-t1);
                    double b = (yp2-yp1)/(t2-t1);
                    double c = (zp2-zp1)/(t2-t1);
                    
                    double x = xp1 - (a * t1);
                    double y = yp1 - (b * t1);
                    double z = zp1 - (c * t1);
                    
                    double t31 = (double)(x3 - x)/(a - a3);
                    double t32 = (double)(y3 - y)/(b - b3);
                    double t33 = (double)(z3 - z)/(c - c3);
                    
                    double diff = Math.abs(t32 - t31) + Math.abs(t33 - t32) + Math.abs(t33 - t31);
                    System.out.println(diff);
                    
//                    System.out.println("==============================");
//                    System.out.println(t31 + " " + t32 + " " + t33);
                    
                    if (t31 == t32 && t32 == t33) {
                        System.out.println(x + "," + y + "," + z + "   " + a + " " + b + " " + c);
//                        System.out.println();
                    }
                    
//                    double x = (t1 * x2 + t1 * t2 * a2 - t1 * t2 * a1 - t2 * x1) / (t1 - t2);
//                    double a =  (x1 - x) / t1 + a1;
//                    
//                    double y = (t1 * y2 + t1 * t2 * b2 - t1 * t2 * b1 - t2 * y1) / (t1 - t2);
//                    double b =  (y1 - y) / t1 + b1;
//                    
//                    double z = (t1 * z2 + t1 * t2 * c2 - t1 * t2 * c1 - t2 * z1) / (t1 - t2);
//                    double c =  (z1 - z) / t1 + c1;
                    
   
                }                
            }
        }
    }

    public Diff getDiff(long t1, long t2, List<Hail> hails) {
        if (t1 == t2) {
            return null;
        }
        Hail h1 = hails.get(0);
        Hail h2 = hails.get(1);
        Hail h3 = hails.get(2);

        long x1 = h1.start.x;
        long y1 = h1.start.y;
        long z1 = h1.start.z;

        long x2 = h2.start.x;
        long y2 = h2.start.y;
        long z2 = h2.start.z;

        long x3 = h3.start.x;
        long y3 = h3.start.y;
        long z3 = h3.start.z;


        long a1 = h1.speed.x;
        long b1 = h1.speed.y;
        long c1 = h1.speed.z;

        long a2 = h2.speed.x;
        long b2 = h2.speed.y;
        long c2 = h2.speed.z;

        long a3 = h3.speed.x;
        long b3 = h3.speed.y;
        long c3 = h3.speed.z;

        long xp1 = x1 + t1 * a1;
        long yp1 = y1 + t1 * b1;
        long zp1 = z1 + t1 * c1;

        long xp2 = x2 + t2 * a2;
        long yp2 = y2 + t2 * b2;
        long zp2 = z2 + t2 * c2;

        double a = (xp2-xp1)/(t2-t1);
        double b = (yp2-yp1)/(t2-t1);
        double c = (zp2-zp1)/(t2-t1);
        
        double x = xp1 - (a * t1);
        double y = yp1 - (b * t1);
        double z = zp1 - (c * t1);
        
        double t31 = (double)(x3 - x)/(a - a3);
        double t32 = (double)(y3 - y)/(b - b3);
        double t33 = (double)(z3 - z)/(c - c3);
        
        double diff = Math.abs(t32 - t31) + Math.abs(t33 - t32) + Math.abs(t33 - t31);
        return new Diff(diff, t1, t2);
    }
    
    static Pos3d rotx(Pos3L pos, double sin, double cos) {
        return new Pos3d(pos.x, (long)(pos.y * cos - pos.z * sin), (pos.z * cos - pos.z * sin));
    }

    static Pos3d rotx(Pos3d pos, double sin, double cos) {
        return new Pos3d(pos.x, (long)(pos.y * cos - pos.z * sin), (pos.z * cos - pos.z * sin));
    }

    static Pos3d roty(Pos3L pos, double sin, double cos) {
        return new Pos3d((pos.x * cos + pos.z * sin), pos.y, (pos.z * cos - pos.x * sin));
    }

    static Pos3d roty(Pos3d pos, double sin, double cos) {
        return new Pos3d((pos.x * cos + pos.z * sin), pos.y, (pos.z * cos - pos.x * sin));
    }

    static Pos3d rotz(Pos3d pos, double sin, double cos) {
        return new Pos3d((pos.x * cos  - pos.y * sin), (pos.x * sin + pos.y * cos), pos.z);
    }


    public static Haild rotx(Hail h, double sin, double cos) {
        return new Haild(rotx(h.start, sin, cos), rotx(h.speed, sin, cos));
    }

    public static Haild rotx(Haild h, double sin, double cos) {
        return new Haild(rotx(h.start, sin, cos), rotx(h.speed, sin, cos));
    }

    public static Haild roty(Hail h, double sin, double cos) {
        return new Haild(roty(h.start, sin, cos), roty(h.speed, sin, cos));
    }

    public static Haild roty(Haild h, double sin, double cos) {
        return new Haild(roty(h.start, sin, cos), roty(h.speed, sin, cos));
    }
    

    public static double getCos(long a) {
        return Math.signum(a) * (Math.abs(a)/Math.sqrt(Math.abs(a) + 1));
//        return Math.signum(a) * ((Math.PI/2)-(Math.abs(a)/Math.sqrt(Math.abs(a) + 1)));
    }

    public static double getCos(double a) {
        return Math.signum(a) * (Math.abs(a)/Math.sqrt(Math.abs(a) + 1));
//        return Math.signum(a) * ((Math.PI/2)-(Math.abs(a)/Math.sqrt(Math.abs(a) + 1)));
    }


    public static double getSin(long a) {
        return Math.signum(a) * (1/Math.sqrt(Math.abs(a) + 1));
//        return Math.signum(a) * ((Math.PI/2)-(1/Math.sqrt(Math.abs(a) + 1)));
    }

    public static double getSin(double a) {
        return Math.signum(a) * (1/Math.sqrt(Math.abs(a) + 1));
//        return Math.signum(a) * ((Math.PI/2)-(1/Math.sqrt(Math.abs(a) + 1)));
    }


    public static double getCos90(long a) {
//        return Math.signum(a) * (Math.abs(a)/Math.sqrt(Math.abs(a) + 1));
        return Math.signum(a) * ((Math.PI/2)-(Math.abs(a)/Math.sqrt(Math.abs(a) + 1)));
    }

    public static double getSin90(long a) {
//        return Math.signum(a) * (1/Math.sqrt(Math.abs(a) + 1));
        return Math.signum(a) * ((Math.PI/2)-(1/Math.sqrt(Math.abs(a) + 1)));
    }

    public static double getCos90(double a) {
//      return Math.signum(a) * (Math.abs(a)/Math.sqrt(Math.abs(a) + 1));
      return Math.signum(a) * ((Math.PI/2)-(Math.abs(a)/Math.sqrt(Math.abs(a) + 1)));
  }

  public static double getSin90(double a) {
//      return Math.signum(a) * (1/Math.sqrt(Math.abs(a) + 1));
      return Math.signum(a) * ((Math.PI/2)-(1/Math.sqrt(Math.abs(a) + 1)));
  }


//  public static Pos3d rot(Pos3d v, Pos3d how) {
//      Pos3d p1 = rotx(v, getSin90((double)how.y/how.z), getCos90((double)how.y/how.z));
//      Pos3d how1 = rotx(how, getSin90((double)how.y/how.z), getCos90((double)how.y/how.z));
//      Pos3d p2 = rotz(p1, getSin90((double)how1.y/how1.x), getCos90((double)how1.y/how1.x));
//      return p2;
//  }

  public static Pos3d rot(Pos3L v, Pos3d how) {
      Pos3d p1 = rotx(v, getSin90((double)how.y/how.z), getCos90((double)how.y/how.z));
//      Pos3d how1 = rotx(how, getSin90((double)how.y/how.z), getCos90((double)how.y/how.z));
      Pos3d p2 = roty(p1, getSin90((double)how.z/how.x), getCos90((double)how.z/how.x));
      return p2;
  }

  public static Haild rot(Hail h, Pos3d how) {
      Pos3d start = rot(h.start, how);
      Pos3d speed = rot(h.speed, how);
      return new Haild(start, speed);
  }

  

//    public static Pos3d rot(Pos3d what, Pos3L how) {
//        Pos3d p1 = rotx(what, getSin(how.y/how.z), getCos(how.y/how.z));
//        Pos3d p2 = rotz(p1, getSin(how.y/how.z), getCos(how.y/how.z));
//        return p2;
//    }
//    
    
    public static void main(String[] args) throws Exception {
//
//        Pos3d v = new Pos3d(2.5, 10, 6.5);
//        Pos3d p1 = rotx(v, getSin90(v.y/v.z), getCos90(v.y/v.z));
//////        Pos3d p1 = rotx(v, getSin90(v.y/v.z), getCos90(v.y/v.z));
//        Pos3d p2 = rotz(p1, getSin90(v.x/v.y), getCos90(v.x/v.y));
//        System.out.println(p1 + " " + p2);
        
//        System.out.println(rot(new Pos3d(2.5, 10, 6.5), new Pos3d(2.5, 10, 6.5)));
        
        
//        Hail h = new Hail(new Pos3L(24, 13, 10), new Pos3L(-3, 1, 2));
//        
//        System.out.println(h);
//        System.out.println(getSin(h.speed.x));
//        System.out.println(getCos(h.speed.x));
//        System.out.println(getSin(h.speed.y));
//        System.out.println(getCos(h.speed.y));
//        System.out.println(getSin(h.speed.z));
//        System.out.println(getCos(h.speed.z));
//        
//        Pos3d p1 = rotx(h.speed, getSin((double)h.speed.y/h.speed.z), getCos((double)h.speed.y/h.speed.z));
//        Pos3d p2 = rotz(p1, getSin((double)h.speed.y/h.speed.x), getCos((double)h.speed.y/h.speed.x));
//        System.out.println(p1 + " " + p2);
////        System.out.println(roty(rotx(h.speed, getSin(h.speed.y), getCos(h.speed.y)), getSin(h.speed.x), getCos(h.speed.x)));
        
        new Task24().run();
        
//        -3, 1, 2
       // sin px = Math.sqrt(Math.abs(a) + 1)
       // cos px = Math.sqrt(Math.abs(a) + 1)/a
        
        
        
//        System.out.println("rot x = " + );             
        
        
    }

}
