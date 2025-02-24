package np.com.rishabkarki.UserSystemVersion2.repository;

import np.com.rishabkarki.UserSystemVersion2.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByEmailAndToken(String email, Integer token);
    Optional<VerificationCode> findByEmail(String email);
}