package tuvarna.ticket_center_server.services;

import tuvarna.ticket_center_common.models.VerificationModel;
import tuvarna.ticket_center_server.models.User;
import tuvarna.ticket_center_server.models.VerificationCode;

public interface VerificationCodeService {

    VerificationCode create(User user);

    VerificationCode findVerificationCode(VerificationModel verificationModel);

    void delete(VerificationCode verificationCode);
}
