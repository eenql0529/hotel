package com.hotel.dto;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.mapping.Array;
import org.modelmapper.ModelMapper;

import com.hotel.constant.Availability;
import com.hotel.entity.RoomImg;
import com.hotel.entity.RoomType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomFormDto {

	private Long id;
	
	@NotBlank(message = "객실타입은 필수 입력입니다.")
	private String typeName;
	

	@NotBlank(message = "객실구성은 필수 입력입니다.")
	private String configuration;
	@NotBlank(message = "객실침대는 필수 입력입니다.")
	private String bedType;
	@NotBlank(message = "객실크기는 필수 입력입니다.")
	private String roomSize;
	@NotBlank(message = "객실전망은 필수 입력입니다.")
	private String view;
	
	private String amenities;
	
	
	private int price;

	private int capacity;
	
	private String comment;

	@NotBlank(message = "객실설명은 필수 입력입니다.")
	private String roomDetail;
	
	private Availability availability;
	
	
	private RoomImg imgId;
	
	private List<RoomImgDto> roomImgDtoList = new ArrayList<>();
	
	private List<Long> roomImgIds = new ArrayList<>();
	
	
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public RoomType createRoom() {
		
		
		return modelMapper.map(this, RoomType.class);
	}
	
	public static RoomFormDto of(RoomType roomType) {
		
		return modelMapper.map(roomType, RoomFormDto.class);
	}
	

}
