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
import com.example.user.User;
import com.example.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private  final MovieRepository movieRepository;
    private  final ScheduleRepository scheduleRepository;
    private final SeatRepository seatRepository;
    private final TheaterRepository theaterRepository;
    private final ReservationSeatRepository reservationSeatRepository;
    private final UserRepository userRepository;
    @Autowired
    public ReservationService(ReservationRepository reservationRepository, MovieRepository movieRepository, ScheduleRepository scheduleRepository, SeatRepository seatRepository, TheaterRepository theaterRepository, ReservationSeatRepository reservationSeatRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.movieRepository = movieRepository;
        this.scheduleRepository = scheduleRepository;
        this.seatRepository = seatRepository;
        this.theaterRepository = theaterRepository;
        this.reservationSeatRepository = reservationSeatRepository;
        this.userRepository = userRepository;
    }

    public List<Theater> getAllTheaters() {
        return theaterRepository.findAll();
    }

    public class DateTimeUtil {

        public static LocalDateTime convertToLocalDateTime(Date date) {
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
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


    public Reservation submitReservation(Reservation reservationData, Long userId) {
        System.out.println("예약 정보 전달 확인: " + reservationData.toString());

        // 예약 정보 생성
        Reservation reservation = new Reservation();
        reservation.setUserId(userId);
        reservation.setTheaterId(reservationData.getTheaterId());
        reservation.setMovieId(reservationData.getMovieId());
        reservation.setScheduleId(reservationData.getScheduleId());
        reservation.setSeats(reservationData.getSeats());

        // 예약 정보 저장
        reservationRepository.save(reservation);

        // 저장된 예약 정보의 ID를 가져옴
        Long reservationId = reservation.getReservationId();

        // 예약된 좌석들의 예약 ID 설정
        List<Integer> seatNumbers = reservationData.getSeats();
        for (Integer seatNumber : seatNumbers) {
            Seat seat = seatRepository.findByTheaterIdAndMovieIdAndScheduleIdAndSeatNumber(
                    reservationData.getTheaterId(),
                    reservationData.getMovieId(),
                    reservationData.getScheduleId(),
                    seatNumber
            ).orElseThrow(EntityNotFoundException::new);
            seat.setReserved(true);
            seat.setReservationId(reservationId); // 예약 ID 설정
            seatRepository.save(seat);
        }
        return reservation;
    }

    public List<ReservationDTO> getReservationsByUserId(Long userId) {
        List<Reservation> reservations = reservationRepository.findByUserId(userId);

        // Reservation 객체를 ReservationDTO로 변환하여 반환합니다.
        return reservations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ReservationDTO 변환 메서드
    private ReservationDTO convertToDTO(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        dto.setReservationId(reservation.getReservationId());
        dto.setUserId(reservation.getUserId());

        // 영화 제목 가져오기
        Movie movie = movieRepository.findById(reservation.getMovieId()).orElse(null);
        if (movie != null) {
            dto.setMovieTitle(movie.getTitle());
            dto.setImageUrl(movie.getImageUrl());
            dto.setMovieId(movie.getMovieId());
        }
        User user = userRepository.findById(reservation.getUserId()).orElse(null);
        if (user != null) {
            dto.setUserName(user.getUsername());
        }
        // 영화관 정보 가져오기
        Theater theater = theaterRepository.findById(reservation.getTheaterId()).orElse(null);
        if (theater != null) {
            dto.setTheaterName(theater.getTheaterName());
            dto.setLocation(theater.getLocation());
        }

        // 상영 일자 및 시간 가져오기
        Schedule schedule = scheduleRepository.findById(reservation.getScheduleId()).orElse(null);
        if (schedule != null) {
            dto.setStartTime(DateTimeUtil.convertToLocalDateTime(schedule.getStartTime()));

            if (movie != null) {
                // 영화의 상영 시간(duration)을 더하여 종료 시간 계산
                LocalDateTime startTime = DateTimeUtil.convertToLocalDateTime(schedule.getStartTime());
                LocalDateTime endTime = startTime.plusMinutes(movie.getDurationMinutes());
                dto.setEndTime(endTime);
            }
        }

        List<ReservationSeat> reservationSeats = reservationSeatRepository.findByReservationId(reservation.getReservationId());
        System.out.println(reservation.getReservationId());
        System.out.println(reservationSeats);
        List<Integer> seats = reservationSeats.stream().map(ReservationSeat::getSeatNumber).collect(Collectors.toList());
        dto.setSeats(seats);

        return dto;
    }

    public void cancelReservation(Long reservationId) {
        // 예약을 취소하고 좌석의 isReserved를 false로 업데이트
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found with ID: " + reservationId));

        List<Integer> seats = reservation.getSeats();
        for (Integer seatNumber : seats) {
            Seat seat = seatRepository.findByTheaterIdAndMovieIdAndScheduleIdAndSeatNumber(
                    reservation.getTheaterId(),
                    reservation.getMovieId(),
                    reservation.getScheduleId(),
                    seatNumber
            ).orElseThrow(() -> new EntityNotFoundException("Seat not found for reservation with ID: " + reservationId));
            seat.setReserved(false);
            seat.setReservationId(null); // 예약 ID를 null로 설정
            seatRepository.save(seat);
        }

        // 예약 삭제
        reservationRepository.deleteById(reservationId);
    }
}
