package org.kramreiter.mtcg.cliclient;

public class CommThread implements Runnable {

    @Override
    public void run() {
        System.out.println("HI FROM A DIFFERENT THREAD");
        long j = 0;
        for (long i = 0; i < Long.MAX_VALUE; i++) {
            j++;
        }
        System.out.println("Successfully counted a lot of numbers: " + j);
    }
}
