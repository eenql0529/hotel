package com.hotel.entity;


import com.hotel.constant.Availability;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity //엔티티 클래스로 정의
@Table(name="room") //테이블 이름 지정
@Setter
@Getter
@ToString
public class Room {
	@Id
	@Column(name="room_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	
	@Column(nullable = false)
	private int roomNum;
	
	@Enumerated(EnumType.STRING)
	private Availability availability;
	

	
	
	
	
	
}
