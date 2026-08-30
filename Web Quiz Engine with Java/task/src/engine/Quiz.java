package engine;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String text;

    //One User can have many quizzes
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @ElementCollection
    private List<String> options;

    @ElementCollection
    private List<Integer> answer;

    //Use QuizBuilder as DTO for now. Should change to a Mapper later
    public Quiz(QuizBuilder quizB, User user){
        this.title = quizB.title();
        this.text = quizB.text();
        this.options = quizB.options();
        this.answer = quizB.answer();
        this.author = user;
    }

    public Quiz() {

    }


    public boolean checkAnswer(List<Integer> answer){
        if (this.answer == null || answer == null) {
            return Objects.equals(this.answer, answer);
        }


        List<Integer> currentAnswer = new ArrayList<>(this.answer);

        return currentAnswer.equals(answer);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public List<String> getOptions() {
        return options;
    }

    public List<Integer> getAnswer() {
        return answer;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Override
    public int hashCode() {
        //Hash code is either dependant on id or memory, if ID has not been set yet
        return Objects.hash(id != null ? id : getClass().hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }

        if (obj == null || getClass() != obj.getClass()){
            return false;
        }

        Quiz quiz = (Quiz) obj;
        //Either one of the IDs is null?
        if (this.id == null || quiz.getId() == null) {
            return false;
        }


        return Objects.equals(this.id, quiz.id);
    }
}
