package ibot.komo.tobi.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ibot.komo.tobi.models.ProjectTasksAccess;

public interface ProjectTaskAccessRepository extends CrudRepository<ProjectTasksAccess, Long>{
	
	List<ProjectTasksAccess> findAllByProjectIdentifierAndUsername(String projectIdentifier, String username);
	
	ProjectTasksAccess findByProjectSequenceAndUsername(String projectSequence, String username);
	
	ProjectTasksAccess findByProjectSequence(String projectSequence);
}
