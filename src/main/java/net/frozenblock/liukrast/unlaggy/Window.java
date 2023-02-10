package main.java.net.frozenblock.liukrast.unlaggy;

import javax.swing.*;
import java.awt.*;

public class Window extends Canvas {
    public Window(int width, int height, String title, Engine engine) {
        JFrame frame = new JFrame(title);
        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));

        JButton resetPos = new JButton("[]");
        resetPos.setBounds(20, 300, 40, 40);
        JButton forward = new JButton(">");
        forward.setBounds(80, 300, 40, 40);
        JButton backward = new JButton("<");
        backward.setBounds(140, 300, 40, 40);

        resetPos.addActionListener(e -> {
            engine.recieveEvent(Engine.Event.EVENT_RESET);
        });
        forward.addActionListener(e -> {
            engine.recieveEvent(Engine.Event.EVENT_FORWARD);
        });
        backward.addActionListener(e -> {
            engine.recieveEvent(Engine.Event.EVENT_BACKWARD);
        });

        frame.add(resetPos);
        frame.add(backward);
        frame.add(forward);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.add(engine);
        frame.setVisible(true);
        engine.start();
    }
}
