package np.com.rishabkarki.UserSystemVersion2.repository;

import np.com.rishabkarki.UserSystemVersion2.model.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthenticationRepository extends JpaRepository<Authentication, Long> {

    Optional<Authentication> findByUsername(@NonNull String username);
    Optional<Authentication> findByEmail(@NonNull String email);

    boolean existsById(@NonNull Long id);
    boolean existsByUsername(@NonNull String username);
    boolean existsByEmail(@NonNull String email);
}