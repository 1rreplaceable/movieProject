package com.example.reservation;

// Reservation.java

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    private Long theaterId;
    private Long movieId;
    private Long scheduleId;
    @ElementCollection
    private List<Integer> seats;

}
