package com.doctolib.childweight.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.doctolib.childweight.domain.HealthcareProvider;
import com.doctolib.childweight.repository.HealthcareProviderRepository;

@Service
public class AuthService {

	private final HealthcareProviderRepository providerRepo;
	private final PasswordEncoder passwordEncoder;
	 private final JwtService jwtService;

	public AuthService(HealthcareProviderRepository providerRepo, PasswordEncoder passwordEncoder, JwtService jwtService) {
		this.providerRepo = providerRepo;
		this.passwordEncoder = passwordEncoder;
		this.jwtService =jwtService;
	}

	public HealthcareProvider register(String name, String facilityId, String professionalId, String passwordHash) {
		String hashedPass = passwordEncoder.encode(passwordHash);
		HealthcareProvider provider = new HealthcareProvider(name, facilityId, professionalId, hashedPass);
		return providerRepo.save(provider);
	}

	public String login(String professionalId, String rawPassword) {
		HealthcareProvider provider = providerRepo.findByProfessionalId(professionalId)
				.orElseThrow(() -> new RuntimeException("Invalid ID"));
		if(!passwordEncoder.matches(rawPassword, provider.getPasswordHash())) {
			throw new RuntimeException("Invalid credentials");
		}
		
		return jwtService.generateToken(provider);
	}
}
