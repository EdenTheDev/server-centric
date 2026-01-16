package cyclenest.osrm;

/**
 * Custom exception to satisfy Part B/C requirements for 
 * robust error handling[cite: 49, 82].
 */
public class OsrmServiceException extends OsrmException {
    
    public OsrmServiceException(String message) {
        super(message, OsrmException.ErrorType.SERVICE_UNAVAILABLE);
    }

    public OsrmServiceException(String message, Throwable cause) {
        super(message, OsrmException.ErrorType.SERVICE_UNAVAILABLE, cause);
    }
}