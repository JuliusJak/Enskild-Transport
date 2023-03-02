package com.example.enskildtransport.controller;

import com.example.enskildtransport.model.*;
import com.example.enskildtransport.repository.RouteRepository;
import com.example.enskildtransport.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.RouteMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/routes/*")
public class RouteController {

    @Autowired
    private RouteService routeService;
    WeatherController weatherController = new WeatherController();

    private List<Route> routeList;


    @PostMapping
    public ResponseEntity<List<Route>> createRoute(@RequestBody Route route) {
        boolean isStartLocationStation = route.startLocationIsStation(route.getStartLocation());
        route.setStartLocationIsStation(isStartLocationStation);

        boolean isEndLocationIsStation = route.endLocationIsStation(route.getEndLocation());
        route.setEndLocationIsStation(isEndLocationIsStation);
        routeService.save(route);
        return ResponseEntity.status(201).body(routeList);
    }


    @GetMapping("/Start/{startLocation}")
    public List<Route> getRouteByStartLocation(@PathVariable String startLocation){
        List<Route> routes = routeService.findByStartLocation(startLocation);
        return routes;
    }

    @GetMapping("/End/{endLocation}")
    public List<Route> getRouteByEndLocation(@PathVariable String endLocation){
        List<Route> routes = routeService.findByEndLocation(endLocation);
        return routes;
    }

    @GetMapping("/{startLocation}/to/{endLocation}/{transportType}/{limit}")
    public ResponseEntity<RouteAndPublicRoute> getRouteByStartAndEndLocation(
            @PathVariable String startLocation,
            @PathVariable String endLocation,
            @PathVariable String transportType,
            @PathVariable int limit,
            RestTemplate restTemplate){

        List<Route> routes = routeService.findRouteByTransportTypeAndStartLocationAndEndLocation(transportType,startLocation, endLocation);

        List<Route> limitedRoutes = routes.subList(0, Math.min(limit, routes.size()));

        String route = routes.toArray()[0].toString();
        String startIsStation = "startLocationIsStation=true";
        String endIsStation = "endLocationIsStation=true";

        if (route.contains(endIsStation) || route.contains(startIsStation)) {
            System.out.println(endIsStation);
            System.out.println(startIsStation);

            RouteAndPublicRoute routeFusion = new RouteAndPublicRoute(limitedRoutes, getPublicRoutes(restTemplate, startLocation, endLocation));
            return ResponseEntity.ok(routeFusion);
        }


        RouteAndPublicRoute routeDetails = new RouteAndPublicRoute(limitedRoutes, getPublicRoutes(restTemplate, startLocation, endLocation));
        return ResponseEntity.ok(routeDetails);
    }

    @GetMapping("/weather/{startLocation}/to/{endLocation}/{transportType}/{limit}")
    public ResponseEntity<RouteFusion> getRouteAndWeatherByStartAndEndLocation(
            @PathVariable String startLocation,
            @PathVariable String endLocation,
            @PathVariable String transportType,
            @PathVariable int limit,
            RestTemplate restTemplate){

        ResponseEntity<GeoCoodingDetails> geoCoodingResponse = weatherController.getGeoCooding(endLocation, restTemplate);
        GeoCoodingDetails geoCoodingDetails = geoCoodingResponse.getBody();
        String lat = geoCoodingDetails.getLat();
        String lon = geoCoodingDetails.getLon();

        StringBuilder builder = new StringBuilder("https://api.openweathermap.org/data/2.5/weather?");
        builder.append("lat=").append(lat)
                .append("&lon=").append(lon)
                .append("&appid=")
                .append("f24f5e9709bc77a5de811683e7de8f19");

        ResponseEntity<Weather> weather = restTemplate.getForEntity(builder.toString(), Weather.class);
        Weather details = weather.getBody();



        List<Route> routes = routeService.findRouteByTransportTypeAndStartLocationAndEndLocation(transportType,startLocation, endLocation);

        String route = routes.toArray()[0].toString();
        String startIsStation = "startLocationIsStation=true";
        String endIsStation = "endLocationIsStation=true";

        if (route.contains(endIsStation) || route.contains(startIsStation)) {

            RouteFusion routeFusion = new RouteFusion(details, routes, getPublicRoutes(restTemplate, startLocation, endLocation));
            return ResponseEntity.ok(routeFusion);
        }
        if (routes.isEmpty() || limit <= 0) {
            details = null;
        }

        List<Route> limitedRoutes = routes.subList(0, Math.min(limit, routes.size()));
        RouteFusion routeDetails = new RouteFusion(details, limitedRoutes, getPublicRoutes(restTemplate, null, null));

        return ResponseEntity.ok(routeDetails);
    }

    @GetMapping("/{transportType}")
    public ResponseEntity<List<Route>> getRouteByTransportType(@PathVariable String transportType) {
        List<Route> routes = routeService.findByTransportType(transportType);
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<Route>> getFavoriteRoutes(){
        List<Route> favoriteRoutes = routeService.findByIsFavorite();
        return ResponseEntity.ok(favoriteRoutes);
    }

    @GetMapping("/favorites/{transportType}")
    public ResponseEntity<List<Route>> getFavoriteRoutesByTransportType(@PathVariable String transportType){
        List<Route> favoriteRoutes = routeService.findByIsFavoriteAndTransportType(true, transportType);
        return ResponseEntity.ok(favoriteRoutes);
    }

    @PutMapping("/{id}/favorite")

    public ResponseEntity<Route> markAsFavorite (@PathVariable Long id){
        Optional<Route> route = routeService.findById(id);
        if(route.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Route existingRoute = route.get();
        existingRoute.setFavorite(true);

        Route updatedRoute = routeService.save(existingRoute);
        return ResponseEntity.ok(updatedRoute);
    }
    @PutMapping("/{id}/unmark-favorite")

    public ResponseEntity<Route> unmarkAsFavorite (@PathVariable Long id){
        Optional<Route> route = routeService.findById(id);
        if(route.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Route existingRoute = route.get();
        existingRoute.setFavorite(false);

        Route updatedRoute= routeService.save(existingRoute);
        return  ResponseEntity.ok(updatedRoute);
    }


    @GetMapping("/public/{startLocation}/to/{endLocation}")
    public List<PublicRoute> getPublicRoutes(RestTemplate restTemplate,
                                       @PathVariable String startLocation,
                                       @PathVariable String endLocation){

        StringBuilder builder = new StringBuilder("https://transportfinal-transport-service.azuremicroservices.io");
        builder.append("/routes/").append(startLocation)
                .append("/to/").append(endLocation);


        ResponseEntity<List<PublicRoute>> response = restTemplate.exchange(
                builder.toString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        return response.getBody();
    }

    @GetMapping("/combined/{startLocation}/to/{startStation}/to/{endStation}")
    public ResponseEntity<RouteAndPublicRoute> getCombinedRoute(RestTemplate restTemplate,
                                                      @PathVariable String startLocation,
                                                      @PathVariable String startStation,
                                                      @PathVariable String endStation) {


        List<Route> routes = routeService.findByStartAndEndLocation(startLocation, endStation);

        //List<Route> limitedRoutes = routes.subList(0, Math.min(limit, routes.size()));

        RouteAndPublicRoute routeDetails = new RouteAndPublicRoute(routes, getPublicRoutes(restTemplate, startStation, endStation));
        return ResponseEntity.ok(routeDetails);
    }
    @GetMapping("get/train/{originId}/{destId}")
    public ResponseEntity<Train> getTrain(@PathVariable String originId, @PathVariable String destId, RestTemplate restTemplate) {

        List<GeoCoodingDetails> detailsList = weatherController.getManyGeoCoodings(originId, destId, restTemplate);
        GeoCoodingDetails detail1 = detailsList.get(0);
        GeoCoodingDetails detail2 = detailsList.get(1);


        StringBuilder builder = new StringBuilder("https://api.resrobot.se/v2.1/trip?");
        builder.append("originCoordLat=").append(detail1.getLat())
                .append("&originCoordLong=").append(detail1.getLon())
                .append("&destCoordLat=").append(detail2.getLat())
                .append("&destCoordLong=").append(detail2.getLon())

                .append("&accessId=")
                .append("900be3c6-6024-4578-9c15-1824fb949211");
        ResponseEntity<Train> train = restTemplate.getForEntity(builder.toString(), Train.class);
        Train details = train.getBody();
        return ResponseEntity.ok(details);
    }



}
/*
    @GetMapping("train/{originId}/{destId}")
    public ResponseEntity<TrainInfo.TrainResponse> getTrain(@PathVariable String originId, @PathVariable String destId, RestTemplate restTemplate) {
        StringBuilder builder = new StringBuilder("https://api.resrobot.se/v2/trip?");
        builder.append("originId=").append(originId)
                .append("&destId=").append(destId)
                .append("&accessId=")
                .append("900be3c6-6024-4578-9c15-1824fb949211");
        ResponseEntity<TrainInfo.TrainResponse> trainResponse = restTemplate.getForEntity(builder.toString(), TrainInfo.TrainResponse.class);
        TrainInfo.TrainResponse details = trainResponse.getBody();
        System.out.println(trainResponse.getBody());
        return ResponseEntity.ok(details);
    }

 */

