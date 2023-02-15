package main.java.net.frozenblock.liukrast.unlaggy;

import main.java.net.frozenblock.liukrast.Colors;
import main.java.net.frozenblock.liukrast.asset.PallineHandler;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Engine extends Canvas implements Runnable, Colors {
    public static final int WIDTH = 1920/2, HEIGHT = WIDTH / 16 * 9; // Static values to init the window size. Will not update later when you resize the window
    private Thread thread; // Just a thread

    private boolean running = false; // Defines if the machine is running
    private boolean engineRunning = false; // Defines if the generator is running
    private PallineHandler pallineHandler; // Class that defines a set of balls and his order, with also some other properties
    private long repeatTime = 0; // Repeats left before stopping
    private long time; // engine runTime, for a graphic purpose
    private final Window window; // Our machine's window
    private ExecutionAlgorithm algorithm = ExecutionAlgorithm.BACKTRACING; // Default algorithm is BACKTRACING, find more at the end of the class
    private ArrayList<Float> rightRunValues = new ArrayList<>(); // All the results on the right after generation happened
    private ArrayList<Float> leftRunValues = new ArrayList<>(); // All the left results
    public int roffset; // Offset for rendering, when you click the arrows to move the graph
    public int loffset; // Same but for left
    public int fps = 0; // Frame Per Second
    boolean lagless = false; // Lagless mode, find more later

    float pr = 0; // a sum of all the rightRunValues divided by the number of values
    float pl = 0; // Same for left

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
        int frames = 0;
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
            frames++;
            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                fps = frames;
                frames = 0;
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
        float r = 0;
        float l = 0;
        for (Float rightRunValue : rightRunValues) {
            r += rightRunValue;
        }
        for (Float leftRunValue : leftRunValues) {
            l += leftRunValue;
        }
        pr = r/(float) rightRunValues.size();
        pl = l/(float) leftRunValues.size();
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
        /*
        * I didnt use any graphic engine on the program, so it uses the CPU also for rendering stuff. This means that you will probably
        * only have 5/9 FPS without lagless mode, while more than 1500fps with the lagless mode. Next time im gonna be using LWJGL (Aka GlSL)
        * */
        time++;
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.darkGray);
        g.fillRect(0,0, window.WIDTH, window.HEIGTH);
        if(!lagless) {
            for (int x = 0; x < window.WIDTH; x++) {
                for (int y = 0; y < window.HEIGTH; y++) {
                    g.setColor(window.render((float) x / (float) window.WIDTH, (float) y / (float) window.HEIGTH, time).toColor());
                    g.fillRect(x, y, 1, 1);
                }
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
        g.setColor(Color.black);
        String pr1 = (pr * 100) + "%";
        g.drawString(pr1, window.WIDTH - 60 - pr1.length()*10, window.HEIGTH/2 - 5);
        String pl1 = (pl * 100) + "%";
        g.drawString(pl1, window.WIDTH - 60 - pr1.length()*10, window.HEIGTH/2 + 400*window.HEIGTH/1080 + 25);
        g.drawString("FPS: " + fps, 10, 20);
        g.dispose();
        bs.show();
    }

    public void setLagless(boolean i) {
        lagless = i;
    }

    private static int fract(int y, int height, float f) {
        return (int) (f*(float)height + (float) y);
    }

    /**
     * ExecutionMode defines how the machine should calculate how many times to repeat the process
     *
     * Auto: calculate how many combinations the current PallineHandler has, and then repeat the engine with that number of times
     * Input: Requires an input from the user*/

    public enum ExecutionMode {
        AUTO,
        INPUT
    }
    /**
     * The algorithm used to find next values
     * BACKTRACING: A fast way to loop ALL the possibilities. May cause some crashes for very big nubers
     * HARD_BACKTRACING: A raw version of the backtracing method. Will not cause crashes, but its a way slower
     * RANDOM: Swaps randomly the objects!*/
    public enum ExecutionAlgorithm {
        BACKTRACING,
        HARD_BACKTRACING,
        RANDOM
    }
}
