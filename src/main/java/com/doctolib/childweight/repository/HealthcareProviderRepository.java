package com.doctolib.childweight.repository;

import com.doctolib.childweight.domain.HealthcareProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthcareProviderRepository extends JpaRepository<HealthcareProvider, Long> {}