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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.asellion.model.LoginUser;
import com.asellion.service.impl.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private AuthenticationManager authenticationManager;
	
	@Autowired
	JwtUtil jwtUtilService;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		try {
			LoginUser creds = new ObjectMapper().readValue(req.getInputStream(), LoginUser.class);
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(creds.getUsername(),
					creds.getPassword(), new ArrayList<>()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {

		User userDetails = (User) auth.getPrincipal();

		String token = JwtUtil.generateToken(userDetails.getUsername());
		res.addHeader(HEADER_AUTH_STRING, TOKEN_PREFIX + token);
		res.addHeader(HEADER_USERNAME_STRING, userDetails.getUsername());
	}
}
