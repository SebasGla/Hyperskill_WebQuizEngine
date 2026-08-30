package engine;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    // Method to be used by Spring Security in auth process
    Optional<User> findUserByEmail(String email);
    boolean existsByEmail(String email);
}
