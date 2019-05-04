package ibot.komo.tobi.repository;

import org.springframework.data.repository.CrudRepository;

import ibot.komo.tobi.models.ProjectAccess;

public interface ProjectAccessRepository extends CrudRepository<ProjectAccess,Long>{
	
	ProjectAccess findByUsernameAndProjectIdentifier(String username, String projectIdentifier);
	
	Iterable<ProjectAccess> findByUsername(String username);
}
