package com.hotel.dto;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.hotel.constant.ReservationStatus;
import com.hotel.entity.Reservation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationHistDto {
	
	//entity -> Dto로 변환
	public ReservationHistDto(Reservation reservation) {
		this.reservationId = reservation.getId();
		this.reserveDate = reservation.getReserveDate()
				.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		this.reservationStatus = reservation.getRsStatus();
		this.checkIn = reservation.getCheckIn();
		this.checkOut = reservation.getCheckOut();
		this.totalPrice = reservation.getTotalPrice();
		this.guest = reservation.getGuest();

	}
	
	
	private Long reservationId;
	
	private String reserveDate;
	
	private String checkIn;
	
	private String checkOut;
	
	private Long totalPrice;
	
	private int guest;

	
	private ReservationStatus reservationStatus;
	
	private List<ReserveDto> reserveDtoList = new ArrayList<>();
	
	//orderItemDto객체를 주문 상품 리스트에 추가하는 메소드
	public void addReserveDto(ReserveDto reserveDto) {
		this.reserveDtoList.add(reserveDto);
	}
	
}
