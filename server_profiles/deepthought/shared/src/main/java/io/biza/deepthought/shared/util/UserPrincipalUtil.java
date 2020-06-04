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
package io.biza.deepthought.shared.util;

import java.util.Map;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import io.biza.deepthought.shared.exception.NotLoggedInException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserPrincipalUtil {
		private static final String CLAIMNAME_GRANTID = "consent_id";
		
        public static Jwt getJwtToken() throws NotLoggedInException {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          if (authentication instanceof AnonymousAuthenticationToken) {
            throw new NotLoggedInException("Anonymous user detected");
          }

          if(authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwt = (JwtAuthenticationToken)authentication;
            return jwt.getToken();
          }

          LOG.error("Presented an invalid token format, I can only handle JwtAuthenticationToken!");
          throw new NotLoggedInException("Invalid Token Format");

        }

        public static String getSubject() {
          try {
            return getJwtToken().getSubject();
          } catch (NotLoggedInException e) {
            return null;
          }
        }

        public static String getConsentId() {
          try {
            Map<String, Object> jwtClaims = getJwtToken().getClaims();
            if(jwtClaims.containsKey(CLAIMNAME_GRANTID))
            	return (jwtClaims.get(CLAIMNAME_GRANTID) == null)?null:jwtClaims.get(CLAIMNAME_GRANTID).toString();
          } catch (NotLoggedInException e) {
          }
          return null;
        }

        public static Map<String, Object> getClaimDetails() throws NotLoggedInException {
          return getJwtToken().getClaims();
        }

}
