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
    private String transportType;
    private boolean isFavorite;
    private boolean isStation;

    public int setTravelTime() {
        Random rand = new Random();
        this.travelTime = rand.nextInt(1000) + 1;
        return travelTime;
    }

}
