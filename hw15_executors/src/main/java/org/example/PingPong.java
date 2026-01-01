package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingPong {
    private static final Logger logger = LoggerFactory.getLogger(PingPong.class);
    private int last = 1;
    private boolean direction = true;
    private boolean thread1Turn = true;


    private synchronized void action(int threadId) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // spurious wakeup https://en.wikipedia.org/wiki/Spurious_wakeup
                // поэтому не if
                while ((thread1Turn && threadId == 2) || (!thread1Turn && threadId == 1)) {
                    wait();
                }

                if (direction && last >= 10) {
                    direction = false;
                } else if (!direction && last <= 1) {
                    direction = true;
                }


                logger.info(String.valueOf(last));

                if (threadId == 2) {
                    if (direction) {
                        last++;
                    } else {
                        last--;
                    }
                }

                thread1Turn = !thread1Turn;

                sleep();
                notifyAll();
                logger.info("after notify");
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        PingPong pingPong = new PingPong();
        new Thread(() -> pingPong.action(1)).start();
        new Thread(() -> pingPong.action(2)).start();
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
