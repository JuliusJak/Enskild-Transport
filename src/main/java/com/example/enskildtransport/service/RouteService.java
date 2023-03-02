package com.example.enskildtransport.service;

import com.example.enskildtransport.model.routesModel.Route;
import com.example.enskildtransport.repository.RouteRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class RouteService {
    @Autowired
    RouteRepository routeRepository;

    private List<Route> routeList;

    public List<Route> getAll() {
        log.debug("All courses is being fetched");
        return routeRepository.findAll();
    }

    public List<Route> findByStartLocation(String startLocation) {
        return routeRepository.findRouteByStartLocation(startLocation);
    }

    public Route save(Route route) {
        return routeRepository.save(route);
    }

    public List<Route> findByEndLocation(String endLocation) {
        return routeRepository.findRouteByEndLocation(endLocation);
    }

    public Optional<Route> findById(Long routeId) {
        return routeRepository.findById(routeId);

    }

    public List<Route> findByIsFavorite() {
        return routeRepository.findByIsFavorite(true);
    }

    public List<Route> findByStartAndEndLocation(String startLocation, String endLocation) {
        log.info("Didn't find a route");
        List<Route> routes = routeRepository.findRouteByStartLocationAndEndLocation(startLocation, endLocation);
        return routes;
    }

    public List<Route> findRouteByTransportTypeAndStartLocationAndEndLocation(String transportType, String startLocation, String endLocation) {
        return routeRepository.findRouteByTransportTypeAndStartLocationAndEndLocation(transportType, startLocation, endLocation);
    }

    public List<Route> findByTransportType(String transportType) {
        return routeRepository.findRouteByTransportType(transportType);
    }

    public List<Route> findByIsFavoriteAndTransportType(boolean b, String transportType) {
        return routeRepository.findByIsFavoriteAndTransportType(b,transportType);
    }

}
