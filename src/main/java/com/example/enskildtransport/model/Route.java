package com.example.enskildtransport.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Route {


    @Id
    @GeneratedValue
    private long id;
    private String startLocation;
    private String endLocation;
    private int travelTime;
    private String transportType;
    private boolean isFavorite;



}
