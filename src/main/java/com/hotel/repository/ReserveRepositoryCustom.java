package com.hotel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hotel.dto.ReservationHistDto;
import com.hotel.dto.SearchDto;

public interface ReserveRepositoryCustom {

	Page<ReservationHistDto> getAdminReservationPage(SearchDto searchDto, Pageable pageable);
}
