package mediator.ui;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
// swing dashboard for simulator
public class Dashboard {
    // main frame
    private JFrame frame;
    // runway status label
    private JLabel runwayStatusLabel;
    // runway visual panel
    private JPanel runwayVisualPanel;
    // landing queue progress bar
    private JProgressBar landingQueueBar;
    // takeoff queue progress bar
    private JProgressBar takeoffQueueBar;
    // log text pane
    private JTextPane logPane;
    // simulation status label
    private JLabel simulationStatusLabel;
    // neon green color
    private final Color NEON_GREEN = Color.decode("#00FF7F");
    // neon pink color
    private final Color NEON_PINK = Color.decode("#FF1493");
    // neon blue color
    private final Color NEON_BLUE = Color.decode("#00B7EB");
    // dark background color
    private final Color DARK_BG = Color.decode("#333333");

    // constructor
    public Dashboard() {
        // create frame
        frame = new JFrame("Airport Tower Dashboard");
        // set layout
        frame.setLayout(new BorderLayout());
        // set size
        frame.setSize(600, 500);
        // set close operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // set dark background
        frame.getContentPane().setBackground(DARK_BG);

        // create runway status panel
        JPanel runwayPanel = new JPanel();
        // set layout
        runwayPanel.setLayout(new BorderLayout());
        // set background
        runwayPanel.setBackground(DARK_BG);
        // add padding
        runwayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // create runway status label
        runwayStatusLabel = new JLabel("Runway: Free", SwingConstants.CENTER);
        // set font
        runwayStatusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        // set text color
        runwayStatusLabel.setForeground(Color.WHITE);

        // create runway visual panel
        runwayVisualPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // draw runway rectangle
                g.setColor(runwayStatusLabel.getText().contains("Occupied") ? NEON_PINK : NEON_GREEN);
                g.fillRect(50, 10, 300, 30);
                // draw border
                g.setColor(Color.WHITE);
                g.drawRect(50, 10, 300, 30);
            }
        };
        // set preferred size
        runwayVisualPanel.setPreferredSize(new Dimension(400, 50));
        // set background
        runwayVisualPanel.setBackground(DARK_BG);

        // add status label
        runwayPanel.add(runwayStatusLabel, BorderLayout.NORTH);
        // add visual panel
        runwayPanel.add(runwayVisualPanel, BorderLayout.CENTER);

        // create landing queue panel
        JPanel landingPanel = new JPanel();
        // set layout
        landingPanel.setLayout(new FlowLayout());
        // set background
        landingPanel.setBackground(DARK_BG);
        // create landing queue bar
        landingQueueBar = new JProgressBar(0, 10);
        // set initial value
        landingQueueBar.setValue(0);
        // show value
        landingQueueBar.setStringPainted(true);
        // set label
        landingQueueBar.setString("Landing Queue: 0");
        // set foreground
        landingQueueBar.setForeground(NEON_BLUE);
        // set background
        landingQueueBar.setBackground(DARK_BG);
        // set text color
        landingQueueBar.setFont(new Font("Arial", Font.PLAIN, 12));
        // create landing icon label
        JLabel landingIcon = new JLabel("✈");
        // set font
        landingIcon.setFont(new Font("Arial", Font.PLAIN, 20));
        // set color
        landingIcon.setForeground(NEON_BLUE);
        // add icon
        landingPanel.add(landingIcon);
        // add bar
        landingPanel.add(landingQueueBar);

        // create takeoff queue panel
        JPanel takeoffPanel = new JPanel();
        // set layout
        takeoffPanel.setLayout(new FlowLayout());
        // set background
        takeoffPanel.setBackground(DARK_BG);
        // create takeoff queue bar
        takeoffQueueBar = new JProgressBar(0, 10);
        // set initial value
        takeoffQueueBar.setValue(0);
        // show value
        takeoffQueueBar.setStringPainted(true);
        // set label
        takeoffQueueBar.setString("Takeoff Queue: 0");
        // set foreground
        takeoffQueueBar.setForeground(NEON_BLUE);
        // set background
        takeoffQueueBar.setBackground(DARK_BG);
        // set text color
        takeoffQueueBar.setFont(new Font("Arial", Font.PLAIN, 12));        // create takeoff icon label
        JLabel takeoffIcon = new JLabel("↑");
        takeoffIcon.setFont(new Font("Arial", Font.PLAIN, 20));         // font
        takeoffIcon.setForeground(NEON_BLUE);     // set color
        takeoffPanel.add(takeoffIcon);        // icon
        takeoffPanel.add(takeoffQueueBar);         // add bar

        // create simulation status label
        simulationStatusLabel = new JLabel("Simulation: Running", SwingConstants.CENTER);
        // set fonts
        simulationStatusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        // set color
        simulationStatusLabel.setForeground(NEON_BLUE);
        // set background
        simulationStatusLabel.setOpaque(true);
        simulationStatusLabel.setBackground(DARK_BG);
        // set border
        simulationStatusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // create status panel
        JPanel statusPanel = new JPanel();
        // set layout
        statusPanel.setLayout(new GridLayout(4, 1, 5, 5));
        // set dark background
        statusPanel.setBackground(DARK_BG);
        // padding
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // add runway status
        statusPanel.add(runwayPanel);
        //  landing queue
        statusPanel.add(landingPanel);
        //takeoff queue
        statusPanel.add(takeoffPanel);
        // our simulation status
        statusPanel.add(simulationStatusLabel);

        // create log pane
        logPane = new JTextPane();
        // make read-only
        logPane.setEditable(false);
        // set background
        logPane.setBackground(DARK_BG);
        // set margins
        logPane.setMargin(new Insets(5, 5, 5, 5));
        // create scroll pane
        JScrollPane scrollPane = new JScrollPane(logPane);
        // set scroll pane background
        scrollPane.setBackground(DARK_BG);
        // set scroll pane border
        scrollPane.setBorder(BorderFactory.createLineBorder(NEON_BLUE, 1));

        // add status panel
        frame.add(statusPanel, BorderLayout.NORTH);
        // add log area
        frame.add(scrollPane, BorderLayout.CENTER);

        // make frame visible
        frame.setVisible(true);
    }

    // updates runway status
    public void updateRunwayStatus(String status, boolean isOccupied) {
        SwingUtilities.invokeLater(() -> {
            // update text
            runwayStatusLabel.setText("Runway: " + status);
            // repaint runway visual
            runwayVisualPanel.repaint();
        });
    }

    // updates queue lengths
    public void updateQueueLengths(int landing, int takeoff) {
        SwingUtilities.invokeLater(() -> {
            // update landing bar
            landingQueueBar.setValue(landing);
            // update landing label
            landingQueueBar.setString("Landing Queue: " + landing);
            // update takeoff bar
            takeoffQueueBar.setValue(takeoff);
            // update takeoff label
            takeoffQueueBar.setString("Takeoff Queue: " + takeoff);
        });
    }

    // adds log message
    public void addLogMessage(String message) {
        // run on edt
        SwingUtilities.invokeLater(() -> {
            try {
                // get document
                StyledDocument doc = logPane.getStyledDocument();
                // define styles
                Style normalStyle = doc.addStyle("normal", null);
                StyleConstants.setForeground(normalStyle, NEON_GREEN);
                StyleConstants.setFontFamily(normalStyle, "Monospaced");
                StyleConstants.setFontSize(normalStyle, 12);

                Style highlightStyle = doc.addStyle("highlight", null);
                StyleConstants.setForeground(highlightStyle, NEON_PINK);
                StyleConstants.setFontFamily(highlightStyle, "Monospaced");
                StyleConstants.setFontSize(highlightStyle, 12);

                // choose style
                Style style = (message.contains("MAYDAY") || message.contains("low fuel")) ? highlightStyle : normalStyle;
                // append message
                doc.insertString(doc.getLength(), message + "\n", style);
                // scroll to bottom
                logPane.setCaretPosition(doc.getLength());
            } catch (BadLocationException e) {
                // log error
                e.printStackTrace();
            }
        });
    }

    // updates simulation status
    public void updateSimulationStatus(boolean running) {
        // run on edt
        SwingUtilities.invokeLater(() -> {
            if (running) {
                // simulation running
                simulationStatusLabel.setText("Simulation: Running");
                simulationStatusLabel.setForeground(NEON_BLUE);
            } else {
                // simulation ended
                simulationStatusLabel.setText("Simulation: Ended");
                simulationStatusLabel.setForeground(NEON_GREEN);
            }
        });
    }

    // closes the dashboard window
    public void close() {
        // run on edt
        SwingUtilities.invokeLater(() -> {
            // dispose frame
            frame.dispose();
        });
    }
}