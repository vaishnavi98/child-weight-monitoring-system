package com.doctolib.childweight.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.doctolib.childweight.domain.Child;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {
	@Query("select DISTINCT c from Child c LEFT JOIN FETCH c.weights WHERE c.id = :id")
	Optional<Child> findByIdWithWeight(@Param("id") Long id);
	
	@Query("select DISTINCT c from Child c LEFT JOIN FETCH c.weights")
	List<Child> findAllWithWeights();

	boolean existsByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndDateOfBirth(
			@NotBlank(message = "Firstname should not be blank") String firstName,
			@NotBlank(message = "Lastname should not be blank") String lastName,
			@NotNull(message = "DOB should not be null") @PastOrPresent(message = "Date Of Birth cannot be in the future") 
			LocalDateTime dateOfBirth);
}