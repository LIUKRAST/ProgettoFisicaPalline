package main.java.net.frozenblock.liukrast.unlaggy;

import main.java.net.frozenblock.liukrast.Colors;
import main.java.net.frozenblock.liukrast.asset.FloatColor;
import main.java.net.frozenblock.liukrast.asset.PallineHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Objects;

public class Window extends Canvas implements Colors {
    public int WIDTH;
    public int HEIGTH;
    private Engine.ExecutionAlgorithm executionAlgorithm = Engine.ExecutionAlgorithm.BACKTRACING;
    private Engine.ExecutionMode executionMode = Engine.ExecutionMode.AUTO;
    public Window(int width, int height, String title, Engine engine) {
        this.WIDTH = width;
        this.HEIGTH = height;
        JFrame frame = new JFrame(title);
        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));

        //
        JButton pallineOrValues = new JButton("v");
        //
        JLabel label = new JLabel("Numero Palline: ");
        JTextField pallineInput = new JTextField();
        JLabel error = new JLabel("NullError");
        //
        JLabel label1 = new JLabel("Caldo: ");
        JTextField coldInput = new JTextField();
        JLabel label2 = new JLabel("Freddo: ");
        JTextField hotInput = new JTextField();
        //
        JButton timeMode = new JButton("Modalità: Auto");
        //
        JTextField timeInput = new JTextField();

        JButton run = new JButton("Genera");
        //
        JButton mode = new JButton("Modalità: Backtracing");

        //
        JButton nextRight = new JButton(">");
        JButton prevRight = new JButton("<");
        JButton reseRight = new JButton("x");
        JButton nextLeft = new JButton(">");
        JButton prevLeft = new JButton("<");
        JButton reseLeft = new JButton("x");
        //

        label1.setVisible(false);
        coldInput.setVisible(false);
        hotInput.setVisible(false);
        error.setVisible(false);
        timeInput.setVisible(false);

        pallineOrValues.addActionListener(e -> {
            if(Objects.equals(pallineOrValues.getText(), "v")) {
                pallineOrValues.setText("x");
                label.setVisible(false);
                pallineInput.setVisible(false);
                label1.setVisible(true);
                coldInput.setVisible(true);
                hotInput.setVisible(true);
            } else {
                pallineOrValues.setText("v");
                label.setVisible(true);
                pallineInput.setVisible(true);
                label1.setVisible(false);
                coldInput.setVisible(false);
                hotInput.setVisible(false);
            }
        });

        timeMode.addActionListener(e -> {
            if(Objects.equals(timeMode.getText(), "Modalità: Auto")) {
                timeMode.setText("Modalità: Input");
                timeInput.setVisible(true);
                executionMode = Engine.ExecutionMode.INPUT;
            } else {
                timeMode.setText("Modalità: Auto");
                timeInput.setVisible(false);
                executionMode = Engine.ExecutionMode.AUTO;
            }
        });

        run.addActionListener(e -> {
            if(Objects.equals(pallineOrValues.getText(), "v")) {
                engine.initGenerator(new PallineHandler(Integer.parseInt(pallineInput.getText())), executionMode, Engine.parseLong(timeInput.getText()), executionAlgorithm);
            } else {
                engine.initGenerator(new PallineHandler(Integer.parseInt(hotInput.getText()), Integer.parseInt(coldInput.getText())), executionMode, Engine.parseLong(timeInput.getText()), executionAlgorithm);
            }
        });

        pallineOrValues.setBounds(WIDTH - 400, 50, 50, 20);
        label.setBounds(pallineOrValues.getBounds().x + pallineOrValues.getBounds().width, pallineOrValues.getY(), 100, 20);
        pallineInput.setBounds(label.getBounds().x + label.getBounds().width, pallineOrValues.getY(), 100, 20);
        error.setBounds(pallineInput.getBounds().x + pallineInput.getBounds().width, pallineOrValues.getY(), 100, 20);
        label1.setBounds(pallineOrValues.getBounds().x + pallineOrValues.getBounds().width, pallineOrValues.getY(), 50, 20);
        coldInput.setBounds(label1.getBounds().x + label1.getBounds().width, pallineOrValues.getY(), 50, 20);
        label2.setBounds(coldInput.getBounds().x + coldInput.getBounds().width, pallineOrValues.getY(), 50, 20);
        hotInput.setBounds(label2.getBounds().x + label2.getBounds().width, pallineOrValues.getY(), 50, 20);
        timeMode.setBounds(WIDTH - 400, 100,200, 20);
        timeInput.setBounds(timeMode.getBounds().x  + timeMode.getBounds().width, timeMode.getY(), 100, 20);
        run.setBounds(WIDTH - 400, 200, 300, 20);
        mode.setBounds(WIDTH - 400, 150, 300, 20);

        nextRight.setBounds(0, HEIGTH/2, 50, (200*HEIGTH/1080)/3);
        prevRight.setBounds(0, HEIGTH/2 + (200*HEIGTH/1080)/3, 50, (200*HEIGTH/1080)/3);
        reseRight.setBounds(0, HEIGTH/2 + 2*(200*HEIGTH/1080)/3, 50, (200*HEIGTH/1080)/3);

        nextLeft.setBounds(0, HEIGTH/2 + 200*HEIGTH/1080 + 10, 50, (200*HEIGTH/1080)/3);
        prevLeft.setBounds(0, HEIGTH/2 + 200*HEIGTH/1080 + 10 + (200*HEIGTH/1080)/3, 50, (200*HEIGTH/1080)/3);
        reseLeft.setBounds(0, HEIGTH/2 + 200*HEIGTH/1080 + 10 + 2*(200*HEIGTH/1080)/3, 50, (200*HEIGTH/1080)/3);


        mode.setVisible(true);
        run.setVisible(true);

        frame.add(pallineOrValues);
        frame.add(label);
        frame.add(pallineInput);
        frame.add(error);

        frame.add(label1);
        frame.add(coldInput);
        frame.add(label2);
        frame.add(hotInput);

        frame.add(timeMode);
        frame.add(timeInput);

        frame.add(run);
        frame.add(mode);

        frame.add(nextRight);
        frame.add(prevRight);
        frame.add(reseRight);

        frame.add(nextLeft);
        frame.add(prevLeft);
        frame.add(reseLeft);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.add(engine);
        frame.setVisible(true);
        engine.start();

        mode.addActionListener(e -> {
            if(Objects.equals(mode.getText(), "Modalità: Backtracing")) {
                mode.setText("Modalità: Backtracing Lento");
                executionAlgorithm = Engine.ExecutionAlgorithm.HARD_BACKTRACING;
            } else if(Objects.equals(mode.getText(), "Modalità: Backtracing Lento")) {
                mode.setText("Modalità: Random");
                executionAlgorithm = Engine.ExecutionAlgorithm.RANDOM;
            } else {
                mode.setText("Modalità: Backtracing");
                executionAlgorithm = Engine.ExecutionAlgorithm.BACKTRACING;
            }
        });

        int speed = 3;

        nextLeft.addActionListener(e -> engine.roffset+=speed);
        prevLeft.addActionListener(e -> engine.roffset = Math.max(0, engine.roffset - speed));
        reseLeft.addActionListener(e -> engine.roffset = 0);

        nextRight.addActionListener(e -> engine.loffset+=speed);
        prevRight.addActionListener(e -> engine.loffset = Math.max(0, engine.loffset - speed));
        reseRight.addActionListener(e -> engine.loffset = 0);


        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                WIDTH = frame.getWidth();
                HEIGTH = frame.getHeight();

                pallineOrValues.setBounds(WIDTH - 400, 50, 50, 20);
                label.setBounds(pallineOrValues.getBounds().x + pallineOrValues.getBounds().width, pallineOrValues.getY(), 100, 20);
                pallineInput.setBounds(label.getBounds().x + label.getBounds().width, pallineOrValues.getY(), 100, 20);
                error.setBounds(pallineInput.getBounds().x + pallineInput.getBounds().width, pallineOrValues.getY(), 100, 20);
                label1.setBounds(pallineOrValues.getBounds().x + pallineOrValues.getBounds().width, pallineOrValues.getY(), 50, 20);
                coldInput.setBounds(label1.getBounds().x + label1.getBounds().width, pallineOrValues.getY(), 50, 20);
                label2.setBounds(coldInput.getBounds().x + coldInput.getBounds().width, pallineOrValues.getY(), 50, 20);
                hotInput.setBounds(label2.getBounds().x + label2.getBounds().width, pallineOrValues.getY(), 50, 20);
                timeMode.setBounds(WIDTH - 400, 100,200, 20);
                timeInput.setBounds(timeMode.getBounds().x  + timeMode.getBounds().width, timeMode.getY(), 100, 20);
                run.setBounds(WIDTH - 400, 200, 300, 20);
                mode.setBounds(WIDTH - 400, 150, 300, 20);

                nextRight.setBounds(0, HEIGTH/2, 50, (200*HEIGTH/1080)/3);
                prevRight.setBounds(0, HEIGTH/2 + (200*HEIGTH/1080)/3, 50, (200*HEIGTH/1080)/3);
                reseRight.setBounds(0, HEIGTH/2 + 2*(200*HEIGTH/1080)/3, 50, (200*HEIGTH/1080)/3);

                nextLeft.setBounds(0, HEIGTH/2 + 200*HEIGTH/1080 + 10, 50, (200*HEIGTH/1080)/3);
                prevLeft.setBounds(0, HEIGTH/2 + 200*HEIGTH/1080 + 10 + (200*HEIGTH/1080)/3, 50, (200*HEIGTH/1080)/3);
                reseLeft.setBounds(0, HEIGTH/2 + 200*HEIGTH/1080 + 10 + 2*(200*HEIGTH/1080)/3, 50, (200*HEIGTH/1080)/3);

                pallineOrValues.updateUI();
                label.updateUI();
                pallineInput.updateUI();
                error.updateUI();
                label1.updateUI();
                coldInput.updateUI();
                label2.updateUI();
                hotInput.updateUI();
                timeMode.updateUI();
                timeInput.updateUI();
                run.updateUI();
                mode.updateUI();

                nextRight.updateUI();
                prevRight.updateUI();
                reseRight.updateUI();
                nextLeft.updateUI();
                prevLeft.updateUI();
                reseLeft.updateUI();
            }
        });
    }

    public FloatColor render(float x, float y, long time) {
        float r = 0.5f + 0.5f * cos(time/20f + x);
        float g = 0.5f + 0.5f * cos(time/20f + y + 2);
        float b = 0.5f + 0.5f * cos(time/20f + x + 4);
        return new FloatColor(r,g,b);
    }

    private static float cos(float x) {
        return (float) Math.cos(x);
    }
}
