package main.java.net.frozenblock.liukrast.asset;

import main.java.net.frozenblock.liukrast.Colors;

import java.util.ArrayList;

public class BinBox implements Colors {
    private final ArrayList<Boolean> BALLS = new ArrayList<>();
    private final int cold;
    private final int hot;

    public BinBox(int cold, int hot) {
        for(int i = 0; i < cold + hot; i++) {
            BALLS.add(false);
        }
        this.cold = cold;
        this.hot = hot;
    }

    public BinBox(int eachBox) {
        this(eachBox, eachBox);
    }

    public boolean isBalanced() {
        if(count(true) == hot && count(false) == cold) {
            return true;
        }
        return false;
    }

    public void increase() {
        boolean cont = false;
        for (Boolean ball : this.BALLS) {
            if (!ball) {
                cont = true;
                break;
            }
        }
        if(!cont) {
            System.err.println("Unable to add, max amount reached");
            return;
        }
        for(int i = 0; i < this.BALLS.size(); i++) {
            if(!this.BALLS.get(i)) {
                this.BALLS.set(i, true);
                for(int k = 0; k < i; k++) {
                    this.BALLS.set(k, false);
                }
                break;
            }
        }
    }

    public String parseString() {
        StringBuilder r = new StringBuilder();
        for(int i = 0; i < BALLS.size(); i++) {
            if(i == BALLS.size()/2) r.append(" ");
            r.append(name(BALLS.get(i))).append(RESET);
        }
        return String.valueOf(r);
    }

    public int count(boolean value) {
        int r = 0;
        for (Boolean ball : this.BALLS) {
            if (ball == value) r++;
        }
        return r;
    }

    public ArrayList<Boolean> getBALLS() {
        return this.BALLS;
    }

    public float getPercentage(boolean r) {
        if(!r) {
            float left = 0;
            for(int i = 0; i < BALLS.size()/2; i++) {
                if(BALLS.get(i)) left++;
            }
            return left / ((float)BALLS.size()/2) * 100;
        } else {
            float p = getPercentage(false);
            return 100 - p;
        }
    }

    public static String name(boolean v) {
        if(v) return RED + "H";
        return BLUE + "C";
    }
}
