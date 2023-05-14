package tuvarna.ticket_center_server.providers;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class VerificationCodeProviderTests {

    @Test
    public void testGenerate() {

        String code = VerificationCodeProvider.generate();
        assertEquals(6, code.length());

        for (char c : code.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }
}
