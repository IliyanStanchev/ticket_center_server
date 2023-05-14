package tuvarna.ticket_center_server.exceptions;

import org.springframework.web.server.ResponseStatusException;

public class InternalErrorResponseStatusException extends ResponseStatusException {

    public InternalErrorResponseStatusException() {
        super(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.");
    }
}
