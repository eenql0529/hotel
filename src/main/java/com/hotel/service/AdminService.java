package com.hotel.service;

import java.awt.print.Pageable;
import java.security.Principal;
import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hotel.constant.ReservationStatus;
import com.hotel.dto.*;
import com.hotel.entity.*;
import com.hotel.repository.*;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {
	private final RoomRepository roomRepository;
	private final RoomImgService roomImgService;
	private final RoomImgRepository roomImgRepository;
	private final ReserveRepository reserveRepository;
	
	//item 테이블에 상품등록(insert)
	public Long saveRoom(RoomFormDto roomFormDto, List<MultipartFile> roomImgFileList) throws Exception {
		
		//1.상품등록
		
		RoomType room = roomFormDto.createRoom(); //dto -> entity
		roomRepository.save(room); //insert(저장)
		
		//2.이미지등록
		for(int i=0; i<roomImgFileList.size(); i++) {
			//★fk키를 사용시 부모테이블에 해당하는 entity를 먼저 넣어줘야 한다.
			RoomImg roomImg = new RoomImg();
			roomImg.setTypeId(room);
			
			//첫번째 이미지 일때 대표상품 이미지 지정
			if(i == 0) {
				roomImg.setRepimgYn("Y");
			}else {
				roomImg.setRepimgYn("N");
			}
			
			roomImgService.saveRoomImg(roomImg, roomImgFileList.get(i));
		}
		
		return room.getId(); //등록한 상품 id를 리턴
	}
	
	public Long updateRoomType(RoomFormDto roomFormDto,
			List<MultipartFile> roomImgFileList) throws Exception {
		
		//1. roomType 엔티티 가져와서 바꾼다.
		RoomType roomType = roomRepository.findById(roomFormDto.getId())
							.orElseThrow(EntityNotFoundException::new);
		//update쿼리문 실행
		/* ★★★ 한번 insert를 진행하면 엔티티가 영속성 컨텍스트에 저장이 되므로 
		그 이후에는 변경감지 기능이 동작하기 때문에 개발자는 update쿼리문을 쓰지 않고
		엔티티 데이터만 변경해주면 된다. */
		roomType.updateRoomType(roomFormDto);
		
		//2. item_img를 바꿔준다. -> 5개의 레코드 전부 변경
		List<Long> roomImgIds = roomFormDto.getRoomImgIds();//상품 이미지 아이디 리스트 조회
		
		for(int i=0; i<roomImgFileList.size();i++) {
			roomImgService.updateRoomImg(roomImgIds.get(i), roomImgFileList.get(i));
			
		}
		
		return roomType.getId(); //변경한 roomType의 id 리턴 
	}
	
	
	//객실타입 삭제
	public void deleteRoomType(Long typeId) {
		//★delete하기 전에 select 를 한번 해준다
		//->영속성 컨텍스트에 엔티티를 저장한 후 변경 감지를 하도록 하기 위해
		RoomType roomType = roomRepository.findById(typeId)
				.orElseThrow(EntityNotFoundException::new);
		
		//delete
		roomRepository.delete(roomType);
	}
	
	
	
	
	@Transactional(readOnly = true)
	public List<RoomTypeListDto> getRoomTypeList(){
		List<RoomTypeListDto> roomTypeList = roomRepository.getRoomTypeList();
		
		return roomTypeList;
	}
	
	
	//예약목록 관리 
	@Transactional(readOnly = true)
	public List<ReservationHistDto> getReservationList(){
		List<Reservation> reservations = reserveRepository.getReservations();
		
		List<ReservationHistDto> reservationHistDtos = new ArrayList<>();
		
		for(Reservation reservation : reservations) {
			
			RoomType typeId = reservation.getTypeId();
			
			Member member = reservation.getMember();
			
			
			ReservationHistDto reservationHistDto = new ReservationHistDto(reservation, typeId,member);
			RoomType roomType = reservationHistDto.getTypeId();
			
			reservationHistDtos.add(reservationHistDto);
			
		}
		
		return reservationHistDtos;
	}
	
	//예약 업데이트
	public void updateReservation(Long reservationId,String status) {
		
		Reservation reservation = reserveRepository.findById(reservationId)
				.orElseThrow(EntityNotFoundException::new);
		
        // 상태 업데이트
        ReservationStatus reservationStatus = ReservationStatus.valueOf(status);
        reservation.setRsStatus(reservationStatus);
		
		
		
	}
	
	//예약업데이트2
	public Long updateReservation2(ReserveDto reserveDto) throws Exception {
		
		//1. roomType 엔티티 가져와서 바꾼다.
		Reservation reservation = reserveRepository.findById(reserveDto.getReservationId())
							.orElseThrow(EntityNotFoundException::new);
		//update쿼리문 실행
		/* ★★★ 한번 insert를 진행하면 엔티티가 영속성 컨텍스트에 저장이 되므로 
		그 이후에는 변경감지 기능이 동작하기 때문에 개발자는 update쿼리문을 쓰지 않고
		엔티티 데이터만 변경해주면 된다. */
		reservation.updateReservation2(reserveDto);

		return reservation.getId(); //변경한 roomType의 id 리턴 
	}
	
	
	

}
