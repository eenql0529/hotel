package com.hotel.dto;

import com.hotel.constant.Availability;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomTypeListDto {
	private Long id;
	
	private String typeName;
	
	private String view;
	
	private String bedType;
	
	private String roomSize;
	
	private String imgUrl;
	
	private String comment;
	
	private int capacity;
	
	private int price;
	
	private Availability availability;
	
	
	@QueryProjection //쿼리dsl로 결과 조회 할때 MainItemDto 객체로 바로 받아올 수 있다.	
	public RoomTypeListDto(Long id, String typeName, String view, String bedType, String roomSize , String imgUrl, String comment,int capacity,int price,Availability availability) {
		
		this.id = id;
		this.typeName = typeName;
		this.view = view;
		this.bedType = bedType;
		this.roomSize = roomSize;
		this.imgUrl = imgUrl;
		this.comment = comment;
		this.capacity = capacity;
		this.price = price;
		this.availability = availability;
	}
	

}
