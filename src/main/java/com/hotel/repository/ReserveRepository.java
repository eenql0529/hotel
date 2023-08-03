package com.hotel.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotel.dto.ReserveDto;
import com.hotel.entity.QReservation;
import com.hotel.entity.QRoomImg;
import com.hotel.entity.QRoomType;
import com.hotel.entity.Reservation;
import com.querydsl.jpa.impl.JPAQueryFactory;

public interface ReserveRepository extends JpaRepository<Reservation, Long>{
	

	
	//현재 로그인한 사용자의 주문 데이터를 페이징 조건에 맞춰서 조회
	@Query("select o from Reservation o where o.member.email = :email order by o.reserveDate desc")
	List<Reservation> findReservations(@Param("email") String email);

	
	//현재 로그인한 회원의 주문 개수가 몇개인지 조회(total)
	
	@Query("select count(o) from Reservation o where o.member.email = :email")
	Long countReservation(@Param("email") String email);
	
	
	@Query("select o from Reservation o order by o.reserveDate desc")
	List<Reservation> getReservations();
	


			
	
}
