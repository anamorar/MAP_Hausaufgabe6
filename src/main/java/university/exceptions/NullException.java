package university.exceptions;

/**
 * Custom exception
 * This exception will be thrown when the received parameter is null
 */
public class NullException extends Exception {
    public NullException (String string) {
        super(string);
    }
}
