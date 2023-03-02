package com.example.enskildtransport.controller;

import com.example.enskildtransport.model.GeoCoodingDetails;
import com.example.enskildtransport.model.Weather;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/weather/*")
public class WeatherController {
    String address = "http://api.openweathermap.org/geo/1.0/direct?";
    String key = "f24f5e9709bc77a5de811683e7de8f19";

    @GetMapping("/{query}")
    public ResponseEntity<GeoCoodingDetails> getGeoCooding(@PathVariable String query, RestTemplate restTemplate) {
        StringBuilder builder = new StringBuilder(address);
        builder.append("q=")
                .append(query)
                .append("&appid=")
                .append(key);
        ResponseEntity<GeoCoodingDetails[]> geoCooding = restTemplate.getForEntity(builder.toString(), GeoCoodingDetails[].class);
        GeoCoodingDetails details = geoCooding.getBody()[0];
        return ResponseEntity.ok(details);
    }

    @GetMapping("/many/{query1}/{query2}")
    public List<GeoCoodingDetails> getManyGeoCoodings(
            @PathVariable String query1,
            @PathVariable String query2,
            RestTemplate restTemplate) {
        StringBuilder builder = new StringBuilder(address);
        builder.append("q=")
                .append(query1)
                .append("&appid=")
                .append(key);
        ResponseEntity<GeoCoodingDetails[]> response1 = restTemplate.getForEntity(builder.toString(), GeoCoodingDetails[].class);
        GeoCoodingDetails details1 = response1.getBody()[0];

        StringBuilder builder2 = new StringBuilder(address);

        builder2.append("q=")
                .append(query2)
                .append("&appid=")
                .append(key);
        ResponseEntity<GeoCoodingDetails[]> response2 = restTemplate.getForEntity(builder2.toString(), GeoCoodingDetails[].class);
        GeoCoodingDetails details2 = response2.getBody()[0];

        List<GeoCoodingDetails> detailsList = new ArrayList<>();
        detailsList.add(details1);
        detailsList.add(details2);

        return detailsList;
    }

    @GetMapping("get/{query}")
    public ResponseEntity<Weather> getWeather(@PathVariable String query, RestTemplate restTemplate) {

        ResponseEntity<GeoCoodingDetails> geoCoodingResponse = getGeoCooding(query, restTemplate);
        GeoCoodingDetails geoCoodingDetails = geoCoodingResponse.getBody();
        String lat = geoCoodingDetails.getLat();
        String lon = geoCoodingDetails.getLon();

        StringBuilder strbuilder = new StringBuilder("https://api.openweathermap.org/data/2.5/weather?");
        strbuilder.append("lat=").append(lat)
                .append("&lon=").append(lon)
                .append("&appid=")
                .append(key);

        ResponseEntity<Weather> weather = restTemplate.getForEntity(strbuilder.toString(), Weather.class);
        Weather details = weather.getBody();
        return ResponseEntity.ok(details);
    }
}
