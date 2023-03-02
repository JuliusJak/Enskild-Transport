package com.example.enskildtransport.model.weatherModel;

import lombok.Data;

@Data
public class Weather {

    //Choose what parameters show when calling on it


    private String name;
    //private Coord coord;
    private WeatherInfo[] weather;
    //private String base;
    private Main main;
    //private int visibility;
    //private Wind wind;
    //private Clouds clouds;
    //private long dt;
    //private Sys sys;
    //private int timezone;
    //private int id;

    //private int cod;


    @Data
    public static class Coord {
        private double lon;
        private double lat;
    }


    @Data
    public static class WeatherInfo {
        //private int id;
        private String main;
        private String description;
        //private String icon;
    }

    @Data
    public static class Main {
        private double temp;
        private double feels_like;
        private double temp_min;
        private double temp_max;
        private int pressure;
        private int humidity;
    }

    @Data
    public static class Wind {
        private double speed;
        private int deg;
    }

    @Data
    public static class Clouds {
        private int all;
    }

    @Data
    public static class Sys {
        private int type;
        private int id;
        private String country;
        private long sunrise;
        private long sunset;
    }
}
