package com.mvppoa.adidas.config;

import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Guilherme.Salomone
 *
 */
@Configuration
public class FeignConfigure {
	public static final int connectTimeOutMillis = 12000;
	public static final int readTimeOutMillis = 12000;

	@Bean
	public Request.Options options() {
		return new Request.Options(connectTimeOutMillis, readTimeOutMillis);
	}
}
