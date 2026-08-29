package engine;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegisterDto(
        @NotBlank(message = "E-Mail darf nicht leer sein")
        @Pattern(regexp = ".+@.+\\..+", message = "Ungültiges E-Mail-Format")
        String email,

        @NotBlank(message = "Passwort darf nicht leer sein")
        @Size(min = 5, message = "Passwort muss mindestens 5 Zeichen lang sein")
        String password) {
}
