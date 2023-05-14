package tuvarna.ticket_center_server.providers;

public class VerificationCodeProvider {

    private static final int CODE_LENGTH = 6;

    public static String generate() {

        String code = "";
        for (int i = 0; i < CODE_LENGTH; i++) {
            code += (int) (Math.random() * 10) % 10;
        }

        return code;
    }
}
