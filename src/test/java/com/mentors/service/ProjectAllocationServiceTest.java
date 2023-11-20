package com.mentors.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.given;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.test.context.SpringBootTest;

import com.mentors.dto.MentorDTO;
import com.mentors.dto.ProjectDTO;
import com.mentors.entity.Mentor;
import com.mentors.entity.Project;
import com.mentors.exception.MentorException;
import com.mentors.repository.MentorRepository;
import com.mentors.repository.ProjectRepository;
import com.mentors.rowmappers.ProjectsToList;




class ProjectAllocationServiceTest {
	private static final Log LOGGER = LogFactory.getLog(ProjectAllocationServiceTest.class);
	
	@Mock
	MentorRepository mentorRepo;
	@Mock
	ProjectRepository projectRepo;
	
	@InjectMocks
	ProjectAllocationService projectService = new ProjectAllocationServiceImpl();
	
	List<Project> projects = new ArrayList<>();
	
	
	
	public void getProjects() throws MentorException {
//		String currDirectory = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
//		File file = new File(currDirectory, "projects.json");
//		JSONArray jsonArray = null;
//		
//		if(file.exists()) {
//			LOGGER.info("File is present in directory.");
//			try {
//				InputStream input = new FileInputStream(file);
//				String json = IOUtils.toString(input, "UTF-8");
//				jsonArray = new JSONArray(json);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}else {
//			LOGGER.info("File doesn't exits.");
//		}
//		
//		ProjectsToList mapper = new ProjectsToList();
//		projects = mapper.jsonArrayToList(jsonArray);
		
		projectRepo.findAll();
//		doReturn(projects).when(projectRepo).findAll();
		
		assertThat(projects).hasSize(12);
		assertEquals(12, projects.size());
		verify(projectRepo, times(1)).findAll();
		
//		when(projectRepo.findAll()).thenReturn(new ArrayList<>());
//		verify(projectRepo, times(1)).findAll();
//		projectService.getAllProjects();
//      verify(projectRepo).findAll();
	}
	
	
	public void allocateProject() throws MentorException {
		ProjectDTO projectDTO = new ProjectDTO();
		projectDTO.setProjectId(20);
		projectDTO.setIdeaOwner(100011);
		projectDTO.setProjectName("Unit Test");
		projectDTO.setMentorDTO(null);
		projectDTO.setReleaseDate(LocalDate.of(2020, 06, 16));
		
		MentorDTO mentorDTO = new MentorDTO();
		mentorDTO.setMentorId(2000);
		projectDTO.setMentorDTO(mentorDTO);
		
		Mentor mentor = new Mentor();
		mentor.setNumberOfProjectsMentored(2);
		
		when(mentorRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(mentor));
		Integer actual = projectService.allocateProject(projectDTO);
		
		Project project = new Project();
		project.setIdeaOwner(projectDTO.getIdeaOwner());
		project.setMentor(mentor);
		project.setProjectId(projectDTO.getProjectId());
		project.setProjectName(projectDTO.getProjectName());
		project.setReleaseDate(projectDTO.getReleaseDate());
		
		assertEquals(20, actual);
//		verify(projectRepo, times(1)).save(project);
		verifyNoMoreInteractions(projectRepo);
		
		
	}

}
