package ptpmcn.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ptpmcn.model.User;
import ptpmcn.repository.UserRepository;

public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findOneByUsername(username);
		AccountStatusUserDetailsChecker checker = new AccountStatusUserDetailsChecker();
		user.ifPresent(checker::check);
		return user.orElseThrow(()-> new UsernameNotFoundException("User not found"));
	}

}
