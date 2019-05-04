package ibot.komo.tobi.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ibot.komo.tobi.models.ProjectTasks;
import ibot.komo.tobi.models.Projects;

public interface ProjectTaskRepository extends CrudRepository<ProjectTasks,Long>{
	
	ProjectTasks findByProjectSequence(String projectSequence);
	List<ProjectTasks> findAllByProjects(Projects projects);
}
