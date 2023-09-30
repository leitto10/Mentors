package com.mentors.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mentors.dto.MentorDTO;
import com.mentors.dto.ProjectDTO;
import com.mentors.entity.Mentor;
import com.mentors.entity.Project;
import com.mentors.exception.MentorException;
import com.mentors.repository.MentorRepository;
import com.mentors.repository.ProjectRepository;

import jakarta.transaction.Transactional;


@Transactional
@Service("projectService")
public class ProjectAllocationServiceImpl implements ProjectAllocationService {
	
	
	@Autowired
	private MentorRepository mentorRepo;
	
	@Autowired
	private ProjectRepository projectRepo;
	

	@Override
	public Integer allocateProject(ProjectDTO projectDTO) throws MentorException {
		Mentor mentor = mentorRepo.findById(projectDTO.getMentorDTO().getMentorId())
				.orElseThrow(() -> new MentorException("Service.MENTOR_NOT_FOUND"));
		
		if(mentor.getNumberOfProjectsMentored() >= 3) {
			throw new MentorException("Service.CANNOT_ALLOCATE_PROJECT");
		}
		
		Project project = new Project();
		project.setIdeaOwner(projectDTO.getIdeaOwner());
		project.setProjectId(projectDTO.getProjectId());
		project.setProjectName(projectDTO.getProjectName());
		project.setReleaseDate(projectDTO.getReleaseDate());
		
		mentor.setNumberOfProjectsMentored(mentor.getNumberOfProjectsMentored() + 1);
		project.setMentor(mentor);
		
		return projectRepo.save(project).getProjectId();
	}

	@Override
	public void updateProjectMentor(Integer projectId, Integer mentorId) throws MentorException {
		Mentor mentor = mentorRepo.findById(mentorId)
				.orElseThrow(() -> new MentorException("Service.MENTOR_NOT_FOUND"));
		
		if(mentor.getNumberOfProjectsMentored() >= 3) {
			throw new MentorException("Service.CANNOT_ALLOCATE_PROJECT");
		}
		
		Project project = projectRepo.findById(mentorId)
				.orElseThrow(() -> new MentorException("Service.PROJECT_NOT_FOUND"));
		
		project.setMentor(mentor);
		mentor.setNumberOfProjectsMentored(mentor.getNumberOfProjectsMentored() + 1);
		
	}

	@Override
	public void deleteProject(Integer projectId) throws MentorException {
		Project project = projectRepo.findById(projectId)
				.orElseThrow(() -> new MentorException("Service.PROJECT_NOT_FOUND"));
		
		if(project.getMentor() != null) {
			Mentor mentor = project.getMentor();
			mentor.setNumberOfProjectsMentored(mentor.getNumberOfProjectsMentored() - 1);
			project.setMentor(null);
		}
		
		projectRepo.delete(project);
	}

	@Override
	public List<MentorDTO> getMentors() throws MentorException {
		List<MentorDTO> mentors = new ArrayList<>();
		List<Mentor> getMentors = (List<Mentor>) mentorRepo.findAll();
		
		if(getMentors.isEmpty()) {
			throw new MentorException("Service.MENTOR_NOT_FOUND");
		}
		
		for(Mentor m: getMentors) {
			MentorDTO mentor = new MentorDTO();
			mentor.setMentorId(m.getMentorId());
			mentor.setMentorName(m.getMentorName());
			mentor.setNumberOfProjectsMentored(m.getNumberOfProjectsMentored());
			mentors.add(mentor);
		}
		
		return mentors;
	}

	@Override
	public List<ProjectDTO> getAllProjects() throws MentorException {
		List<ProjectDTO> projects = new ArrayList<>();
		List<Project> getProjects = (List<Project>) projectRepo.findAll();
		
		if(getProjects.isEmpty()) {
			throw new MentorException("Service.PROJECT_NOT_FOUND");
		}
		
		for(Project p: getProjects) {
			ProjectDTO project = new ProjectDTO();
			project.setIdeaOwner(p.getIdeaOwner());
			project.setProjectId(p.getProjectId());
			project.setProjectName(p.getProjectName());
			project.setReleaseDate(p.getReleaseDate());
			
			if(p.getMentor() != null) {
				MentorDTO mentor = new MentorDTO();
				mentor.setMentorId(p.getMentor().getMentorId());
				mentor.setMentorName(p.getMentor().getMentorName());
				mentor.setNumberOfProjectsMentored(p.getMentor().getNumberOfProjectsMentored());
				
				project.setMentorDTO(mentor);
			}
			
			projects.add(project);
		}
		
		return projects;
	}

	@Override
	public List<ProjectDTO> getProjectsByMentorId(Integer mentorId) throws MentorException {
		// WE NEED TO LOOK INTO THIS QUERY
		List<ProjectDTO> projects = new ArrayList<>();
		List<Project> getProjects = (List<Project>) projectRepo.findAll();
		
		Mentor mentor = mentorRepo.findById(mentorId)
				.orElseThrow(() -> new MentorException("Service.MENTOR_NOT_FOUND"));
		
		if(getProjects.isEmpty()) {
			throw new MentorException("Service.PROJECT_NOT_FOUND");
		}
		
		for(Project p: getProjects) {
			if(p.getMentor().getMentorId() == mentorId) {
				ProjectDTO project = new ProjectDTO();
				project.setIdeaOwner(p.getIdeaOwner());
				project.setProjectId(p.getProjectId());
				project.setProjectName(p.getProjectName());
				project.setReleaseDate(p.getReleaseDate());
				projects.add(project);
			}
		}
		
		return projects;
	}

}
