package org.rostfactory.sharemodule.exception;

import org.rostfactory.sharemodule.dto.MessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class DefaultAdvice {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<MessageDto> handleException(MethodArgumentTypeMismatchException e) {
        return ResponseEntity
                .badRequest()
                .body(getMessageDto("Аргументы запроса некорректны."));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<MessageDto> handleException(EntityNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(getMessageDto("Объект не найден"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageDto> handleException(Exception e) {
        return ResponseEntity
                .badRequest()
                .body(getMessageDto("Unexpected server error"));
    }

    @ExceptionHandler(MoneyOrLicenseNotFoundException.class)
    public ResponseEntity<MessageDto> handleException(MoneyOrLicenseNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.PRECONDITION_FAILED)
                .body(getMessageDto("Недостаточно средств или отсутствуют водительские права для предзаказа автомомбиля"));
    }

    @ExceptionHandler(PasswordIncorrectException.class)
    public ResponseEntity<MessageDto> handleException(PasswordIncorrectException e) {
        return ResponseEntity
                .badRequest()
                .body(getMessageDto("Введен неправильный пароль"));
    }

    @ExceptionHandler(SellersBusyException.class)
    public ResponseEntity<MessageDto> handleException(SellersBusyException e) {
        return ResponseEntity
                .status(HttpStatus.PRECONDITION_FAILED)
                .body(getMessageDto("Все продавцы заняты, идет всегородская лотерея"));
    }

    private MessageDto getMessageDto(String message) {
        return MessageDto.builder().message(message).build();
    }

}
