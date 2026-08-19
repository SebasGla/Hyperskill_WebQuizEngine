package engine;

import java.util.List;

public record QuizGetter(int id, String title, String text, List<String> options) {
    public static QuizGetter fromEntity(Quiz quiz) {
        return new QuizGetter(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getText(),
                quiz.getOptions()
        );
    }
}