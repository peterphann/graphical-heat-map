package com.cosmic.heatmap;

public class Timer {

    private long start;
    private String operation;
    private Window window;

    public Timer(String operation, Window window) {
        this.operation = operation;
        this.window = window;
        start = System.nanoTime();
    }

    public long end() {
        long ms = (System.nanoTime() - start) / 1000000;
        System.out.println(operation + ": " + ms + " ms");
        return ms;
    }

}
