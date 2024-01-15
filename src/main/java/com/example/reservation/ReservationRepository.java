package com.example.reservation;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

// ReservationRepository.java
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}

