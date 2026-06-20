package com.example.BankProject.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="system_config")
public class SystemConfig {
	
	@Id
	@Column(name= "config_key", nullable = false)
	private String configKey;
	
	private String configValue;
	
	private String description;
	
	private LocalDateTime updatedAt;
	
	//constructor
	public SystemConfig() {
		
	}

	//setter and getters
	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	//constructor
	public SystemConfig(String configKey, String configValue, String description, LocalDateTime updatedAt) {
		super();
		this.configKey = configKey;
		this.configValue = configValue;
		this.description = description;
		this.updatedAt = updatedAt;
	}
	
	

}
