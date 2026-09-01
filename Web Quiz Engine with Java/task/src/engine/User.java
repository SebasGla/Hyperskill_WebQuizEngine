package engine;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Email
    private String email;

    private String password;
    private String authority;

    @ElementCollection
    private List<CompletedQuiz> completedQuizzes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public void completeQuiz(int quizId, String dateTime){
        this.completedQuizzes.add(new CompletedQuiz(quizId, dateTime));
    }

    public List<CompletedQuiz> getCompletedQuizzes() {
        return completedQuizzes;
    }
}
