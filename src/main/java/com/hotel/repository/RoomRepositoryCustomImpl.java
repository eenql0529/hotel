package com.hotel.repository;

import java.util.List;

import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.hotel.dto.QRoomTypeListDto;
import com.hotel.dto.ReserveDto;
import com.hotel.dto.RoomTypeListDto;
import com.hotel.dto.RoomtypeSearchDto;
import com.hotel.entity.QMember;
import com.hotel.entity.QReservation;
import com.hotel.entity.QRoomImg;
import com.hotel.entity.QRoomType;
import com.hotel.entity.RoomType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class RoomRepositoryCustomImpl implements RoomRepositoryCustom {

	private JPAQueryFactory queryFactory;
	
	public RoomRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	


	@Override
	public List<RoomTypeListDto> getRoomTypeList() {
		QRoomType roomType = QRoomType.roomType;
		QRoomImg roomImg = QRoomImg.roomImg;
		
		List<RoomTypeListDto> content = queryFactory
				.select(
						new QRoomTypeListDto(
								roomType.id,
								roomType.typeName,
								roomType.view,
								roomType.bedType,
								roomType.roomSize,
								roomImg.imgUrl,
								roomType.comment,
								roomType.capacity,
								roomType.price,
								roomType.availability
								)
						)
				.from(roomImg)
				.join(roomImg.typeId, roomType)
				.where(roomImg.repimgYn.eq("Y"))
				.orderBy(roomType.id.desc())
				.fetch();
		
		return content;
	}



	private BooleanExpression search(String searchQuery) {
		
		return QRoomType.roomType.typeName.like("%"+searchQuery+"%");
	}
	
	@Override
	public Page<RoomType> getAdminRoomPage(RoomtypeSearchDto roomtypeSearchDto, Pageable pageable) {
		List<RoomType> content = queryFactory
				.selectFrom(QRoomType.roomType)
				.where(search(roomtypeSearchDto.getSearchQuery()))
				.orderBy(QRoomType.roomType.id.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		long total = queryFactory.select(Wildcard.count).from(QRoomType.roomType)
				.where(search(roomtypeSearchDto.getSearchQuery()))
				.fetchOne();
		
		
		return new PageImpl<>(content, pageable, total);
	}
	
	private BooleanExpression typeNameLike(String searchQuery) {
		
		return StringUtils.isEmpty(searchQuery) ?
				null : QRoomType.roomType.typeName.like("%" + searchQuery + "%");
	}
	
}
