package com.hotel.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.hotel.dto.CustomerListDto;
import com.hotel.dto.QCustomerListDto;
import com.hotel.dto.SearchDto;
import com.hotel.entity.QMember;
import com.hotel.entity.QWalkInCustomer;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.JPAQueryBase;

import jakarta.persistence.EntityManager;

public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{

	private JPAQueryFactory queryFactory;
	
	public MemberRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	
	private BooleanExpression searchTypeCondition(String searchType, String keyword) {
		
		
	    if (!StringUtils.isEmpty(searchType) && !StringUtils.isEmpty(keyword)) {
	        if (StringUtils.equals("name", searchType)) {
	            return QWalkInCustomer.walkInCustomer.name.like("%" + keyword + "%")
	                    .or(QMember.member.name.like("%" + keyword + "%"));
	        } else if (StringUtils.equals("birth", searchType)) {
	            return QWalkInCustomer.walkInCustomer.birth.like("%" + keyword + "%")
	                    .or(QMember.member.birth.like("%" + keyword + "%"));
	        } else if (StringUtils.equals("phone", searchType)) {
	            return QWalkInCustomer.walkInCustomer.phone.like("%" + keyword + "%")
	                    .or(QMember.member.phone.like("%" + keyword + "%"));
	        } else if (StringUtils.equals("email", searchType)) { // 이 부분에서 검색 유형을 "email"로 수정해야 합니다.
	            return QMember.member.email.like("%" + keyword + "%");
	        }
	    }
	    // 기본적으로 null을 반환하거나, 다른 처리 방법을 선택할 수 있습니다.
	    return null;
	}
	private BooleanExpression searchTypeConditionWalkInCustomer(String searchType, String keyword) {
		
		
		if (!StringUtils.isEmpty(searchType) && !StringUtils.isEmpty(keyword)) {
			if (StringUtils.equals("name", searchType)) {
				return QWalkInCustomer.walkInCustomer.name.like("%" + keyword + "%")
						;
			} else if (StringUtils.equals("birth", searchType)) {
				return QWalkInCustomer.walkInCustomer.birth.like("%" + keyword + "%")
						;
			} else if (StringUtils.equals("phone", searchType)) {
				return QWalkInCustomer.walkInCustomer.phone.like("%" + keyword + "%")
						;
			} 
		}
		// 기본적으로 null을 반환하거나, 다른 처리 방법을 선택할 수 있습니다.
		return null;
	}
	
	private BooleanExpression searchTypeConditionMember(String searchType, String keyword) {
		if (!StringUtils.isEmpty(searchType) && !StringUtils.isEmpty(keyword)) {
			 if (StringUtils.equals("name", searchType)) {
				 return QMember.member.name.like("%" + keyword + "%");
			 } else if(StringUtils.equals("birth", searchType)) {
				 return QMember.member.birth.like("%" + keyword + "%");
			 }else if(StringUtils.equals("phone", searchType)) {
		            return QMember.member.phone.like("%" + keyword + "%");
			 }else if (StringUtils.equals("email", searchType)) {
				 return QMember.member.email.like("%" + keyword + "%");
			 }
		}
	    return null;
	}


	@Override
	public Page<CustomerListDto> getAdminMemberPage(SearchDto searchDto, Pageable pageable) {
	       QWalkInCustomer  walkInCustomer = QWalkInCustomer.walkInCustomer;
	       QMember member = QMember.member;
	       String searchType = searchDto.getSearchType();
	       String keyword = searchDto.getKeyword();
	       String category = searchDto.getCategory();
	       
	       System.out.println(category);
	       List<CustomerListDto> content;


	       if ("member".equals(category)) {
	           // 회원 정보만 조회
	           content = queryFactory
	                   .select(new QCustomerListDto(
	                           Expressions.constant("member"),
	                           member.id,
	                           member.name,
	                           member.phone,
	                           member.birth,
	                           member.address,
	                           member.email,
	                           member.regTime
	                   ))
	                   .from(member)
	                   .where(searchTypeConditionMember(searchType, keyword))
	                   .orderBy(member.regTime.desc())
	                   .fetch();
	       } else if ("walk_in_customer".equals(category)) {
	           // Walk-in 고객 정보만 조회
	           content = queryFactory
	                   .select(new QCustomerListDto(
	                           Expressions.constant("walk_in_customer"),
	                           walkInCustomer.id,
	                           walkInCustomer.name,
	                           walkInCustomer.phone,
	                           walkInCustomer.birth,
	                           walkInCustomer.address,
	                           Expressions.constant(""),
	                           walkInCustomer.regTime
	                   ))
	                   .from(walkInCustomer)
	                   .where(searchTypeConditionWalkInCustomer(searchType, keyword))
	                   .orderBy(walkInCustomer.regTime.desc())
	                   .fetch();
	       } else {
	           // 모든 고객 정보 조회
	           List<CustomerListDto> walkInCustomerContent = queryFactory
	                   .select(new QCustomerListDto(
	                           Expressions.constant("walk_in_customer"),
	                           walkInCustomer.id,
	                           walkInCustomer.name,
	                           walkInCustomer.phone,
	                           walkInCustomer.birth,
	                           walkInCustomer.address,
	                           Expressions.constant(""),
	                           walkInCustomer.regTime
	                   ))
	                   .from(walkInCustomer)
	                   .where(searchTypeConditionWalkInCustomer(searchType, keyword))
	                   .orderBy(walkInCustomer.regTime.desc())
	                   .fetch();

	           List<CustomerListDto> memberContent = queryFactory
	                   .select(new QCustomerListDto(
	                           Expressions.constant("member"),
	                           member.id,
	                           member.name,
	                           member.phone,
	                           member.birth,
	                           member.address,
	                           member.email,
	                           member.regTime
	                   ))
	                   .from(member)
	                   .where(searchTypeConditionMember(searchType, keyword))
	                   .orderBy(member.regTime.desc())
	                   .fetch();

	           content = new ArrayList<>();
	           content.addAll(walkInCustomerContent);
	           content.addAll(memberContent);
	       }
	    	// 등록일자를 기준으로 내림차순 정렬
	    	Collections.sort(content, Comparator.comparing(CustomerListDto::getRegTime).reversed());

	    	//임의로 회원번호 지정
	        int memberNumber = content.size();
	        for (CustomerListDto dto : content) {
	           
	                dto.setId(memberNumber--);
	           
	        }
	    	
	        // 전체 결과 수 계산
	        long total = content.size();

	        // 페이지 번호와 페이지 크기를 이용하여 해당 페이지의 결과만 가져오기
	        int start = (int) pageable.getOffset();
	        int end = Math.min((start + pageable.getPageSize()), content.size());
	        List<CustomerListDto> pageContent = content.subList(start, end);
	        
	        return new PageImpl<>(pageContent, pageable, total);
	    }
	

	
}
