package com.example.BankProject.DTO;

public class SystemConfigDTO {
	private String configKey;
	
	private String configValue;
	
	public SystemConfigDTO() {
		
	}

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

	public SystemConfigDTO(String configKey, String configValue) {
		super();
		this.configKey = configKey;
		this.configValue = configValue;
	}
	
	

}
