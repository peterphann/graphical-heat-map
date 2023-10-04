package com.cosmic.heatmap;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

// How does one optimize this

public class HeatMap extends BufferedImage {

    double xMin, xMax, yMin, yMax;
    private double sensitivity;
    private Expression expression;
    private Argument x, y;

    private boolean colored;
    private int mode;

    private static DecimalFormat f = new DecimalFormat("#.##");

    private double[][] slopes;
    private int[][] colors;

    public HeatMap(int width, int height, double scale, double xMin, double xMax, double yMin, double yMax, double sensitivity, String expression, int mode) {
        super((int) (width / scale), (int) (height / scale), TYPE_INT_RGB);

        x = new Argument("x");
        y = new Argument("y");
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.sensitivity = sensitivity;
        this.expression = new Expression(expression, x, y);
        this.mode = mode;

        slopes = new double[getWidth()][getHeight()];
        colors = new int[getWidth()][getHeight()];

        beginComputation();
    }

    public void beginComputation() {
        double xInt = (xMax - xMin) / (getWidth() - 1);
        double yInt = (yMax - yMin) / (getHeight() - 1);

        for (int xV = 0; xV < getWidth(); xV++) {
            double decimal = (double) (xV) / (getWidth() - 1);
            Main.window.setTitle("Heat Map [" + f.format(decimal * 100) + "%]");

            x.setArgumentValue(xMin + xV * xInt);
            for (int yV = 0; yV < getHeight(); yV++) {
                y.setArgumentValue(yMin + yV * yInt);

                slopes[xV][yV] = expression.calculate();
            }
        }

        computeColors();
    }

    public void computeColors() {
        int color, value;

        switch (mode) {
            case 0 -> {
                for (int xV = 0; xV < getWidth(); xV++) {
                    for (int yV = 0; yV < getHeight(); yV++) {
                        color = scaleColor(slopes[xV][yV]).getRGB();
                        colors[xV][yV] = color;
                        this.setRGB(xV, getHeight() - yV - 1, color);
                    }
                }
            }
            case 1 -> {
                for (int xV = 0; xV < getWidth(); xV++) {
                    for (int yV = 0; yV < getHeight(); yV++) {
                        value = scaleColor(slopes[xV][yV]).getRed();
                        color = slopes[xV][yV] >= 0 ? new Color(0, value, 0).getRGB() : new Color(value, 0, 0).getRGB();
                        colors[xV][yV] = color;
                        this.setRGB(xV, getHeight() - yV - 1, color);
                    }
                }
            }
            case 2 -> {
                for (int xV = 0; xV < getWidth(); xV++) {
                    for (int yV = 0; yV < getHeight(); yV++) {
                        color = slopes[xV][yV] >= 0 ? scaleColor(slopes[xV][yV]).getRGB() : new Color(0, 0, 0).getRGB();
                        colors[xV][yV] = color;
                        this.setRGB(xV, getHeight() - yV - 1, color);
                    }
                }
            }
            case 3 -> {
                for (int xV = 0; xV < getWidth(); xV++) {
                    for (int yV = 0; yV < getHeight(); yV++) {
                        color = slopes[xV][yV] <= 0 ? scaleColor(slopes[xV][yV]).getRGB() : new Color(0, 0, 0).getRGB();
                        colors[xV][yV] = color;
                        this.setRGB(xV, getHeight() - yV - 1, color);
                    }
                }
            }
        }
    }

    private Color scaleColor(double slope) {
        int brightness = limitColor((int) (Math.log(Math.abs(slope)) / sensitivity));
        return new Color(brightness, brightness, brightness);
    }

    private static int limitColor(double n) {
        int n2 = (int) Math.round(n);
        return n2 < 0 ? 0 : Math.min(n2, 255);
    }

    public void updateSensitivity(double sensitivity) {
        Timer timer = new Timer("Sensitivity", Main.window);
        this.sensitivity = sensitivity;
        computeColors();
        timer.end();
    }

    public void toggleColor() {
        mode = mode + 1 == 4 ? 0 : mode + 1;
        computeColors();
    }

    public int getMode() {
        return mode;
    }
}
