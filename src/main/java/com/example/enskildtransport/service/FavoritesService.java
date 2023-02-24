package com.example.enskildtransport.service;


import com.example.enskildtransport.model.Favorites;
import com.example.enskildtransport.repository.FavoritesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoritesService {

    @Autowired
    private FavoritesRepository favoritesRepository;

}
