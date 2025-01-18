package com.backend.ServerPrac.global.apiPayload.code.exception;

import com.backend.ServerPrac.global.apiPayload.ApiResponse;
import com.backend.ServerPrac.global.apiPayload.code.ErrorReasonDto;
import com.backend.ServerPrac.global.apiPayload.code.Status.ErrorStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import jakarta.validation.ConstraintViolationException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;


@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    // 어렵게 생긴 클래스지만, 하는 역할을 쉽다.
    // @RestControllerAdvice 를 붙였기 때문에 모든 RestController 에 대해 에러를 감시함. (패키지 단위로도 할 수 있음.)
    // 내부 매서드, validation, handleMethodArgumentNotValid, onThrowException, exception 들은 각각 지정된 에러가 감지되면 호출됨.
    // 위 매서드들이 오류를 전달 받아 오류메시지를 만들고 handleExceptionInternal로 응답을 만들어 클라이언트에 전달.

    // @RestControllerAdvice : RestController에서 발생하는 에러를 감지.
    // @ExceptionHandler : 특정 타입의 예외가 발생했을 때 호출될 매서드 지정
    // ConstraintViolationException : Java Bean Validation API에서 반환하는 예외. 데이터 유효성 검증 과정에서 하나 이상의 제약 조건 위반이 발생했을 때 던져지는 예외.
    // MethodArgumentNotValidException : 컨트롤러 측에서 유효하지 않은 파라미터를 전달 받으면 반환. 위엣 친구와는 비슷하나, 반환하는 주체가 다름.
    // GeneralException : 직접 정의한 에러
    // Exception : 그 외의 모든 에러
    // handleExceptionInternal : 웹 응답을 만들어줌. ResponseEntityExceptionHandler에도 있는 매서드(override 할 필요는 없음).

    // ConstraintViolationException을 처리하는 메서드입니다.
    @ExceptionHandler
    public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request) {
        // 예외에서 첫 번째 제약 위반 메시지를 추출합니다.
        String errorMessage = e.getConstraintViolations().stream()
                .map(constraintViolation -> constraintViolation.getMessage()) // 각 제약 위반의 메시지를 가져옵니다.
                .findFirst() // 첫 번째 메시지를 찾습니다.
                .orElseThrow(() -> new RuntimeException("ConstraintViolationException 추출 도중 에러 발생")); // 메시지가 없으면 런타임 예외를 발생시킵니다.

        // 추출한 메시지를 ErrorStatus로 변환하여 예외를 처리합니다.
        return handleExceptionInternalConstraint(e, ErrorStatus.valueOf(errorMessage), HttpHeaders.EMPTY, request);
    }



    // MethodArgumentNotValidException을 처리하는 메서드입니다.
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();

        // 모든 필드 에러를 처리합니다.
        e.getBindingResult().getFieldErrors().stream()
                .forEach(fieldError -> {
                    String fieldName = fieldError.getField(); // 필드 이름을 가져옵니다.
                    String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse(""); // 에러 메시지를 가져옵니다.
                    errors.merge(fieldName, errorMessage, (existingErrorMessage, newErrorMessage) -> existingErrorMessage + ", " + newErrorMessage); // 같은 필드의 에러 메시지를 결합합니다.
                });

        // 에러 목록을 ErrorStatus와 함께 예외를 처리합니다.
        return handleExceptionInternalArgs(e, HttpHeaders.EMPTY, ErrorStatus.valueOf("_BAD_REQUEST"), request, errors);
    }



    // 일반적인 Exception을 처리하는 메서드입니다.
    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e, WebRequest request) {
        e.printStackTrace(); // 예외의 스택 트레이스를 출력합니다.

        // 내부 서버 오류 상태로 예외를 처리합니다.
        return handleExceptionInternalFalse(e, ErrorStatus._INTERNAL_SERVER_ERROR, HttpHeaders.EMPTY, ErrorStatus._INTERNAL_SERVER_ERROR.getHttpStatus(), request, e.getMessage());
    }



    // GeneralException을 처리하는 메서드입니다.
    @ExceptionHandler(value = GeneralException.class)
    public ResponseEntity<Object> onThrowException(GeneralException generalException, HttpServletRequest request) {
        // GeneralException에서 오류 이유와 HTTP 상태를 가져옵니다.
        ErrorReasonDto errorReasonHttpStatus = generalException.getErrorReasonHttpStatus();
        // 예외를 처리합니다.
        return handleExceptionInternal(generalException, errorReasonHttpStatus, null, request);
    }


    // 예외 처리를 위한 공통 메서드입니다.
    private ResponseEntity<Object> handleExceptionInternal(Exception e, ErrorReasonDto reason, HttpHeaders headers, HttpServletRequest request) {
        // ApiResponse 객체를 생성하여 실패 응답을 작성합니다.
        ApiResponse<Object> body = ApiResponse.onFailure(reason.getCode(), reason.getMessage(), null);

        // WebRequest 객체를 생성합니다.
        WebRequest webRequest = new ServletWebRequest(request);
        // ResponseEntityExceptionHandler의 handleExceptionInternal 메서드를 호출하여 예외를 처리합니다.
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                reason.getHttpStatus(),
                webRequest
        );
    }

    // 예외 처리를 위한 공통 메서드입니다. (추가적인 오류 포인트 포함)
    private ResponseEntity<Object> handleExceptionInternalFalse(Exception e, ErrorStatus errorCommonStatus, HttpHeaders headers, HttpStatus status, WebRequest request, String errorPoint) {
        // ApiResponse 객체를 생성하여 실패 응답을 작성합니다.
        ApiResponse<Object> body = ApiResponse.onFailure(errorCommonStatus.getCode(), errorCommonStatus.getMessage(), errorPoint);
        // ResponseEntityExceptionHandler의 handleExceptionInternal 메서드를 호출하여 예외를 처리합니다.
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                status,
                request
        );
    }

    // 예외 처리를 위한 공통 메서드입니다. (인자 오류 포함)
    private ResponseEntity<Object> handleExceptionInternalArgs(Exception e, HttpHeaders headers, ErrorStatus errorCommonStatus, WebRequest request, Map<String, String> errorArgs) {
        // ApiResponse 객체를 생성하여 실패 응답을 작성합니다.
        ApiResponse<Object> body = ApiResponse.onFailure(errorCommonStatus.getCode(), errorCommonStatus.getMessage(), errorArgs);
        // ResponseEntityExceptionHandler의 handleExceptionInternal 메서드를 호출하여 예외를 처리합니다.
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                errorCommonStatus.getHttpStatus(),
                request
        );
    }

    // 예외 처리를 위한 공통 메서드입니다. (ConstraintViolationException 포함)
    private ResponseEntity<Object> handleExceptionInternalConstraint(Exception e, ErrorStatus errorCommonStatus, HttpHeaders headers, WebRequest request) {
        // ApiResponse 객체를 생성하여 실패 응답을 작성합니다.
        ApiResponse<Object> body = ApiResponse.onFailure(errorCommonStatus.getCode(), errorCommonStatus.getMessage(), null);
        // ResponseEntityExceptionHandler의 handleExceptionInternal 메서드를 호출하여 예외를 처리합니다.
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                errorCommonStatus.getHttpStatus(),
                request
        );
    }
}
