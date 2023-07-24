package com.hotel.Exception;

public class OutOfStockException extends RuntimeException {
	
	//남아있는 객실이 없을때
	public OutOfStockException(String message) {
		super(message);
	}

}
