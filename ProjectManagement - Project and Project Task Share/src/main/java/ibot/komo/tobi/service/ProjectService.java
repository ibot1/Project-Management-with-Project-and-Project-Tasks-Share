package ibot.komo.tobi.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ibot.komo.tobi.exception.ProjectIdException;
import ibot.komo.tobi.exception.ProjectNotFoundException;
import ibot.komo.tobi.exception.UsernameAlreadyExistsException;
import ibot.komo.tobi.models.ProjectAccess;
import ibot.komo.tobi.models.Projects;
import ibot.komo.tobi.models.Users;
import ibot.komo.tobi.repository.ProjectAccessRepository;
import ibot.komo.tobi.repository.ProjectRepository;
import ibot.komo.tobi.repository.UserRepository;

@Service
public class ProjectService {
	
	@Autowired
	ProjectRepository projectRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ProjectAccessRepository projectAccessRepository;
	
	public Projects createUpdateProject(Projects project, String username) {
		String projectIdentifier = project.getProjectIdentifier().toUpperCase();
		project.setProjectIdentifier(projectIdentifier);
		username = username.toUpperCase();
		Users user = userRepository.findByUsername(username);
		
		if( user == null)	throw new UsernameAlreadyExistsException("Username: '" + username + "' hasn't been registered!");
		
		if(projectRepository.findByProjectIdentifier(projectIdentifier) != null ) {
			if( projectAccessRepository.findByUsernameAndProjectIdentifier(username, projectIdentifier) == null)	throw new ProjectIdException("Project ID: '" + projectIdentifier + "' already assigned to a Project and you dont have access to it!");
			else	throw new ProjectIdException("Project ID '" + project.getProjectIdentifier().toUpperCase() + "' already exists");
		}else {
			project.setProjectLeader(username); 
			project.setUsers(user);
			ProjectAccess projectAccess = new ProjectAccess(); 
			projectAccess.setUsername(username);
			projectAccess.setProjectIdentifier(projectIdentifier);
			projectAccess.setProject(project);
			List<ProjectAccess> tmp = project.getProjectAccess();
			tmp.add(projectAccess);
			project.setProjectAccess(tmp);
		}
		return projectRepository.save(project);
	}
	
	public Projects getProject(String projectIdentifier, String username) {
		username = username.toUpperCase();
		projectIdentifier = projectIdentifier.toUpperCase();
		
		if(userRepository.findByUsername(username) == null)	throw new UsernameAlreadyExistsException("Username: '" + username + "' hasn't been registered!");
		
		if(projectRepository.findByProjectIdentifier(projectIdentifier)!= null) {
			if(projectAccessRepository.findByUsernameAndProjectIdentifier(username,projectIdentifier) == null)	throw new ProjectIdException("Project ID: '" + projectIdentifier + "' already assigned to a Project and you dont have access to it!");
		}
		return projectRepository.findByProjectIdentifier(projectIdentifier);
	}
	
	public Iterable<Projects> getAllProjects(String username){
		username = username.toUpperCase();
		
		if(userRepository.findByUsername(username) == null)	throw new UsernameAlreadyExistsException("Username: '" + username + "' hasn't been registered!");
		
		if(projectAccessRepository.findByUsername(username) == null)	throw new ProjectNotFoundException("You dont have access to any Project!");
		
		Iterable<ProjectAccess> projectAccess = projectAccessRepository.findByUsername(username);
		List<Projects> projects = new ArrayList<Projects>();
		
		for(ProjectAccess tmp : projectAccess)	projects.add(tmp.getProject());
		
		return projects;
	}
	
	public Boolean deleteProject(String projectIdentifier, String username) {
		username = username.toUpperCase();
		projectIdentifier = projectIdentifier.toUpperCase();
		
		if(userRepository.findByUsername(username) == null)	throw new UsernameAlreadyExistsException("Username '" + username + "' hasn't been registered!");
		
		Projects projects = projectRepository.findByProjectIdentifier(projectIdentifier);
		
		if( projects == null)	throw new ProjectNotFoundException("Project ID: ' " + projectIdentifier + "' isnt associated to any project!");
		if(!projects.getProjectLeader().toUpperCase().equals(username))		return false;
		
		projectRepository.delete(projects);
		return true;
	}
	
	public Boolean shareProject(String projectIdentifier, String receiver, String sender) {
		projectIdentifier = projectIdentifier.toUpperCase();
		System.out.println(receiver + " " +  sender);
		receiver = receiver.toUpperCase();
		sender = sender.toUpperCase();
		Projects project = projectRepository.findByProjectIdentifier(projectIdentifier);
		
		if(project == null)	throw new ProjectNotFoundException("Project ID: ' " + projectIdentifier + "' isnt associated to any project!");
		if(userRepository.findByUsername(sender) == null || userRepository.findByUsername(receiver) == null)	throw new UsernameAlreadyExistsException("Username '" + sender + "'  or '" + receiver +" hasn't been registered!");
		if(!project.getProjectLeader().equals(sender))	throw new UsernameAlreadyExistsException("Only Project creators can delete and share Projects!");
		if(projectAccessRepository.findByUsernameAndProjectIdentifier(receiver, projectIdentifier) != null)		throw new ProjectNotFoundException("Project has  already been shared with '" + receiver + "' !");

		ProjectAccess projectAccess = new ProjectAccess();
		projectAccess.setProject(project);
		projectAccess.setProjectIdentifier(projectIdentifier);
		projectAccess.setUsername(receiver);
		//back-setting projectAccess for project
		projectAccessRepository.save(projectAccess);
		
		return true;
	}
	
	
}
