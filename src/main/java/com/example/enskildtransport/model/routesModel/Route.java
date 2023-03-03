package com.example.enskildtransport.model.routesModel;

import com.example.enskildtransport.model.DirectionDirection;
import com.example.enskildtransport.model.DirectionPlaces;
import com.example.enskildtransport.model.StationNames;
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

    //Choose what parameters show when calling a route

    @Id
    @GeneratedValue
    private long id;
    private String startLocation;
    private String endLocation;
    private String description = getRandomDirection() + ": " + getRandomImaginaryPlace().toUpperCase()
            + " then " + getRandomDirection() + ": " + getRandomImaginaryPlace().toUpperCase();
    private int travelTime = setTravelTime();
    private int delay;
    private String transportType;
    private boolean isFavorite;
    private boolean startLocationIsStation;
    private boolean endLocationIsStation;

    public int setTravelTime() {
        Random rand = new Random();
        boolean isDelay = false;
        this.travelTime = rand.nextInt(24*60) + 1;
        if (travelTime < rand.nextInt(24*60) + 1) {
            isDelay = true;
        }
        if (isDelay){
            delay = travelTime *= 0.35;
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

    private static String getRandomDirection() {
        Random rand = new Random();
        return DirectionDirection.directionList.get(rand.nextInt(DirectionDirection.directionList.size()));
    }

    private static String getRandomImaginaryPlace() {
        Random rand = new Random();
        return DirectionPlaces.directionList.get(rand.nextInt(DirectionPlaces.directionList.size()));
    }
}
