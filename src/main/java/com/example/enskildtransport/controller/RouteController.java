package com.example.enskildtransport.controller;

import com.example.enskildtransport.model.Car;
import com.example.enskildtransport.model.Walk;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/routes")
public class RouteController {

    @GetMapping("/Car")
    public Car selectCar(@RequestBody Car car) {

    }

    @GetMapping("/Walk")
    public Walk selectCar(@RequestBody Walk walk) {

    }
}
