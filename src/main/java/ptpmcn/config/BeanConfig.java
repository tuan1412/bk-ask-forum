package ptpmcn.config;

import javax.servlet.http.HttpServletResponse;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ptpmcn.dto.UserDto;
import ptpmcn.dto.UserRegistrationDto;
import ptpmcn.model.User;

@Configuration
public class BeanConfig {

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

		modelMapper.addMappings(new PropertyMap<User, UserDto>() {
			@Override
			protected void configure() {
				using(convertUrl).map(source.getAvatar()).setAvatar(null);

			}
		});

		return modelMapper;
	}

}
