package main.java.net.frozenblock.liukrast.unlaggy;

import main.java.net.frozenblock.liukrast.asset.BinBox;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.math.BigInteger;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class Engine extends Canvas implements Runnable {

    public static final int WIDTH = 1920/2, HEIGHT = WIDTH / 16 * 9;
    private Thread thread;
    private boolean running = false;

    int offset = 0;

    private BinBox binBox;

    Window window;

    ArrayList<Float> right_diagram = new ArrayList<>();
    ArrayList<Float> left_diagram = new ArrayList<>();

    public Engine(int numeroPalline) {
        this.window = new Window(WIDTH, HEIGHT, "Engine", this);
        binBox = new BinBox(numeroPalline);
        System.out.println(combinations(binBox.getBALLS().size() / 2) + " combinazioni stanno per essere generate dal programma");
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
        if(binBox.isBalanced()) {
            System.out.println(binBox.parseString() + "; %: [S: " + binBox.getPercentage(false) + "; R: " + binBox.getPercentage(true) + ";]");
            left_diagram.add(binBox.getPercentage(false));
            right_diagram.add(binBox.getPercentage(true));
        }
        binBox.increase();
        if(binBox.count(true) == binBox.getBALLS().size()) {
            System.out.println(combinations(binBox.getBALLS().size() / 2) + " combinazioni generate");
            //System.exit(0);
            stop();
        }
    }

    private void render() {
        int leftSpacing = 30;
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.black);
        g.fillRect(0,0, 120000, 12000);

        g.setColor(Color.gray);
        g.fillRect(leftSpacing + offset,10, combinations(binBox.getBALLS().size() / 2).intValue() * 3,100);
        g.fillRect(leftSpacing + offset,120, combinations(binBox.getBALLS().size() / 2).intValue() * 3,100);

        for(int i = 0; i < left_diagram.size(); i++) {
            float p = left_diagram.get(i);
            g.setColor(Color.green);
            g.fillRect(leftSpacing + offset + i*3, 9 + (int)(p), 3, 3);
        }

        for(int i = 0; i < right_diagram.size(); i++) {
            float p = right_diagram.get(i);
            g.setColor(Color.red);
            g.fillRect(leftSpacing + offset + i*3, 119 + (int)(p), 3, 3);
        }

        g.dispose();
        bs.show();
    }

    public void recieveEvent(Event event) {
        switch (event) {
            case EVENT_RESET -> offset = 0;
            case EVENT_FORWARD -> offset += 16;
            case EVENT_BACKWARD -> offset -= 16;
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Quante palline vogliamo usare?");
        int numPalline = input.nextInt();
        while(numPalline % 2 != 0) {
            System.out.println("Il numero inserito non è pari. Inserisci nuovamente");
            System.out.print("Quante palline vogliamo usare?");
            numPalline = input.nextInt();
        }
        new Engine(numPalline/2);
    }

    public enum Event {
        EVENT_RESET,
        EVENT_FORWARD,
        EVENT_BACKWARD;
    }
}
