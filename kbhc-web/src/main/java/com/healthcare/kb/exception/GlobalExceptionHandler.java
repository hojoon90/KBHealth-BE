package com.healthcare.kb.exception;

import com.healthcare.kb.dto.AppResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.healthcare.kb.constant.MessageConst.WRONG_PARAMETER;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 기타 예외
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<AppResponse<Void>> handleException(Exception ex) {
        log.error("handleException: {}", ex.getMessage(), ex);
        return getExceptionResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());

    }

    /**
     * 사용자 인증 예외
     * @param ex
     * @return
     */
    @ExceptionHandler(AuthorizeException.class)
    protected ResponseEntity<AppResponse<Void>> handleAuthorizeException(Exception ex) {
        log.error("AuthorizeException: {}", ex.getMessage(), ex);
        return getExceptionResponseEntity(HttpStatus.UNAUTHORIZED, ex.getMessage());

    }

    /**
     * 데이터 처리 예외
     * @param ex
     * @return
     */
    @ExceptionHandler(DataProcessException.class)
    protected ResponseEntity<AppResponse<Void>> handleDataProcessException(Exception ex) {
        log.error("UserDataException: {}", ex.getMessage(), ex);
        return getExceptionResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    /**
     * 데이터 미존재 예외
     * @param ex
     * @return
     */
    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<AppResponse<Void>> handleDataNotFoundException(Exception ex) {
        log.error("DataNotFoundException: {}", ex.getMessage(), ex);
        return getExceptionResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * 권한 없음 예외
     * @param ex
     * @return
     */
    @ExceptionHandler(ForbiddenException.class)
    protected ResponseEntity<AppResponse<Void>> handleForbiddenException(Exception ex) {
        log.error("ForbiddenException: {}", ex.getMessage(), ex);
        return getExceptionResponseEntity(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    /**
     * 데이터 충돌 예외
     * @param ex
     * @return
     */
    @ExceptionHandler(ConflictException.class)
    protected ResponseEntity<AppResponse<Void>> handleConflictException(Exception ex) {
        log.error("ConflictException: {}", ex.getMessage(), ex);
        return getExceptionResponseEntity(HttpStatus.CONFLICT, ex.getMessage());
    }

    /**
     * 파라미터 요청 예외
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object processValidationError(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException: {}", ex.getMessage(), ex);
        return getExceptionResponseEntity(HttpStatus.BAD_REQUEST, WRONG_PARAMETER.getMessage());
    }

    private ResponseEntity<AppResponse<Void>> getExceptionResponseEntity(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(
                AppResponse.responseFail(httpStatus, message), httpStatus);
    }
}


