package com.hotel.entity;

import com.hotel.dto.WalkInCustomerDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="walk_in_customer")
@Setter
@Getter
@ToString
public class WalkInCustomer extends BaseEntity {
	@Id
	@Column(name="walk_in_customer_id") //테이블로 생성될때 컬럼이름을 지정해준다
	@GeneratedValue(strategy = GenerationType.IDENTITY) //기본키를 자동으로 생성해주는 전략 사용(시퀀스랑 비슷)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String birth;
	
	@Column(nullable = false)
	private String phone;
	
	@Column(nullable = true)
	private String address;
	
	
	public static WalkInCustomer createWalkInCustomer(WalkInCustomerDto walkInCustomerDto) {
		
		WalkInCustomer customer = new WalkInCustomer();
		customer.setName(walkInCustomerDto.getName());
		customer.setBirth(walkInCustomerDto.getBirth());
		customer.setPhone(walkInCustomerDto.getPhone());
		customer.setAddress(walkInCustomerDto.getAddress());
		
		return customer;
	}
	

}
