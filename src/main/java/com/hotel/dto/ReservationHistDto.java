package com.hotel.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.hotel.constant.ReservationStatus;
import com.hotel.entity.Member;
import com.hotel.entity.Reservation;
import com.hotel.entity.RoomImg;
import com.hotel.entity.RoomType;
import com.hotel.entity.WalkInCustomer;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationHistDto {
			
	private Long reservationId;
	
	private String reserveDate;
	
	private String checkIn;
	
	private String checkOut;
	
	private Long totalPrice;
	
	private Long count;
	
	private int guest;
	
	private List<ReserveDto> reserveDtoList = new ArrayList<>();
	
	private String name;
	
	private String phone;
	
	private String status;
	
	private String typeName;
	
	private String view;
	
	private RoomType typeId;
	
	private Member member;
	
	private String imgUrl;
	
	public void addReserveDto(ReserveDto reserveDto) {
		this.reserveDtoList.add(reserveDto);
	}
	
	@QueryProjection
	public ReservationHistDto(Long reservationId,LocalDateTime reserveDate,String checkIn,String checkOut
			,Long totalPrice,Long count,int guest,String typeName,String view,ReservationStatus status,String name,String phone) {
		
		this.reservationId = reservationId;
		this.reserveDate = reserveDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));;
		this.checkIn = checkIn;
		this.checkOut = checkOut;
		this.totalPrice = totalPrice;
		this.count = count;
		this.guest = guest;
		this.typeName = typeName;
		this.view = view;
		this.name = name;
		this.phone = phone;
		
		//스위치문 써서 enum 한글로쓰기
		switch (status) {
		case CANCEL: {
			
			this.status = "취소";
			break;
		}
		case CHECK_IN: {
			
			this.status = "체크인";
			break;
		}
		case CHECK_OUT: {
			
			this.status = "체크아웃";
			break;
		}
		case RESERVATION: {
			
			this.status = "예약";
			break;
		}
	case NO_SHOW: {
		
		this.status = "노쇼";
		break;
	}}
		
		
	}
	
	//entity -> Dto로 변환 (마이페이지 예약내역)
	public ReservationHistDto(Reservation reservation, RoomType roomType, Member member) {
		this.reservationId = reservation.getId();
		this.reserveDate = reservation.getReserveDate()
				.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
		this.checkIn = reservation.getCheckIn();
		this.checkOut = reservation.getCheckOut();
		this.totalPrice = reservation.getTotalPrice();
		this.guest = reservation.getGuest();
		this.typeId = roomType;
		this.member = member;
		
		//스위치문 써서 enum 한글로쓰기
		switch (reservation.getRsStatus()) {
		case CANCEL: {
			
			this.status = "취소";
			break;
		}
		case CHECK_IN: {
			
			this.status = "체크인";
			break;
		}
		case CHECK_OUT: {
			
			this.status = "체크아웃";
			break;
		}
		case RESERVATION: {
			
			this.status = "예약";
			break;
		}
	case NO_SHOW: {
		
		this.status = "노쇼";
		break;
	}}

	}
	
}
