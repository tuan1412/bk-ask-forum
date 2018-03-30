package ptpmcn.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

public class TokenAuthenticationServiceImpl implements TokenAuthenticationService {

	@Override
	public Authentication getAuthentication(HttpServletRequest request) {
		return null;
	}

}
