package com.cosmic.heatmap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Shut up I know this looks terrible

public class Window extends JFrame implements ActionListener {

    public static int width = 800;
    public static int height = 800;

    public HeatMap map;
    public JPanel heatMapPanel, sliderPanel;
    public JSlider sensitivitySlider;
    public JTextField expressionField;
    public JButton updateButton;

    public Window() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(5 * width / 4, height));
        this.getContentPane().setBackground(Defaults.MAIN_COLOR);
        this.setLayout(new BorderLayout());
        this.setResizable(false);

        Taskbar.getTaskbar().setIconImage(new ImageIcon("src/icon.png").getImage());

        heatMapPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(map, 0, 0, width, height, null);
            }
        };
        heatMapPanel.setPreferredSize(new Dimension(width, height));

        sliderPanel = new JPanel();
        sliderPanel.setPreferredSize(new Dimension(width / 4, height));
        sliderPanel.setBackground(Defaults.MAIN_COLOR);

        JLabel sensitivityLabel = new JLabel("Sensitivity");

        sensitivitySlider = new JSlider();
        sensitivitySlider.setMaximum(0);
        sensitivitySlider.setMaximum(100);
        sensitivitySlider.setValue((int) (Defaults.SENSITIVITY * 2000));
        sensitivitySlider.setPreferredSize(new Dimension(3 * width / 16, 20));
        sensitivitySlider.addChangeListener(e -> {
            map.updateSensitivity(sensitivitySlider.getValue() / 2000.0);
            heatMapPanel.repaint();
        });

        expressionField = new JTextField();
        expressionField.setText(Defaults.EXPRESSION);
        expressionField.setPreferredSize(new Dimension(3 * width / 16, 30));

        updateButton = new JButton();
        updateButton.setText("Update");
        updateButton.addActionListener(this);
        updateButton.setPreferredSize(new Dimension(3 * width / 16, 64));

        sliderPanel.add(sensitivityLabel);
        sliderPanel.add(sensitivitySlider);
        sliderPanel.add(expressionField);
        sliderPanel.add(updateButton);

        this.add(heatMapPanel, BorderLayout.CENTER);
        this.add(sliderPanel, BorderLayout.EAST);
        this.pack();
    }

    public void createMap() {
        map = new HeatMap(width, height, Defaults.SCALE);
        heatMapPanel.repaint();
        //ImageIO.write(map, "png", new File("output.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateButton) {
            double sensitivity = sensitivitySlider.getValue() / 1000.0;
            String expression = expressionField.getText();
            map.updateAll(expression, sensitivity);
            heatMapPanel.repaint();
        }
    }
}
