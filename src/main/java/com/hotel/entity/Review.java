package com.hotel.entity;

import java.time.LocalDateTime;

import com.hotel.constant.ReservationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name="review") //테이블 이름 지정
@Setter
@Getter
@ToString
public class Review {
	@Id
	@Column(name="review_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private int rating;
	
	private String reviewText;
	
	private LocalDateTime reviewDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="member_id")
	private Member member;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="room_id")
	private Room room;
}
