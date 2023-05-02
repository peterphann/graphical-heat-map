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

    private static DecimalFormat f = new DecimalFormat("#.##");

    private double[][] slopes;
    private int[][] colors;

    public HeatMap(int width, int height, double scale) {
        super((int) (width / scale), (int) (height / scale), TYPE_INT_RGB);

        x = new Argument("x");
        y = new Argument("y");
        sensitivity = Defaults.SENSITIVITY;
        xMin = Defaults.X_START;
        xMax = Defaults.X_END;
        yMin = Defaults.Y_START;
        yMax = Defaults.Y_END;

        expression = new Expression(Defaults.EXPRESSION, x, y);

        slopes = new double[getWidth()][getHeight()];
        colors = new int[getWidth()][getHeight()];

        beginComputation();
    }

    public void beginComputation() {
        int color;
        double xInt = (xMax - xMin) / (getWidth() - 1);
        double yInt = (yMax - yMin) / (getHeight() - 1);

        Timer timer = new Timer("Map", Main.window);

        for (int xV = 0; xV < getWidth(); xV++) {
            for (int yV = 0; yV < getHeight(); yV++) {
                double decimal = ((double) (xV) * getWidth() + yV) / (getWidth() * getHeight());
                System.out.println(f.format(decimal * 100) + "%");

                x.setArgumentValue(xMin + xV * xInt);
                y.setArgumentValue(yMin + yV * yInt);

                slopes[xV][yV] = expression.calculate();

                color = scaleColor(slopes[xV][yV]).getRGB();
                colors[xV][yV] = color;
                this.setRGB(xV, getHeight() - yV - 1, color);
            }
        }
        timer.end();
    }

    public void computeColors() {
        int color;

        for (int xV = 0; xV < getWidth(); xV++) {
            for (int yV = 0; yV < getHeight(); yV++) {
                color = scaleColor(slopes[xV][yV]).getRGB();
                colors[xV][yV] = color;
                this.setRGB(xV, getHeight() - yV - 1, color);
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

    public void updateAll(String expression, double sensitivity) {
        this.expression.setExpressionString(expression);
        this.sensitivity = sensitivity;
        beginComputation();
    }

}
