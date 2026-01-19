package cyclenest.osrm;

/**
 * OsrmServiceException - Specifically for when the external Docker service
 * is completely unreachable or offline.
 * * This helps us handle "Service Unavailable" states separately from 
 * logic errors in our own code.
 */
public class OsrmServiceException extends OsrmException {
    
    // Constructor for simple error messages
    public OsrmServiceException(String message) {
        super(message, OsrmException.ErrorType.SERVICE_UNAVAILABLE);
    }

    // Constructor that wraps the original error (like a Connection Refused)
    public OsrmServiceException(String message, Throwable cause) {
        super(message, OsrmException.ErrorType.SERVICE_UNAVAILABLE, cause);
    }
}