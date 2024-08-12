package com.example.taskmanagerstudent.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {
//    @Autowired
//    private MessageSource messageSource;
//
//    @ExceptionHandler(AppException.class)
//    ResponseEntity<ApiResponse> handleAppException(AppException exception, HttpServletRequest request) {
//        Locale locale = LocaleContextHolder.getLocale();
//        ErrorCode errorCode = exception.getErrorCode();
//        ApiResponse apiResponse = new ApiResponse();
//        apiResponse.setCode(errorCode.getCode());
//        apiResponse.setMessage(messageSource.getMessage(errorCode.name(), null, locale));
//        return ResponseEntity
//                .status(errorCode.getStatus())
//                .body(apiResponse);
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(MethodArgumentNotValidException exception) {
//        Locale locale = LocaleContextHolder.getLocale();
//        Map<String, String> errors = new HashMap<>();
//        exception.getBindingResult().getFieldErrors().forEach(error -> {
//            String fieldName = error.getField();
//            String errorMessage = messageSource.getMessage(error.getDefaultMessage(), null, locale);
//            errors.put(fieldName, errorMessage);
//        });
//
//        ApiResponse<Map<String, String>> apiResponse = new ApiResponse<>();
//        apiResponse.setCode(ErrorCode.INVALID_KEY.getCode());
//        apiResponse.setMessage(messageSource.getMessage("error.invalidInput", null, locale));
//        apiResponse.setResult(errors);
//
//        return ResponseEntity.status(ErrorCode.INVALID_KEY.getStatus()).body(apiResponse);
//    }
//
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<ApiResponse<String>> handleRuntimeException(Exception ex) {
//        Locale locale = LocaleContextHolder.getLocale();
//        String errorMessage = messageSource.getMessage("error.uncategorized", null, locale);
//
//        ApiResponse<String> apiResponse = new ApiResponse<>();
//        apiResponse.setResult(null);
//        apiResponse.setMessage(errorMessage);
//
//        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
