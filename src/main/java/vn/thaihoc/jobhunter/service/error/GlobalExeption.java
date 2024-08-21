package vn.thaihoc.jobhunter.service.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExeption {
    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<String> handleIdInvalidException(IdInvalidException idException) {
        return ResponseEntity.badRequest().body(idException.getMessage());
    }

}
