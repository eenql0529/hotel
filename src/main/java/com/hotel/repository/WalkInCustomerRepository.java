package com.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.entity.WalkInCustomer;

public interface WalkInCustomerRepository extends JpaRepository<WalkInCustomer, Long> {

	WalkInCustomer findByNameAndBirth(String name,String birth);
	
}
