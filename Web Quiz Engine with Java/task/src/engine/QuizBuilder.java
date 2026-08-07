package engine;

import java.util.List;

public record QuizBuilder(String title, String text, List<String> options, int answer) {}