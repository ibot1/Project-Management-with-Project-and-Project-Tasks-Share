package ibot.komo.tobi.repository;
import org.springframework.data.repository.CrudRepository;

import ibot.komo.tobi.models.Users;

public interface UserRepository extends CrudRepository<Users,Long>{
	
	Users findByUsername(String username);
	
	Users getById(Long id);

}
