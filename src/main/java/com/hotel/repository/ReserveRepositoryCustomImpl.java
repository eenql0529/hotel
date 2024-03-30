package com.hotel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.hotel.dto.QReservationHistDto;
import com.hotel.dto.ReservationHistDto;
import com.hotel.dto.SearchDto;
import com.hotel.entity.QMember;
import com.hotel.entity.QReservation;
import com.hotel.entity.QRoomType;
import com.hotel.entity.QWalkInCustomer;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class ReserveRepositoryCustomImpl implements ReserveRepositoryCustom {

	private JPAQueryFactory queryFactory;
	
	public ReserveRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	
	private BooleanExpression searchTypeCondition(String searchType, String keyword) {
		
		if(!StringUtils.isEmpty(searchType) && !StringUtils.isEmpty(keyword) ) {
			if(StringUtils.equals("name", searchType)) {
				return QReservation.reservation.member.name.like("%"+keyword+"%")
						.or(QReservation.reservation.walkInCustomer.name.like("%"+keyword+"%"));
			}else if(StringUtils.equals("check_in", searchType)) {
				return QReservation.reservation.checkIn.eq(keyword);
				
			}
		else if(StringUtils.equals("check_out", searchType)) {
			return QReservation.reservation.checkOut.eq(keyword);
			
		}
		}
		
		return null;
	}
	
	
	@Override
	public Page<ReservationHistDto> getAdminReservationPage(SearchDto searchDto, Pageable pageable) {
		
		QReservation reservation = QReservation.reservation;
		QMember member = QMember.member;
		QWalkInCustomer walkInCustomer = QWalkInCustomer.walkInCustomer;
		QRoomType roomType = QRoomType.roomType;
		String searchType = searchDto.getSearchType();
		String keyword = searchDto.getKeyword();
		
		/*select r.*,
		CASE WHEN m.member_id IS NOT NULL THEN m.name WHEN w.walk_in_customer_id IS NOT NULL THEN w.name ELSE NULL END AS customer_name,
		CASE WHEN m.member_id is not null then m.phone when w.walk_in_customer_id is not null then w.phone else null end as customer_phone
		from reservation r left join member m on r.member_id = m.member_id left join walk_in_customer w on w.walk_in_customer_id = r.walk_in_customer_id;*/ 
		List<ReservationHistDto> content = queryFactory
				.select(
						new QReservationHistDto(
								reservation.id,
								reservation.reserveDate,
								reservation.checkIn,
								reservation.checkOut,
								reservation.totalPrice,
								reservation.count,
								reservation.guest,
								roomType.typeName,
								roomType.view,
								reservation.rsStatus,
						        Expressions.cases()
					            .when(member.id.isNotNull()).then(member.name)
					            .when(walkInCustomer.id.isNotNull()).then(walkInCustomer.name)
					            .otherwise((String) null)
								,Expressions.cases()
					            .when(member.id.isNotNull()).then(member.name)
					            .when(walkInCustomer.id.isNotNull()).then(walkInCustomer.name)
					            .otherwise((String) null))
						)
				.from(reservation)
				.leftJoin(reservation.member,member)
				.leftJoin(reservation.walkInCustomer,walkInCustomer)
				.leftJoin(reservation.typeId,roomType)
				.where(searchTypeCondition(searchType, keyword))
				.orderBy(reservation.reserveDate.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
				
				long total = queryFactory
						.select(Wildcard.count)
						.from(reservation)
						.leftJoin(reservation.member,member)
						.leftJoin(reservation.walkInCustomer,walkInCustomer)
						.leftJoin(reservation.typeId,roomType)
						.where(searchTypeCondition(searchType, keyword))
						.fetchOne();
				
		     
		
		
		
				return new PageImpl<>(content, pageable, total);
	}

}
