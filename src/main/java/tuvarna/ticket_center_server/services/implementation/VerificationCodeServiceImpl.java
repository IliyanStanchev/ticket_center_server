package tuvarna.ticket_center_server.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tuvarna.ticket_center_common.models.VerificationModel;
import tuvarna.ticket_center_server.models.User;
import tuvarna.ticket_center_server.models.VerificationCode;
import tuvarna.ticket_center_server.providers.VerificationCodeProvider;
import tuvarna.ticket_center_server.repositories.VerificationCodeRepository;
import tuvarna.ticket_center_server.services.VerificationCodeService;

import java.time.LocalDateTime;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    public VerificationCode create(User user) {

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setUser(user);
        verificationCode.setCreationDate(LocalDateTime.now());
        verificationCode.setExpirationDate(LocalDateTime.now().plusMonths(1));
        verificationCode.setCode(VerificationCodeProvider.generate());

        return verificationCodeRepository.save(verificationCode);
    }

    public VerificationCode findVerificationCode(VerificationModel verificationModel) {

        return verificationCodeRepository.findByUserEmailAndCode(verificationModel.getEmail(), verificationModel.getVerificationCode());
    }

    public void delete(VerificationCode verificationCode) {
        verificationCodeRepository.delete(verificationCode);
    }
}
