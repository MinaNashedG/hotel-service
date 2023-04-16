package com.booking.recruitment.hotel.controller;

import com.booking.recruitment.hotel.model.Hotel;
import com.booking.recruitment.hotel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
public class HotelSearchController {

    private final HotelService hotelService;

    @Autowired
    public HotelSearchController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Hotel> searchByCityId(Long id, @RequestParam String sortBy) {
        return hotelService.filterHotels(id, sortBy);
    }
}
