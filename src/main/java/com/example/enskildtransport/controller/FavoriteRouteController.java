package com.example.enskildtransport.controller;

import com.example.enskildtransport.model.routesModel.Route;
import com.example.enskildtransport.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/favorite/*")
public class FavoriteRouteController {

    @Autowired
    private RouteService routeService;

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
