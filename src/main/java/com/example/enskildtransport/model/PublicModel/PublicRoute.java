package com.example.enskildtransport.model.PublicModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PublicRoute {

    //Choose what parameters show when calling a public route
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
