package com.hotel.entity;


import com.hotel.constant.ContactStatus;
import com.hotel.dto.ContactDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity //엔티티 클래스로 정의
@Table(name="contact") //테이블 이름 지정
@Setter
@Getter
@ToString
public class Contact extends BaseEntity{
	
	@Id
	@Column(name="contact_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	private String content;
	
	@Enumerated(EnumType.STRING)
	private ContactStatus contactStatus; //답변상태
	
	
	public Contact createContact(ContactDto contactDto) {
		
		Contact contact = new Contact();
		contact.setName(contactDto.getName());
		contact.setEmail(contactDto.getEmail());
		contact.setTitle(contactDto.getTitle());
		contact.setContent(contactDto.getContent());
		contact.setContactStatus(ContactStatus.pending);
		
		return contact;
	}
	
	
}
