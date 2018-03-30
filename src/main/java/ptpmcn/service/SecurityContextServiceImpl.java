package ptpmcn.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import ptpmcn.model.User;
import ptpmcn.repository.UserRepository;

public class SecurityContextServiceImpl implements SecurityContextService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public Optional<User> getCurrentUser(){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepository.findOneByUsername(username);
	}

}
