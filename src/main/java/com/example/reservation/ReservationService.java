package com.example.reservation;

// ReservationService.java

import com.example.movie.Movie;
import com.example.movie.MovieRepository;
import com.example.schedule.Schedule;
import com.example.theater.Theater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private  final MovieRepository movieRepository;
    @Autowired
    public ReservationService(ReservationRepository reservationRepository, MovieRepository movieRepository) {
        this.reservationRepository = reservationRepository;
        this.movieRepository = movieRepository;
    }

    public List<Theater> getAllTheaters() {
        return reservationRepository.findAll();
    }

    public List<Movie> getMoviesByTheater(Long theaterId) {
        System.out.println("getMoviesByTheater 실행");
        return movieRepository.findByTheaterTheaterId(theaterId);
    }

    public List<Schedule> getSchedulesByMovie(Long movieId) {
        // 여기에서 선택된 영화에 따른 상영 일정 목록을 가져오는 로직을 추가
        return null;
    }

    public Reservation submitReservation(Reservation reservationData) {
        // 여기에서 예약 정보를 처리하고 저장하는 로직을 추가
        return null;
    }
}
