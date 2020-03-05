package com.asellion.service.impl;

import static com.asellion.security.SecurityConstants.EXPIRATION_TIME;
import static com.asellion.security.SecurityConstants.SECRET;
import static com.asellion.security.SecurityConstants.TOKEN_PREFIX;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {
	
	public static String generateToken(String username) {
		
		return Jwts.builder().setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		
	}
	
	public static String extractSubject(String tokenHeader) {
		return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(tokenHeader.replace(TOKEN_PREFIX, ""))
		.getBody().getSubject();
	}
}
