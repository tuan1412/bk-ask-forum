package ptpmcn.config;

import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ptpmcn.dto.AnswerDto;
import ptpmcn.dto.QuestionDto;
import ptpmcn.dto.UserDto;
import ptpmcn.dto.UserRegistrationDto;
import ptpmcn.model.Answer;
import ptpmcn.model.Question;
import ptpmcn.model.User;
import ptpmcn.repository.AnswerRepository;
import ptpmcn.repository.QuestionRepository;
import ptpmcn.repository.UserRepository;

@Configuration
public class BeanConfig {
	
	@Autowired
	private AnswerRepository answerRepository;
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationEntryPoint restAuthenticationEntryPoint() {
		return (req, res, authExp) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();

		Converter<String, String> encodePassword = ctx -> passwordEncoder().encode(ctx.getSource());

		modelMapper.addMappings(new PropertyMap<UserRegistrationDto, User>() {
			@Override
			protected void configure() {
				using(encodePassword).map(source.getPassword()).setPassword(null);
			}
		});

		Converter<String, String> convertUrl = ctx -> ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/upload/" + ctx.getSource()).build().encode().toUriString();
		Converter<Long, Long> countVoteUser = ctx -> userRepository.countVote(ctx.getSource());

		modelMapper.addMappings(new PropertyMap<User, UserDto>() {
			@Override
			protected void configure() {
				using(convertUrl).map(source.getAvatar()).setAvatar(null);
				using(countVoteUser).map(source.getId()).setVote(0);

			}
		});
		
		
		Converter<Long, Long> countVote = ctx -> answerRepository.countVote(ctx.getSource());
		
		Converter<Set<User>, List<Long>> listVoteUsers = ctx -> ctx.getSource().stream().map(u->u.getId()).collect(Collectors.toList());
		
		modelMapper.addMappings(new PropertyMap<Answer, AnswerDto>() {
			@Override
			protected void configure() {
				using(countVote).map(source.getId()).setVote(0);
				using(convertUrl).map(source.getUser().getAvatar()).setUserAvatar(null);
				using(listVoteUsers).map(source.getVoteUsers()).setVoteUserIds(null);
			}
		});
		
		Converter<Long, Long> countVoteQuestion = ctx -> questionRepository.countVote(ctx.getSource());
		
		modelMapper.addMappings(new PropertyMap<Question, QuestionDto>() {
			@Override
			protected void configure() {
				using(countVoteQuestion).map(source.getId()).setVote(0);
				using(convertUrl).map(source.getUser().getAvatar()).setUserAvatar(null);
				using(listVoteUsers).map(source.getVoteUsers()).setVoteUserIds(null);
			}
		});

		return modelMapper;
	}

}
