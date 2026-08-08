package engine;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Answer(boolean success) {
    @JsonProperty("feedback")
    public String feedback(){
        return this.success ? "Congratulations, you're right!" : "Wrong answer! Please, try again.";
    }
}
