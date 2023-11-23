package com.hotel.dto;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;

import com.hotel.constant.ReservationStatus;
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
	
	private Long reservationId;
	
	private ReservationStatus rsStatus;
	

	
	//엔티티 -> Dto로 바꿔준다
    public ReserveDto(Reservation reservation, String imgUrl) {
        if (reservation != null) {
            this.typeName = reservation.getTypeId().getTypeName();
            this.totalPrice = reservation.getTotalPrice();
            this.reservationId = reservation.getId();
            this.rsStatus = reservation.getRsStatus();
            
            // 예약의 count가 null이 아닌 경우에만 값을 설정
            if (reservation.getCount() != null) {
                this.count = reservation.getCount();
            } else {
                // count가 null인 경우, 예외 처리 또는 기본값 설정
                // 여기에서는 기본값으로 0을 설정하도록 했습니다.
                this.count = (long) 0;
            }
        } else {
            // reservation이 null인 경우에도 count에 기본값 설정
            this.count = (long) 0;
        }
        this.imgUrl = imgUrl;
    }
	
	
    
	

	
	
}
