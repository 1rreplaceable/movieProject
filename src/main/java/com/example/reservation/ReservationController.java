package com.example.reservation;

// ReservationController.java

import com.example.movie.Movie;
import com.example.schedule.Schedule;
import com.example.theater.Theater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/theaters")
    public List<Theater> getTheaters() {
        return reservationService.getAllTheaters();
    }

    @GetMapping("/theaters/{theaterId}/movies")
    public List<Movie> getMoviesByTheater(@PathVariable Long theaterId) {
        return reservationService.getMoviesByTheater(theaterId);
    }

    @GetMapping("/theaters/{theaterId}/movies/{movieId}/schedules")
    public List<Schedule> getSchedulesByMovieAndTheater(
            @PathVariable Long theaterId,
            @PathVariable Long movieId) {
        System.out.println("Controller: getSchedulesByMovieAndTheater is called");
        return reservationService.getMovieSchedulesByTheaterAndMovie(theaterId, movieId);
    }


    @PostMapping("/reservations")
    public Reservation submitReservation(@RequestBody Reservation reservationData) {
        // 예약 데이터 처리 로직
        return reservationService.submitReservation(reservationData);
    }
}
