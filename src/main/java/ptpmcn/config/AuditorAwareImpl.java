package ptpmcn.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

import ptpmcn.model.User;
import ptpmcn.service.SecurityContextService;

public class AuditorAwareImpl implements AuditorAware<User> {

	
	@Autowired
	private SecurityContextService securityContextService;
	
	@Override
	public Optional<User> getCurrentAuditor() {
        return securityContextService.getCurrentUser();
	}
}
