package ptpmcn.auth;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;

import ptpmcn.model.User;

public interface TokenHandler {
	Optional<UserDetails> parseUserFromToken(String token);

	String createTokenForUser(User user);
}
