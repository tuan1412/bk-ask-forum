package ptpmcn.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Question implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank	
	private String title;
	
	@NotBlank
	@NonNull
	private String content;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@CreatedBy
	private User user;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createAt;
	
	@LastModifiedDate
	@Column(nullable = false, updatable = true)
	private LocalDateTime lastModified;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private Category category;
	
	@JsonIgnore
	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Answer> answers = new HashSet<>();
	
	@ManyToMany(mappedBy="followQuestions")
	private Set<User> users = new HashSet<>();
	
	@ManyToMany(mappedBy="voteQuestions")
	private Set<User> voteUsers = new HashSet<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "question", orphanRemoval = true, cascade = CascadeType.ALL)
	private Set<Notification> notifications = new HashSet<>();
	
	
	public void addAnswer(Answer answer) {
		answers.add(answer);
		answer.setQuestion(this);
	}

}
