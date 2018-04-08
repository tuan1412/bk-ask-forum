package ptpmcn.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of= {"id"})
@NamedEntityGraph(name = "User.roles",
attributeNodes = @NamedAttributeNode("roles"))
public class User implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String avatar = "avatar.png";
	
	@NotBlank
	private String username;
	
	@NotBlank
	private String password;
	
	@NotBlank
	private String fullname;
	
	@Email
	private String email;

	@Transient
	private boolean banned = false;
		
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createAt;
	
	@JsonIgnore
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "user_role",
				joinColumns = @JoinColumn(name = "user_id"),
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
	
	@Transient
	private int followers;
	@Transient
	private int vote;
	@PostLoad
	private void postLoad() {
		int answerVote = answers.stream()
								.map(Answer::getVote)
								.reduce(Integer::sum)
								.orElse(0);
		vote = answerVote;
		
		followers = followingUsers.size();
		
		banned = roles.isEmpty();
	}
	
	@JsonIgnore
	@OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
	private Set<Question> questions = new HashSet<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
	private Set<Answer> answers = new HashSet<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
	private Set<Notification> notifications = new HashSet<>();
	
	@JsonIgnore
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "user_question",
				joinColumns = @JoinColumn(name = "user_id"),
				inverseJoinColumns = @JoinColumn(name = "follow_question_id"))
	private Set<Question> followQuestions = new HashSet<>();
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "user_follow",
			   joinColumns = @JoinColumn(name = "following_id"),
			   inverseJoinColumns = @JoinColumn(name = "followed_id"))
	
	private Set<User> followedUsers = new HashSet<>();
	
	@JsonIgnore
	@ManyToMany(mappedBy="followedUsers")
	private Set<User> followingUsers = new HashSet<>();
	
	public void addRole(Role role) {
		roles.add(role);
		role.getUsers().add(this);
	}
	
	public void removeRole(Role role) {
		roles.remove(role);
		role.getUsers().remove(this);
	}
	
	public void followQuestion(Question question) {
		followQuestions.add(question);
		question.getUsers().add(this);
	}
	
	public void unfollowQuestion(Question question) {
		followQuestions.remove(question);
		question.getUsers().remove(this);
	}
	
	public void followUser(User user) {
		followedUsers.add(user);
		user.getFollowingUsers().add(this);
	}
	
	public void unfollowUser(User user) {
		followedUsers.remove(user);
		user.getFollowingUsers().remove(this);
	}
	
	public boolean isAdmin() {
		return roles.contains(new Role("ADMIN"));
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toSet());
	}
	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}
}
