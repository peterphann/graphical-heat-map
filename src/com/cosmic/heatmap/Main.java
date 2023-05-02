package com.cosmic.heatmap;

import com.formdev.flatlaf.FlatDarkLaf;

public class Main {

    public static Window window;

    public static void main(String[] args) {
        System.setProperty("apple.awt.application.name", "Heat Map");
        System.setProperty("apple.awt.application.appearance", "system");
        FlatDarkLaf.setup();

        window = new Window();

        window.createMap();
        window.setVisible(true);
    }

}
