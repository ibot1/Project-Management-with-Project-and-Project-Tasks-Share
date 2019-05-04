package ibot.komo.tobi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ibot.komo.tobi.models.Users;
import ibot.komo.tobi.repository.UserRepository;

@Service
public class CustomUserDetailService  implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
		Users user = userRepository.findByUsername(username);
		
		if(user == null)	new UsernameNotFoundException("User not Found");
		
		return user;
		
	}
	
	@Transactional
	public Users loadUserById(Long id) {
		Users user = userRepository.getById(id);
		
		if(user == null)	new UsernameNotFoundException("User not Found");
		return user;
	}
}
