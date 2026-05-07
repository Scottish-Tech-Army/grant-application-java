package org.tfg.grant_java.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.tfg.grant_java.config.ErrorConfig;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@WebMvcTest(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {GlobalExceptionHandler.class, ErrorConfig.class})
class GlobalExceptionHandlerTest {

    @Test
    void shouldReturnMappedErrorDetails_whenErrorCodeExists() {
        // Arrange
        ErrorConfig errorConfig = mock(ErrorConfig.class);

        ErrorDetails mapped = ErrorDetails.builder()
                .errorCode("1001")
                .message("Invalid request")
                .errorType("ValidationError")
                .httpStatus(400)
                .build();

        when(errorConfig.getErrorDetails()).thenReturn(List.of(mapped));

        GlobalExceptionHandler handler = new GlobalExceptionHandler(errorConfig);

        // Act
        var response = handler.handleServiceException(new ServiceException("1001"));

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrorCode()).isEqualTo("1001");
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid request");
        assertThat(response.getBody().getErrorType()).isEqualTo("ValidationError");
        assertThat(response.getBody().getHttpStatus()).isEqualTo(400);

        verify(errorConfig).getErrorDetails();
    }

    @Test
    void shouldReturnDefaultError_whenErrorCodeIsUnknown() {
        // Arrange: config contains no matching code
        ErrorConfig errorConfig = mock(ErrorConfig.class);
        when(errorConfig.getErrorDetails()).thenReturn(List.of());

        GlobalExceptionHandler handler = new GlobalExceptionHandler(errorConfig);

        // Act
        var response = handler.handleServiceException(new ServiceException("does-not-exist"));

        // Assert: must return defaultErrorDetails (as per your handler)
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrorCode()).isEqualTo("9999");
        assertThat(response.getBody().getMessage())
                .isEqualTo("An unexpected error occurred. Please try again later.");
        assertThat(response.getBody().getErrorType()).isEqualTo("UnknownError");
        assertThat(response.getBody().getHttpStatus()).isEqualTo(200);

        verify(errorConfig).getErrorDetails();
    }
}