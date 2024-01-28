package com.hotel.dto;

import com.hotel.entity.Member;
import com.hotel.repository.ReserveRepository;

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
	private int count; //이용횟수
	
	public MemberListDto(Member member) {
		
		this.memberId = member.getId();
		this.name = member.getName();
		this.phone = member.getPhone();
		this.email = member.getEmail();
		this.address = member.getAddress();
		
	}
}
