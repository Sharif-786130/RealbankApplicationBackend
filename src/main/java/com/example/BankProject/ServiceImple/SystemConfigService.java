package com.example.BankProject.ServiceImple;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.BankProject.Entity.SystemConfig;
import com.example.BankProject.Repository.SystemConfigRepository;

//(Admin Rules)
@Service
public class SystemConfigService {
	
	private final SystemConfigRepository configRepo;
	
	public SystemConfigService(SystemConfigRepository configRepo) {
		this.configRepo=configRepo;
	}
	
	public String getValue(String key) {
		return configRepo.findById(key)
				.orElseThrow(()-> new RuntimeException("Config is not Found"))
				.getConfigValue();
	}
	
	public SystemConfig save(SystemConfig config) {
		config.setUpdatedAt(LocalDateTime.now());
		return configRepo.save(config);
	}

}
