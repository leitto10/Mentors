package com.mentors.repository;

import org.springframework.data.repository.CrudRepository;

import com.mentors.entity.Project;

public interface ProjectRepository extends CrudRepository<Project, Integer> {

}
