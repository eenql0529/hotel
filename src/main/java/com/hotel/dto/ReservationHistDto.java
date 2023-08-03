package com.hotel.dto;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.hotel.constant.ReservationStatus;
import com.hotel.entity.Member;
import com.hotel.entity.Reservation;
import com.hotel.entity.RoomImg;
import com.hotel.entity.RoomType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationHistDto {
	
	//entity -> Dto로 변환
	public ReservationHistDto(Reservation reservation, RoomType roomType, Member member) {
		this.reservationId = reservation.getId();
		this.reserveDate = reservation.getReserveDate()
				.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
		this.reservationStatus = reservation.getRsStatus();
		this.checkIn = reservation.getCheckIn();
		this.checkOut = reservation.getCheckOut();
		this.totalPrice = reservation.getTotalPrice();
		this.guest = reservation.getGuest();
		this.typeId = roomType;
		this.member = member;
		this.reservationStatus= reservation.getRsStatus();
		
		//스위치문 써서 enum 한글로쓰기
		switch (reservation.getRsStatus()) {
		case WAITING: {
			
			this.status = "대기";
			break;
		}
		case CONFIRMED: {
			
			this.status = "확정";
			break;
		}
		case CANCELED: {
			
			this.status = "취소";
			break;
		}
		case DELETED: {
			
			this.status = "삭제";
			break;
		}}

	}

		
	private Long reservationId;
	
	private String reserveDate;
	
	private String checkIn;
	
	private String checkOut;
	
	private Long totalPrice;
	
	private int guest;
	
	private RoomType typeId;
	
	private ReservationStatus reservationStatus;
	
	private List<ReserveDto> reserveDtoList = new ArrayList<>();
	
	private String imgUrl;
	
	private Member member;
	
	private String name;
	
	private String status;
	
	
	//orderItemDto객체를 주문 상품 리스트에 추가하는 메소드
	public void addReserveDto(ReserveDto reserveDto) {
		this.reserveDtoList.add(reserveDto);
	}
	
}
