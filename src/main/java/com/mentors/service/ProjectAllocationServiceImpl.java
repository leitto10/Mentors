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
		project.setProjectName(projectDTO.getProjectName());
		project.setReleaseDate(projectDTO.getReleaseDate());
		
		mentor.setNumberOfProjectsMentored(mentor.getNumberOfProjectsMentored() + 1);
		project.setMentor(mentor);
		
		return projectRepo.save(project).getProjectId();
	}

	
	@Override
	public List<MentorDTO> getMentors(Integer numberOfProjectsMentored) throws MentorException {
		List<Mentor> getMentors = mentorRepo.findNumberOfProjectsMentored(numberOfProjectsMentored);
		
		if(getMentors.isEmpty()) {
			throw new MentorException("Service.MENTOR_NOT_FOUND");
		}
		
		List<MentorDTO> mentors = new ArrayList<>();
		for(Mentor mentor: getMentors) {
			MentorDTO mentorDTO = new MentorDTO();
			mentorDTO.setMentorId(mentor.getMentorId());
			mentorDTO.setMentorName(mentor.getMentorName());
			mentorDTO.setNumberOfProjectsMentored(mentor.getNumberOfProjectsMentored());
			
			mentors.add(mentorDTO);
		}
		
		return mentors;
	}
	
	
	@Override
	public List<ProjectDTO> getAllProjects() throws MentorException {
		List<Project> projects = (List<Project>) projectRepo.findAll();
		
		if(projects.isEmpty()) {
			throw new MentorException("Service.PROJECT_NOT_FOUND");
		}
		
		List<ProjectDTO> projectsDTO = new ArrayList<>();
		for(Project project: projects) {
			ProjectDTO projectDTO = new ProjectDTO();
			projectDTO.setIdeaOwner(project.getIdeaOwner());
			projectDTO.setProjectId(project.getProjectId());
			projectDTO.setProjectName(project.getProjectName());
			projectDTO.setReleaseDate(project.getReleaseDate());
			
			if(project.getMentor() != null) {
				MentorDTO mentor = new MentorDTO();
				mentor.setMentorId(project.getMentor().getMentorId());
				mentor.setMentorName(project.getMentor().getMentorName());
				mentor.setNumberOfProjectsMentored(project.getMentor().getNumberOfProjectsMentored());
				
				projectDTO.setMentorDTO(mentor);
			}
			
			projectsDTO.add(projectDTO);
		}
		
		
		return projectsDTO;
	}


	@Override
	public void updateProjectMentor(Integer projectId, Integer mentorId) throws MentorException {
		Mentor mentor = mentorRepo.findById(mentorId)
				.orElseThrow(() -> new MentorException("Service.MENTOR_NOT_FOUND"));
		
		if(mentor.getNumberOfProjectsMentored() >= 3) {
			throw new MentorException("Service.CANNOT_ALLOCATE_PROJECT");
		}
		
		Project project = projectRepo.findById(projectId)
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


}
