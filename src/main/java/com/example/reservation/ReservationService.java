package com.example.reservation;

// ReservationService.java

import com.example.movie.Movie;
import com.example.movie.MovieRepository;
import com.example.schedule.Schedule;
import com.example.schedule.ScheduleRepository;
import com.example.seat.Seat;
import com.example.seat.SeatRepository;
import com.example.theater.Theater;
import com.example.theater.TheaterRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private final TheaterRepository theaterRepository;
    @Autowired
    public ReservationService(ReservationRepository reservationRepository, MovieRepository movieRepository, ScheduleRepository scheduleRepository, SeatRepository seatRepository, TheaterRepository theaterRepository) {
        this.reservationRepository = reservationRepository;
        this.movieRepository = movieRepository;
        this.scheduleRepository = scheduleRepository;
        this.seatRepository = seatRepository;
        this.theaterRepository = theaterRepository;
    }

    public List<Theater> getAllTheaters() {
        return theaterRepository.findAll();
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
        System.out.println("예약 정보 전달 확인: " + reservationData.toString());

        // 예약 정보 생성
        Reservation reservation = new Reservation();
        reservation.setTheaterId(reservationData.getTheaterId());
        reservation.setMovieId(reservationData.getMovieId());
        reservation.setScheduleId(reservationData.getScheduleId());
        reservation.setSeats(reservationData.getSeats());

        // 예약 정보 저장
        reservationRepository.save(reservation);

        // 저장된 예약 정보 반환 (이 부분을 원하는 대로 수정 가능)
        List<Integer> seatNumbers = reservationData.getSeats();
        for (Integer seatNumber : seatNumbers) {
            Seat seat = seatRepository.findByTheaterIdAndMovieIdAndScheduleIdAndSeatNumber(
                    reservationData.getTheaterId(),
                    reservationData.getMovieId(),
                    reservationData.getScheduleId(),
                    seatNumber
            ).orElseThrow(EntityNotFoundException::new);
            seat.setReserved(true);
            seatRepository.save(seat);
        }
        return reservation;
    }
}
