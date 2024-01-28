package com.hotel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hotel.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {

	@Modifying
	@Query(value = "select * from contact order by contact_id desc limit 10" , nativeQuery = true)
	List<Contact> getContactList();
	
	
}
