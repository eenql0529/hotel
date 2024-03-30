 package com.hotel.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hotel.dto.RoomTypeListDto;
import com.hotel.service.RoomService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	
	private final RoomService roomService;
	
	@GetMapping(value="/")
	public String main(Model model) {
		
List<RoomTypeListDto> roomTypeList = roomService.getRoomTypeList();

		model.addAttribute("roomTypeList", roomTypeList);
		return "main";
	}
}
