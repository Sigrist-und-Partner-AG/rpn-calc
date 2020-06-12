package ch.bztf;

/**
 * @author Timothy R. Schmid
 * 
 * 
 */
public class RPNCalc {

    /** The maximum output precision allowed. */
    public static final int MAX_PRECISION = 15;

    /** The default output precision. */
    private static final int DEFAULT_PRECISION = 3;

    /**
     * The result of the previous calculation.
     * This is overwritten every time an RPN
     * expression is successfully evaluated.
     * This field is {@code null} if no result
     * is available.
     */
    private Double result = null;

    /**
     * The output precision of {@link #result}
     * as the number of decimal places, ranging
     * from 0 to {@link #MAX_PRECISION}.
     */
    private int precision = DEFAULT_PRECISION;

    /** Constructor that sets all fields to default values. */
    public RPNCalc() {}

    /**
     * Constructor that takes an initial precision value.
     * 
     * @param precision The initial precision to be set.
     * @throws IllegalArgumentException Raised if the precision is invalid.
     */
    public RPNCalc(int precision) throws IllegalArgumentException {
        setPrecision(precision);
    } 

    /**
     * Helper function which validates that a given
     * precision is in the permitted range.
     * 
     * @param precision The precision to be checked.
     * @throws IllegalArgumentException Raised if the precision is invalid.
     */
    private void checkPrecision(int precision) throws IllegalArgumentException {
        if (precision < 0 || precision > MAX_PRECISION) {
            throw new IllegalArgumentException(String.format("Precision %d is out of range", precision));
        }
    }

    /**
     * Gets the current output precision.
     * 
     * @return The output precision.
     */
    public int getPrecision() {
        return this.precision;
    }

    /**
     * Sets the output precision to a given value.
     * 
     * @param precision The precision to be set.
     * @return The new precision, given by {@code precision}.
     * @throws IllegalArgumentException Raised if the new precision is invalid.
     */
    public int setPrecision(int precision) throws IllegalArgumentException {
        checkPrecision(precision);
        return (this.precision = precision);
    }

    /**
     * Shifts the output precision by a given number of decimal places.
     * 
     * @param places The amount of places the current output
     *               precision is to be shifted by. This value
     *               may be positive or negative. A positive
     *               value means that more places are added.
     * @return The new precision.
     * @throws IllegalArgumentException Raised if the new precision is invalid.
     */
    public int shiftPrecision(int places) throws IllegalArgumentException {
        int new_precision = this.precision + places;
        checkPrecision(new_precision);
        return (this.precision = new_precision);
    }

    /**
     * Gets the last result in numerical form.
     * 
     * @return The previous calculation result.
     *         {@code null} if none is available. 
     */
    public Double getLastResult() {
        return this.result;
    }

    /**
     * Converts a given double to a string and formats
     * the output using the currently set precision.
     * 
     * @param num The number to be formatted.
     * @return The fully formatted number.
     *         The empty string is returned if {@code null}
     *         is passed in for {@code num}.
     */
    private String formatNumber(Double num) {
        return (num == null) ? "" : String.format("%." + this.precision + "f", num);
    }

    /**
     * Retrieves the last result as a String and formats
     * the output using the currently set precision.
     * 
     * @return The fully formatted result.
     *         The empty string is returned if {@code null}
     *         is passed in for {@code num}.
     */
    public String getFormattedLastResult() {
        return formatNumber(this.result);
    }

    // getFormattedLastSequence

    /**
     * Evaluates a full RPN expression.
     * 
     * @param expr The RPN expression in string form.
     * @return The immediate result in numerical form.
     *         If the expression cannot be reduced to a
     *         single number, {@code null} is returned.
     */
    public Double eval(String expr) {
        // do the tricky stuff here...
        /*
        var tokens = split(expr);
        for t in tokens {
            <modify stack>
            <error on stack underflow>
        }
        <print remaining tokens + trailing whitespace>
        */
        return 0.0;
    }
}