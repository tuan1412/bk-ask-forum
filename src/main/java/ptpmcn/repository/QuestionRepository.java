package ptpmcn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ptpmcn.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}
