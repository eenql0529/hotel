package com.hotel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotel.dto.RoomImgDto;
import com.hotel.entity.RoomType;

public interface RoomRepository extends JpaRepository<RoomType, Long>,
										RoomRepositoryCustom{
	
//	List<RoomType> findByTypeName(String typeName);
	@Modifying
	@Query(value = "select * from room_type " , nativeQuery = true)
	List<RoomType> getAdminList();	
	
	@Modifying
	@Query(value = "select * from room_img " , nativeQuery = true)
	List<RoomImgDto> getImgDtos();	
	
	
	@Modifying
	@Query(value = "select ")

}
