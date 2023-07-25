package com.hotel.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hotel.dto.RoomFormDto;
import com.hotel.dto.RoomTypeListDto;
import com.hotel.service.RoomService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RoomController {
	
	private final RoomService roomService;
	
	
	//객실전체 리스트
	@GetMapping(value="/rooms/roomList")
	public String roomList(Model model) {
		
		List<RoomTypeListDto> roomTypeList = roomService.getRoomTypeList();
		
		model.addAttribute("roomTypeList", roomTypeList);
		
		return "rooms/roomList";
	}
	
	@GetMapping(value = "/rooms/{id}")
	public String roomDetails(Model model, @PathVariable("id") Long typeId, Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated()) {
	        // 로그인되지 않은 상태이므로 로그인 안내창을 띄워줄 수 있습니다.
	        // 예를 들어, model에 로그인 안내창 메시지를 추가하고 해당 뷰에서 처리할 수 있습니다.
	        
	    	model.addAttribute("loginMessage", "로그인 후 이용해주세요.");
	    	RoomFormDto roomFormDto = roomService.getRoomDtl(typeId);
		    model.addAttribute("rooms", roomFormDto);
		    return "rooms/roomDetails";
	    }

	    RoomFormDto roomFormDto = roomService.getRoomDtl(typeId);
	    model.addAttribute("rooms", roomFormDto);
	    return "rooms/roomDetails";
	}
	
	//프로모션
	@GetMapping(value="/rooms/promotions")
	public String promotions() {
		
		return "rooms/promotions";
	}
	
	
	

	
	
//	//객실예약
//	@GetMapping(value = "/reserve/{id}")
//	public @ResponseBody ResponseEntity reserve(Model model,@RequestBody @Valid RoomFormDto roomFormDto,BindingResult bindingResult,Principal principal,@PathVariable("id") Long typeId) {
//
//		if(bindingResult.hasErrors()) {
//			StringBuilder sb = new StringBuilder();
//			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//			
//			for (FieldError fieldError : fieldErrors) {
//				sb.append(fieldError.getDefaultMessage()); //에러메세지 합친다.
//			}
//			return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
//		}
//		
//		String email = principal.getName(); 
//		
//
//
//		return new ResponseEntity<Long>(HttpStatus.OK);
//
//	}
	
//	@PostMapping(value="/reserve")
//	public @ResponseBody ResponseEntity reservation(Principal principal ,BindingResult bindingResult) {
//		
//		if(bindingResult.hasErrors()) {
//			StringBuilder sb = new StringBuilder();
//			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//			
//			for(FieldError fieldError : fieldErrors) {
//				sb.append(fieldError.getDefaultMessage());
//			}
//			
//			return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
//		}
//		
//		
//		return new ResponseEntity<Long>(HttpStatus.OK);
//	}
	


}
