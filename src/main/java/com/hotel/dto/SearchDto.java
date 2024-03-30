package com.hotel.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDto {
    //검색 
    private String keyword; //검색 키워드
    private String searchType; //검색 타입
    
    private String category;
}
