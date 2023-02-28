package com.example.enskildtransport.model;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonAlias;

@Data
public class Train {

    @JsonAlias("Trip")
    private TripDetails[] trip;


    @Data
    public static class TripDetails {

        @JsonAlias("Origin")
        private OriginDetails origin;

        @JsonAlias("ServiceDays")
        private ServiceDaysDetails[] serviceDays;

        @Data
        public static class OriginDetails {
            private String name;
            private String type;
            private String id;
            private String extId;
            private double lon;
            private double lat;
            private int routeIdx;
            private String prognosisType;
            private String time;
            private String date;
        }

        @Data
        public static class ServiceDaysDetails {
            private String planningPeriodBegin;
            private String planningPeriodEnd;
        }
    }
}








/*
@Data
public class Train {
    //private TripList[] Trip;

    private Origin origin;
    private Destination destination;

    @Data
    public static class TripList {
        private Destination destination;
        private Origin origin;
    }

    @Data
    public static class Origin {
        private String name;
        private String type;
        private String id;
        private String extId;
        private double lon;
        private double lat;
        private int routeIdx;
        private String prognosisType;
        private String time;
        private String date;


    }

    @Data
    public static class Destination {
        private String name;
        private String type;
        private String id;
        private String extId;
    }

}
*/
