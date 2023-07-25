package com.hotel.repository;

import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.domain.Page;

import com.hotel.dto.ReserveDto;
import com.hotel.dto.RoomTypeListDto;
import com.hotel.entity.RoomType;

public interface RoomRepositoryCustom {
	Page<RoomType> getAdminRoomPage(Pageable pageable);
	
	List<RoomTypeListDto> getRoomTypeList();
	
	
	
}
