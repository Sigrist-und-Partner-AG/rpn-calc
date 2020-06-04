package ch.bztf;

/**
 * @author Timothy R. Schmid
 * 
 * 
 */
public class RPNCalc {

    /* The maximum output precision allowed. */
    private static final int MAX_PRECISION = 15;

    /**
     * The result of the previous calculation.
     * This is overwritten every time an RPN
     * expression is successfully evaluated.
     */
    private double result;

    /**
     * The output precision of {@link #result}
     * as the number of decimal places, ranging
     * from 0 to {@link #MAX_PRECISION}.
     */
    private int precision;

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
     * Shifts the output precision by a given number of
     * decimal places.
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
}