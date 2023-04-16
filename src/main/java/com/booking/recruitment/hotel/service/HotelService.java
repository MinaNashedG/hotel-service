package com.booking.recruitment.hotel.service;

import com.booking.recruitment.hotel.exception.ElementNotFoundException;
import com.booking.recruitment.hotel.model.Hotel;

import java.util.List;

public interface HotelService {
  List<Hotel> getAllHotels();

  List<Hotel> getHotelsByCity(Long cityId);

  Hotel createNewHotel(Hotel hotel);

  Hotel getHotelById(Long id) throws ElementNotFoundException;

  void deleteHotelById(Long id) throws ElementNotFoundException;

  List<Hotel> filterHotels(Long cityId, String sortBy);
}
