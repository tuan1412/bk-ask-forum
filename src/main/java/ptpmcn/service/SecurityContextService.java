package ptpmcn.service;

import java.util.Optional;

import ptpmcn.model.User;

public interface SecurityContextService {

	Optional<User> getCurrentUser();

}