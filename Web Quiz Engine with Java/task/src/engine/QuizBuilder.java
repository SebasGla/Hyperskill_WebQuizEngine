package engine;

import java.util.List;
import jakarta.validation.constraints.*;

public record QuizBuilder(@NotNull String title,@NotNull String text,@Size(min = 2) List<String> options, List<Integer> answer) {
}