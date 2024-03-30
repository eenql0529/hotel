package com.hotel.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import com.hotel.dto.CustomerListDto;
import com.hotel.dto.SearchDto;
import com.hotel.entity.Member;

public interface MemberRepository extends JpaRepository<Member,Long>,
										  MemberRepositoryCustom{
	//select * from member where email = ?
	Member findByEmail(String email);
	
	
	//고객리스트
	@Query("select o from Member o order by o.id desc")
	List<Member> getMemberList();
	
	
	//홈페이지 회원 + 현장 고객 리스트 //where절에 컬럼명은 동적으로 지정불가능 !
//	@Query(value=
//		    "SELECT 'walk_in_customer' AS customerType, walk_in_customer_id AS customerId, name, phone, birth, address, null AS email, reg_time " +
//		    "FROM walk_in_customer " +
//		    "WHERE " +
//		    "CASE " +
//		    "   WHEN :searchType = 'name' THEN name LIKE CONCAT('%', :keyword , '%') " +
//		    "   WHEN :searchType = 'birth' THEN birth LIKE CONCAT('%', :keyword , '%') " +
//		    "   WHEN :searchType = 'phone' THEN phone LIKE CONCAT('%', :keyword , '%') " +
//		    "END " +
//		    "UNION ALL " +
//		    "SELECT 'member' AS customerType, member_id AS customerId, name, phone, birth, address, email, reg_time " +
//		    "FROM member " +
//		    "WHERE " +
//		    "CASE " +
//		    "   WHEN :searchType = 'name' THEN name LIKE CONCAT('%', :keyword , '%') " +
//		    "   WHEN :searchType = 'birth' THEN birth LIKE CONCAT('%', :keyword , '%') " +
//		    "   WHEN :searchType = 'phone' THEN phone LIKE CONCAT('%', :keyword , '%') " +
//		    "   WHEN :searchType = 'email' THEN email LIKE CONCAT('%', :keyword , '%') " +
//		    "END " +
//		    "ORDER BY reg_time DESC", 
//		    nativeQuery = true)
//	List<Object[]> getSearchCustomerList(@Param("searchType") String searchType, @Param("keyword") String keyword);
//
//	default List<CustomerListDto> getCustomerListDto(String searchType, String keyword) {
//		
//	    List<Object[]> results = getSearchCustomerList(searchType, keyword);
//	    List<CustomerListDto> dtos = new ArrayList<>();
//	    for (Object[] result : results) {
//	        CustomerListDto dto = new CustomerListDto();
//            dto.setCustomerType((String) result[0]);
//            dto.setCustomerId((Long) result[1]);
//            dto.setName((String) result[2]);
//            dto.setPhone((String) result[3]);
//            dto.setBirth((String) result[4]);
//            dto.setAddress((String) result[5]);
//            dto.setEmail((String) result[6]);
//            
//            // Timestamp를 LocalDateTime으로 변환
//            Timestamp regTimeTimestamp = (Timestamp) result[7];
//            LocalDateTime regTime = regTimeTimestamp.toLocalDateTime();
//            dto.setRegTime(regTime);
//            
//            dtos.add(dto);
//	    }
//	    return dtos;
//	}
	
	
    @Query(value="SELECT 'walk_in_customer' AS customerType, walk_in_customer_id AS customerId, name, phone, birth, address, null AS email, reg_time FROM walk_in_customer  UNION ALL SELECT 'member' AS customerType, member_id AS customerId, name, phone, birth, address, email, reg_time FROM member order by reg_time desc ", nativeQuery = true)
    List<Object[]> getCustomerList();

    default List<CustomerListDto> getCustomerListDto() {
        List<Object[]> results = getCustomerList();
        List<CustomerListDto> dtos = new ArrayList<>();
        for (Object[] result : results) {
            CustomerListDto dto = new CustomerListDto();
            dto.setCustomerType((String) result[0]);
            dto.setCustomerId((Long) result[1]);
            dto.setName((String) result[2]);
            dto.setPhone((String) result[3]);
            dto.setBirth((String) result[4]);
            dto.setAddress((String) result[5]);
            dto.setEmail((String) result[6]);
            
            // Timestamp를 LocalDateTime으로 변환
            Timestamp regTimeTimestamp = (Timestamp) result[7];

            
            dtos.add(dto);
        }
        return dtos;
    }


}
