package com.example.enskildtransport.model.trainModel;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TrainInfo {

    //a combination of both legs and currency. So that I can return both in the same method

    private List<Leg> legs;
    private String currency;

    public TrainInfo(TrainResponse response) {
        this.legs = new ArrayList<>();
        for (LegResponse legResponse : response.getTrain().getLegs()) {
            this.legs.add(new Leg(legResponse));
        }
        this.currency = response.getTrain().getCurrency();
    }

    @Data
    public static class TrainResponse {
        private TrainInfoResponse train;
    }

    @Data
    public static class TrainInfoResponse {
        private List<LegResponse> legs;
        private String currency;
    }

    @Data
    public static class LegResponse {
        private String name;
        private String type;
        private String direction;
        private String origin;
        private String destination;
        private String origTime;
        private String destTime;
        private String travelTime;
    }

    @Data
    public static class Leg {
        private String origin;
        private String destination;
        private String type;
        private String direction;
        private String name;
        private String departureTime;
        private String arrivalTime;
        private String travelTime;

        public Leg(LegResponse legResponse) {
            this.origin = legResponse.getOrigin();
            this.destination = legResponse.getDestination();
            this.type = legResponse.getType();
            this.direction = legResponse.getDirection();
            this.name = legResponse.getName();
            this.departureTime = legResponse.getOrigTime();
            this.arrivalTime = legResponse.getDestTime();
            this.travelTime = legResponse.getTravelTime();
        }
    }
}
