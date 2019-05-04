package ibot.komo.tobi.web;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ibot.komo.tobi.models.Projects;
import ibot.komo.tobi.service.MapValidationErrorService;
import ibot.komo.tobi.service.ProjectService;



@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
	@PostMapping("")
	public ResponseEntity<?> createUpdatewProject(@Valid @RequestBody Projects project, BindingResult result, Principal principal){
		ResponseEntity<?> respErr = mapValidationErrorService.MapValidationService(result);
		if(respErr != null)	return respErr;
		return new ResponseEntity<Projects>(projectService.createUpdateProject(project, principal.getName()), HttpStatus.OK);
	}
	
	@GetMapping("/{projectId}")
	public ResponseEntity<?> getProjectById(@PathVariable String projectId, Principal principal){
		return new ResponseEntity<Projects>(projectService.getProject(projectId, principal.getName()), HttpStatus.OK);
	}
	
	@GetMapping("/all")
	public Iterable<?> getAllProjects(Principal principal){
		//return new ResponseEntity<Iterable<Projects>>(projectService.getAllProjects(principal.getName()), HttpStatus.OK);
		return projectService.getAllProjects(principal.getName());
	}
	
	@DeleteMapping("/{projectId}")
	public ResponseEntity<?> deleteProject(@PathVariable String projectId, Principal principal) {
		if(projectService.deleteProject(projectId, principal.getName()) == false)	return new ResponseEntity<String>("Cant delete because arent the creator of Project with ID: '" + projectId.toUpperCase() + "' !", HttpStatus.OK);
		return new ResponseEntity<String>("Project with ID: '" + projectId.toUpperCase() + "' was deleted", HttpStatus.OK);
	}
	
	@PostMapping("/share/{args}")
	public ResponseEntity<?> ShareProject(@PathVariable String[]args, Principal principal) {
		if(projectService.shareProject(args[0],args[1],principal.getName()) == true)		return new ResponseEntity<String>("Project with ID: '" + args[0].toUpperCase() + "' was shared", HttpStatus.OK);
		return new ResponseEntity<String>("Project with ID: '" + args[0].toUpperCase() + "' wasnt shared", HttpStatus.OK);
	}
}
