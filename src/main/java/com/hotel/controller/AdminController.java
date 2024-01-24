package com.hotel.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hotel.constant.ReservationStatus;
import com.hotel.dto.ReservationHistDto;
import com.hotel.dto.ReserveDto;
import com.hotel.dto.RoomFormDto;
import com.hotel.dto.RoomImgDto;
import com.hotel.dto.RoomTypeListDto;
import com.hotel.dto.RoomtypeSearchDto;
import com.hotel.entity.Reservation;
import com.hotel.entity.RoomType;
import com.hotel.repository.InventoryRepository;
import com.hotel.repository.ReserveRepository;
import com.hotel.repository.RoomRepository;
import com.hotel.service.AdminService;
import com.hotel.service.ReservationService;
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
	private final ReserveRepository reserveRepository;
	private final InventoryRepository inventoryRepository;

	
	@GetMapping("/test")
	public String test() {
		
		return "/admin/test";
		
	}
	@GetMapping("/admin")
	public String admin2(
	        @RequestParam(name = "selectedDate", required = false) String selectedDate,
	        Model model) {
	    // 선택된 날짜를 기반으로 필요한 업데이트 수행
	    adminService.updateInventory();
	    

	    // selectedDate가 비어 있다면 오늘 날짜로 설정
	    if (selectedDate == null || selectedDate.isEmpty()) {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
	        selectedDate = dateFormat.format(new Date());
	    }
	 


	    return "admin/admin";
	}
	
	@ResponseBody
	@GetMapping("/admin/dashboard")
	public Map<String, Object> admin(
			@RequestParam(name = "selectedDate", required = false) String selectedDate,
			Model model) {
		System.out.println(selectedDate);
	    // selectedDate가 비어 있다면 오늘 날짜로 설정ss
	    if (selectedDate == null || selectedDate.isEmpty()) {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
	        selectedDate = dateFormat.format(new Date());
	        
	        
	    }
		// 모델에 업데이트된 데이터 추가
		Map<String, Object> responseData = new HashMap<>();
		responseData.put("selectedDate", selectedDate);
		responseData.put("countCheckIn", reserveRepository.countCheckInToday(selectedDate));
		responseData.put("countCheckIn2", reserveRepository.countRsStatusCheckInToday(selectedDate));
		responseData.put("countCheckOut", reserveRepository.countCheckOutToday(selectedDate));
		responseData.put("countCheckOut2", reserveRepository.countRsStatusCheckOutToday(selectedDate));
		responseData.put("countStock", inventoryRepository.countStockToday(selectedDate));
		responseData.put("countReservationToday", reserveRepository.countReservationToday(selectedDate));
		responseData.put("countCanceledReservationToday", reserveRepository.countCanceledReservationToday(selectedDate));
		responseData.put("calculateOccupancyRate", reserveRepository.calculateOccupancyRate(selectedDate));

		 // calculateRate에서 반환되는 List<Object[]>를 Map에 추가
	    List<Object[]> calculateRateResult = reserveRepository.calculateRate(selectedDate);
	    List<Map<String, Object>> calculateRateList = new ArrayList<>();
	    for (Object[] row : calculateRateResult) {
	        Map<String, Object> rowMap = new HashMap<>();
	        rowMap.put("typeId", row[0]);
	        rowMap.put("count", row[1]);
	        rowMap.put("occupancyRate", row[2]);
	        calculateRateList.add(rowMap);
	    }
	    responseData.put("calculateRate", calculateRateList);
		
		return responseData;
	}

	


	// 객실타입리스트
	@GetMapping(value = {"/admin/roomtypeList","/admin/roomtypeList/{page}"})
	public String roomtypeList(Model model) {

		
		
		
		List<RoomTypeListDto> roomType = adminService.getRoomTypeList();

		model.addAttribute("rooms",roomType);
		

		return "admin/roomtypeList";
	}

	// 객실등록
	@GetMapping(value = "/admin/room")
	public String roomForm(Model model) {
		model.addAttribute("roomFormDto", new RoomFormDto());
		return "admin/roomtypeForm";
	}
	
	
	// 객실, 객실이미지 등록(insert)
	@PostMapping(value = "/admin/room/new")
	public String roomNew(@Valid RoomFormDto roomFormDto, BindingResult bindingResult, Model model,
			@RequestParam("roomImgFile") List<MultipartFile> roomImgFileList) {

		if (bindingResult.hasErrors()) {
			return "admin/roomtypeForm";

		}

		// 상품등록전에 첫번째 이미지가 있는지 없는지 검사(첫번째 이미지는 필수 입력값)
		if (roomImgFileList.get(0).isEmpty()) {
			model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수입니다.");
			return "admin/roomtypeForm";
		}

		try {
			adminService.saveRoom(roomFormDto, roomImgFileList);

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "상품 등록 중 에러가 발생했습니다.");
			return "admin/roomtypeForm";
		}

		return "redirect:/";
	}

	// 객실 수정페이지 보기
	@GetMapping(value = "/admin/room/{typeId}")
	public String roomDtl(@PathVariable("typeId") Long typeId, Model model) {

		try {
			RoomFormDto roomFormDto = roomService.getRoomDtl(typeId);
			model.addAttribute("roomFormDto", roomFormDto);
			System.out.println("typeId:"+typeId);
			System.out.println("roomFormDto.getBedType:"+roomFormDto.getBedType());

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "객실정보를 가져올 때 에러가 발생했습니다.");
			// 에러발생시 비어있는 객체를 넘겨준다.
			model.addAttribute("roomFormDto", new RoomFormDto());

			return "admin/roomtypeForm";
		}

		return "admin/roomModifyForm";
	}

//	// 객실 수정(update)
	@PostMapping(value = "/admin/room/{typeId}")
	public String roomUpadate(@Valid RoomFormDto roomFormDto, Model model, BindingResult bindingResult,
			@RequestParam("roomImgFile") List<MultipartFile> roomImgFileList) {

		if (bindingResult.hasErrors()) {
			return "admin/roomtypeForm";
		}

		// 첫번째 이미지가 있는지 검사
		if (roomImgFileList.get(0).isEmpty() && roomFormDto.getId() == null) {
			model.addAttribute("errorMessage", "첫번째 객실 이미지는 필수입니다.");
			return "admin/roomtypeForm";

		}

		try {
			adminService.updateRoomType(roomFormDto, roomImgFileList);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "객실 수정 중 에러가 발생했습니다.");
			return "admin/roomtypeForm";
		}

		return "redirect:/";
	}

	// 객실타입삭제
	@DeleteMapping("/room/delete/{typeId}")
	public @ResponseBody ResponseEntity deleteRoomType(@PathVariable("typeId") Long typeId, Principal principal) {

		adminService.deleteRoomType(typeId);

		return new ResponseEntity<Long>(typeId, HttpStatus.OK);

	}

	// 고객 예약목록조회
	@GetMapping(value = "/admin/reservationList")
	public String reservationList(Model model) {
		System.out.println("d111111111111111111111fdfdfdfdfd");

		List<ReservationHistDto> reservationHistDtoList = adminService.getReservationList();

		model.addAttribute("reservations", reservationHistDtoList);
	
		return "admin/reservationList";

	}

	// 예약 상태 업데이트

	@PostMapping("/admin/{reservationId}/updateStatus")
	public ResponseEntity updateReservationStatus(@PathVariable("reservationId") Long reservationId,
			@RequestParam("status") String status, Principal principal) {

		// 2. 주문 상태 업데이트
		adminService.updateReservation(reservationId, status);
		
		System.out.println(status);
		System.out.println(reservationId);

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	// 예약 상태 (update)
	@PostMapping(value = "/admin/{reservationId}/update2")
	public String updateReservation(@Valid ReserveDto reserveDto, Model model) {

	

		try {
			System.out.println("dfdfdfdfdfd");
			adminService.updateReservation2(reserveDto);
			System.out.println("d111111111111111111111fdfdfdfdfd");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "객실 수정 중 에러가 발생했습니다.");
			return "admin/reservationList";
		}

		return "redirect:/";
	}

}
