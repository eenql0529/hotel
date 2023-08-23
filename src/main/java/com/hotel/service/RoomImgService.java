package com.hotel.service;

import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hotel.entity.RoomImg;
import com.hotel.repository.RoomImgRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomImgService {

	//private String roomImgLocation = "C:/hotel/room";
	private final RoomImgRepository roomImgRepository;
	private final FileService fileService;
	
	@Value("${roomImgLocation}")
	private String roomImgLocation;
	
	/*
	 * 이미지 저장
	    1.파일을 itemImgLocation에 저장
	    2.item_img 테이블에 저장
	*/
	
	public void saveRoomImg(RoomImg roomImg, MultipartFile roomImgFile) throws Exception{
		String oriImgName = roomImgFile.getOriginalFilename();  //파일이름 -> 이미지1.jpg
		String imgName = "";
		String imgUrl = "";
		
		//1.파일을 itemImgLocation에 저장
		if(!StringUtils.isEmpty(oriImgName)) {
			//oriImgName이 빈문자열이 아니라면 이미지 파일 업로드
			imgName = fileService.uploadFile(roomImgLocation, 
					oriImgName, roomImgFile.getBytes());
			imgUrl = "/images/room/" + imgName;
		}
		
		//2.item_img 테이블에 저장
		
		//(이미지1.jpg, ERSFHG4FDGD454.jpg, /images/item/ERSFHG4FDGD454.jpg)로 entity값을 update
		roomImg.updateRoomImg(oriImgName, imgName, imgUrl);
		roomImgRepository.save(roomImg); //db에 insert
		
	}
	
	//이미지 업데이트 메소드
	public void updateRoomImg(Long roomImgId, MultipartFile roomImgFile)
		throws Exception{
		if(!roomImgFile.isEmpty()) { //첨부한 파일이 있으면
			
			//해당 이미지를 가져오고
			RoomImg savedRoomImg = roomImgRepository.findById(roomImgId)
										.orElseThrow(EntityNotFoundException::new);
			
			//기존 이미지 파일 C:/hotel/room 폴더에서 삭제
			if(!StringUtils.isEmpty(savedRoomImg.getImgName())) {
				fileService.deleteFile(roomImgLocation + "/" + savedRoomImg.getImgName()); 
			} 
			
			//수정된 이미지 파일 C:/shop/item 에 업로드
			String oriImgName = roomImgFile.getOriginalFilename();
			String imgName = fileService.uploadFile(roomImgLocation, oriImgName, roomImgFile.getBytes());
			String imgUrl = "/images/room/" + imgName;
			
			//update쿼리문 실행
			/* ★★★ 한번 insert를 진행하면 엔티티가 영속성 컨텍스트에 저장이 되므로 
			그 이후에는 변경감지 기능이 동작하기 때문에 개발자는 update쿼리문을 쓰지 않고
			엔티티 데이터만 변경해주면 된다. */
			savedRoomImg.updateRoomImg(oriImgName, imgName, imgUrl);
		}
	}
	
	

}
