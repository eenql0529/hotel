package com.hotel.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.hotel.entity.Member;
import com.hotel.entity.WalkInCustomer;
import com.querydsl.core.annotations.QueryProjection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerListDto {
    private String customerType;
    private Long customerId;  //primary key
    private int id;
    private String name;
    private String phone;
    private String birth;
    private String address;
    private String email;
    private int count; //이용횟수
    private String regTime;
    

    @QueryProjection //쿼리dsl로 결과 조회 할때 MainItemDto 객체로 바로 받아올 수 있다.
    public CustomerListDto(String customerType, Long customerId, String name,
    		String phone,String birth,String address,String email,LocalDateTime regTime) {
    	
    	this.customerType = customerType;
    	this.customerId = customerId;
    	this.name = name;
    	this.phone = phone;
    	this.birth = birth;
    	this.address = address;
    	this.email = email;
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	this.regTime = regTime.format(formatter);;
        // DateTimeFormatter를 사용하여 원하는 형식으로 변환
    }


	public CustomerListDto() {
		// TODO Auto-generated constructor stub
	}
    

    
}
