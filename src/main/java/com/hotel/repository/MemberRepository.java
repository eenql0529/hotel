package com.hotel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hotel.entity.Member;

public interface MemberRepository extends JpaRepository<Member,Long> {
	//select * from member where email = ?
	Member findByEmail(String email);
	
	
	//고객리스트
	@Query("select o from Member o order by o.id desc")
	List<Member> getMemberList();
	

}
