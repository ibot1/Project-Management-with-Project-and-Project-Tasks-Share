package ibot.komo.tobi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ibot.komo.tobi.models.Users;
import ibot.komo.tobi.exception.UsernameAlreadyExistsException;
import ibot.komo.tobi.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder; 

	public Users saveUser(Users newUser) {
		try{
			newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
			newUser.setConfirmPasswd("");
			newUser.setUsername(newUser.getUsername().toUpperCase());
			
			return userRepository.save(newUser);
		}catch(Exception e) {
			throw new UsernameAlreadyExistsException("Username '" + newUser.getUsername() + "' already exists");
		}
		
	}
}

