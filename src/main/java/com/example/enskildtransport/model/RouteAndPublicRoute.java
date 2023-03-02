package com.example.enskildtransport.model;

import lombok.Data;

import java.util.List;

@Data
public class RouteAndPublicRoute {

    private List<Route> routes;
    private List<PublicRoute> publicRoutes;

    public RouteAndPublicRoute(List<Route> routes, List<PublicRoute> publicRoutes) {
        this.routes = routes;
        this.publicRoutes = publicRoutes;
    }
}
