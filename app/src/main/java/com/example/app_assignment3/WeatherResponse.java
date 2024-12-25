package com.example.app_assignment3;
public class WeatherResponse {
    private Main main;

    public Main getMain() {
        return main;
    }

    public class Main {
        private double temp;

        public double getTemp() {
            return temp;
        }
    }
}
