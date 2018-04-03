package ptpmcn.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ptpmcn.auth.UserAuthentication;
import ptpmcn.model.User;
import ptpmcn.repository.UserRepository;

@Service
@Transactional
public class SecurityContextServiceImpl implements SecurityContextService {
	
	@Autowired
	private UserRepository userRepository;
	@Override
	public Optional<User> getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return Optional.empty();
		}
		if (authentication instanceof UserAuthentication) {
			UserAuthentication authUser = (UserAuthentication) authentication;
			User user = (User) authUser.getUser();
			return Optional.ofNullable(user);
		}
		return userRepository.findOneByUsername(authentication.getName());
		}
}
