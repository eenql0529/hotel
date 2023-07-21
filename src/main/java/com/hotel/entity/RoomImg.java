package com.hotel.entity;



import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="room_img")
@Setter
@Getter
public class RoomImg  extends BaseEntity{
	
	@Id
	@Column(name="img_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String imgName;
	
	private String oriImgName; //원본 이미지 파일명
	
	private String imgUrl;
	
	private String repimgYn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_id")
	@OnDelete(action= OnDeleteAction.CASCADE)
	private RoomType typeId;
	
	//이미지에 대한 정보를 업데이트 하는 메소드
	public void updateRoomImg(String oriImgName, String imgName, String imgUrl) {
		this.oriImgName = oriImgName;
		this.imgName = imgName;
		this.imgUrl = imgUrl;
	}
}
