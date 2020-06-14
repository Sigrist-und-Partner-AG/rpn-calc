package ch.bztf;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author Timothy R. Schmid
 */
public class RPNCalc {

    /** The maximum output precision allowed. */
    public static final int MAX_PRECISION = 15;

    /** The default output precision. */
    private static final int DEFAULT_PRECISION = 3;

    /**
     * The output precision of {@link #result} as the number of decimal places,
     * ranging from 0 to {@link #MAX_PRECISION}.
     */
    private int precision = DEFAULT_PRECISION;

    /**
     * The immediate result of the previous calculation.
     * This is overwritten every time an RPN expression is successfully evaluated,
     * as long as exactly one operand remains on the stack (perfect evaluation).
     * This field is {@code null} if this is not the case.
     */
    private Double result = null;

    /** The complete result of the previous calculation. */
    private Stack<Double> leftover = null;

    /** The registers from "a" to "z" (lowercase only) for saving values. */
    private Map<String, Double> registers = new HashMap<String, Double>();

    /** Constructor that sets all fields to default values. */
    public RPNCalc() {
        clearRegisters();
    }

    /**
     * Constructor that takes an initial precision value.
     * 
     * @param precision The initial precision to be set.
     * @throws IllegalArgumentException Raised if the precision is invalid.
     */
    public RPNCalc(int precision) throws IllegalArgumentException {
        setPrecision(precision);
        clearRegisters();
    } 

    /** 
     * Clears all default registers by setting them to zero.
     * It is legal to use this function for initialization.
     * <p>
     * Note that this function never affects manually added registers.
     * </p>
     */
    public void clearRegisters() {
        for (char c = 'a'; c <= 'z'; c++) {
            this.registers.put(c + "", 0.0);
        }
    }

    /**
     * Gets the value of the specified register.
     * 
     * @param reg The register to be queried.
     * @return The value of register {@code reg}.
     *         {@code null} is returned if it does not exist.
     */
    public Double getRegister(String reg) {
        return registers.get(reg);
    }

    /**
     * Stores a given value in the specified register.
     * 
     * @param reg The register the value will be stored in.
     * @param val The value to be stored in {@code reg}.
     * @return Whether the assignment to {@code reg} succeeded.
     *         {@code false} is returned if the register does not exist.
     */
    public boolean setRegister(String reg, Double val) {
        if (registers.containsKey(reg)) {
            registers.put(reg, val);
            return true;
        }
        return false;
    }

    /**
     * Adds a new register and initializes it to the given value.
     * 
     * @param reg The name of the register to be added.
     * @param val The initial value to be stored in {@code reg}.
     * @return Whether the creation of {@code reg} succeeded.
     *         {@code false} is returned if the register already existed,
     *         in which case the initialization will also not take place.
     */
    public boolean addRegister(String reg, Double val) {
        if (!registers.containsKey(reg)) {
            registers.put(reg, val);
            return true;
        }
        return false;
    }

    /**
     * Helper function which validates the range of a given precision.
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
     * @param places The amount of places the current output precision is to 
     *               be shifted by. This value may be positive or negative. 
     *               A positive value means that more places are added.
     * @return The new precision.
     * @throws IllegalArgumentException Raised if the new precision is invalid.
     */
    public int shiftPrecision(int places) throws IllegalArgumentException {
        int new_precision = this.precision + places;
        checkPrecision(new_precision);
        return (this.precision = new_precision);
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
     * Gets the last result in numerical form.
     * 
     * @return The previous calculation result.
     *         {@code null} if none is available. 
     */
    public Double getLastResult() {
        return this.result;
    }

    /**
     * Retrieves the last result as a String and formats
     * the output using the currently set precision.
     * 
     * @return The fully formatted result.
     *         The empty string is returned if none is available.
     */
    public String getFormattedLastResult() {
        return formatNumber(this.result);
    }

    /**
     * Retrieves the complete result stack of the last calculation.
     * 
     * @return The result stack. If {@link #getLastResult()} returns a 
     *         non-{@code null} value, it will hold exactly one element.
     */
    public Stack<Double> getLastStack() {
        return this.leftover;
    }

    /**
     * Retrieves the complete result stack of the last calculation as a
     * string and formats each value using the currently set precision.
     * The values are delimited by a single space each.
     * 
     * @return The fully formatted result stack.
     *         The empty string is returned if it is empty or none is available.
     */
    public String getFormattedLastStack() {
        if (this.leftover != null) {
            @SuppressWarnings("unchecked")                    // Necessary for cast
            var stack = (Stack<Double>)this.leftover.clone(); // Don't mutate field
            var builder = new StringBuilder(); // More efficient for concatenation
            while (!stack.isEmpty()) {
                builder.insert(0, formatNumber(stack.pop()));
                if (!stack.isEmpty()) {
                    builder.insert(0, " ");
                }
            }
            return builder.toString();
        }
        return "";
    }

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
     * Stores the topmost value of the stack in the passed register.
     * 
     * @param stack The stack holding any number of possible operands.
     * @param reg The register the topmost stack value will be stored in.
     * @return The topmost value of the stack. It is only peeked, not popped.
     * @throws EmptyStackException Raised on stack underflow.
     */
    private Double store(Stack<Double> stack, String reg) throws EmptyStackException {
        Double operand = stack.peek();
        setRegister(reg, operand);
        return operand;
    }

    /**
     * Helper function to check whether the array index following
     * a given register contains a 'store' operation. 
     * This is necessary in order to determine whether the register
     * at {@code reg_index} should be treated as an assignable lvalue.
     * 
     * @param tokens An RPN expression as an array of tokens.
     * @param reg_index The index the register in question appeared at.
     * @return {@code true} if the next index contains a 'store' operation,
     *         {@code false} otherwise. {@code false} is also returned if
     *         the array does not contain any further elements.
     */
    private boolean peek4Store(String[] tokens, int reg_index) {
        return (
            tokens.length > reg_index + 1 &&
            tokens[reg_index + 1].equals("<-") 
        );
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
    public Double eval(String expr) throws RPNCalcException {

        /* Save registers so we can restore them in case of error. */
        Map<String, Double> saved_regs = new HashMap<String, Double>(this.registers);

        var stack = new Stack<Double>();  // Operand stack
        String[] tokens = tokenize(expr); // Iterable expression
        int i = 0;                        // Token index

        try {

           /*
            * The algorithm abides by the following rules:
            * 1. Literal values are pushed onto the stack.
            * 2. If a register is followed by a '<-' token,
            *    the topmost value of the stack is stored in it.
            *    Otherwise, the register is pushed onto the stack.
            * 3. Operators pop the required number of operands off
            *    the stack and push the result back onto the stack.
            */
            for (; i < tokens.length; i++) {
                try {
                    stack.push(Double.parseDouble(tokens[i])); // Literals are operands
                } catch (NumberFormatException e) {
                    Double reg_val = getRegister(tokens[i]);   // Check if register
                    if (reg_val != null) {
                        if (peek4Store(tokens, i)) {  
                            store(stack, tokens[i]); // Treat register as lvalue
                            i++;                     // Skip next token (store)
                        } else {
                            stack.push(reg_val);     // Treat register as rvalue
                        }
                    } else {              
                        stack.push(apply(stack, tokens[i]));   // Apply operator
                    }
                }
            } // end for()

        } catch (EmptyStackException e) {
            this.registers = saved_regs;
            throw new RPNCalcException("Stack underflow", tokens[i], i);
        } catch (ArithmeticException e) {
            this.registers = saved_regs;
            throw new RPNCalcException("Division by zero", tokens[i], i);
        } catch (IllegalArgumentException e) {
            this.registers = saved_regs;
            throw new RPNCalcException("Unrecognized symbol", tokens[i], i);
        }

        this.leftover = stack;                                   // Complete result
        this.result = (stack.size() == 1 ? stack.peek() : null); // Immediate result 
        return this.result;
    }
}