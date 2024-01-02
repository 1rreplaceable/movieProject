package com.example.movie;
// Movie.java

import com.example.theater.Theater;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;

    private String title;
    private int durationMinutes;
    private String genre;
    private Date releaseDate;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "theater_id", referencedColumnName = "theaterId")
    @JsonBackReference
    private Theater theater;
}
