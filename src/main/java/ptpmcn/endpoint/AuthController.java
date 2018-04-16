package ptpmcn.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ptpmcn.auth.TokenHandler;
import ptpmcn.service.SecurityContextService;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private SecurityContextService securityContextService;
	
	@Autowired
	private TokenHandler tokenHandler;
	
	@PostMapping
	public AuthResponse auth(@RequestBody AuthParams authParams) throws AuthenticationException{
		UsernamePasswordAuthenticationToken loginToken = authParams.toAuthenticationToken();
		Authentication authentication = authenticationManager.authenticate(loginToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		 return securityContextService.getCurrentUser().map(u -> {
	            final String token = tokenHandler.createTokenForUser(u);
	            return new AuthResponse(token, u.getId());
	        }).orElseThrow(RuntimeException::new); 
		
	}
	
	public static final class AuthParams {
		private String username;
		private String password;
		
		public AuthParams() {
			
		}
		
		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public AuthParams(String username, String password) {
			super();
			this.username = username;
			this.password = password;
		}
		
		public UsernamePasswordAuthenticationToken toAuthenticationToken() {
			return new UsernamePasswordAuthenticationToken(username, password);
		}
	}
	
	public static final class AuthResponse {
		private String token;
		private long id;

		public AuthResponse() {
			
		}
		
		public AuthResponse(String token, long id) {
			super();
			this.token = token;
			this.id = id;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}	
		
		public Long getId() {
			return id;
		}
		
		public void setId(Long id) {
			this.id = id;
		}
	}

}
