package com.example.reservation;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ReservationSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reservation_reservation_id")
    private Long reservationId;

    private Integer seatNumber;
}