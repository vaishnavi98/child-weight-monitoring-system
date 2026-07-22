package com.doctolib.childweight.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doctolib.childweight.domain.HealthcareProvider;

@Repository
public interface HealthcareProviderRepository extends JpaRepository<HealthcareProvider, Long> {
    Optional<HealthcareProvider> findByProfessionalId(String professionalId);
}