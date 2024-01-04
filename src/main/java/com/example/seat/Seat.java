package com.example.seat;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "seats")
@Data
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;

    private Long theaterId;
    private Long movieId;
    private Long scheduleId;
    private int seatNumber;
    private boolean isReserved;  // 변경된 필드명
}
