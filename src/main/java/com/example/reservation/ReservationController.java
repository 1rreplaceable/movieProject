package com.example.reservation;

// ReservationController.java

import com.example.movie.Movie;
import com.example.schedule.Schedule;
import com.example.seat.Seat;
import com.example.theater.Theater;
import com.example.user.User;
import jakarta.servlet.http.HttpSession;
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
    public ResponseEntity<String> submitReservation(@RequestBody Reservation reservationData, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        System.out.println("예약버튼 잘 눌림");
        try {
            reservationService.submitReservation(reservationData,user.getId());
            return ResponseEntity.ok("Reservation submitted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error submitting reservation");
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<ReservationDTO>> getReservationsByUserId(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        List<ReservationDTO> reservations = reservationService.getReservationsByUserId(user.getId());
        return ResponseEntity.ok(reservations);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<?> cancelReservation(@PathVariable Long reservationId) {
        try {
            // 예약 취소 로직 수행
            reservationService.cancelReservation(reservationId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
