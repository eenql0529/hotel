package com.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.entity.Inventory;
import com.hotel.entity.RoomType;

public interface InventoryRepository  extends JpaRepository<Inventory, Long>{

    // Inventory 엔티티의 date와 roomType 필드를 이용하여 조회
    Inventory findByDateAndRoomType(String date, RoomType roomType);
    
    
}
