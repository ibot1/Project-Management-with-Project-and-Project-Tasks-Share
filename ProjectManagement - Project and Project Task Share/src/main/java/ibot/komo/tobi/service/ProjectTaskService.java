package ibot.komo.tobi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ibot.komo.tobi.exception.ProjectIdException;
import ibot.komo.tobi.exception.ProjectNotFoundException;
import ibot.komo.tobi.exception.UsernameAlreadyExistsException;
import ibot.komo.tobi.models.ProjectAccess;
import ibot.komo.tobi.models.ProjectTasks;
import ibot.komo.tobi.models.ProjectTasksAccess;
import ibot.komo.tobi.models.Projects;
import ibot.komo.tobi.repository.ProjectAccessRepository;
import ibot.komo.tobi.repository.ProjectRepository;
import ibot.komo.tobi.repository.ProjectTaskAccessRepository;
import ibot.komo.tobi.repository.ProjectTaskRepository;
import ibot.komo.tobi.repository.UserRepository;


@Service
public class ProjectTaskService {
	
	@Autowired
	ProjectTaskRepository projectTaskRepository;
	
	@Autowired
	ProjectRepository projectRepository;
	
	@Autowired
	ProjectAccessRepository projectAccessRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ProjectTaskAccessRepository projectTaskAccessRepository;
	
	
	public ProjectTasks addProjectTask(String projectId, ProjectTasks projectTask, String username) {
		//status,
		//check if the user exist
		//check if the project exist 
		//check if you have project access
		//add to the project_task_access table and set a counter
		//add to project_task table
		
		projectId = projectId.toUpperCase();
		username = username.toUpperCase();
		Projects project = projectRepository.findByProjectIdentifier(projectId);		
		
		if(project == null)	throw new ProjectNotFoundException("Project with ID: '"+ projectId + "' wasnt found!");

		final int counter = project.getCounter() + 1;
		final String projectSequence = projectId + "-" + String.valueOf(counter);

		if(projectAccessRepository.findByUsernameAndProjectIdentifier(username, projectId) == null)		throw new ProjectIdException("You dont have access to the Project with ID '" + projectId + "' !");
		if(projectTaskRepository.findByProjectSequence(projectSequence) != null)	throw new ProjectNotFoundException("Project-Task with ID: '"+ projectSequence + "' was already assigned!");

		projectTask.setProjectSequence(projectSequence);
		projectTask.setProjects(project);
		
		if(projectTask.getPriority() == 0)		projectTask.setPriority(3);
		if(projectTask.getStatus() == "" || projectTask.getStatus() == null)	projectTask.setStatus("TO_DO");
		
		ProjectTasksAccess projectTasksAccess = new ProjectTasksAccess();
		projectTasksAccess.setProjectSequence(projectSequence);
		projectTasksAccess.setUsername(username);
		projectTasksAccess.setProjectTasks(projectTask);
		projectTasksAccess.setProjectIdentifier(projectId);
		List<ProjectTasksAccess> tmp = projectTask.getProjectTasksAccess();
		tmp.add(projectTasksAccess);
		projectTask.setProjectTasksAccess(tmp);
		project.setCounter(counter);
		
		return projectTaskRepository.save(projectTask);
		
	}
	
	public List<ProjectTasks> getAllProjectTask(String projectId, String username){
		projectId = projectId.toUpperCase();
		username = username.toUpperCase();
		Projects project = projectRepository.findByProjectIdentifier(projectId);
		List<ProjectTasks> projectTasks = new ArrayList<ProjectTasks>();
		
		if(userRepository.findByUsername(username) == null)		throw new UsernameAlreadyExistsException("Username: '" + username + "' hasn't been registered!");
		if(projectAccessRepository.findByUsernameAndProjectIdentifier(username, projectId) == null)	throw new ProjectNotFoundException("You dont have access to Project with ID '" + projectId + "' or it doesnt exist!");
		if(project.getProjectLeader().equals(username))		projectTasks = projectTaskRepository.findAllByProjects(project);
		else {
			List<ProjectTasksAccess> projectTasksAccess = projectTaskAccessRepository.findAllByProjectIdentifierAndUsername(projectId, username);	
			for(ProjectTasksAccess tmp : projectTasksAccess) 	projectTasks.add(tmp.getProjectTasks());
		}
				
		return projectTasks;
	}
	
	public ProjectTasksAccess getProjectTask(String projectId, String projectSequence, String username) {
		projectId = projectId.toUpperCase();
		projectSequence = projectSequence.toUpperCase();
		username = username.toUpperCase();
		Projects project = projectRepository.findByProjectIdentifier(projectId);
		if(userRepository.findByUsername(username) == null)		throw new UsernameAlreadyExistsException("Username: '" + username + "' hasn't been registered!");
		if(projectAccessRepository.findByUsernameAndProjectIdentifier(username, projectId) == null)	throw new ProjectNotFoundException("You dont have access to Project with ID '" + projectId + "' !");
		if(project.getProjectLeader().equals(username))	return projectTaskAccessRepository.findByProjectSequence(projectSequence);
		
		return projectTaskAccessRepository.findByProjectSequenceAndUsername(projectSequence, username);
	}
	
	public ProjectTasks updateProjectTask(String projectId, String projectSequence, String username, ProjectTasks projectTask) {
		projectId = projectId.toUpperCase();
		projectSequence = projectSequence.toUpperCase();
		username = username.toUpperCase();
		Projects projects = projectRepository.findByProjectIdentifier(projectId);
		
		if(projects == null)	throw new ProjectNotFoundException("Project with ID: '"+ projectId + "' wasnt found!");
		if(userRepository.findByUsername(username) == null)		throw new UsernameAlreadyExistsException("Username: '" + username + "' hasn't been registered!");
		if(projectAccessRepository.findByUsernameAndProjectIdentifier(username, projectId) == null)		throw new ProjectNotFoundException("You dont have access to Project with ID '" + projectId + "' !");
		if(projectTaskAccessRepository.findByProjectSequenceAndUsername(projectSequence, username) == null)		throw new ProjectNotFoundException("You dont have access to the project-task with sequence-number '" + projectSequence + "' or the projectTask doesnt exist!");
		
		if(projectTask.getPriority() == 0)		projectTask.setPriority(3);
		if(projectTask.getStatus() == "" || projectTask.getStatus() == null)	projectTask.setStatus("TO_DO");
		
		ProjectTasks updated = projectTaskRepository.findByProjectSequence(projectSequence);
		updated = projectTask;
		return projectTaskRepository.save(updated);
	}
	
	public Boolean deleteProjectTask(String projectId, String projectSequence, String username) {
		projectId = projectId.toUpperCase();
		projectSequence = projectSequence.toUpperCase();
		username = username.toUpperCase();
		ProjectTasks projectTask = projectTaskRepository.findByProjectSequence(projectSequence);
		
		if(projectTask == null)		throw new ProjectNotFoundException("Project Task with id '" + projectSequence + "' doesnt exist");
		if(projectRepository.findByProjectIdentifier(projectId) == null)	throw new ProjectNotFoundException("Project with ID: '"+ projectId + "' wasnt found!");
		if(userRepository.findByUsername(username) == null)		throw new UsernameAlreadyExistsException("Username: '" + username + "' hasn't been registered!");
		if(projectAccessRepository.findByUsernameAndProjectIdentifier(username, projectId) == null)		throw new ProjectNotFoundException("You dont have access to Project with ID '" + projectId + "' !");
		if(projectTaskAccessRepository.findByProjectSequenceAndUsername(projectSequence, username) == null)		throw new ProjectNotFoundException("You dont have access to the project-task with sequence-number '" + projectSequence + "' or the projectTask doesnt exist!");
		
		projectTaskRepository.delete(projectTask);
		return true;
	}
	
	//projectId, sender, receiver, projectSequence
	public Boolean shareProjectTask(String projectId, String sender, String receiver, String projectSequence) {
		projectId = projectId.toUpperCase();
		projectSequence = projectSequence.toUpperCase();
		receiver = receiver.toUpperCase();
		sender = sender.toUpperCase();
		ProjectTasks projectTask = projectTaskRepository.findByProjectSequence(projectSequence);
		ProjectAccess projectAccess = projectAccessRepository.findByUsernameAndProjectIdentifier(sender, projectId);
		ProjectAccess projectAccess2 = new ProjectAccess(projectAccess);
		projectAccess2.setUsername( receiver);
		
		ProjectTasksAccess projectTasksAccess = new ProjectTasksAccess();
		
		if(projectTask == null)		throw new ProjectNotFoundException("Project Task with id '" + projectSequence + "' doesnt exist");
		if(projectRepository.findByProjectIdentifier(projectId) == null)	throw new ProjectNotFoundException("Project with ID: '"+ projectId + "' wasnt found!");
		if(userRepository.findByUsername(receiver) == null || userRepository.findByUsername(sender) == null)		throw new UsernameAlreadyExistsException("Usernames : '" + sender + "' or" + "'" + receiver + "' hasn't been registered!");
		if(projectAccess == null)		throw new ProjectNotFoundException("You dont have access to Project with ID '" + projectId + "' !");
		if(projectTaskAccessRepository.findByProjectSequenceAndUsername(projectSequence, sender) == null)		throw new ProjectNotFoundException("You dont have access to the project-task with sequence-number '" + projectSequence + "' or the projectTask doesnt exist!");
		if(projectTaskAccessRepository.findByProjectSequenceAndUsername(projectSequence, receiver) != null)		throw new ProjectNotFoundException("You already have access to the project-task with sequence-number '" + projectSequence + "' or the projectTask doesnt exist!");
		if(projectAccessRepository.findByUsernameAndProjectIdentifier(receiver, projectId) == null)		projectAccessRepository.save(projectAccess2);
			
		projectTasksAccess.setProjectSequence(projectSequence);
		projectTasksAccess.setProjectIdentifier(projectId);
		projectTasksAccess.setUsername(receiver);
		projectTasksAccess.setProjectTasks(projectTask);
		projectTaskAccessRepository.save(projectTasksAccess);
		return true;
	}
}
