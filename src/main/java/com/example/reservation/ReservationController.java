package com.example.reservation;

// ReservationController.java

import com.example.movie.Movie;
import com.example.schedule.Schedule;
import com.example.seat.Seat;
import com.example.theater.Theater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    // 좌석 및 예약된 좌석 조회 API
    @GetMapping("/seats/{theaterId}/{movieId}/{scheduleId}")
    public ResponseEntity<Map<String, List<Seat>>> getSeatsAndReservedSeats(
            @PathVariable Long theaterId,
            @PathVariable Long movieId,
            @PathVariable Long scheduleId) {
        try {
            Map<String, List<Seat>> seatData = reservationService.getSeatsAndReservedSeatsByTheaterMovieAndSchedule(theaterId, movieId, scheduleId);
            return ResponseEntity.ok(seatData);
        } catch (Exception e) {
            // 예외 발생 시 적절한 응답을 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/reserve")
    public ResponseEntity<String> reserveSeats(@RequestBody List<Long> selectedSeats) {
        System.out.println("reservation controller 넘어옴");
        reservationService.reserveSeats(selectedSeats);
        return ResponseEntity.ok("Seats reserved successfully");
    }


    @PostMapping("/cancel")
    public void cancelReservation(@RequestBody List<Long> seatIds) {
        reservationService.cancelReservation(seatIds);
    }


    @PostMapping("/reservations")
    public ResponseEntity<String> submitReservation(@RequestBody Reservation reservationData) {
        System.out.println("예약버튼 잘 눌림");
        try {
            // 예약 데이터를 서비스로 전달하여 저장
            reservationService.submitReservation(reservationData);
            return ResponseEntity.ok("Reservation submitted successfully");
        } catch (Exception e) {
            // 예약 저장 중에 문제가 발생하면 적절한 응답 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error submitting reservation");
        }
    }
}
