package ptpmcn.auth;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ptpmcn.model.User;
import ptpmcn.repository.UserRepository;
@Service
public class TokenHandlerImpl implements TokenHandler {

	@Value("app.jwt.secret")
	private String secret;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Optional<UserDetails> parseUserFromToken(String token) {
		String subject = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
		User user = userRepository.findOneById(Long.valueOf(subject));

		return Optional.ofNullable(user);
	}

	@Override
	public String createTokenForUser(User user) {
		ZonedDateTime afterOneWeek = ZonedDateTime.now().plusWeeks(1);

		return "Bearer " + Jwts.builder()
				.setSubject(user.getId().toString())
				.signWith(SignatureAlgorithm.HS512, secret)
				.setExpiration(Date.from(afterOneWeek.toInstant()))
				.compact();
	}

}
