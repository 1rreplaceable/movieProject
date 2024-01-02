package com.example.reservation;

import com.example.theater.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

// ReservationRepository.java
public interface ReservationRepository extends JpaRepository<Theater, Long> {

}

