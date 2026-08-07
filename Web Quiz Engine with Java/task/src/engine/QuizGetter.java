package engine;

import java.util.List;

public record QuizGetter(int id, String title, String text, List<String> options) {}