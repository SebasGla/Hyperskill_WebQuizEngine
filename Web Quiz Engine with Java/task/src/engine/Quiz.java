package engine;

import java.util.List;
import java.util.Objects;

public class Quiz {
    private Integer id;
    private String title;
    private String text;
    private List<String> options;
    private List<Integer> answer;

    public Quiz(QuizBuilder quizB){
        this.title = quizB.title();
        this.text = quizB.text();
        this.options = quizB.options();
        this.answer = quizB.answer();
    }

    public boolean checkAnswer(List<Integer> answer){
        return this.answer.equals(answer);
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
