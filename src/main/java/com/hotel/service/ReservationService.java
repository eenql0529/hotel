package com.hotel.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotel.dto.ReserveDto;
import com.hotel.entity.Member;
import com.hotel.entity.Reservation;
import com.hotel.entity.RoomType;
import com.hotel.repository.MemberRepository;
import com.hotel.repository.ReserveRepository;
import com.hotel.repository.RoomRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {
	
	private final RoomRepository roomRepository;
	private final MemberRepository memberRepository;
	private final ReserveRepository reserveRepository;
	
	
	//예약하기
	public Long reserve(ReserveDto reserveDto, String email) {
		
		//1.주문할 객실을 조회
		RoomType roomType = roomRepository.findById(reserveDto.getTypeId())
										  .orElseThrow(EntityNotFoundException::new);
		//2.현재 로그인한 회원의 이메일을 이용해 회원정보를 조회
		Member member = memberRepository.findByEmail(email);
		
		//3.주문할 상품 엔티티와 주문 수량을 이용하여 주문 상품 엔티티를 생성

		Reservation reservation = Reservation.createReserve(member,roomType, reserveDto.getCount());
		
		reserveRepository.save(reservation);
		
		return reservation.getId();
		
	}

}
