package com.avob.openadr.server.oadr20b.vtn;

import java.security.cert.X509Certificate;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import com.avob.openadr.security.OadrFingerprintSecurity;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.common.vtn.security.OadrSecurityRoleService;

/**
 * x509 oadr fingerprint mechanism demonstration
 * 
 * this fingerprint should be check against some sort of database
 * 
 * @author bertrand
 *
 */
@Service
public class Oadr20bX509AuthenticatedUserDetailsService
		implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bX509AuthenticatedUserDetailsService.class);
	@Resource
	private OadrSecurityRoleService oadrSecurityRoleService;

	@Override
	public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) {
		X509Certificate certificate = (X509Certificate) token.getCredentials();
		String fingerprint = "";
		LOGGER.warn("\"========= Oadr20bX509AuthenticatedUserDetailsService =========");
		try {
			fingerprint = OadrFingerprintSecurity.getOadr20bFingerprint(certificate);
		} catch (OadrSecurityException e) {
			LOGGER.warn("\"========= Oadr20bX509AuthenticatedUserDetailsService: Fail =========");
			throw new UsernameNotFoundException("", e);
		}
		LOGGER.warn("\"========= Oadr20bX509AuthenticatedUserDetailsService: Success =========");

		return oadrSecurityRoleService.grantX509Role(fingerprint);

	}
}
