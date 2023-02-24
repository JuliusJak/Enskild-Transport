package com.example.enskildtransport.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Favorites {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long favoriteId;
    public Long getFavoriteId(){
        return favoriteId;
    }
    public void setFavoriteId(Long favoriteId) {
        this.favoriteId = favoriteId;
    }
}
