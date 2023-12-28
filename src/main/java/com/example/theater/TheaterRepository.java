package com.example.theater;

// TheaterRepository.java

import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
}
