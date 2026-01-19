package cyclenest.osrm;

/**
 * OsrmException - Custom exception for handling OSRM-specific failures.
 * This helps us distinguish between a network timeout and a bad response.
 */
public class OsrmException extends Exception {
    
    // Categorising different ways the routing service might fail
    public enum ErrorType {
        SERVICE_UNAVAILABLE,
        INVALID_RESPONSE,
        TIMEOUT
    }

    private final ErrorType type;

    // Standard constructor for basic error messages
    public OsrmException(String message, ErrorType type) {
        super(message);
        this.type = type;
    }

    // Constructor that keeps the 'cause' (stack trace) for easier debugging
    public OsrmException(String message, ErrorType type, Throwable cause) {
        super(message, cause);
        this.type = type;
    }

    public ErrorType getType() {
        return type;
    }
}