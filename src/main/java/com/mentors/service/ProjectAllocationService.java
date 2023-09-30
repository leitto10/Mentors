package com.mentors.service;

import java.util.List;

import com.mentors.dto.MentorDTO;
import com.mentors.dto.ProjectDTO;
import com.mentors.exception.MentorException;


public interface ProjectAllocationService {
	
	public Integer allocateProject(ProjectDTO projectDTO) throws MentorException;

	public void updateProjectMentor(Integer projectId, Integer mentorId) throws MentorException;
	
	public void deleteProject(Integer projectId) throws MentorException;

	public List<MentorDTO> getMentors() throws MentorException;
	
	public List<ProjectDTO> getAllProjects() throws MentorException;

	public List<ProjectDTO> getProjectsByMentorId(Integer mentorId) throws MentorException;

}
