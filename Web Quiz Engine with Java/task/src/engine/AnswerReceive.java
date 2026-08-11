package engine;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AnswerReceive(@NotNull @JsonProperty("answer") List<Integer> answerReceived) {
}
