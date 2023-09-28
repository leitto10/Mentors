package com.mentors.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.mentors.entity.Mentor;

public interface MentorRepository extends CrudRepository<Mentor, Integer> {

	@Query("SELECT m FROM Mentor m WHERE m.numberOfProjectsMentored = ?1")
	List<Mentor> findNumberOfProjectsMentored(Integer numberOfProjectsMentored);
}
