package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("file:c://temp//transit.properties")
public class AppConfig {

	@Autowired
	private Environment env;

	public String getGooleAPIKey(){
		return env.getProperty("google.api.key");
	}

	public String getHomeAddress(){
		return env.getProperty("address.home");
	}

	public String getWorkAddress(){
		return env.getProperty("address.work");
	}

	public String getProxyUrl(){
		return env.getProperty("proxy.url");
	}
	
	public String getProxyPort(){
		return env.getProperty("proxy.port");
	}

	public String getProxyUser(){
		return env.getProperty("proxy.user");
	}

	public String getProxyPassword(){
		return env.getProperty("proxy.password");
	}

}
