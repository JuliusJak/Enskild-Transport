package com.example.enskildtransport.repository;

import com.example.enskildtransport.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findRouteByStartLocation(String startLocation);
    List<Route> findRouteByEndLocation (String endLocation);
    List<Route> findByIsFavorite(boolean b);
    List<Route> findByIsFavoriteAndTransportType(boolean b, String transportType);
    List<Route> findRouteByStartLocationAndEndLocation(String startLocation, String endLocation);
    List<Route> findRouteByTransportType(String transportType);
    List<Route> findRouteByTransportTypeAndStartLocationAndEndLocation(String transportType, String startLocation, String endLocation);
}
