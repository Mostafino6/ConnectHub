package org.example.demo;

public class MainApplication extends Application {
    private static MainApplication instance; // Singleton instance
    public MainApplication() {
        instance = this;
    }
    public static MainApplication getInstance() {
        return instance;
    }
}
