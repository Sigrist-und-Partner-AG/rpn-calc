package ch.bztf;

import java.util.EmptyStackException;
import java.util.Stack;

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
     * Checks whether a given operator has unary arity.
     * 
     * @param op The operator in string form.
     * @return {@code true} if {@code op} is a unary operator,
     *         {@code false} otherwise. 
     */
    private boolean isUnary(String op) {
        switch (op) {
            case "neg":
            case "pow2":
            case "sqrt":
                return true;
            default:
                return false;
        }
    }

    /**
     * Checks whether a given operator has binary arity.
     * 
     * @param op The operator in string form.
     * @return {@code true} if {@code op} is a binary operator,
     *         {@code false} otherwise. 
     */
    private boolean isBinary(String op) {
        switch (op) {
            case "+":
            case "-":
            case "*":
            case "/":
            case "%":
            case "pow":
                return true;
            default:
                return false;
        }
    }

    /**
     * Applies an operator to a stack in its current state.
     * As many operands are popped off the stack as needed.
     * 
     * @param stack The stack holding any number of possible operands.
     * @param op The operator to be applied.
     *           Both unary and binary operators are supported.
     * @return The calculation result of the operator application. 
     * @throws EmptyStackException Raised on stack underflow.
     * @throws ArithmeticException Raised in the event of division by zero.
     * @throws IllegalArgumentException Raised if {@code op} is an unknown operator.
     */
    private Double apply(Stack<Double> stack, String op) 
    throws EmptyStackException, ArithmeticException, IllegalArgumentException {
        if (isUnary(op)) {
            Double operand = stack.pop();
            switch (op) {
                case "neg":  return -operand;
                case "pow2": return operand * operand;
                case "sqrt": return Math.sqrt(operand);
            }
        } else if (isBinary(op)) {
            Double right = stack.pop();
            Double left  = stack.pop();
            switch (op) {
                case "+": return left + right;
                case "-": return left - right;
                case "*": return left * right;
                case "/": return left / right;
                case "%": return left % right;
                case "pow": return Math.pow(left, right);
            }
        }
        throw new IllegalArgumentException("Unknown operator '" + op + "'");
    }

    /**
     * Splits a given RPN expression into a number of
     * string tokens, with whitespace as the delimiter.
     * Leading and trailing whitespace is stripped. 
     * 
     * @param expr The expression to be tokenized.
     * @return The resulting array of tokens.
     */
    private String[] tokenize(String expr) {
        return expr.trim().split("\\s+");
    }

    /**
     * Evaluates a full RPN expression.
     * 
     * @param expr The RPN expression in string form.
     * @return The immediate result in numerical form.
     *         If the expression cannot be reduced to a
     *         single number, {@code null} is returned.
     */
    public Double eval(String expr) {

        var stack = new Stack<Double>();
        String[] tokens = tokenize(expr);

        for (String token : tokens) {
            /* Push literal operands onto the stack */
            try {
                stack.push(Double.parseDouble(token));
            } catch (NumberFormatException e) {


                // apply();
            }
        } 

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