package com.bfm.acs.crazycricket.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServerConfig {
	@Value("${server.ip}")
	private String ip;
	@Value("${server.port}")
	private String port;

	public String getIpAddress() {
		return ip;
	}

	public void setIpAddress(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
}
