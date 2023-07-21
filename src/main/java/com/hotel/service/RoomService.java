package com.hotel.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotel.dto.RoomFormDto;
import com.hotel.dto.RoomImgDto;
import com.hotel.dto.RoomTypeListDto;
import com.hotel.entity.RoomImg;
import com.hotel.entity.RoomType;
import com.hotel.repository.RoomImgRepository;
import com.hotel.repository.RoomRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomService {
	
	private final RoomRepository roomRepository ;
	private final RoomImgRepository roomImgRepository ;
	
	
	//객실타입 가져오기
	@Transactional(readOnly = true)
	public RoomFormDto getRoomDtl(Long typeId) {
		//1.item_img 테이블의 이미지를 가져온다.
		List<RoomImg> roomImgList = roomImgRepository.sae(typeId);
		
		//ItemImg 엔티티 객체 -> ItemImgDto로 변환
		List<RoomImgDto> roomImgDtoList = new ArrayList<>();
		for(RoomImg roomImg : roomImgList) {
			RoomImgDto roomImgDto = RoomImgDto.of(roomImg);
			roomImgDtoList.add(roomImgDto);
		}
		
		//2.item 테이블에 있는 데이터를 가져온다.
		RoomType roomType = roomRepository.findById(typeId)
										  .orElseThrow(EntityNotFoundException::new);
		
		//Item 엔티티 객체 -> dto로 변환
		RoomFormDto roomFormDto = RoomFormDto.of(roomType);
		
		//3.ItemFormDto에 이미지 정보(itemImgDtoList)를 넣어준다.
		roomFormDto.setRoomImgDtoList(roomImgDtoList);
		
		return roomFormDto;
	}

	
	
	
	@Transactional(readOnly = true)
	public List<RoomTypeListDto> getRoomTypeList(){
		List<RoomTypeListDto> roomTypeList = roomRepository.getRoomTypeList();
		
		return roomTypeList;
	}
	
}
