package com.example.enskildtransport.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PublicRoute {

    private long id;
    private String startLocation;
    private String endLocation;
    private String departure;
    private String arrival;
    private String delayDescription;
    private int travelTime;
    private int delay;
    private int changes;
    private boolean isFavorite;
    private boolean endLocationStation;
    private boolean startLocationStation;

}
