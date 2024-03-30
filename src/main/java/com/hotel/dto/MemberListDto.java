package com.hotel.dto;

import com.hotel.entity.Member;
import com.hotel.entity.WalkInCustomer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberListDto {
	
	private Long memberId;
	private String name;
	private String phone;
	private String email;
	private String address;
	private String birth;
	private int count; //이용횟수
	

}
