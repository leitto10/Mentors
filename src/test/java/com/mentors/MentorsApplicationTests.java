package com.mentors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mentors.dto.MentorDTO;
import com.mentors.dto.ProjectDTO;
import com.mentors.entity.Mentor;
import com.mentors.entity.Project;
import com.mentors.exception.MentorException;
import com.mentors.repository.MentorRepository;
import com.mentors.repository.ProjectRepository;
import com.mentors.service.ProjectAllocationService;
import com.mentors.service.ProjectAllocationServiceImpl;


@ExtendWith(MockitoExtension.class)
class MentorsApplicationTests {
	private static final Log LOGGER = LogFactory.getLog(MentorsApplicationTests.class);
		
	@Mock
	MentorRepository mentorRepo;
	@Mock
	ProjectRepository projectRepo;
	@InjectMocks
	ProjectAllocationService projectService = new ProjectAllocationServiceImpl();
	
	List<Project> projects = new ArrayList<>();
	
	Project project = new Project();
	
	@BeforeEach
	public void pupulateEntity() {
		Mentor mentor = new Mentor();
		mentor.setNumberOfProjectsMentored(2);
		
		project.setIdeaOwner(100011);
		project.setMentor(mentor);
		project.setProjectId(20);
		project.setProjectName("Unit Test");
		project.setReleaseDate(LocalDate.of(2020, 06, 16));
		
	}
	
	public ProjectDTO populateDTO() {
		ProjectDTO projectDTO = new ProjectDTO();
		projectDTO.setProjectId(20);
		projectDTO.setIdeaOwner(100011);
		projectDTO.setProjectName("Unit Test");
		projectDTO.setReleaseDate(LocalDate.of(2020, 06, 16));
		
		MentorDTO mentorDTO = new MentorDTO();
		mentorDTO.setMentorId(1000);
		projectDTO.setMentorDTO(mentorDTO);
		
		return projectDTO;
	}
	
	@Test
	public void allocateProject() throws MentorException {
//		Mockito.when(projectRepo.save(any(Project.class))).thenReturn(new Project());
		Mockito.when(projectRepo.save(project)).thenReturn(project);
		ProjectDTO projectDTO = populateDTO();
		projectService.allocateProject(projectDTO);
		
//		assertEquals(20, actual);
		verify(projectRepo, times(1)).save(any());
//		verifyNoMoreInteractions(projectRepo);
		
	}
	
	
	public void allocateProjectCannotAllocateTest() throws Exception {
		ProjectDTO projectDTO = new ProjectDTO();
		projectDTO.setIdeaOwner(10009);
		projectDTO.setProjectName("Android ShoppingApp");
		projectDTO.setReleaseDate(LocalDate.of(2019, 9, 27));
		
		MentorDTO mentorDTO = new MentorDTO();
		mentorDTO.setMentorId(1000);
		projectDTO.setMentorDTO(mentorDTO);
		
		
		Mentor mentor = new Mentor();
		mentor.setNumberOfProjectsMentored(5);
		
		when(mentorRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(mentor));
		MentorException exception = Assertions.assertThrows(MentorException.class, 
				() -> projectService.allocateProject(projectDTO));
		
		assertEquals("Service.CANNOT_ALLOCATE_PROJECT", exception.getMessage());
	}
	
	
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
		
//		projectRepo.findAll();
//		doReturn(projects).when(projectRepo).findAll();
		
//		assertThat(projects).hasSize(12);
//		assertEquals(12, projects.size());
//		verify(projectRepo, times(1)).findAll();
		
//		when(projectRepo.findAll()).thenReturn(new ArrayList<>());
//		verify(projectRepo, times(1)).findAll();
//		projectService.getAllProjects();
//      verify(projectRepo).findAll();
	}

}
