package com.booking.recruitment.hotel.repository;

import com.booking.recruitment.hotel.model.City;
import com.booking.recruitment.hotel.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    @Query("Select h from Hotel h where h.city.id=:cityId")
    public List<Hotel> findByCityId(Long cityId);
}
