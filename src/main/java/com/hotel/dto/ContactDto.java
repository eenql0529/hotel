package com.hotel.dto;

import com.hotel.constant.ContactStatus;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactDto {

	private  Long contactId;
	
	@NotBlank(message =  "*이름은 필수 입력사항입니다.")
	private String name;
	
	@NotBlank(message =  "*이메일은 필수 입력사항입니다.")
	private String email;
	
	@NotBlank(message =  "*제목은 필수 입력사항입니다.")
	private String title;
	
	@NotBlank(message =  "*내용은 필수 입력사항입니다.")
	private String content;
	
	private ContactStatus contactStatus; //답변상태
	
}
