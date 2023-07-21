package com.hotel.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hotel.dto.RoomFormDto;
import com.hotel.dto.RoomTypeListDto;
import com.hotel.service.RoomService;

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
	
	//객실상세정보
	@GetMapping(value="/rooms/{id}")
	public String roomDetails(Model model, @PathVariable("id") Long typeId) {
		RoomFormDto roomFormDto = roomService.getRoomDtl(typeId);
		model.addAttribute("rooms", roomFormDto);
		
		
		return "rooms/roomDetails";
	}
	
	//프로모션
	@GetMapping(value="/rooms/promotions")
	public String promotions() {
		
		return "rooms/promotions";
	}
	
	
	
	
	
	//객실예약
	@GetMapping(value = "/rooms/reserve")
	public String reserve() {
		
		return "reserve/reserve";

	}
	


}
