package com.hotel.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
import lombok.ToString;

@Entity
@Table(name = "inventory")
@Setter
@Getter
@ToString
public class Inventory {

	@Id
	@Column(name="inventory_id")
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	private String date;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="type_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private RoomType roomType;
	
	private int stock = 3;
	
	
	
}
