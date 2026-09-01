package engine;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;

    //Constuctor Injektion
    public QuizController(QuizRepository quizRepository, UserRepository userRepository){
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<QuizGetter> postQuiz(@Valid @RequestBody QuizBuilder quizBuilder,
                                               @AuthenticationPrincipal UserDetails userDetails){

        User currentUser = userRepository.findUserByEmail(userDetails.getUsername()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        Quiz newQuiZ = new Quiz(quizBuilder, currentUser);
        //Save with the Quiz with a generated Id
        quizRepository.save(newQuiZ);

        QuizGetter quizResponse = QuizGetter.fromEntity(newQuiZ);
        return new ResponseEntity<>(quizResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<QuizGetter>> getAllQuizzes(){
        List<Quiz> quizList = (List<Quiz>) quizRepository.findAll();
        List<QuizGetter> quizGetterList = quizList.stream()
                .map(QuizGetter::fromEntity)
                .toList();

        return new ResponseEntity<>(quizGetterList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizGetter> getQuiz(@PathVariable int id){
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new QuizNotFoundException(id));
        QuizGetter dto = QuizGetter.fromEntity(quiz);
        return ResponseEntity.ok(dto); // Status 200 ok
    }

    @PostMapping("/{id}/solve")
    public ResponseEntity<AnswerResponse> postAnswer(@PathVariable int id,@Valid @RequestBody AnswerReceive reqAns){
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new QuizNotFoundException(id));
        AnswerResponse dto = new AnswerResponse(quiz.checkAnswer(reqAns.answerReceived()));
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable int id,@AuthenticationPrincipal UserDetails userDetails){
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new QuizNotFoundException(id));
        if (quiz.getAuthor().getEmail() == userDetails.getUsername()){
            quizRepository.delete(quiz);
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }


}