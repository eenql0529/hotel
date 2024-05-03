package com.hotel.controller;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hotel.dto.ContactDto;
import com.hotel.dto.MemberFormDto;
import com.hotel.entity.Member;
import com.hotel.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	
	//문의페이지
	@GetMapping(value="/contact")
	public String contact(Model model) {
		
		model.addAttribute("contactDto", new ContactDto());
		
		return "member/contact";
	}
	
	//문의하기
	@PostMapping(value="/contact/new")
	public String submitContact(@Valid ContactDto contactDto, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			return "member/contact";

		}
		
		try {
			memberService.contact(contactDto);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "잠시 후 다시 시도해주세요");
			return "member/contact";
		}
		return "member/contact"; //성공시
		
	}
	//로그인
	@GetMapping(value="/members/login")
	public String login() {
		return "member/loginForm";
	}
	
	//회원가입 화면
	@GetMapping(value = "/members/signUp")
	public String signUp(Model model) {
		model.addAttribute("memberFormDto" , new MemberFormDto());
		return "member/signUpForm";
	}
	
	//회원가입
	@PostMapping("/members/signUp")
	public @ResponseBody ResponseEntity signUp(@RequestBody @Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
		//@Valid: 유효성을 검증하려는 객체 앞에 붙인다.
		//BindingResult: 유효성 검증 후의 결과가 들어있다.
		
	    if (bindingResult.hasErrors()) {
	        FieldError firstError = bindingResult.getFieldErrors().get(0); // 첫 번째 에러 메시지만 가져옴
			
			return new ResponseEntity<String>(firstError.getDefaultMessage(), HttpStatus.BAD_REQUEST);
		}
	    // 비밀번호가 조건에 맞지 않는 경우 처리
	    if (!strongPassword(memberFormDto.getPassword())) {
	        return new ResponseEntity<String>("비밀번호는 8글자 이상, 영문, 숫자, 특수문자(@$!%*#?&)를 사용해야 합니다.", HttpStatus.BAD_REQUEST);
	    }
	    
	    // 비밀번호와 비밀번호 확인이 다른 경우 처리
	    if (!memberFormDto.getPassword().equals(memberFormDto.getPasswordRetype())) {
	        return new ResponseEntity<String>("비밀번호와 비밀번호 확인이 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
	    }
	    
		Long memberId;
		
		try {		
			//MemberFormDto -> Member Entity, 비밀번호 암호화
		Member member = Member.createMember(memberFormDto, passwordEncoder);
		memberId=memberService.saveMember(member);
			
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Long>(memberId,HttpStatus.OK); //성공시

	}
	
	//로그인 실패했을때
	@GetMapping(value="/members/login/error")
	public String loginError(Model model) {
		model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해주세요.");
		return "member/loginForm";
	}
	
    public static boolean strongPassword(String password) {
        // 비밀번호가 8글자 이상, 영문, 숫자, 특수문자(@$!%*#?&) 중 하나 이상을 포함하는지 확인하는 정규 표현식
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
	
}
