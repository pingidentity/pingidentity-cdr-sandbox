/*******************************************************************************
 * Copyright (C) 2020 Biza Pty Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *******************************************************************************/
package io.biza.deepthought.shared.component.security;

import java.security.Provider;
import java.security.Security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import com.nimbusds.jose.crypto.bc.BouncyCastleProviderSingleton;

@Configuration
@EnableWebSecurity
public class SecurityConfigurationWeb extends WebSecurityConfigurerAdapter {
        @Override
	protected void configure(final HttpSecurity http) throws Exception {
		/**
		 * Inject RSASSA-PSS support into runtime
		 */
		Provider bc = BouncyCastleProviderSingleton.getInstance();
		Security.addProvider(bc);

		// JWT Validation
		http.oauth2ResourceServer().jwt();
		// Anonymous access, method security to pickup
		// http.anonymous();
	}
}

