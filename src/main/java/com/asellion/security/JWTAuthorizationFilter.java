package com.asellion.security;

import static com.asellion.security.SecurityConstants.HEADER_AUTH_STRING;
import static com.asellion.security.SecurityConstants.HEADER_USERNAME_STRING;
import static com.asellion.security.SecurityConstants.TOKEN_PREFIX;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.asellion.service.impl.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	public JWTAuthorizationFilter(AuthenticationManager authManager) {
		super(authManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String header = req.getHeader(HEADER_AUTH_STRING);
		if (header == null || !header.startsWith(TOKEN_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}
		try {
			UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			chain.doFilter(req, res);
		} catch (ExpiredJwtException | MalformedJwtException ex) {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
		}
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String tokenHeader = request.getHeader(HEADER_AUTH_STRING);
		String userNameHeader = request.getHeader(HEADER_USERNAME_STRING);
		if (tokenHeader != null) {
			String userId = JwtUtil.extractSubject(tokenHeader);
			if (userId != null && userId.equals(userNameHeader)) {
				return new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
			}
			return null;
		}
		return null;
	}
}
