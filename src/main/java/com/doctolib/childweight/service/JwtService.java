package com.doctolib.childweight.service;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.doctolib.childweight.domain.HealthcareProvider;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;

@Service
public class JwtService {

	private static final String SECRET_KEY = "your-256-bit-secret-your-256-bit-secret";
	// must be at least 256 bits (32+ chars) for HS256

	private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

	public String generateToken(HealthcareProvider provider) {
		return Jwts.builder().setSubject(provider.getProfessionalId()).claim("name", provider.getName())
				.claim("facility", provider.getFacilityId()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 3600000)).signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	public boolean validateToken(String token, HealthcareProvider provider) {
		String professionalId = extractProfessionalId(token);
		return (professionalId.equals(provider.getProfessionalId()) && !isTokenExpired(token));
	}

	   private String extractProfessionalId(String token) {
	        Claims claims = Jwts.parserBuilder()
	                .setSigningKey(key)
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	        return claims.getSubject();
	    }

	    private boolean isTokenExpired(String token) {
	        Claims claims = Jwts.parserBuilder()
	                .setSigningKey(key)
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	        Date expiration = claims.getExpiration();
	        return expiration.before(new Date());
	    }
}
