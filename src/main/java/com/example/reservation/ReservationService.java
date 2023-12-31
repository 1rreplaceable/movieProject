package com.example.reservation;

// ReservationService.java

import com.example.movie.Movie;
import com.example.movie.MovieRepository;
import com.example.schedule.Schedule;
import com.example.schedule.ScheduleRepository;
import com.example.seat.Seat;
import com.example.seat.SeatRepository;
import com.example.theater.Theater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private  final MovieRepository movieRepository;
    private  final ScheduleRepository scheduleRepository;
    private final SeatRepository seatRepository;
    @Autowired
    public ReservationService(ReservationRepository reservationRepository, MovieRepository movieRepository, ScheduleRepository scheduleRepository, SeatRepository seatRepository) {
        this.reservationRepository = reservationRepository;
        this.movieRepository = movieRepository;
        this.scheduleRepository = scheduleRepository;
        this.seatRepository = seatRepository;
    }

    public List<Theater> getAllTheaters() {
        return reservationRepository.findAll();
    }

    public List<Movie> getMoviesByTheater(Long theaterId) {
        System.out.println("getMoviesByTheater 실행");
        return movieRepository.findByTheaterTheaterId(theaterId);
    }

    public List<Schedule> getMovieSchedulesByTheaterAndMovie(Long theaterId, Long movieId) {
        System.out.println("getMovieSchedulesByTheaterAndMovie 실행");
        return scheduleRepository.findByTheaterIdAndMovieId(theaterId, movieId);
    }

    public Map<String, List<Seat>> getSeatsAndReservedSeatsByTheaterMovieAndSchedule(Long theaterId, Long movieId, Long scheduleId) {
        Map<String, List<Seat>> result = new HashMap<>();

        List<Seat> seats = seatRepository.findByTheaterIdAndMovieIdAndScheduleId(theaterId, movieId, scheduleId);
        List<Seat> reservedSeats = seatRepository.findByTheaterIdAndMovieIdAndScheduleIdAndIsReservedTrue(theaterId, movieId, scheduleId);

        result.put("seats", seats);
        result.put("reservedSeats", reservedSeats);

        return result;
    }


    // 변경된 메서드
    public void reserveSeats(List<Long> seatIds) {
        // 예약된 좌석을 찾아서 isReversed을 true로 업데이트
        List<Seat> reversedSeats = seatRepository.findAllById(seatIds);
        reversedSeats.forEach(seat -> seat.setReserved(true));
        seatRepository.saveAll(reversedSeats);
    }

    // 변경된 메서드
    public void cancelReservation(List<Long> seatIds) {
        // 예약을 취소하고 좌석의 isReversed을 false로 업데이트
        List<Seat> cancelledSeats = seatRepository.findAllById(seatIds);
        cancelledSeats.forEach(seat -> seat.setReserved(false));
        seatRepository.saveAll(cancelledSeats);
    }


    public Reservation submitReservation(Reservation reservationData) {
        // 여기에서 예약 정보를 처리하고 저장하는 로직을 추가
        return null;
    }
}
