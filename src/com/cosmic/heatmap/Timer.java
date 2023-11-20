package com.cosmic.heatmap;

public class Timer {

    private long start;
    private String operation;

    public Timer(String operation, Window window) {
        this.operation = operation;
        start = System.nanoTime();
    }

    public long end() {
        long ms = (System.nanoTime() - start) / 1000000;
        System.out.println(operation + ": " + ms + " ms");
        return ms;
    }

}
