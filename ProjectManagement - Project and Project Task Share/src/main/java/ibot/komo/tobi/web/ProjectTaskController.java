package ibot.komo.tobi.web;
import ibot.komo.tobi.service.MapValidationErrorService;
import ibot.komo.tobi.service.ProjectTaskService;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ibot.komo.tobi.exception.ProjectNotFoundException;
import ibot.komo.tobi.models.ProjectTasks;
import ibot.komo.tobi.models.ProjectTasksAccess;

@RestController 
@RequestMapping("/api/backlog")
@CrossOrigin
public class ProjectTaskController {
	

	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
	@Autowired
	private ProjectTaskService projectTaskService;
	
	@PostMapping("/{projectIdentifier}")
	public ResponseEntity<?> addProjectTask(@Valid @RequestBody ProjectTasks projectTask, BindingResult result, @PathVariable String projectIdentifier, Principal principal){
		ResponseEntity<?> erroMap = mapValidationErrorService.MapValidationService(result);
		
		if(erroMap != null)		return erroMap;
		
		return new ResponseEntity<ProjectTasks>(projectTaskService.addProjectTask(projectIdentifier, projectTask, principal.getName()), HttpStatus.OK);
	}
	
	@GetMapping("/{projectId}")
	public ResponseEntity<?> getAllProjectTask(@Valid @PathVariable String projectId, Principal principal){
		List<ProjectTasks> projectTasks = projectTaskService.getAllProjectTask(projectId, principal.getName());
		if(projectTasks == null) throw new ProjectNotFoundException(" You dont have access to any Project Task in the Project with ID'" + projectId + "' or there isnt any project Task in the project");
		
		return new ResponseEntity<List<ProjectTasks>>(projectTasks, HttpStatus.OK);
	}
	
	@GetMapping("/{projectId}/{projectSequence}")
	public ResponseEntity<?> findProjectTask(@Valid @PathVariable String projectId, @PathVariable String projectSequence, Principal principal){
		ProjectTasksAccess projectTasksAccess = projectTaskService.getProjectTask( projectId,  projectSequence,  principal.getName());
		if(projectTasksAccess == null)	throw new ProjectNotFoundException("You dont have access to the project-task with sequence-number '" + projectSequence + "' or the projectTask doesnt exist!");
		
		return new ResponseEntity<ProjectTasks>(projectTasksAccess.getProjectTasks(), HttpStatus.OK);
	}
	    
	@PatchMapping("/{projectId}/{projectSequence}")
	public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTasks projectTask, BindingResult result,
			  										@PathVariable String projectId, @PathVariable String projectSequence, Principal principal ){ 
		ResponseEntity<?> erroMap = mapValidationErrorService.MapValidationService(result);
		
		if(erroMap != null)		return erroMap;
		
		return new ResponseEntity<ProjectTasks>(projectTaskService.updateProjectTask(projectId, projectSequence, principal.getName(), projectTask), HttpStatus.OK);
	}
	
	@DeleteMapping("/{projectId}/{projectSequence}")
	public ResponseEntity<?> deleteProjectTask(@PathVariable String projectId, @PathVariable String projectSequence, Principal principal){	  
		if(projectTaskService.deleteProjectTask(projectId, projectSequence, principal.getName()) != true)	throw new ProjectNotFoundException("You dont have access to the project-task with sequence-number '" + projectSequence + "' or the projectTask doesnt exist!"); 
		return new ResponseEntity<String>("Project-Task with id '" + projectSequence + "' has been deleted",HttpStatus.OK);
	}
	
	//projectId, sender, receiver, projectSequence
	@PostMapping("/share/{args}")
	public ResponseEntity<?> shareProjectTasks(@PathVariable String[] args, Principal principal) {	  
		if(projectTaskService.shareProjectTask(args[0], principal.getName(), args[1], args[2]) != true)		throw new ProjectNotFoundException("ProjectTask with Id '" + args[2] + "' wasnt shared to user '" + args[1] + "' !"); 
		return new ResponseEntity<String>("Project-Task with id '" + args[2] + "' was shared with '" + args[1] + "' !", HttpStatus.OK);
	}
}