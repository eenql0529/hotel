package com.hotel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.hotel.config.AuditorAwareImpl;

@EnableJpaAuditing //JAP의 auditing 기능을 활성화
@Configuration
public class AuditConfig {

	@Bean
	public AuditorAware<String> auditorProvider() {
		return new AuditorAwareImpl();
	}
}
