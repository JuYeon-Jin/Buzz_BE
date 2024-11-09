package com.buzz.community.exception;

import com.buzz.community.dto.error.ErrorResponseDTO;
import com.buzz.community.exception.post.DuplicateFileNameException;
import com.buzz.community.exception.post.FileCountLimitExceededException;
import com.buzz.community.exception.post.FileSizeLimitExceededException;
import com.buzz.community.exception.post.NotAllowedException;
import com.buzz.community.exception.user.*;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    /*
    *   400 Bad Request: 클라이언트가 잘못된 요청을 보냈을 때.
        401 Unauthorized: 인증되지 않은 요청.
        403 Forbidden: 권한이 없는 요청.
        404 Not Found: 리소스를 찾을 수 없을 때.
        409 Conflict: 요청이 현재 서버의 상태와 충돌할 때.
        500 Internal Server Error: 서버 내부에서 문제가 발생했을 때.
    * */


    // 1. IllegalArgumentException 핸들러
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgument(IllegalArgumentException e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "ERROR_CODE_0001",
                "잘못된 요청입니다: " + e.getMessage() + ". 요청 파라미터를 확인하세요."
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // 2. Exception 핸들러 (기본적으로 모든 예외 처리)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralException(Exception e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "ERROR_CODE_0002",
                "서버에서 예상치 못한 오류가 발생했습니다. 오류 메시지: " + e.getMessage() +
                        ". 서버관리자에게 문의하세요."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    // 3. DataAccessException 핸들러 (데이터베이스 오류 처리)
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataAccessException(DataAccessException e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "ERROR_CODE_0003",
                "데이터베이스 오류가 발생했습니다: " + e.getMessage() +
                        ". 데이터베이스 연결 또는 쿼리를 확인하세요."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    // 4. UserNotFoundException 핸들러 (사용자 찾기 실패 처리)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFound(UserNotFoundException e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "ERROR_CODE_0004",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


    // 5. InvalidPasswordException 핸들러 (비밀번호 오류)
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidPassword(InvalidPasswordException e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "ERROR_CODE_0005",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    // 6. DuplicateUsernameException 핸들러 (중복된 사용자 이름 처리)
    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateUsername(DuplicateUsernameException e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "ERROR_CODE_0006",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    // 7. InvalidInputException 핸들러 (잘못된 입력 처리)
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidInput(InvalidInputException e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "ERROR_CODE_0007",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // 8. DuplicateFileNameException 핸들러 (중복된 파일 이름 처리)
    @ExceptionHandler(DuplicateFileNameException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateFileName(DuplicateFileNameException e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "ERROR_CODE_0008",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // 9. InvalidTokenException 핸들러 (토큰 유효성 오류)
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidToken(InvalidTokenException e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "ERROR_CODE_0009",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    // 10. FileSizeLimitExceededException 핸들러 (파일 크기 초과 오류)
    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<ErrorResponseDTO> handleFileSizeLimitExceeded(FileSizeLimitExceededException e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "ERROR_CODE_0010",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // 11. FileCountLimitExceededException 핸들러 (파일 개수 초과 오류)
    @ExceptionHandler(FileCountLimitExceededException.class)
    public ResponseEntity<ErrorResponseDTO> handleFileCountLimitExceeded(FileCountLimitExceededException e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "ERROR_CODE_0011",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // 12. NotAllowedException 핸들러 (허가되지 않은 요청에 대한 핸들러)
    @ExceptionHandler(NotAllowedException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotAllowedRequest (NotAllowedException e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "ERROR_CODE_0012",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

}
