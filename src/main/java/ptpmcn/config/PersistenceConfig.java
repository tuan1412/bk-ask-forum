package ptpmcn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import ptpmcn.model.User;

@EnableJpaAuditing(auditorAwareRef="auditorProvider")
@Configuration
public class PersistenceConfig {
	@Bean
	public AuditorAware<User> auditorProvider() {
		return new AuditorAwareImpl();
	}
}
