package com.hotel.service;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.hotel.Exception.OutOfStockException;
import com.hotel.constant.ReservationStatus;
import com.hotel.dto.ReservationHistDto;
import com.hotel.dto.ReserveDto;
import com.hotel.entity.Inventory;
import com.hotel.entity.Member;
import com.hotel.entity.Reservation;
import com.hotel.entity.RoomImg;
import com.hotel.entity.RoomType;
import com.hotel.repository.InventoryRepository;
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
	private final InventoryRepository inventoryRepository;

	private final Reservation reserve;

	
	//예약하기
	@Transactional
	public Long reserve(ReserveDto reserveDto, String email) {
		
		//1.주문할 객실을 조회
		RoomType roomType = roomRepository.findById(reserveDto.getTypeId())
										  .orElseThrow(EntityNotFoundException::new);
		//2.현재 로그인한 회원의 이메일을 이용해 회원정보를 조회
		Member member = memberRepository.findByEmail(email);
		

		LocalDate startDate = LocalDate.parse(reserveDto.getCheckIn());
        LocalDate endDate = LocalDate.parse(reserveDto.getCheckOut());

        long daysBetween = endDate.toEpochDay() - startDate.toEpochDay(); //두 날짜 차이 구하기.
        
        reserveDto.setCount(daysBetween);
        
        // 여기서 트랜잭션 시작
        Reservation reservation = null;
        try {
            reservation = reserve.createReserve(member, roomType, reserveDto);
            reserveRepository.save(reservation);

            startDate = LocalDate.parse(reserveDto.getCheckIn());
            endDate = LocalDate.parse(reserveDto.getCheckOut());

            while (!startDate.isAfter(endDate.minusDays(1))) {
                Inventory inventory = inventoryRepository.findByDateAndRoomType(startDate.toString(), roomType);
                if (inventory != null && inventory.getStock() > 0) {
                    inventory.setStock(inventory.getStock() - 1); // 재고 감소
                    inventoryRepository.save(inventory);
                } else if(inventory == null) {
                	inventory = new Inventory(); // 새로운 Inventory 객체 생성
                	inventory.setDate(startDate.toString());
                	inventory.setRoomType(roomType);
                    inventory.setStock(inventory.getStock() - 1); // 재고 감소
                    inventoryRepository.save(inventory);
                }
                else {
                    throw new OutOfStockException("재고가 부족합니다.");
                }

                startDate = startDate.plusDays(1);
            }

            // 예약 및 재고 업데이트가 모두 성공하면 커밋됨
        } catch (Exception e) {
            // 예약 또는 재고 업데이트 중 하나라도 실패하면 롤백
            if (reservation != null) {
                reserveRepository.delete(reservation);
            }
            throw e; // 예외 다시 던짐
        }

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
			Member member = reservation.getMember();

			ReservationHistDto reservationHistDto = new ReservationHistDto(reservation, typeId,member);
			
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
	
	
	
	
	
	
	//예약삭제
	public void deleteReserve(Long reservationId) {
		//★delete하기 전에 select 를 한번 해준다
		//->영속성 컨텍스트에 엔티티를 저장한 후 변경 감지를 하도록 하기 위해
		Reservation reservation = reserveRepository.findById(reservationId)
				.orElseThrow(EntityNotFoundException::new);
	    RoomType roomType = reservation.getTypeId();
		//delete
		reserveRepository.delete(reservation);
		
		 // 3. 예약 기간 동안의 날짜 목록 생성
	    LocalDate startDate = LocalDate.parse(reservation.getCheckIn());
	    LocalDate endDate = LocalDate.parse(reservation.getCheckOut());
	    List<String> dateList = new ArrayList<>();
	    while (!startDate.isAfter(endDate)) {
	        dateList.add(startDate.toString());
	        startDate = startDate.plusDays(1);
	    }

	    // 4. 예약 취소에 따른 재고 복원
	    try {
	        for (String date : dateList) {
	            Inventory inventory = inventoryRepository.findByDateAndRoomType(date, roomType);
	            if (inventory != null) {
	                inventory.setStock(inventory.getStock() + 1); // 재고 증가
	                inventoryRepository.save(inventory);
	            }
	            // 만약 해당 날짜의 Inventory가 없다면 새로 생성하지 않음
	        }

	        // 5. 예약 삭제
	        reserveRepository.delete(reservation);
	    } catch (Exception e) {
	        // 복원 중 오류 발생 시 롤백 처리 등 예외 처리 필요
	        throw e;
	    }
		
	}
	
	//예약상태변경
	@Transactional
	@Scheduled(cron = "0 0 * * * *") // 매 시간 0분에 실행
	public void updateReservationStatus() {
		List<Reservation> checkInReservations = reserveRepository.updateCheckIn();
		List<Reservation> checkOutReservations = reserveRepository.updateCheckOut();
		
		for(Reservation reservation : checkInReservations) {
			
			reservation.setRsStatus(ReservationStatus.CHECK_IN);
		}
		for(Reservation reservation  : checkOutReservations) {
			reservation.setRsStatus(ReservationStatus.CHECK_OUT);
		}
		
	}
	
	
	
}
