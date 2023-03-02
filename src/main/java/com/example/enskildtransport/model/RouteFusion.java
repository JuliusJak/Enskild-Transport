package com.example.enskildtransport.model;

import lombok.Data;

import java.util.List;

@Data
public class RouteFusion {

    private Weather details;

    private List<Route> routes;

    private List<PublicRoute> publicRoutes;


    public RouteFusion(Weather details, List<Route> routes, List<PublicRoute> publicRoutes) {
        this.details = details;
        this.routes = routes;
        this.publicRoutes = publicRoutes;
    }
}
