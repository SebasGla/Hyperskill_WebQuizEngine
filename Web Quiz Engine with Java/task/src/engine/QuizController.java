package engine;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final InMemoryQuizRepository quizRepository;

    //Constuctor Injektion
    public QuizController(InMemoryQuizRepository quizRepository){
        this.quizRepository = quizRepository;
    }

    @PostMapping
    public ResponseEntity<QuizGetter> postQuiz(@RequestBody QuizBuilder quizBuilder){
        Quiz newQuiZ = new Quiz(quizBuilder);
        //Save with the Quiz with a generated Id

        quizRepository.save(newQuiZ);


        QuizGetter quizResponse = new QuizGetter(newQuiZ.getId(), newQuiZ.getTitle(), newQuiZ.getText(), newQuiZ.getOptions());
        return new ResponseEntity<>(quizResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<QuizGetter>> getAllQuizzes(){
        List<Quiz> quizList = quizRepository.findAll();
        List<QuizGetter> quizGetterList = quizList.stream()
                .map(q -> new QuizGetter(q.getId(), q.getTitle(), q.getText(), q.getOptions()))
                .toList();

        return new ResponseEntity<>(quizGetterList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizGetter> getQuiz(@PathVariable int id){
        Optional<Quiz> optionalQuiz = quizRepository.findById(id);
        return optionalQuiz
                .map(quiz -> {
                    QuizGetter dto = new QuizGetter(quiz.getId(), quiz.getTitle(), quiz.getText(), quiz.getOptions());
                    return ResponseEntity.ok(dto); // Status 200 ok
                })
                .orElseGet(() -> ResponseEntity.notFound().build()); // Status 404 Not Found
    }

    @PostMapping("/{id}/solve?answer={index}")
    public ResponseEntity<Answer> postAnswer(@RequestParam int id, @RequestParam int index){

        return new ResponseEntity<>(new Answer()
    }



}