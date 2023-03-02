package com.example.enskildtransport.model.routesModel;

import com.example.enskildtransport.model.weatherModel.Weather;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class RouteDetails {

    //a combination of both routes and weather. So that I can return both in the same method

    private Weather details;
    private List<Route> routes;

    public RouteDetails(Weather details, List<Route> routes) {
        this.details = details;
        this.routes = routes;
    }


}