package com.example.enskildtransport.model.routesModel;

import com.example.enskildtransport.model.PublicModel.PublicRoute;
import com.example.enskildtransport.model.weatherModel.Weather;
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
