package mediator;

import javax.swing.*;
import java.awt.*;

public class Dashboard {
    // main frame
    private JFrame frame;
    // runway status label
    private JLabel runwayStatusLabel;
    // queue lengths label
    private JLabel queueLengthsLabel;
    // log text area
    private JTextArea logArea;

    public Dashboard() {
        // create frame
        frame = new JFrame("Airport Tower Dashboard");
        // set layout
        frame.setLayout(new BorderLayout());
        // set size
        frame.setSize(400, 300);
        // set close operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}}
