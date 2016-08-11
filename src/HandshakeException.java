/**
 * Created by roman on 30.07.16.
 */
public class HandshakeException extends RuntimeException {
    public HandshakeException(String message) {
        super(message);
    }

    public HandshakeException(String message, Throwable cause) {
        super(message, cause);
    }
}
