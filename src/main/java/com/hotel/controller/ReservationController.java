package com.hotel.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import com.hotel.dto.ReservationHistDto;
import com.hotel.dto.ReserveDto;
import com.hotel.dto.RoomFormDto;
import com.hotel.entity.RoomType;
import com.hotel.service.ReservationService;
import com.hotel.service.RoomService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReservationController {
	
	private final ReservationService reservationService;
	private final RoomService roomService;
	
	
	//예약페이지 보여주기
	
	@GetMapping(value ="/reserve/{id}" )
	public String reserve(Model model, @PathVariable("id") Long typeId, Authentication authentication) {


	    RoomFormDto roomFormDto = roomService.getRoomDtl(typeId);
	    model.addAttribute("rooms", roomFormDto);
	    return "reserve/reserve";
	}
	
	
	
	
	
	
	
	//예약

	@PostMapping(value = "/reservation")
	public @ResponseBody ResponseEntity order(@RequestBody @Valid ReserveDto reserveDto,
			BindingResult bindingResult, Principal principal) {
		//Principal: 로그인한 사용자의 정보를 가져올 수 있다.
		System.out.println(reserveDto.getCheckIn()+"askjfhasklgjhaslgkjahsgk");
		if(bindingResult.hasErrors()) {
			StringBuilder sb = new StringBuilder();
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
			
			for (FieldError fieldError : fieldErrors) {
				sb.append(fieldError.getDefaultMessage()); //에러메세지를 합친다.
			}
			
			return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
		}
		
		String email = principal.getName(); //id에 해당하는 정보를 가지고 온다(email)
		Long reservationId;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy", Locale.ENGLISH);
		LocalDate startDate = LocalDate.parse(reserveDto.getCheckIn(), formatter);
        LocalDate endDate = LocalDate.parse(reserveDto.getCheckOut(), formatter);
		
        long daysBetween = endDate.toEpochDay() - startDate.toEpochDay(); //두 날짜 차이 구하기.
        System.out.println(daysBetween);
        reserveDto.setCount(daysBetween);

		try {
			reservationId = reservationService.reserve(reserveDto, email); //주문하기
			
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Long>(reservationId, HttpStatus.OK); //성공시
	}
	
	
	
	
	//예약내역 
	@GetMapping(value = "/check")
	public String reserveHist(Principal principal, Model model) {

		//2. 서비스 호출
		List<ReservationHistDto> reservationHistDtoList = 
				reservationService.getReservationList(principal.getName());
		

		//3. 서비스에서 가져온 값들을 view단에 model을 이용해 전송

		model.addAttribute("reservations", reservationHistDtoList);

		return "reserve/reserveList";
	}
	
	
	
	//예약취소
	@PostMapping("/reservation/{reservationId}/cancel")
	public @ResponseBody ResponseEntity cancelReserve(@PathVariable("reservationId") Long reservationId
				, Principal principal) {
		//1. 본인확인
		if(!reservationService.validateReserve(reservationId, principal.getName())) {
				return new ResponseEntity<String>("예약 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
				
		}
		
		//2.주문취소
		System.out.println(reservationId);
		reservationService.cancelReserve(reservationId);
		
		return new ResponseEntity<Long>(reservationId, HttpStatus.OK); //성공했을때
	}
	
}
