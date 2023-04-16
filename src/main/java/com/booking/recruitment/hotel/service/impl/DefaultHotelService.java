package com.booking.recruitment.hotel.service.impl;

import com.booking.recruitment.hotel.exception.BadRequestException;
import com.booking.recruitment.hotel.exception.ElementNotFoundException;
import com.booking.recruitment.hotel.model.City;
import com.booking.recruitment.hotel.model.Hotel;
import com.booking.recruitment.hotel.repository.CityRepository;
import com.booking.recruitment.hotel.repository.HotelRepository;
import com.booking.recruitment.hotel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
class DefaultHotelService implements HotelService {
  private final HotelRepository hotelRepository;
  private final CityRepository cityRepository;

  private static final String DISTANCE = "distance";
  private static final int HOTELS_LIMIT = 3;

  @Autowired
  DefaultHotelService(HotelRepository hotelRepository, CityRepository cityRepository) {
    this.hotelRepository = hotelRepository;
    this.cityRepository = cityRepository;
  }

  @Override
  public List<Hotel> getAllHotels() {
    return hotelRepository.findAll();
  }

  @Override
  public List<Hotel> getHotelsByCity(Long cityId) {
    return hotelRepository.findAll().stream()
        .filter((hotel) -> cityId.equals(hotel.getCity().getId()))
        .collect(Collectors.toList());
  }

  @Override
  public Hotel createNewHotel(Hotel hotel) {
    if (hotel.getId() != null) {
      throw new BadRequestException("The ID must not be provided when creating a new Hotel");
    }

    return hotelRepository.save(hotel);
  }

  @Override
  public Hotel getHotelById(Long id) throws ElementNotFoundException {
    return hotelRepository
            .findById(id)
            .orElseThrow(() -> new ElementNotFoundException("Hotel is not found"));
  }

  @Override
  public void deleteHotelById(Long id) throws ElementNotFoundException {
    hotelRepository
            .findById(id)
            .orElseThrow(() -> new ElementNotFoundException("Hotel is not found"));

    hotelRepository.deleteById(id);
  }

  @Override
  public List<Hotel> filterHotels(Long cityId, String sortBy) throws ElementNotFoundException {
    City city = cityRepository.findById(cityId)
            .orElseThrow(() -> new ElementNotFoundException("City is not found"));
    double lat1 = city.getCityCentreLatitude();
    double lon1 = city.getCityCentreLongitude();

    List<Hotel> hotels = hotelRepository.findByCityId(city.getId());
    Map<Double, List<Hotel>> map = DISTANCE.equals(sortBy) ? new TreeMap<>() : new HashMap<>();
    for (Hotel hotel : hotels) {
      Double distance = haversine(lat1, lon1, hotel.getLatitude(), hotel.getLongitude());
      List<Hotel> hot = map.containsKey(distance) ? new ArrayList(map.get(distance)) : new ArrayList<>();
      hot.add(hotel);
      map.put(distance, hot);
    }

    List<Hotel> result = new ArrayList<>();
    for (List<Hotel> resultHotels : map.values()) {
      for (Hotel h : resultHotels) {
        if (result.size() == HOTELS_LIMIT) {
          return result;
        } else {
          result.add(h);
        }
      }
    }

    return result;
  }

  double haversine(double lat1, double lon1,
                   double lat2, double lon2) {
    // distance between latitudes and longitudes
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);

    // convert to radians
    lat1 = Math.toRadians(lat1);
    lat2 = Math.toRadians(lat2);

    // apply formulae
    double a = Math.pow(Math.sin(dLat / 2), 2) +
            Math.pow(Math.sin(dLon / 2), 2) *
                    Math.cos(lat1) *
                    Math.cos(lat2);
    double rad = 6371;
    double c = 2 * Math.asin(Math.sqrt(a));
    return rad * c;
  }
}
