package com.hotel.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberFormDto {
	
	@NotBlank(message =  "이름을 입력해 주세요.")
	private String name;
	
	@NotEmpty(message = "이메일을 입력해 주세요.")
	@Email(message= "이메일 형식으로 입력해 주세요.")
	private String email;
	
	@NotEmpty(message = "비밀번호를 입력해 주세요.")
	@Length(min = 8, max = 16, message = "비밀번호는 8자~16자 사이로 입력해주세요.")
	private String password;
	
	@NotEmpty(message = "비밀번호를 입력해 주세요.")
	@Length(min = 8, max = 16, message = "비밀번호는 8자~16자 사이로 입력해주세요.")
	private String passwordRetype;
	
	@NotEmpty(message = "주소를 입력해 주세요.")
	private String address;
	
	@NotEmpty(message = "휴대폰 번호를 입력해 주세요.")
	private String phone;

	@NotEmpty(message = "생년월일을 입력해 주세요.")
	private String birth;
}
