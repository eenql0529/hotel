package com.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotel.entity.Inventory;
import com.hotel.entity.RoomType;

public interface InventoryRepository  extends JpaRepository<Inventory, Long>{

    // Inventory 엔티티의 date와 roomType 필드를 이용하여 조회
    Inventory findByDateAndRoomType(String date, RoomType roomType);
    
    //오늘남은객실수
    @Query("select coalesce(sum(i.stock),0) from Inventory i where date(i.date) =  STR_TO_DATE(:selectedDate, '%Y년 %m월 %d일')")
    int countStockToday(@Param("selectedDate") String selectedDate);
    
    
}
