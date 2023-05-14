package tuvarna.ticket_center_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tuvarna.ticket_center_server.models.VerificationCode;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    @Query("SELECT v FROM VERIFICATION_CODES v WHERE v.user.email = ?1 AND v.code = ?2")
    VerificationCode findByUserEmailAndCode(String email, String verificationCode);
}
