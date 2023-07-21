package com.hotel.controller;

import java.awt.print.Pageable;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hotel.dto.RoomFormDto;
import com.hotel.dto.RoomImgDto;
import com.hotel.dto.RoomTypeListDto;
import com.hotel.entity.RoomType;
import com.hotel.repository.RoomRepository;
import com.hotel.service.AdminService;
import com.hotel.service.RoomImgService;
import com.hotel.service.RoomService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;
	private final RoomService roomService;
	private final RoomRepository roomRepository;
	
	//객실관리
	@GetMapping(value= "/admin/room")
	public String roomMng(Model model) {
		
		//of(조회할 페이지의 번호 ★0부터 시작, 한페이지당 조회할 데이터 갯수)
		//url경로에 페이지가 있으면 해당 페이지 번호를 조회하도록 하고 페이지 번호가 없으면 0페이지를 조회	
		
		List<RoomTypeListDto> roomType = adminService.getRoomTypeList();

		model.addAttribute("roomType", roomType);

		
		return "admin/roomMng";
	}
	
	
	
	
	//객실등록
	@GetMapping(value="/admin/room/new")
	public String roomForm(Model model) {
		model.addAttribute("roomFormDto", new RoomFormDto());
		return "admin/roomForm";
	}
	
	//객실, 객실이미지 등록(insert)
	@PostMapping(value = "/admin/room/new")
	public String roomNew(@Valid RoomFormDto roomFormDto, BindingResult bindingResult,
			Model model, @RequestParam("roomImgFile") List<MultipartFile> roomImgFileList) {
		
		if(bindingResult.hasErrors()) {
			return "admin/roomForm";
			
		}
		
		//상품등록전에 첫번째 이미지가 있는지 없는지 검사(첫번째 이미지는 필수 입력값)
		if(roomImgFileList.get(0).isEmpty()) {
			model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수입니다.");
			return "admin/roomForm";
		}
		
		try {
			adminService.saveRoom(roomFormDto, roomImgFileList);
		
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "상품 등록 중 에러가 발생했습니다.");
			return "admin/roomForm";
		}
		
		return "redirect:/";
	}
	
	
	
	//객실 수정페이지 보기
	@GetMapping(value = "/admin/room/{typeId}")
	public String roomDtl(@PathVariable("typeId") Long typeId, Model model) {
		
		try {
			RoomFormDto roomFormDto = roomService.getRoomDtl(typeId);
			model.addAttribute("roomFormDto",roomFormDto);
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage","객실정보를 가져올 때 에러가 발생했습니다.");
			//에러발생시 비어있는 객체를 넘겨준다.
			model.addAttribute("roomFormDto", new RoomFormDto());
			
			return "admin/roomForm";
		}
		
		return "admin/roomModifyForm";
	}
	
	//객실 수정(update)
	@PostMapping(value = "/admin/room/{typeId}")
	public String roomUpadate(@Valid RoomFormDto roomFormDto, Model model,
			BindingResult bindingResult,
			@RequestParam("roomImgFile") List<MultipartFile> roomImgFileList) {
		
		if(bindingResult.hasErrors()) {
			return "admin/roomForm";
		}
		
		//첫번째 이미지가 있는지 검사
		if(roomImgFileList.get(0).isEmpty() && roomFormDto.getId() == null) {
			model.addAttribute("errorMessage", "첫번째 객실 이미지는 필수입니다.");
			return "admin/roomForm";
			
		}
		
		try {
			adminService.updateRoomType(roomFormDto, roomImgFileList);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "객실 수정 중 에러가 발생했습니다.");
			return "admin/roomForm";
		}
		
		return "redirect:/";
	}
	
	
	//객실타입삭제
	@DeleteMapping("/room/delete/{typeId}")
	public  @ResponseBody ResponseEntity deleteRoomType(@PathVariable("typeId") Long typeId
			, Principal principal) {
		
		adminService.deleteRoomType(typeId);
		
		return new ResponseEntity<Long>(typeId, HttpStatus.OK);
		
	}
}
