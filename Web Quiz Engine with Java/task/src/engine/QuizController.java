package engine;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizRepository quizRepository;

    //Constuctor Injektion
    public QuizController(QuizRepository quizRepository){

        this.quizRepository = quizRepository;
    }

    @PostMapping
    public ResponseEntity<QuizGetter> postQuiz(@Valid @RequestBody QuizBuilder quizBuilder){
        Quiz newQuiZ = new Quiz(quizBuilder);
        //Save with the Quiz with a generated Id
        quizRepository.save(newQuiZ);

        QuizGetter quizResponse = new QuizGetter(newQuiZ.getId(), newQuiZ.getTitle(), newQuiZ.getText(), newQuiZ.getOptions());
        return new ResponseEntity<>(quizResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<QuizGetter>> getAllQuizzes(){
        List<Quiz> quizList = (List<Quiz>) quizRepository.findAll();
        List<QuizGetter> quizGetterList = quizList.stream()
                .map(q -> new QuizGetter(q.getId(), q.getTitle(), q.getText(), q.getOptions()))
                .toList();

        return new ResponseEntity<>(quizGetterList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizGetter> getQuiz(@PathVariable int id){
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new QuizNotFoundException(id));
        QuizGetter dto = new QuizGetter(quiz.getId(), quiz.getTitle(), quiz.getText(), quiz.getOptions());
        return ResponseEntity.ok(dto); // Status 200 ok
    }

    @PostMapping("/{id}/solve")
    public ResponseEntity<AnswerResponse> postAnswer(@PathVariable int id,@Valid @RequestBody AnswerReceive reqAns){
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new QuizNotFoundException(id));
        AnswerResponse dto = new AnswerResponse(quiz.checkAnswer(reqAns.answerReceived()));
        return ResponseEntity.ok(dto);
    }



}