package ch.bztf;

/**
 * @author Timothy R. Schmid
 */
public class RPNCalcException extends Exception {

    /** Necessary as {@code Exception} implements {@code Serializable}. */
    private static final long serialVersionUID = 1L;

    /**
     * Exception constructor that takes a plain error message.
     * 
     * @param message The error message. It can be retrieved via
     *                {@link Exception#getMessage()}.
     */
    public RPNCalcException(String message) {
        super(message);
    }

    /**
     * Exception constructor that takes an error message in
     * addition to arguments detailing the exact position.
     * 
     * @param message The error message. 
     *                It can be retrieved via {@link Exception#getMessage()}.
     * @param token The specific token in the RPN expression that caused the error.
     * @param index The corresponding index for {@code token}.
     */
    public RPNCalcException(String message, String token, int index) {
        super(String.format("%s ('%s' at index %d)", message, token, index));
    }
}