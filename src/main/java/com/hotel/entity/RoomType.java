package com.hotel.entity;


import com.hotel.constant.Availability;
import com.hotel.dto.RoomFormDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "room_type")
@Setter
@Getter
@ToString
public class RoomType extends BaseEntity {

	@Id
	@Column(name="type_id") //테이블로 생성될때 컬럼이름을 지정해준다
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String typeName; // 객실타입명

	private String configuration; // 객실구성

	private String bedType; // 침대타입

	private String roomSize; // 객실사이즈

	private String view; // 전망

	@Lob // clob과 같은 큰타입의 문자타입으로 컬럼을 만든다
	@Column(nullable = false, columnDefinition = "longtext")
	private String amenities; // 어메니티이미지


	private int price; // 가격

	private int capacity; // 인원
	
	private String comment;  

	@Lob // clob과 같은 큰타입의 문자타입으로 컬럼을 만든다
	@Column(nullable = false, columnDefinition = "longtext")
	private String roomDetail;
	
	@Enumerated(EnumType.STRING)
	private Availability availability;
	
	


	
	//roomType 엔티티 수정
	public void updateRoomType(RoomFormDto roomFormDto) {
		this.typeName = roomFormDto.getTypeName();
		this.configuration = roomFormDto.getConfiguration();
		this.bedType = roomFormDto.getBedType();
		this.roomSize = roomFormDto.getRoomSize();
		this.view = roomFormDto.getView();
		this.amenities = roomFormDto.getAmenities();
		this.price = roomFormDto.getPrice();
		this.capacity = roomFormDto.getCapacity();
		this.comment = roomFormDto.getComment();
		this.roomDetail = roomFormDto.getRoomDetail();
		
		
	}

	
	
	
	
	

}
