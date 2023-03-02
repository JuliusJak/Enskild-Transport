package com.example.enskildtransport.model.trainModel;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonAlias;

@Data
public class Train {

    //this determines what parameters show when calling on a train route
    //it is never used because we as a group took the decision to not use it,
    // but I don't want to delete it, so it will stay :)
    @JsonAlias("Trip")
    private TripDetails[] trip;


    @Data
    public static class TripDetails {

        @JsonAlias("Origin")
        private OriginDetails origin;

        @JsonAlias("Destination")
        private DestinationDetails destination;

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
        public static class DestinationDetails {
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