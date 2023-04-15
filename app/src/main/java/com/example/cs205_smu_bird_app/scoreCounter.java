package com.example.cs205_smu_bird_app;

public class scoreCounter {
    private int score = 0; // Shared score variable
    private Object mutex = new Object(); // Mutex object for synchronization
    private Thread incrementThread; // Thread for incrementing the score
    private Thread decrementThread; // Thread for decrementing the score
    private boolean incrementFlag = false; // Flag to indicate whether to increment the score
    private boolean decrementFlag = false; // Flag to indicate whether to decrement the score

    // Constructor to initialize the threads
    public scoreCounter() {
        incrementThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (mutex) { // Acquire lock on mutex
                    if (incrementFlag) {
                        score += 10; // Increment the score
                        incrementFlag = false; // Reset increment flag
                    }
                } // Release lock on mutex
            }
        });

        decrementThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (mutex) { // Acquire lock on mutex
                    if (decrementFlag) {
                        score -= 5; // Decrement the score
                        decrementFlag = false; // Reset decrement flag
                    }
                } // Release lock on mutex
            }
        });
    }

    // Method to start the threads
    public void startThreads() {
        incrementThread.start();
        decrementThread.start();
    }

    // Method to stop the threads
    public void stopThreads() {
        incrementThread.interrupt();
        decrementThread.interrupt();
    }

    // Method to manually increment the score
    public void increment() {
        synchronized (mutex) { // Acquire lock on mutex
            incrementFlag = true; // Set increment flag
        } // Release lock on mutex
    }

    // Method to manually decrement the score
    public void decrement() {
        synchronized (mutex) { // Acquire lock on mutex
            decrementFlag = true; // Set decrement flag
        } // Release lock on mutex
    }

    // Method to get the current score
    public int getScore() {
        synchronized (mutex) { // Acquire lock on mutex
            return score; // Return the current score
        } // Release lock on mutex
    }
}
