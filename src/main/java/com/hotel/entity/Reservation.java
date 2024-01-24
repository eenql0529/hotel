package com.hotel.entity;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.context.annotation.Configuration;

import com.hotel.constant.ReservationStatus;
import com.hotel.dto.ReserveDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity //엔티티 클래스로 정의
@Table(name="reservation") //테이블 이름 지정
@Setter
@Getter
@ToString
@Configuration
public class Reservation extends BaseEntity {

	@Id
	@Column(name="reservation_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String checkIn;
	
	private String checkOut;
	
	private LocalDateTime reserveDate;
	
	private Long count;
	
	private Long totalPrice;
	
	private int guest;
	
	
	@Enumerated(EnumType.STRING)
	private ReservationStatus rsStatus;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="member_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Member member;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="type_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private RoomType typeId;


	
	
	public Reservation createReserve(Member member,RoomType roomType, ReserveDto reserveDto) {
		Reservation reservation = new Reservation();
		reservation.setMember(member);
		reservation.setCheckIn(reserveDto.getCheckIn());
		reservation.setCheckOut(reserveDto.getCheckOut());
		reservation.setTypeId(roomType);
		reservation.setCount(reserveDto.getCount());
		reservation.setGuest(reserveDto.getGuest());
		reservation.setTotalPrice(roomType.getPrice()*reserveDto.getCount());
		reservation.setRsStatus(ReservationStatus.RESERVATION);
		reservation.setReserveDate(LocalDateTime.now());

		

		
		return reservation;
	}
	

	

	

	
	
	//예약 취소
	public void cancelReserve() {
		this.rsStatus = ReservationStatus.CANCEL;
		
	}
	
//	//예약 업데이트
//	public void updateReservation(ReservationStatus rsStatus) {
//		this.rsStatus = rsStatus;
//		if(rsStatus == rsStatus.DELETED) {
//
//		}
//	}
//	//예약 업데이트
//	public void updateReservation2(ReserveDto reserveDto) {
//		this.rsStatus = reserveDto.getRsStatus();
//		if(rsStatus == rsStatus.DELETED) {
//
//		}
//	}
	
}
