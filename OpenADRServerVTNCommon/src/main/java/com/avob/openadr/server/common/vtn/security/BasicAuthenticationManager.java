package com.avob.openadr.server.common.vtn.security;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class BasicAuthenticationManager implements AuthenticationManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(BasicAuthenticationManager.class);

	public static final String BASIC_REALM = "basic-oadr.avob.com";

	@Resource
	private OadrSecurityRoleService oadrSecurityRoleService;

	@Override
	public Authentication authenticate(Authentication authentication) {
		String username = authentication.getName();
		String pw = authentication.getCredentials().toString();
		LOGGER.warn("\"========= OadrSecurityRoleService => " + username + " => " + pw);

		CharSequence rawPassword = pw;

		User grantBasicRole = oadrSecurityRoleService.grantBasicRole(username, rawPassword);
		return new UsernamePasswordAuthenticationToken(username, pw, grantBasicRole.getAuthorities());

	}

}
