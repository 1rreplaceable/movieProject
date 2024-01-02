package com.example.theater;

// Theater.java

import com.example.movie.Movie;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

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


    @OneToMany(mappedBy = "theater")
    @JsonManagedReference
    private List<Movie> movies;
}
