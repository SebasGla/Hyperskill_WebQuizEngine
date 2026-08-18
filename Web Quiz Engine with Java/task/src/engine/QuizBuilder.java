package engine;

import java.util.List;
import jakarta.validation.constraints.*;

public record QuizBuilder(@NotBlank String title,@NotBlank String text,@NotNull @Size(min = 2) List<String> options, List<Integer> answer) {
}