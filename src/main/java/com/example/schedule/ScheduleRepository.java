package com.example.schedule;

// ScheduleRepository.java

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByTheaterIdAndMovieId(Long theaterId, Long movieId);
}
