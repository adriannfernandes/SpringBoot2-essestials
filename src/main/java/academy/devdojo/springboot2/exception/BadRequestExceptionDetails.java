package academy.devdojo.springboot2.exception;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;


@Getter
@SuperBuilder
public class BadRequestExceptionDetails extends ExceptionDetails {

}
