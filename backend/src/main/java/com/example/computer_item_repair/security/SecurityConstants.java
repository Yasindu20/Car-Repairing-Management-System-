package com.example.computer_item_repair.security;

import java.util.Base64;

public class SecurityConstants {

    public static final String SIGN_UP_URLS = "/api/users/**";
    public static final String H2_URL = "h2-console/**";
    // Convert the secret to Base64 encoding
    public static final String SECRET = Base64.getEncoder().encodeToString("SecretKeyToGenJWTs".getBytes());
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final long EXPIRATION_TIME = 300_000;
}