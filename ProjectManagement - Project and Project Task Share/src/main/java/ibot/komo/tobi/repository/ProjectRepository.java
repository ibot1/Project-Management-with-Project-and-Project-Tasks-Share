package ibot.komo.tobi.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ibot.komo.tobi.models.Projects;

public interface ProjectRepository extends CrudRepository<Projects,Long>{
	
	Projects findByProjectIdentifier(String projectIdentifier);
	List<Projects> findAllByProjectIdentifier(String projectIdentifier);
	
	//Projects findByProjectIdentifierAndProjectLeader(String projectLeader);
	void deleteByProjectIdentifier(String projectIdentifier);
	
}
