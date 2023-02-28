package com.example.enskildtransport.controller;

import com.example.enskildtransport.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("routes")
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

    @GetMapping("get/weather/{lat}/{lon}")
    public ResponseEntity<Weather> getWeather(@PathVariable String lat, @PathVariable String lon, RestTemplate restTemplate) {
        StringBuilder builder = new StringBuilder("https://api.openweathermap.org/data/2.5/weather?");
        builder.append("lat=").append(lat)
                .append("&lon=").append(lon)
                .append("&appid=")
                .append("f24f5e9709bc77a5de811683e7de8f19");
        ResponseEntity<Weather> weather = restTemplate.getForEntity(builder.toString(), Weather.class);
        Weather details = weather.getBody();
        return ResponseEntity.ok(details);
    }


















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
