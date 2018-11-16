package fr.istic.gm.weassert.test.exception;

/**
 * The main application exception
 */
public class WeAssertException extends RuntimeException {

    public WeAssertException(String message) {
        super(message);
    }

    public WeAssertException(String message, Throwable cause) {
        super(message, cause);
    }
}
