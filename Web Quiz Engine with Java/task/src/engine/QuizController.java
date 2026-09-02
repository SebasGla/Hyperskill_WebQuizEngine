package engine;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final int pageSize = 10;
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final CompletedQuizRepository completedQuizRepository;

    //Constuctor Injektion
    public QuizController(QuizRepository quizRepository, UserRepository userRepository, CompletedQuizRepository completedQuizRepository){
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
        this.completedQuizRepository = completedQuizRepository;
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
    public ResponseEntity<Page<QuizGetter>> getAllQuizzes(@RequestParam(defaultValue = "0") int page){
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<QuizGetter> quizGetterPage = quizRepository.findAll(pageable).map(QuizGetter::fromEntity);

        return new ResponseEntity<>(quizGetterPage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizGetter> getQuiz(@PathVariable int id){
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new QuizNotFoundException(id));
        QuizGetter dto = QuizGetter.fromEntity(quiz);
        return ResponseEntity.ok(dto); // Status 200 ok
    }

    @PostMapping("/{id}/solve")
    public ResponseEntity<AnswerResponse> postAnswer(@PathVariable int id,@Valid @RequestBody AnswerReceive reqAns,
                                                     @AuthenticationPrincipal UserDetails userDetails){
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new QuizNotFoundException(id));
        User currentUser = userRepository
                .findUserByEmail(userDetails.getUsername())
                .orElseThrow(() ->new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        AnswerResponse dto = new AnswerResponse(quiz.checkAnswer(reqAns.answerReceived()));
        if (dto.success()){
            completedQuizRepository.save(new CompletedQuiz(quiz.getId(), currentUser ));
        }
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

    @GetMapping("/completed")
    public Page<CompletedQuiz> getUserCompleted(@RequestParam(defaultValue = "0") int page,
                                                @AuthenticationPrincipal UserDetails userDetails){
        User currentUser = userRepository
                .findUserByEmail(userDetails.getUsername())
                .orElseThrow(() ->new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("completedAdd").descending());

        return   completedQuizRepository.findBySolver(currentUser,pageable);
    }


}