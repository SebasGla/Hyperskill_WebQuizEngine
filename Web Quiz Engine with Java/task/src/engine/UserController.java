package engine;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/register")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserRegisterDto dto){
        if(userRepository.existsByEmail(dto.email())){
            return ResponseEntity.badRequest().build();
        }

        //Hash password
        String encodedPassword = passwordEncoder.encode(dto.password());

        //generate and save user to repository
        User user = new User();
        user.setEmail(dto.email());
        user.setPassword(encodedPassword);
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}
