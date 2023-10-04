package com.cosmic.heatmap;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

// Shut up I know this looks terrible

public class Window extends JFrame implements ActionListener {

    public static int width = 800;
    public static int height = 800;

    public HeatMap map;
    public JMenuBar menuBar;
    public JMenu fileMenu;
    public JMenuItem saveImage;
    public JPanel heatMapPanel, sliderPanel;
    public JSlider sensitivitySlider;
    public JTextField expressionField, scaleField, xMinField, xMaxField, yMinField, yMaxField;
    public JButton updateButton, modeButton;
    public JFileChooser fileChooser;

    public Window() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Heat Map");
        this.setPreferredSize(new Dimension(5 * width / 4, height));
        this.getContentPane().setBackground(Defaults.MAIN_COLOR);
        this.setLayout(new BorderLayout());
        this.setResizable(false);

        Taskbar.getTaskbar().setIconImage(new ImageIcon("src/icon.png").getImage());

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        saveImage = new JMenuItem("Save Image");
        saveImage.addActionListener(this);
        fileMenu.add(saveImage);


        String userDir = System.getProperty("user.home");
        fileChooser = new JFileChooser(userDir + "/Desktop");
        fileChooser.setFileFilter(new FileNameExtensionFilter("*.png", "png"));

        heatMapPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(map, 0, 0, width, height, null);
            }
        };
        heatMapPanel.setPreferredSize(new Dimension(width, height));

        sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.PAGE_AXIS));
        sliderPanel.setPreferredSize(new Dimension(width / 4, height));
        sliderPanel.setBackground(Defaults.MAIN_COLOR);

        // X Range Panel

        xMinField = new JTextField();
        xMinField.setText(String.valueOf(Defaults.X_START));
        xMinField.setPreferredSize(new Dimension(width / 16, 30));

        xMaxField = new JTextField();
        xMaxField.setText(String.valueOf(Defaults.X_END));
        xMaxField.setPreferredSize(new Dimension(width / 16, 30));

        JPanel xRange = new JPanel(new FlowLayout());
        xRange.add(new JLabel("x"));
        xRange.add(xMinField);
        xRange.add(xMaxField);

        // Y Range Panel

        yMinField = new JTextField();
        yMinField.setText(String.valueOf(Defaults.Y_START));
        yMinField.setPreferredSize(new Dimension(width / 16, 30));

        yMaxField = new JTextField();
        yMaxField.setText(String.valueOf(Defaults.Y_END));
        yMaxField.setPreferredSize(new Dimension(width / 16, 30));

        JPanel yRange = new JPanel(new FlowLayout());
        yRange.add(new JLabel("y"));
        yRange.add(yMinField);
        yRange.add(yMaxField);

        // Scale Panel
        scaleField = new JTextField();
        scaleField.setText(String.valueOf(Defaults.SCALE));
        scaleField.setPreferredSize(new Dimension(width / 16, 30));

        JPanel scalePanel = new JPanel();
        scalePanel.add(new JLabel("Scale"));
        scalePanel.add(scaleField);

        // Sensitivity Panel
        sensitivitySlider = new JSlider();
        sensitivitySlider.setMaximum(0);
        sensitivitySlider.setMaximum(100);
        sensitivitySlider.setValue((int) (Defaults.SENSITIVITY * 2000));
        sensitivitySlider.setPreferredSize(new Dimension(3 * width / 16, 20));
        sensitivitySlider.addChangeListener(e -> {
            map.updateSensitivity(sensitivitySlider.getValue() / 2000.0);
            heatMapPanel.repaint();
        });

        JPanel sensitivityPanel = new JPanel();
        JPanel sensitivityLabel = new JPanel();
        sensitivityLabel.setPreferredSize(new Dimension(width / 4, 20));
        sensitivityLabel.add(new JLabel("Sensitivity"));
        sensitivityPanel.add(sensitivitySlider);

        // Expression Panel
        expressionField = new JTextField();
        expressionField.setText(Defaults.EXPRESSION);
        expressionField.setPreferredSize(new Dimension(3 * width / 16, 30));

        JPanel expressionPanel = new JPanel();
        JPanel expressionLabel = new JPanel();
        expressionLabel.setPreferredSize(new Dimension(width / 4, 20));
        expressionPanel.add(new JLabel("Expression"));
        expressionPanel.add(expressionField);

        // Button Panel
        updateButton = new JButton();
        updateButton.setText("Update");
        updateButton.addActionListener(this);
        updateButton.setPreferredSize(new Dimension(3 * width / 32, 32));

        modeButton = new JButton();
        modeButton.setText("Mode");
        modeButton.addActionListener(this);
        modeButton.setPreferredSize(new Dimension(3 * width / 32, 32));

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(updateButton);
        buttonPanel.add(modeButton);

        // Padding Panel

        JPanel padding = new JPanel();
        padding.setPreferredSize(new Dimension(width / 4, 350));

        sliderPanel.add(xRange);
        sliderPanel.add(yRange);
        sliderPanel.add(scalePanel);
        sliderPanel.add(sensitivityLabel);
        sliderPanel.add(sensitivityPanel);
        sliderPanel.add(expressionLabel);
        sliderPanel.add(expressionPanel);
        sliderPanel.add(buttonPanel);
        sliderPanel.add(padding);

        this.setJMenuBar(menuBar);
        this.add(heatMapPanel, BorderLayout.CENTER);
        this.add(sliderPanel, BorderLayout.EAST);
        this.pack();
    }

    public void createMap() {
        map = new HeatMap(width, height, Defaults.SCALE, Defaults.X_START, Defaults.X_END, Defaults.Y_START, Defaults.Y_END, Defaults.SENSITIVITY, Defaults.EXPRESSION, Defaults.MODE);
        heatMapPanel.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateButton) {
            double xMin = Double.parseDouble(xMinField.getText());
            double xMax = Double.parseDouble(xMaxField.getText());
            double yMin = Double.parseDouble(yMinField.getText());
            double yMax = Double.parseDouble(yMaxField.getText());
            double sensitivity = sensitivitySlider.getValue() / 2000.0;
            double scale = Double.parseDouble(scaleField.getText());
            String expression = expressionField.getText();
            int mode = map.getMode();
            map = new HeatMap(800, 800, scale, xMin, xMax, yMin, yMax, sensitivity, expression, mode);
            heatMapPanel.repaint();
        }

        if (e.getSource() == saveImage) {
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    System.out.println(file.getAbsolutePath());
                    ImageIO.write(map, "png", new File(file.getAbsolutePath() + ".png"));
                } catch (IOException ex) {
                    System.out.println("Error while saving image");
                }
            }
        }

        if (e.getSource() == modeButton) {
            map.toggleColor();
            heatMapPanel.repaint();
        }

    }

}
