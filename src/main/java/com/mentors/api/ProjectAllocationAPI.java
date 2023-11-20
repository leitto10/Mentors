package com.mentors.api;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mentors.dto.MentorDTO;
import com.mentors.dto.ProjectDTO;
import com.mentors.exception.MentorException;
import com.mentors.service.ProjectAllocationService;


@Validated
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProjectAllocationAPI {

	@Autowired
	private ProjectAllocationService projectService;
	
	@Autowired
	private Environment enviroment;
	
	
	@GetMapping("mentor/{mentorId}")
	public ResponseEntity<MentorDTO> getMentor(@PathVariable Integer mentorId) throws MentorException {
		return new ResponseEntity<MentorDTO>(projectService.getMentor(mentorId), HttpStatus.OK);
	}
    
	@PostMapping("project")
    public ResponseEntity<String> allocateProject(@Valid @RequestBody ProjectDTO projectDTO) throws MentorException {
		Integer projectId = projectService.allocateProject(projectDTO);
		String message = enviroment.getProperty("API.ALLOCATION_SUCCESS");
		return new ResponseEntity<String>(message + ": "+projectId, HttpStatus.CREATED);
    }
	
	@GetMapping("projects")
	public ResponseEntity<List<ProjectDTO>> getProjects() throws MentorException {
		List<ProjectDTO> projects = projectService.getAllProjects();
		return new ResponseEntity<List<ProjectDTO>>(projects, HttpStatus.OK);
	}

	@GetMapping("mentors/{numberOfProjectsMentored}")
    public ResponseEntity<List<MentorDTO>> getMentors(@PathVariable Integer numberOfProjectsMentored) throws MentorException {
		List<MentorDTO> mentors = projectService.getMentors(numberOfProjectsMentored);
		return new ResponseEntity<List<MentorDTO>>(mentors, HttpStatus.OK);
    }

	@PutMapping("project/{projectId}/{mentorId}") 
    public ResponseEntity<String> updateProjectMentor(@PathVariable Integer projectId,
    		@PathVariable
    		@Min(value=1000, message="{mentor.mentorid.invalid}")
    		@Max(value=9999, message="{mentor.mentorid.invalid}")
    		Integer mentorId) throws MentorException {
		projectService.updateProjectMentor(projectId, mentorId);
		String message = enviroment.getProperty("API.PROJECT_UPDATE_SUCCESS");
		return new ResponseEntity<String>(message, HttpStatus.OK);
    }

	@DeleteMapping("project/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable Integer projectId) throws MentorException {
		projectService.deleteProject(projectId);
		String message = enviroment.getProperty("API.PROJECT_DELETE_SUCCESS");
	return new ResponseEntity<String>(message, HttpStatus.OK);
    }
	
}


