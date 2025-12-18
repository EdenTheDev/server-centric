package osrm;

/**
 * Enhanced Exception for OSRM operations.
 * Includes error codes to differentiate between client and server issues (QoS).
 */
public class OsrmException extends Exception {

    public enum ErrorType {
        INVALID_COORDINATES,
        SERVICE_UNAVAILABLE,
        UNEXPECTED_ERROR
    }

    private final ErrorType errorType;

    public OsrmException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public OsrmException(String message, ErrorType errorType, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}