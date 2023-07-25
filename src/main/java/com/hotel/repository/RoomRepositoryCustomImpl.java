package com.hotel.repository;

import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.domain.Page;

import com.hotel.dto.QRoomTypeListDto;
import com.hotel.dto.ReserveDto;
import com.hotel.dto.RoomTypeListDto;
import com.hotel.entity.QMember;
import com.hotel.entity.QReservation;
import com.hotel.entity.QRoomImg;
import com.hotel.entity.QRoomType;
import com.hotel.entity.RoomType;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class RoomRepositoryCustomImpl implements RoomRepositoryCustom {

	private JPAQueryFactory queryFactory;
	
	public RoomRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	
	
	@Override
	public Page<RoomType> getAdminRoomPage(Pageable pageable) {
		return null;
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
								roomType.location,
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
	
}
