package bg.pazar.pazarbg.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Message not found")
public class MessageNotFoundException extends RuntimeException{
}