package com.hotel.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReservationStatus {
	WAITING("대기")
	,CONFIRMED("확정")
	,CANCELED("취소")
	,DELETED("삭제");
	
	
	private final String desc;

}
