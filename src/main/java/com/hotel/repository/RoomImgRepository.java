package com.hotel.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotel.entity.RoomImg;

public interface RoomImgRepository extends JpaRepository<RoomImg, Long> {
	
	//select * from room_img where type_id = ? order by room_id asc;
//	List<RoomImg> findByTypeId(Long typeId);
	
	@Modifying
	@Query(value = "select * from room_img where type_id = ?1 order by type_id asc", nativeQuery = true)
	List<RoomImg> sae(Long typeId);



	@Query(value = "select * from room_img where type_id = ?1 and repimg_yn = ?2", nativeQuery = true)
	RoomImg findByTypeIdAndRepimgYn(Long typeId , String repimgYn);
	

}
