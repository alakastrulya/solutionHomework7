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
        // create runway status label
        runwayStatusLabel = new JLabel("Runway: Free");
        // create queue lengths label
        queueLengthsLabel = new JLabel("Landing Queue: 0 | Takeoff Queue: 0");
        // create log area
        logArea = new JTextArea();
        // make log read-only
        logArea.setEditable(false);
        // create scroll pane for log
        JScrollPane scrollPane = new JScrollPane(logArea);

        // create status panel
        JPanel statusPanel = new JPanel();
        // set layout for status panel
        statusPanel.setLayout(new GridLayout(2, 1));
        // add runway status
        statusPanel.add(runwayStatusLabel);
        // add queue lengths
        statusPanel.add(queueLengthsLabel);

        // add status panel to frame
        frame.add(statusPanel, BorderLayout.NORTH);
        // add scroll pane to frame
        frame.add(scrollPane, BorderLayout.CENTER);

        // make frame visible
        frame.setVisible(true);
    }

    // updates runway status
    public void updateRunwayStatus(String status) {
        // run on edt
        SwingUtilities.invokeLater(() -> {
            // update label
            runwayStatusLabel.setText("Runway: " + status);
        });
    }

    // updates queue lengths
    public void updateQueueLengths(int landing, int takeoff) {
        // run on edt
        SwingUtilities.invokeLater(() -> {
            // update label
            queueLengthsLabel.setText("Landing Queue: " + landing + " | Takeoff Queue: " + takeoff);
        });
    }

    // adds log message
    public void addLogMessage(String message) {
        // run on edt
        SwingUtilities.invokeLater(() -> {
            // append message
            logArea.append(message + "\n");
            // scroll to bottom
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
}
