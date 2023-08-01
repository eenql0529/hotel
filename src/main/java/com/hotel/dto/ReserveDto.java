package com.hotel.dto;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;

import com.hotel.entity.Reservation;
import com.hotel.entity.RoomType;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReserveDto {
	
	private Long typeId;
	
	private String typeName;
	
	private String imgUrl;
	
	private String checkIn;
	
	private String checkOut;
	
	private int guest;
	
	private LocalDateTime ReserveDate;
	
	private Long totalPrice;
	
	private Long count;
	

	
	//엔티티 -> Dto로 바꿔준다
    public ReserveDto(Reservation reservation, String imgUrl) {
        if (reservation != null) {
            this.typeName = reservation.getTypeId().getTypeName();
            this.count = reservation.getCount();
            this.totalPrice = reservation.getTotalPrice();
        }
        this.imgUrl = imgUrl;
    }
	
	
    
	

	
	
}
