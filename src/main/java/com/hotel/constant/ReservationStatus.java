package com.hotel.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReservationStatus {
	RESERVATION("예약")
	,CHECK_IN("체크인")
	,CHECK_OUT("체크아웃")
	,CANCEL("취소")
	,NO_SHOW("노쇼");
	
	
	private final String desc;

}
