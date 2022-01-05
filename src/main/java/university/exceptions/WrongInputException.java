package university.exceptions;

/**
 * Custom exception
 * Used for wrong input parameters in controller
 */
public class WrongInputException extends Exception {
    public WrongInputException(String string) {
        super(string);
    }
}
