package osrm;

public class OsrmException extends Exception {

    public OsrmException(String message) {
        super(message);
    }

    public OsrmException(String message, Throwable cause) {
        super(message, cause);
    }
}