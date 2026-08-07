package engine;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryQuizRepository {
    private final Map<Integer, Quiz> memoryStore = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public Quiz save(Quiz quiz){
        if (quiz.getId() == null) {
            quiz.setId(idGenerator.getAndIncrement());
        }

        memoryStore.put(quiz.getId(), quiz);
        return quiz;
    }

    public Optional<Quiz> findById(Integer id) {
        return Optional.ofNullable(memoryStore.get(id));
    }

    public List<Quiz> findAll() {
        return new ArrayList<>(memoryStore.values());
    }
}
