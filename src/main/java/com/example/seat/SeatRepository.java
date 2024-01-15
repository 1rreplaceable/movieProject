package com.example.seat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByTheaterIdAndMovieIdAndScheduleId(Long theaterId, Long movieId, Long scheduleId);

    List<Seat> findByTheaterIdAndMovieIdAndScheduleIdAndIsReservedTrue(Long theaterId, Long movieId, Long scheduleId);

    Optional<Seat> findByTheaterIdAndMovieIdAndScheduleIdAndSeatNumber(
            Long theaterId, Long movieId, Long scheduleId, Integer seatNumber);
}
