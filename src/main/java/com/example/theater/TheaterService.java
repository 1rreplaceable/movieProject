package com.example.theater;

// TheaterService.java

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheaterService {

    private final TheaterRepository theaterRepository;

    @Autowired
    public TheaterService(TheaterRepository theaterRepository) {
        this.theaterRepository = theaterRepository;
    }

    public List<Theater> getAllTheaters() {
        return theaterRepository.findAll();
    }
}
