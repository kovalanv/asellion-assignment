package com.asellion.security;

public class SecurityConstants {
	public static final String SECRET = "SecretKeyToGenJWTs";
	public static final long EXPIRATION_TIME = 864_000_000; // 10 days
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_AUTH_STRING = "Authorization";
	public static final String HEADER_USERID_STRING = "userId";
	public static final String HEADER_USERNAME_STRING = "username";
	public static final String PRODUCTS_URL = "/products/**";

}