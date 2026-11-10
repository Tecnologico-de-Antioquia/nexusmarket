package nexusmarket.application.adapters.rest.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import nexusmarket.application.domain.exceptions.DomainException;
import nexusmarket.application.domain.exceptions.EntityNotFoundException;
import nexusmarket.application.domain.exceptions.InsufficientStockException;
import nexusmarket.application.domain.exceptions.InvalidCredentialsException;
import nexusmarket.application.domain.exceptions.InvalidRequestException;
import nexusmarket.application.domain.exceptions.InvalidStateTransitionException;
import nexusmarket.application.domain.exceptions.ResourceAlreadyExistsException;
import nexusmarket.application.domain.exceptions.UnauthorizedOperationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentials(
            InvalidCredentialsException exception, HttpServletRequest request) {
        return build(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", exception.getMessage(), request, null);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            EntityNotFoundException exception, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, "NOT_FOUND", exception.getMessage(), request, null);
    }

    @ExceptionHandler(UnauthorizedOperationException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedOperation(
            UnauthorizedOperationException exception, HttpServletRequest request) {
        return build(HttpStatus.FORBIDDEN, "FORBIDDEN", exception.getMessage(), request, null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(
            AccessDeniedException exception, HttpServletRequest request) {
        return build(HttpStatus.FORBIDDEN, "FORBIDDEN", "No tiene permisos para realizar esta operacion.",
                request, null);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleConflict(
            ResourceAlreadyExistsException exception, HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, "CONFLICT", exception.getMessage(), request, null);
    }

    @ExceptionHandler(InvalidStateTransitionException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidState(
            InvalidStateTransitionException exception, HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, "INVALID_STATE_TRANSITION", exception.getMessage(), request, null);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientStock(
            InsufficientStockException exception, HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, "INSUFFICIENT_STOCK", exception.getMessage(), request, null);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidRequest(
            InvalidRequestException exception, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", exception.getMessage(), request, null);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Map<String, Object>> handleDomain(
            DomainException exception, HttpServletRequest request) {
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "DOMAIN_ERROR", exception.getMessage(), request, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException exception, HttpServletRequest request) {
        List<Map<String, String>> details = exception.getBindingResult().getFieldErrors().stream()
                .map(this::fieldError)
                .toList();
        return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR",
                "La solicitud contiene datos invalidos.", request, details);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(
            ConstraintViolationException exception, HttpServletRequest request) {
        List<Map<String, String>> details = exception.getConstraintViolations().stream()
                .map(violation -> Map.of(
                        "field", violation.getPropertyPath().toString(),
                        "message", violation.getMessage()))
                .toList();
        return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR",
                "La solicitud contiene datos invalidos.", request, details);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Map<String, Object>> handleUnreadable(
            Exception exception, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "INVALID_REQUEST",
                "La solicitud no tiene un formato valido.", request, null);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoResource(
            NoResourceFoundException exception, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, "NOT_FOUND", "El recurso solicitado no existe.", request, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleUnexpected(
            Exception exception, HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR",
                "Ocurrio un error inesperado.", request, null);
    }

    private Map<String, String> fieldError(FieldError fieldError) {
        return Map.of(
                "field", fieldError.getField(),
                "message", fieldError.getDefaultMessage() == null ? "Valor invalido" : fieldError.getDefaultMessage());
    }

    private ResponseEntity<Map<String, Object>> build(
            HttpStatus status,
            String code,
            String message,
            HttpServletRequest request,
            List<Map<String, String>> details) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("code", code);
        body.put("message", message);
        body.put("path", request.getRequestURI());
        body.put("requestId", resolveRequestId(request));
        if (details != null) {
            body.put("details", details);
        }
        return ResponseEntity.status(status)
                .header("X-Request-Id", (String) body.get("requestId"))
                .body(body);
    }

    private String resolveRequestId(HttpServletRequest request) {
        String requestId = request.getHeader("X-Request-Id");
        return requestId == null || requestId.isBlank() ? UUID.randomUUID().toString() : requestId;
    }
}
