package com.example.enskildtransport.repository;

import com.example.enskildtransport.model.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritesRepository extends JpaRepository<Favorites, Long> {

    Favorites saveFavorite(Favorites favorites);
    Favorites getFavoriteById(Long id);
    List<Favorites> getAllFavorites();
    void deleteFavorite(Long id);


}
