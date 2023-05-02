package com.cosmic.heatmap;

public class Timer {

    private long start;
    private long end;
    private String operation;
    private Window window;

    public Timer(String operation, Window window) {
        this.operation = operation;
        this.window = window;
        start = System.nanoTime();
    }

    public long end() {
        end = System.nanoTime();
        long ms = (end - start) / 1000000;
        System.out.println(operation + " took " + ms + " ms to complete");
        window.setTitle(operation + " took " + ms + " ms to complete");
        return ms;
    }

}
