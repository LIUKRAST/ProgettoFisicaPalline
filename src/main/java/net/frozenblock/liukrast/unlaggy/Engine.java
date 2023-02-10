package main.java.net.frozenblock.liukrast.unlaggy;

import main.java.net.frozenblock.liukrast.asset.BinBox;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.math.BigInteger;

public class Engine extends Canvas implements Runnable {

    public static final int WIDTH = 1920/2, HEIGHT = WIDTH / 16 * 9;
    private Thread thread;
    private boolean running = false;

    private BinBox binBox;

    public Engine(int numeroPalline) {
        new Window(WIDTH, HEIGHT, "Engine", this);
        binBox = new BinBox(numeroPalline);
        System.out.println(combinations(binBox.getBALLS().size() / 2) + " combinazioni stanno per essere generate dal programma");
    }

    private long tick = 0;

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
                //System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
        stop();
    }

    public static BigInteger combinations(int n) {
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

    private void tick() {
        tick++;
        if(binBox.isBalanced()) {
            System.out.println(binBox.parseString() + "; %: [S: " + binBox.getPercentage(false) + "; R: " + binBox.getPercentage(true) + ";]");
        }
        binBox.increase();
        if(binBox.count(true) == binBox.getBALLS().size()) {
            System.out.println(combinations(binBox.getBALLS().size() / 2) + " combinazioni generate");
            stop();
        }
    }
    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.black);
        g.fillRect(0,0,WIDTH, HEIGHT);

        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        new Engine(2);
    }
}
