package com.example.enskildtransport.controller;

import com.example.enskildtransport.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/routes/*")
public class RouteController {



    @GetMapping("weather/{query}")
    public ResponseEntity<GeoCoodingDetails> getGeoCooding(@PathVariable String query, RestTemplate restTemplate) {
        StringBuilder builder = new StringBuilder("http://api.openweathermap.org/geo/1.0/direct?");
        builder.append("q=")
                .append(query).append("&appid=")
                .append("f24f5e9709bc77a5de811683e7de8f19");
        ResponseEntity<GeoCoodingDetails[]> geoCooding = restTemplate.getForEntity(builder.toString(), GeoCoodingDetails[].class);
        GeoCoodingDetails details = geoCooding.getBody()[0];
        return ResponseEntity.ok(details);
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
        StringBuilder builder = new StringBuilder("https://api.resrobot.se/v2.1/trip?");
        builder.append("originId=").append(originId)
                .append("&destId=").append(originId)
                .append("&accessId=")
                .append("900be3c6-6024-4578-9c15-1824fb949211");
        ResponseEntity<Train> train = restTemplate.getForEntity(builder.toString(), Train.class);
        Train details = train.getBody();
        System.out.println(train.getBody());
        return ResponseEntity.ok(details);
    }


    @GetMapping("traffic/{originId}/{destId}")
    public ResponseEntity<Train> getGeoCooding(@PathVariable String originId, @PathVariable String destId, RestTemplate restTemplate) {
        StringBuilder builder = new StringBuilder("https://api.resrobot.se/v2.1/trip?");
        builder
                .append("format=").append("json")

                .append("&originId=").append(originId)
                .append("&destId=").append(destId)
                .append("&accessId=").append("900be3c6-6024-4578-9c15-1824fb949211");

        ResponseEntity<Train> traffic = restTemplate
                .getForEntity(builder.toString(), Train.class);


        return ResponseEntity.ok(traffic.getBody());
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

















/*





    @GetMapping("/Car")
    public Car selectCar(@RequestBody Car car) {

        return null;
    }

    @GetMapping("/Walk")
    public Walk selectCar(@RequestBody Walk walk) {

        return null;

    }

    @PostMapping("/add/favorites")
    public Favorites saveAsFavorite(@PathVariable Long id, @RequestBody Favorites favorites) {
        //id = ??
        favorites.setFavoriteId(id);
        return favoritesRepository.saveFavorite(favorites);
    }

    @PostMapping("/all/favorites")
    public List<Favorites> getAllFavorites() {

        List<Favorites> favorites = favoritesRepository.getAllFavorites();

        return favorites;
    }

 */
}
