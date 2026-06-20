package com.example.BankProject.Mapper;

import java.time.LocalDateTime;

import com.example.BankProject.DTO.SystemConfigDTO;
import com.example.BankProject.Entity.SystemConfig;

public class SystemConfigMapper {
	
	public static SystemConfig toEntity(SystemConfigDTO dto) {
		SystemConfig config = new SystemConfig();
		config.setConfigKey(dto.getConfigKey());
		config.setConfigValue(dto.getConfigValue());
		config.setUpdatedAt(LocalDateTime.now());
		return config;
	}

}
