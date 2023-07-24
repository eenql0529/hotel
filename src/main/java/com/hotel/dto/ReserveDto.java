package com.hotel.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReserveDto {
	
	@NotNull(message = "객실타입 아이디 필수")
	private Long typeId;
	
	private int count;
	

	
	
}
