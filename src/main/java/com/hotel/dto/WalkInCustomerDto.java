package com.hotel.dto;

import java.time.LocalDateTime;

import com.hotel.constant.ReservationStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalkInCustomerDto {

	//현장고객정보
	
	@NotBlank(message =  "이름은 필수입력 값입니다.")
	private String name;
	
	@NotEmpty(message = "주소는 필수입력 값입니다.")
	private String address;
	
	@NotEmpty(message = "휴대폰 번호는 필수입력 값입니다.")
	private String phone;
	
	@NotEmpty(message = "생년월일은 필수입력 값입니다.")
	private String birth;
	
	
	//예약
	private Long typeId;
	
	private String typeName;
	
	private String imgUrl;
	
	private String checkIn;
	
	private String checkOut;
	
	private int guest;
	
	private LocalDateTime ReserveDate;
	
	private Long totalPrice;
	
	private Long count;
	
	private Long reservationId;
	
	private ReservationStatus rsStatus;
	
	
}
