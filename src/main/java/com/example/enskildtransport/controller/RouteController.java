package com.example.enskildtransport.controller;

import com.example.enskildtransport.model.PublicModel.PublicRoute;
import com.example.enskildtransport.model.exceptionHandler.ExceptionHandler;
import com.example.enskildtransport.model.routesModel.Route;
import com.example.enskildtransport.model.routesModel.RouteAndPublicRoute;
import com.example.enskildtransport.model.routesModel.RouteFusion;
import com.example.enskildtransport.model.trainModel.Train;
import com.example.enskildtransport.model.weatherModel.GeoCoodingDetails;
import com.example.enskildtransport.model.weatherModel.Weather;
import com.example.enskildtransport.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;

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
    public List<Route> getRouteByStartLocation(@PathVariable String startLocation) throws ExceptionHandler{
        List<Route> routes = routeService.findByStartLocation(startLocation);
        if (routes.isEmpty()) {
            throw new ExceptionHandler("No route available. Check your spelling");
        }
        return routes;
    }

    @GetMapping("/End/{endLocation}")
    public List<Route> getRouteByEndLocation(@PathVariable String endLocation)throws ExceptionHandler{
        List<Route> routes = routeService.findByEndLocation(endLocation);
        if (routes.isEmpty()) {
            throw new ExceptionHandler("No route available. Check your spelling");
        }
        return routes;
    }

    @GetMapping("/{transportType}")
    public ResponseEntity<List<Route>> getRouteByTransportType(@PathVariable String transportType) {
        List<Route> routes = routeService.findByTransportType(transportType);
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/{startLocation}/to/{endLocation}/{transportType}/{limit}")
    public ResponseEntity<RouteAndPublicRoute> getRouteByStartAndEndLocation(
            @PathVariable String startLocation,
            @PathVariable String endLocation,
            @PathVariable String transportType,
            @PathVariable int limit,
            RestTemplate restTemplate) throws ExceptionHandler {


            List<Route> routes = routeService.findRouteByTransportTypeAndStartLocationAndEndLocation(transportType, startLocation, endLocation);
            List<Route> limitedRoutes = routes.subList(0, Math.min(limit, routes.size()));

            if (routes.isEmpty()) {

                throw new ExceptionHandler("No route available. Check your spelling");
            }

            String route = routes.toArray()[0].toString();
            String startIsStation = "startLocationIsStation=true";
            String endIsStation = "endLocationIsStation=true";

            if (route.contains(endIsStation) || route.contains(startIsStation)) {
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
            RestTemplate restTemplate) throws ExceptionHandler{

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

        if (routes.isEmpty()) {
            throw new ExceptionHandler("No route available. Check your spelling");
        }


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

    @GetMapping("/combined/{startLocation}/to/{startStation}/to/{endStation}/{limit}")
    public ResponseEntity<RouteAndPublicRoute> getCombinedRoute(
            RestTemplate restTemplate,
            @PathVariable String startLocation,
            @PathVariable String startStation,
            @PathVariable int limit,
            @PathVariable String endStation) throws ExceptionHandler{

        List<Route> routes = routeService.findByStartAndEndLocation(startLocation, endStation);
        if (routes.isEmpty()) {
            throw new ExceptionHandler("No route available. Check your spelling");
        }

        List<Route> limitedRoutes = routes.subList(0, Math.min(limit, routes.size()));

        RouteAndPublicRoute routeDetails = new RouteAndPublicRoute(limitedRoutes, getPublicRoutes(restTemplate, startStation, endStation));
        return ResponseEntity.ok(routeDetails);
    }





    //it's not being used, but I also don't want to delete it
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