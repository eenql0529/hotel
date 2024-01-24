package com.hotel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hotel.dto.ReserveDto;
import com.hotel.dto.RoomTypeListDto;
import com.hotel.dto.RoomtypeSearchDto;
import com.hotel.entity.RoomType;

public interface RoomRepositoryCustom {
	Page<RoomType> getAdminRoomPage(RoomtypeSearchDto roomtypeSearchDto,Pageable pageable);
	
	List<RoomTypeListDto> getRoomTypeList();
	
	
	
}
