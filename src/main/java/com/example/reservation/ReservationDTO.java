package com.example.reservation;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class ReservationDTO {
    private Long reservationId;
    private Long userId;
    private String userName;
    private Long movieId;
    private String movieTitle;
    private String theaterName;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String imageUrl;
    private List<Integer> seats;
}
