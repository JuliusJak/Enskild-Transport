package com.example.enskildtransport.controller;

import com.example.enskildtransport.model.*;
import com.example.enskildtransport.repository.RouteRepository;
import com.example.enskildtransport.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private List<Route> routeList;

    @GetMapping("weather/{query}")
    public ResponseEntity<GeoCoodingDetails> getGeoCooding(@PathVariable String query, RestTemplate restTemplate) {
        StringBuilder builder = new StringBuilder("http://api.openweathermap.org/geo/1.0/direct?");
        builder.append("q=")
                .append(query)
                .append("&appid=")
                .append("f24f5e9709bc77a5de811683e7de8f19");
        ResponseEntity<GeoCoodingDetails[]> geoCooding = restTemplate.getForEntity(builder.toString(), GeoCoodingDetails[].class);
        GeoCoodingDetails details = geoCooding.getBody()[0];
        return ResponseEntity.ok(details);
    }

    @GetMapping("weather/many/{query1}/{query2}")
    public List<GeoCoodingDetails> getManyGeoCoodings(
            @PathVariable String query1,
            @PathVariable String query2,
            RestTemplate restTemplate) {
        StringBuilder builder = new StringBuilder("http://api.openweathermap.org/geo/1.0/direct?");
        builder.append("q=")
                .append(query1)
                .append("&appid=")
                .append("f24f5e9709bc77a5de811683e7de8f19");
        ResponseEntity<GeoCoodingDetails[]> response1 = restTemplate.getForEntity(builder.toString(), GeoCoodingDetails[].class);
        GeoCoodingDetails details1 = response1.getBody()[0];

        builder = new StringBuilder("http://api.openweathermap.org/geo/1.0/direct?");
        builder.append("q=")
                .append(query2)
                .append("&appid=")
                .append("f24f5e9709bc77a5de811683e7de8f19");
        ResponseEntity<GeoCoodingDetails[]> response2 = restTemplate.getForEntity(builder.toString(), GeoCoodingDetails[].class);
        GeoCoodingDetails details2 = response2.getBody()[0];

        List<GeoCoodingDetails> detailsList = new ArrayList<>();
        detailsList.add(details1);
        detailsList.add(details2);

        return detailsList;
    }




    @GetMapping("get/weather/{query}")
    public ResponseEntity<Weather> getWeather(@PathVariable String query, RestTemplate restTemplate) {

        ResponseEntity<GeoCoodingDetails> geoCoodingResponse = getGeoCooding(query, restTemplate);
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
        System.out.println(weather.getBody());
        return ResponseEntity.ok(details);
    }

    //https://api.openweathermap.org/data/2.5/weather?
    // lat=59.3251172&lon=18.0710935&appid=f24f5e9709bc77a5de811683e7de8f19


    @GetMapping("get/train/{originId}/{destId}")
    public ResponseEntity<Train> getTrain(@PathVariable String originId, @PathVariable String destId, RestTemplate restTemplate) {

        List<GeoCoodingDetails> detailsList = getManyGeoCoodings(originId, destId, restTemplate);
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
        System.out.println(train.getBody());
        return ResponseEntity.ok(details);
    }


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
    public ResponseEntity<List<Route>> getRouteByStartAndEndLocation(
            @PathVariable String startLocation,
            @PathVariable String endLocation,
            @PathVariable String transportType,
            @PathVariable int limit){

        List<Route> routes = routeService.findRouteByTransportTypeAndStartLocationAndEndLocation(transportType,startLocation, endLocation);

        List<Route> limitedRoutes = routes.subList(0, Math.min(limit, routes.size()));

        String route = routes.toArray()[0].toString();
        String startIsStation = "startLocationIsStation=true";
        String endIsStation = "endLocationIsStation=true";

        if (route.contains(endIsStation) || route.contains(startIsStation)) {
            System.out.println(endIsStation);
            System.out.println(startIsStation);

        }


        return ResponseEntity.ok(limitedRoutes);
    }

    @GetMapping("/weather/{startLocation}/to/{endLocation}/{transportType}/{limit}")
    public ResponseEntity<RouteDetails> getRouteByStartAndEndLocation(
            @PathVariable String startLocation,
            @PathVariable String endLocation,
            @PathVariable String transportType,
            @PathVariable int limit,
            RestTemplate restTemplate){

        ResponseEntity<GeoCoodingDetails> geoCoodingResponse = getGeoCooding(endLocation, restTemplate);
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
            System.out.println(endIsStation);
            System.out.println(startIsStation);
        }


        if (routes.isEmpty() || limit <= 0) {
            details = null;
        }

        List<Route> limitedRoutes = routes.subList(0, Math.min(limit, routes.size()));
        RouteDetails routeDetails = new RouteDetails(details, limitedRoutes);

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

