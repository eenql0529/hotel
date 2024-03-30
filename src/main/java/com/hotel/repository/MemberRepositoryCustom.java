package com.hotel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hotel.dto.CustomerListDto;
import com.hotel.dto.SearchDto;
import com.hotel.entity.Member;

public interface MemberRepositoryCustom {

	Page<CustomerListDto> getAdminMemberPage(SearchDto searchDto, Pageable pageable);
	
	
}
