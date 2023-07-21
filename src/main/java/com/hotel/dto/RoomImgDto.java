package com.hotel.dto;

import org.modelmapper.ModelMapper;

import com.hotel.entity.RoomImg;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoomImgDto {
	private Long id;
	
	private String imgName;
	
	private String oriImgName;
	
	private String imgUrl;
	
	private String repimgYn;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	//entity -> dto로 변환
	public static RoomImgDto of(RoomImg roomImg) {
		return modelMapper.map(roomImg, RoomImgDto.class);
	}
	
	
	
}
