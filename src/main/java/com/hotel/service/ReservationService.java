package com.hotel.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.hotel.dto.ReservationHistDto;
import com.hotel.dto.ReserveDto;
import com.hotel.entity.Member;
import com.hotel.entity.Reservation;
import com.hotel.entity.RoomImg;
import com.hotel.entity.RoomType;
import com.hotel.repository.MemberRepository;
import com.hotel.repository.ReserveRepository;
import com.hotel.repository.RoomImgRepository;
import com.hotel.repository.RoomRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@Configuration
@RequiredArgsConstructor
public class ReservationService {
	
	private final RoomRepository roomRepository;
	private final MemberRepository memberRepository;
	private final ReserveRepository reserveRepository;
	private final RoomImgRepository roomImgRepository;

	private final Reservation reserve;

	
	//예약하기
	public Long reserve(ReserveDto reserveDto, String email) {
		
		//1.주문할 객실을 조회
		RoomType roomType = roomRepository.findById(reserveDto.getTypeId())
										  .orElseThrow(EntityNotFoundException::new);
		//2.현재 로그인한 회원의 이메일을 이용해 회원정보를 조회
		Member member = memberRepository.findByEmail(email);
		
		//3.주문할 상품 엔티티와 주문 수량을 이용하여 주문 상품 엔티티를 생성

		Reservation reservation = reserve.createReserve(member,roomType, reserveDto);
		
		reserveRepository.save(reservation);
		
		return reservation.getId();
		
	}
	
	
	//마이페이지 예약목록
	@Transactional(readOnly = true)
	public List<ReservationHistDto> getReservationList(String email) {
		//1. 유저 아이디와 페이징 조건을 이용하여 주문 목록을 조회
		List<Reservation> reservations = reserveRepository.findReservations(email);
		
		List<ReservationHistDto> reservationHistDtos = new ArrayList<>();
		
		//3. 주문 리스트를 순회하면서 구매 이력 페이지에 전달할 DTO(OrderHistDto)를 생성
		for (Reservation reservation : reservations) {
			
			RoomType typeId = reservation.getTypeId();


			ReservationHistDto reservationHistDto = new ReservationHistDto(reservation, typeId);
			RoomType roomType = reservationHistDto.getTypeId();
			
			for (Reservation reserve : reservations) {
				//상품의 대표 이미지
				//RoomImg roomImg = roomImgRepository.findByTypeIdAndRepimgYn(reserve.getTypeId().getId(), "Y");
				
				
				
				
				RoomImg roomimg = roomImgRepository.findByTypeIdAndRepimgYn(roomType.getId(), "Y");
				
				reservationHistDto.setImgUrl(roomimg.getImgUrl());
				ReserveDto reserveDto1 = new ReserveDto(reserve, roomimg.getImgUrl());
				
				reservationHistDto.addReserveDto(reserveDto1);
				
				
			}
			reservationHistDtos.add(reservationHistDto);

		}

		return reservationHistDtos; //4.페이지 구현 객체를 생성하여 return
	}

	//본인확인(현재 로그인한 사용자와 주문데이터를 생성한 사용자가 같은지 검사)
	@Transactional(readOnly = true)
	public boolean validateReserve(Long reservationId, String email) {
		Member curMember = memberRepository.findByEmail(email);//로그인한 사용자 찾기
		Reservation reservation = reserveRepository.findById(reservationId)
				.orElseThrow(EntityNotFoundException::new);  //주문내역 
		
		Member savedMember = reservation.getMember(); //주문한 사용자 찾기
		
		//로그인한 사용자의 이메일과 주문한 사용자의 이메일이 같은지 최종 비교
		if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
			//같지 않으면
			return false;
		}
		
		return true;	
	}
	
	//예약취소
	public void cancelReserve(Long reservationId) {
		Reservation reservation = reserveRepository.findById(reservationId)
													.orElseThrow(EntityNotFoundException::new);
		
		//OrderStatus를 update -> entity 의 필드 값을 바꿔주면 된다.
		reservation.cancelReserve();
		
		reserveRepository.delete(reservation);
	}
	
	
	
	
//	
//	//예약삭제
//	public void deleteReserve(Long reservationId) {
//		//★delete하기 전에 select 를 한번 해준다
//		//->영속성 컨텍스트에 엔티티를 저장한 후 변경 감지를 하도록 하기 위해
//		Reservation reservation = reserveRepository.findById(reservationId)
//				.orElseThrow(EntityNotFoundException::new);
//		
//		//delete
//		reserveRepository.delete(reservation);
//	}
//	
	
	
}
