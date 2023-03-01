package com.example.enskildtransport.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class RouteDetails {

    private Weather details;
    private List<Route> routes;

    public RouteDetails(Weather details, List<Route> routes) {
        this.details = details;
        this.routes = routes;
    }


}