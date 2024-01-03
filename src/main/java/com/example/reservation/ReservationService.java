package com.example.reservation;

// ReservationService.java

import com.example.movie.Movie;
import com.example.movie.MovieRepository;
import com.example.schedule.Schedule;
import com.example.schedule.ScheduleRepository;
import com.example.theater.Theater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private  final MovieRepository movieRepository;
    private  final ScheduleRepository scheduleRepository;
    @Autowired
    public ReservationService(ReservationRepository reservationRepository, MovieRepository movieRepository, ScheduleRepository scheduleRepository) {
        this.reservationRepository = reservationRepository;
        this.movieRepository = movieRepository;
        this.scheduleRepository = scheduleRepository;
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

    public Reservation submitReservation(Reservation reservationData) {
        // 여기에서 예약 정보를 처리하고 저장하는 로직을 추가
        return null;
    }
}
