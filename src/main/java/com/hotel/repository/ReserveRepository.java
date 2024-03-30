package com.hotel.repository;

import java.time.LocalDate;
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

public interface ReserveRepository extends JpaRepository<Reservation, Long>, ReserveRepositoryCustom{
	

	
	//현재 로그인한 사용자의 주문 데이터를 페이징 조건에 맞춰서 조회
	@Query("select o from Reservation o where o.member.email = :email order by o.reserveDate desc")
	List<Reservation> findReservations(@Param("email") String email);

	
	//현재 로그인한 회원의 주문 개수가 몇개인지 조회(total)
	
	@Query("select count(o) from Reservation o where o.member.email = :email")
	Long countReservation(@Param("email") String email);
	
	
	@Query("select o from Reservation o order by o.reserveDate desc")
	List<Reservation> getReservations();
	
	//예상매출액
	@Query("select IFNULL(sum(r.totalPrice),0) from Reservation r WHERE DATE(r.checkOut) = STR_TO_DATE(:selectedDate, '%Y년 %m월 %d일')")
	Long sumTotalPrice(@Param("selectedDate")String selectedDate);
	
    @Query("SELECT COUNT(*) FROM Reservation r WHERE DATE(r.checkIn) = STR_TO_DATE(:selectedDate, '%Y년 %m월 %d일')")
    int countCheckInToday(@Param("selectedDate") String selectedDate);
	
	//오늘 체크인한 수
	@Query("SELECT COUNT(r) FROM Reservation r WHERE DATE(r.checkIn) = STR_TO_DATE(:selectedDate, '%Y년 %m월 %d일') AND r.rsStatus = 'CHECK_IN'")
	int countRsStatusCheckInToday (@Param("selectedDate") String selectedDate);
	
	//오늘 체크아웃해야하는 수
	@Query("select count(r) from Reservation r where date(r.checkOut) = STR_TO_DATE(:selectedDate, '%Y년 %m월 %d일')")
	int countCheckOutToday(@Param("selectedDate") String selectedDate);
	
	//오늘 체크아웃한 수
	@Query("SELECT COUNT(r) FROM Reservation r WHERE DATE(r.checkOut) = STR_TO_DATE(:selectedDate, '%Y년 %m월 %d일') AND r.rsStatus = 'CHECK_OUT'")
	int countRsStatusCheckOutToday (@Param("selectedDate") String selectedDate);
	
	//오늘 들어온 예약
	@Query("select count(r) from Reservation r where date(r.reserveDate) = STR_TO_DATE(:selectedDate, '%Y년 %m월 %d일') and r.rsStatus = 'RESERVATION'")
	int countReservationToday(@Param("selectedDate") String selectedDate);
	//오늘 취소된 예약
	@Query("select count(r) from Reservation r where date(r.updateTime) = STR_TO_DATE(:selectedDate, '%Y년 %m월 %d일') and r.rsStatus = 'CANCEL'")
	int countCanceledReservationToday(@Param("selectedDate") String selectedDate);
	
    @Query(value = "SELECT ROUND(IFNULL((COUNT(distinct res.reservation_id) / (COUNT(distinct inv.type_id) * 3)) * 100, 0)) AS occupancy_rate " +
            "FROM reservation res " +
            "JOIN inventory inv ON res.check_in = inv.date " +
            "WHERE DATE(inv.date) = STR_TO_DATE(:selectedDate, '%Y년 %m월 %d일')", nativeQuery = true)
    Double calculateOccupancyRate(@Param("selectedDate") String selectedDate);
    
    @Query(value ="select rt.type_name, IFNULL((COUNT(rs.reservation_id)), 0), ROUND(IFNULL((COUNT(rs.reservation_id) / 3 * 100), 0)) as occupancy_rate from room_type rt LEFT JOIN reservation rs on rt.type_id = rs.type_id AND date(rs.check_in) <= STR_TO_DATE(:selectedDate, '%Y년 %m월 %d일') AND date(rs.check_out) > STR_TO_DATE(:selectedDate, '%Y년 %m월 %d일') GROUP BY rt.type_name", nativeQuery = true)
    List<Object[]> calculateRate(@Param("selectedDate") String selectedDate);
    
	//호텔이용횟수
	@Query(value = "select count(*) from reservation where member_id = :memberId", nativeQuery = true)
	int countHotelServiceMember(@Param("memberId") Long memberId);
	
	//호텔이용횟수
	@Query(value = "select count(*) from reservation where walk_in_customer_id = :customerId", nativeQuery = true)
	int countHotelServiceCustomer(@Param("customerId") Long customerId);
	
	
	//예약상태 업데이트
	@Query(value = "select * from reservation where check_in = CURDATE() AND HOUR(CURRENT_TIMESTAMP()) >= 14",nativeQuery = true)
	List<Reservation> updateCheckIn();
	
	@Query(value = "select * from reservation where check_out = CURDATE() AND HOUR(CURRENT_TIMESTAMP()) >= 12",nativeQuery = true)
	List<Reservation> updateCheckOut();
}
