package com.example.reservation;

// ReservationRequest.java

import lombok.Data;

import java.util.List;
@Data
public class ReservationRequest {
    private Long theaterId;
    private Long movieId;
    private Long scheduleId;
    private List<Long> seats;

}
