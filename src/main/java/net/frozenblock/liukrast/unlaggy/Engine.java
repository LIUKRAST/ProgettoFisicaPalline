package main.java.net.frozenblock.liukrast.unlaggy;

import main.java.net.frozenblock.liukrast.Colors;
import main.java.net.frozenblock.liukrast.asset.PallineHandler;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Engine extends Canvas implements Runnable, Colors {
    public static final int WIDTH = 1920/2, HEIGHT = WIDTH / 16 * 9;
    private Thread thread;
    private boolean running = false;
    private boolean engineRunning = false;
    private PallineHandler pallineHandler;
    private long repeatTime = 0;
    private long time;
    private final Window window;
    private ExecutionAlgorithm algorithm = ExecutionAlgorithm.BACKTRACING;
    private ArrayList<Float> rightRunValues = new ArrayList<>();
    private ArrayList<Float> leftRunValues = new ArrayList<>();
    public int roffset;
    public int loffset;

    public Engine() {
        this.window = new Window(WIDTH, HEIGHT, "Palline",this);
        pallineHandler = new PallineHandler(2);
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        while(running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1) {
                tick();
                delta--;
            }
            if(running)
                render();
            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
            }
        }
        stop();
    }

    public void initGenerator(PallineHandler handler, ExecutionMode executionMode, long loops, ExecutionAlgorithm algorithm) {
        this.loffset = 0;
        this.roffset = 0;
        this.rightRunValues = new ArrayList<>();
        this.leftRunValues = new ArrayList<>();
        this.pallineHandler = handler;
        long stloops = loops;
        if(executionMode == ExecutionMode.AUTO) {
            loops = handler.combinations().longValue() + 1;
            stloops = handler.combinations().longValue();
        }
        repeatTime = loops;
        this.algorithm = algorithm;
        System.out.println(CYAN + "Initializing Engine!\n {\n"+ PURPLE +" \"RunLoops\": " + stloops + ";\n \"ExecutionMode\": " + executionMode.name() + ";\n \"Algorithm\": " + algorithm.name() + ";\n \"Size\": " + handler.getPalline().size() + ";" + CYAN + "\n }");
        engineRunning = true;
    }

    public static long parseLong(String s) {
        long l = 0;
        try {
            l = Long.parseLong(s);
        } catch (Exception e) {
            System.out.println(RED + "[ERROR] " + RESET + "Ignore this if you are running in ExecutionMode.AUTO, this might be caused by an invalid input");
        }
        return l;
    }

    public void tick() {
        if(engineRunning && repeatTime > 0 && pallineHandler != null) {
            if(algorithm == ExecutionAlgorithm.BACKTRACING) {
                if (pallineHandler.isBalanced()) {
                    System.out.println(pallineHandler.parseString());
                    rightRunValues.add(pallineHandler.percentage(true));
                    leftRunValues.add(pallineHandler.percentage(false));
                }
                boolean b = pallineHandler.increase(false);
                repeatTime--;
                if (!b) {
                    repeatTime = -1;
                }
            } else if(algorithm == ExecutionAlgorithm.HARD_BACKTRACING) {
                if (pallineHandler.isBalanced()) {
                    System.out.println(pallineHandler.parseString());
                    rightRunValues.add(pallineHandler.percentage(true));
                    leftRunValues.add(pallineHandler.percentage(false));
                }
                boolean b = pallineHandler.increase(false);
                repeatTime--;
                if (!b) {
                    repeatTime = -1;
                }
            } else {
                pallineHandler.switchRandom();
                System.out.println(pallineHandler.parseString());
                rightRunValues.add(pallineHandler.percentage(true));
                leftRunValues.add(pallineHandler.percentage(false));
                repeatTime--;
            }
        }
    }

    public void render() {
        time++;
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        for(int x = 0; x < window.WIDTH; x++) {
            for(int y = 0; y < window.HEIGTH; y++) {
                g.setColor(window.render((float)x/(float)window.WIDTH,(float)y/(float)window.HEIGTH,time).toColor());
                g.fillRect(x,y,1,1);
            }
        }
        g.setColor(Color.gray);
        g.fillRect(60, window.HEIGTH/2, Math.min(3* rightRunValues.size(), window.WIDTH - 120), 200*window.HEIGTH/1080 + 3);
        g.fillRect(60, window.HEIGTH/2 + 200*window.HEIGTH/1080 + 10, Math.min(3*leftRunValues.size(), window.WIDTH - 120), 200*window.HEIGTH/1080 + 3);
        g.setColor(Color.green);
        int ll = leftRunValues.size() - (window.WIDTH - 120)/3;
        if(ll < loffset) loffset = Math.max(0, loffset - 3);
        int lr = rightRunValues.size() - (window.WIDTH - 120)/3;
        if(lr < roffset) roffset = Math.max(0, loffset - 3);
        for(int i = loffset; i < Math.min(leftRunValues.size(), (window.WIDTH - 120)/3 + loffset); i++) {
            g.fillRect(60 + (i-loffset)*3, fract(window.HEIGTH/2, 200*window.HEIGTH/1080, leftRunValues.get(i)), 3, 3);
        }
        g.setColor(Color.red);
        for(int i = roffset; i < Math.min(rightRunValues.size(), (window.WIDTH - 120)/3 + roffset); i++) {
            g.fillRect(60 + (i-roffset)*3, fract(window.HEIGTH/2, 200*window.HEIGTH/1080, rightRunValues.get(i)) + 200*window.HEIGTH/1080 + 10, 3, 3);
        }
        g.dispose();
        bs.show();
    }

    private static int fract(int y, int height, float f) {
        return (int) (f*(float)height + (float) y);
    }

    public enum ExecutionMode {
        AUTO,
        INPUT
    }
    public enum ExecutionAlgorithm {
        BACKTRACING,
        HARD_BACKTRACING,
        RANDOM
    }
}
