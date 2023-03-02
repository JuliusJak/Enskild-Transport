package com.example.enskildtransport.model.routesModel;

import com.example.enskildtransport.model.PublicModel.PublicRoute;
import lombok.Data;

import java.util.List;

@Data
public class RouteAndPublicRoute {

    //a combination of both routes and public routs. So that I can return both in the same method
    private List<Route> routes;
    private List<PublicRoute> publicRoutes;

    public RouteAndPublicRoute(List<Route> routes, List<PublicRoute> publicRoutes) {
        this.routes = routes;
        this.publicRoutes = publicRoutes;
    }
}
