package com.mentors.rowmappers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import com.mentors.model.Project;



// Takes JSON array and maps the objects into a list
public class ProjectsToList {

	public List<Project> jsonArrayToList(JSONArray jsonArray) throws JSONException {
		List<Project> projects = new ArrayList<>();
		
		
		for(int i=0; i<jsonArray.length(); i++) {
			JSONObject current = jsonArray.getJSONObject(i);
			Project project = new Project();
			LocalDate releaseDate;
			
			project.setProjectId(current.getInt("projectId"));
			project.setProjectName(current.getString("projectName"));
			
			releaseDate = LocalDate.parse(current.getString("releaseDate"));
			project.setReleaseDate(releaseDate);
			
			project.setIdeaOwner(current.getInt("ideaOwner"));
			project.setMentor(null);
			
			projects.add(project);
			
		}
		
		return projects;
	}
}
