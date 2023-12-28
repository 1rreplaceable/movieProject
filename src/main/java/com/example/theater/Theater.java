package com.example.theater;

// Theater.java

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Theater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long theaterId;

    private String theaterName;
    private String location;
    private int screeningHalls;
    private int totalSeats;
    private String phoneNumber;

}
