package com.hotel.entity;


import java.time.LocalDateTime;

import com.hotel.constant.ReservationStatus;

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
public class Reservation {

	@Id
	@Column(name="reservation_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private LocalDateTime checkInDate;
	
	private LocalDateTime checkOutDate;
	
	private LocalDateTime ReserveDate;
	
	private int count;
	
	private int totalPrice;
	
	
	
	@Enumerated(EnumType.STRING)
	private ReservationStatus rsStatus;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="member_id")
	private Member member;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="type_id")
	private RoomType typeId;
	
	

	
	
	public static Reservation createReserve(Member member,RoomType roomType, int count) {
		Reservation reservation = new Reservation();
		reservation.setMember(member);
		reservation.setTypeId(roomType);
		reservation.setCount(count);
		reservation.setTotalPrice(roomType.getPrice());
		reservation.setRsStatus(ReservationStatus.RESERVATION);
		reservation.setReserveDate(LocalDateTime.now());

		roomType.removeStock();
		

		
		
		return reservation;
	}
	

	
	
	public int getTotalPrice(RoomType roomType) {
		return totalPrice = roomType.getPrice() * count;
	}
	
	//재고를 원래대로
	public void cancel() {
		this.getTypeId().addStock();
	}
	
	
}
