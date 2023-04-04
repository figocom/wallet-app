package com.softex.figo.walletapp.handler;

import com.softex.figo.walletapp.exception.FileStorageException;
import com.softex.figo.walletapp.exception.ItemNotFoundException;
import com.softex.figo.walletapp.exception.TokenRefreshException;
import com.softex.figo.walletapp.response.ErrorDTO;
import com.softex.figo.walletapp.response.WebResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {
   @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<WebResponse<ErrorDTO>> handler_404(ItemNotFoundException e, HttpServletRequest request) {
        return ResponseEntity.status(404)
                .body( new WebResponse<>(new ErrorDTO(e.getMessage(), 404)));
    }
    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<WebResponse<ErrorDTO>> handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        return ResponseEntity.status(403).body(new WebResponse<>(new ErrorDTO(ex.getMessage(), 403)));
    }
    @ExceptionHandler(value = FileStorageException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<WebResponse<ErrorDTO>> handleFileStorageException(FileStorageException ex, WebRequest request) {
        return ResponseEntity.status(403).body(new WebResponse<>(new ErrorDTO(ex.getMessage(), 403)));
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }


}
