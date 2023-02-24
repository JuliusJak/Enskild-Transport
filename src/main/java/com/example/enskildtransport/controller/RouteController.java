package com.example.enskildtransport.controller;

import com.example.enskildtransport.model.Car;
import com.example.enskildtransport.model.Favorites;
import com.example.enskildtransport.model.Walk;
import com.example.enskildtransport.repository.FavoritesRepository;
import com.example.enskildtransport.service.FavoritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routes")
public class RouteController {

    @Autowired
    private FavoritesRepository favoritesRepository;
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
}
