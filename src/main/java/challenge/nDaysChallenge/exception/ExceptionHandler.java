package challenge.nDaysChallenge.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.BindException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    //커스텀 예외
    @org.springframework.web.bind.annotation.ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException e){
        ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    //NotFound
    @org.springframework.web.bind.annotation.ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<Object> handleNotFound(HttpClientErrorException.NotFound e) {
        log.warn("handleNotFound", e);
        ErrorCode errorCode = CommonError.NOT_FOUND;
        return handleExceptionInternal(errorCode);
    }

    //Forbidden
    @org.springframework.web.bind.annotation.ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<Object> handleForbidden(HttpClientErrorException.Forbidden e) {
        log.warn("handleForbidden", e);
        ErrorCode errorCode = CommonError.FORBIDDEN;
        return handleExceptionInternal(errorCode);
    }

    //Unauthorized
    @org.springframework.web.bind.annotation.ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<Object> handleUnauthorized(HttpClientErrorException.Unauthorized e) {
        log.warn("handleUnauthorized", e);
        ErrorCode errorCode = CommonError.UNAUTHORIZED;
        return handleExceptionInternal(errorCode);
    }

    //Internal Server
    @org.springframework.web.bind.annotation.ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<Object> handleInternalServer(HttpServerErrorException.InternalServerError e) {
        log.warn("handleInternalServer", e);
        ErrorCode errorCode = CommonError.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(errorCode);
    }

    //모든 예외
    @org.springframework.web.bind.annotation.ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllException(Exception e) {
        log.warn("handleAllException", e);
        ErrorCode errorCode = CommonError.BAD_REQUEST;
        return handleExceptionInternal(errorCode);
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }

}
