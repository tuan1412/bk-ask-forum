package ptpmcn.errorhandling;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errors = new ArrayList<String>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}
		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Argument not vaild", errors);
		return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex,WebRequest request){
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), "Not found resource");
		return new ResponseEntity<ApiError>(apiError, new HttpHeaders(), apiError.getStatus());	
	}
	
	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ResponseEntity<ApiError> handleEmptyDeleteResult(EmptyResultDataAccessException ex, WebRequest request){
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Argument not vaild", "Not delete resource");
		return new ResponseEntity<ApiError>(apiError, new HttpHeaders(), apiError.getStatus());
	}
	
	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ApiError> handleForbiddenException(ForbiddenException ex, WebRequest request){
		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ex.getMessage(), "Action forbiddent");
		return new ResponseEntity<ApiError>(apiError, new HttpHeaders(), apiError.getStatus());
	}
	
	@ExceptionHandler(FileUploadException.class)
	public ResponseEntity<ApiError> handleFileUpload(EmptyResultDataAccessException ex, WebRequest request){
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), "File upload error");
		return new ResponseEntity<ApiError>(apiError, new HttpHeaders(), apiError.getStatus());
	}
}
