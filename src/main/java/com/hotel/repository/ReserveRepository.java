package com.hotel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hotel.dto.RoomImgDto;
import com.hotel.entity.Reservation;

public interface ReserveRepository extends JpaRepository<Reservation, Long>{
	
	
	
}
