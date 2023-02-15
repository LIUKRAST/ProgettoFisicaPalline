package main.java.net.frozenblock.liukrast.asset;

import java.awt.*;

public class FloatColor {
    private final float r,g,b;
    public FloatColor(float r, float g, float b) {
        if(r>1) System.err.println("RED Out of Bounds: " + r);
        if(g>1) System.err.println("GREEN Out of Bounds: " + g);
        if(b>1) System.err.println("BLUE Out of Bounds: " + b);
        this.r = r;
        this.g = g;
        this.b = b;
    }
    public Color toColor() {
        return new Color((int)(r * 255), (int)(g * 255), (int)(b * 255));
    }
}
