package io.github.teamomo.moment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MomentAlreadyExistsException extends RuntimeException {

    public MomentAlreadyExistsException(String message) {
        super(message);
    }



}
