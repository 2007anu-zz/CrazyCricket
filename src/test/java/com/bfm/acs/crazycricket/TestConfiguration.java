/**
 * 
 */
package com.bfm.acs.crazycricket;

import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {
	@Bean
	public ErrorController errorController(ErrorAttributes errorAttributes) {
		return new ErrorController(errorAttributes);
	}
}
