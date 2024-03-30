package com.hotel.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailDto {
	
	private Long contactId;
	
	private String title;
	private String content;
}
