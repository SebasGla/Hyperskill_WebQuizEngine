package engine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CompletedQuizRepository extends PagingAndSortingRepository<CompletedQuiz, Integer>, CrudRepository<CompletedQuiz, Integer> {
     Page<CompletedQuiz> findBySolver(User solver, Pageable pageable);
}
