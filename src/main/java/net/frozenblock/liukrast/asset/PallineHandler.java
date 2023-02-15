package main.java.net.frozenblock.liukrast.asset;

import main.java.net.frozenblock.liukrast.Colors;

import java.math.BigInteger;
import java.util.ArrayList;

public class PallineHandler implements Colors {
    private int cold;
    private int hot;
    ArrayList<Boolean> palline;
    public PallineHandler(int cold, int hot) {
        this.cold = cold;
        this.hot = hot;
        palline = new ArrayList<>();
        for(int i = 0; i < hot; i++) {
            palline.add(true);
        }
        for(int i = 0; i < cold; i++) {
            palline.add(false);
        }
    }
    public PallineHandler(int each) {
        this(each, each);
    }

    public void switchRandom() {
        int tot = palline.size()/2;
        int rndm1 = (int) (Math.random() * tot);
        int rndm2 = (int) (Math.random() * tot) + tot;
        switchTwo(rndm1, rndm2);
    }

    public void switchTwo(int pos1, int pos2) {
        boolean one = palline.get(pos1);
        boolean two = palline.get(pos2);
        palline.set(pos1, two);
        palline.set(pos2, one);
    }

    public ArrayList<Boolean> getPalline() {
        return palline;
    }

    public boolean increase(boolean hard) {
        for(int i = 0; i < this.palline.size(); i++) {
            if(!this.palline.get(i)) {
                this.palline.set(i, true);
                for(int k = 0; k < i; k++) {
                    this.palline.set(k, false);
                }
                break;
            }
        }
        if(!isBalanced() && !hard) {
            if(count(true) == palline.size()) {
                return false;
            }
            increase(false);
        }
        return true;
    }

    public int count(boolean hot) {
        int c = 0;
        for (Boolean aBoolean : palline) {
            if (aBoolean == hot) c++;
        }
        return c;
    }


    public BigInteger combinations() {
        int n = (cold + hot)/2;
        BigInteger numerator = factorial(2 * n);
        BigInteger denominator = factorial(n).pow(2);
        return numerator.divide(denominator);
    }

    private static BigInteger factorial(int n) {
        BigInteger result = BigInteger.valueOf(1);
        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    public boolean isBalanced() {
        return count(true) == hot && count(false) == cold;
    }
    public static String name(boolean v) {
        if(v) return RED + "H";
        return BLUE + "C";
    }
    public String parseString() {
        StringBuilder r = new StringBuilder();
        for(int i = 0; i < palline.size(); i++) {
            if(i == palline.size()/2) r.append(" ");
            r.append(name(palline.get(i))).append(RESET);
        }
        return String.valueOf(r);
    }

    public float percentage(boolean right) {
        float hot = 0;
        if(!right) {
            for(int i = 0; i < palline.size()/2; i++) {
                if(palline.get(i)) hot++;
            }
        } else {
            for(int i = 0; i < palline.size()/2; i++) {
                if(palline.get(i + palline.size()/2)) hot++;
            }
        }
        return hot/(palline.size()/2f);
    }
}
