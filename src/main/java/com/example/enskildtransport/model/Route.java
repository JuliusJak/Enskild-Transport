package com.example.enskildtransport.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Random;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Route {


    @Id
    @GeneratedValue
    private long id;
    private String startLocation;
    private String endLocation;
    private int travelTime = setTravelTime();
    private boolean delay;
    private String transportType;
    private boolean isFavorite;
    private boolean startLocationIsStation;
    private boolean endLocationIsStation;

    public int setTravelTime() {
        Random rand = new Random();
        this.travelTime = rand.nextInt(24*60) + 1;
        if (travelTime < rand.nextInt(24*60) + 1) {
            delay = true;
        }
        if (delay){
            travelTime*= 1.35;
        }
        return travelTime;
    }

    public boolean startLocationIsStation(String startLocation) {

        if (StationNames.stationsSweden.contains(startLocation)) {
            startLocationIsStation = true;

            return startLocationIsStation;
        } else {

            return false;
        }

    }
    public boolean endLocationIsStation(String endLocation) {



        if (StationNames.stationsSweden.contains(endLocation)) {
            endLocationIsStation = true;

            return endLocationIsStation;
        } else {

            return false;
        }

    }

}
