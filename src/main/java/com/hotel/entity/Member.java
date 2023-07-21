package com.hotel.entity;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.hotel.constant.Role;
import com.hotel.dto.MemberFormDto;

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
@Table(name="member") //테이블 이름 지정
@Setter
@Getter
@ToString
public class Member {
	
	@Id
	@Column(name="member_id") //테이블로 생성될때 컬럼이름을 지정해준다
	@GeneratedValue(strategy = GenerationType.AUTO) //기본키를 자동으로 생성해주는 전략 사용(시퀀스랑 비슷)
	private Long id;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String phone;
	
	@Column(nullable = true)
	private String address;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder ) {
		String password = passwordEncoder.encode(memberFormDto.getPassword());
		
		Member member = new Member();
		member.setName(memberFormDto.getName());
		member.setEmail(memberFormDto.getEmail());
		member.setAddress(memberFormDto.getAddress());
		member.setPassword(password);
		member.setPhone(memberFormDto.getPhone());
		member.setRole(Role.ADMIN);
		
		return member;
		
		
	}
}
