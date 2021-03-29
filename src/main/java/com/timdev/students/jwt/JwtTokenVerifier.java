package com.timdev.students.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtTokenVerifier extends OncePerRequestFilter {

	private final Algorithm secretKey;
	private final JwtConfig jwtConfig;

	@Autowired
	public JwtTokenVerifier(Algorithm secretKey, JwtConfig jwtConfig) {
		this.secretKey = secretKey;
		this.jwtConfig = jwtConfig;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                                HttpServletResponse response,
	                                FilterChain filterChain) throws ServletException, IOException {
		String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());
		String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");

		if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			JWTVerifier verifier = JWT
					.require(secretKey)
					.build();
			final DecodedJWT decoded = verifier.verify(token);

			String username = decoded.getSubject();
			Claim authoritiesClaim = decoded.getClaims().get("authorities");

			final List<String> list = authoritiesClaim.asList(String.class);
			Set<SimpleGrantedAuthority> simpleGrantedAuthorities = list
					.stream()
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toSet());

			Authentication authentication = new UsernamePasswordAuthenticationToken(
					username,
					null,
					simpleGrantedAuthorities);

			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (JWTVerificationException e) {
			throw new IllegalStateException((String.format("Token %s is cannot be trusted", token)));
		}

		filterChain.doFilter(request, response);
	}
}
