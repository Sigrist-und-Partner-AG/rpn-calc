package ch.bztf;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * This is the test suite for the {@link RPNCalc} class.
 * The tests are executed using the standard JUnit framework.
 * 
 * @author Timothy R. Schmid
 */
public class RPNCalcTest {
    
    /** The delta to be used in double assertions using an epsilon. */
    private static final double delta = 1.0e-15;

    /** The calculator instance under test. */
    private RPNCalc calc;

    @Before
    public void setUp() {
        calc = new RPNCalc();
    }

    @Test
    public void testSettingPrecisionReturnsCorrectValue() {
        /* Set via constructor */
        assertEquals(11, new RPNCalc(11).getPrecision());
        /* Set via absolute value */
        assertEquals(0, calc.setPrecision(0));
        assertEquals(RPNCalc.MAX_PRECISION, calc.setPrecision(RPNCalc.MAX_PRECISION));
        /* Set via relative value */
        calc.setPrecision(1);
        assertEquals(0, calc.shiftPrecision(-1));
        calc.setPrecision(RPNCalc.MAX_PRECISION-1);
        assertEquals(RPNCalc.MAX_PRECISION, calc.shiftPrecision(+1));
    }

    @Test
    public void testSettingPrecisionThrowsOnOutOfBounds() {
        /* Set via constructor */
        assertThrows(IllegalArgumentException.class, () -> new RPNCalc(-1));
        assertThrows(IllegalArgumentException.class, () -> new RPNCalc(RPNCalc.MAX_PRECISION+1));
        /* Set via absolute value */
        assertThrows(IllegalArgumentException.class, () -> calc.setPrecision(-1));
        assertThrows(IllegalArgumentException.class, () -> calc.setPrecision(RPNCalc.MAX_PRECISION+1));
        /* Set via relative value */
        calc.setPrecision(0);
        assertThrows(IllegalArgumentException.class, () -> calc.shiftPrecision(-1));
        calc.setPrecision(RPNCalc.MAX_PRECISION);
        assertThrows(IllegalArgumentException.class, () -> calc.shiftPrecision(+1));
    }

    @Test
    public void testConstructorInitializesDefaultRegisters() {
        for (char c = 'a'; c <= 'z'; c++) {
            assertEquals(0.0, calc.getRegister(c + ""), 0.0);
        }
    }

    @Test
    public void testAccessingValidRegisterReturnsCorrectValue() {
        assertTrue(calc.setRegister("a", 1.0));
        assertEquals(1.0, calc.getRegister("a"), 0.0);
    }

    @Test
    public void testAccessingInvalidRegisterIsNoOp() {
        assertFalse(calc.setRegister("A", 1.0));
        assertNull(calc.getRegister("A"));
    }

    @Test
    public void testAddingNewRegisterStoresCorrectValue() {
        assertTrue(calc.addRegister("PI", Math.PI));
        assertEquals(Math.PI, calc.getRegister("PI"), 0.0);
    }

    @Test
    public void testAddingExistingRegisterIsNoOp() {
        /* Default register */
        assertFalse(calc.addRegister("e", 3.0));
        assertEquals(0.0, calc.getRegister("e"), 0.0);
        /* Manually added register */
        assertTrue(calc.addRegister("E", Math.E));
        assertFalse(calc.addRegister("E", 0.0));
        assertEquals(Math.E, calc.getRegister("E"), 0.0);
    }

    @Test
    public void testEvaluatingUnaryExpressionReturnsCorrectResult() throws RPNCalcException {
        assertEquals(-6.0, calc.eval("6 neg"),    delta);
        assertEquals( 0.9, calc.eval("-0.9 abs"), delta);
        assertEquals(25.0, calc.eval("5 pow2"),   delta);
        assertEquals(12.0, calc.eval("144 sqrt"), delta);
    }

    @Test
    public void testEvaluatingBinaryExpressionReturnsCorrectResult() throws RPNCalcException {
        assertEquals( 5.0, calc.eval("2 3 +"),   delta);
        assertEquals(-1.0, calc.eval("2 3 -"),   delta);
        assertEquals( 1.5, calc.eval("3 2 /"),   delta);
        assertEquals(12.0, calc.eval("4 3 *"),   delta);
        assertEquals( 2.0, calc.eval("10 4 %"),  delta);
        assertEquals( 8.0, calc.eval("2 3 pow"), delta);
    }

    @Test
    public void testEvaluatingNAryExpressionReturnsCorrectResult() throws RPNCalcException {
        assertEquals(-3.0, calc.eval("2 -10 5 sum"),         delta);
        assertEquals( 4.0, calc.eval("8 4 3 1 avg"),         delta);
        assertEquals(-1.0, calc.eval("-1 0 +1 min"),         delta);
        assertEquals(21.0, calc.eval("12 0.6 2.1e1 20 max"), delta);
        assertEquals( 6.0, calc.eval("1 2 3 a b c cnt"),     delta);
    }

    @Test
    public void testEvaluatingNAryExpressionHandlesOneOperand() throws RPNCalcException {
        assertEquals(   144.0,                 calc.eval("144 sum"),      delta);
        assertEquals(100000.0,                 calc.eval("1e5 avg"),      delta);
        assertEquals(Double.NaN,               calc.eval("NaN min"),      delta);
        assertEquals(Double.POSITIVE_INFINITY, calc.eval("Infinity max"), delta);
        assertEquals(     1.0,                 calc.eval("0 cnt"),        delta);
    }

    @Test
    public void testEvaluatingComplexExpressionReturnsCorrectResult() throws RPNCalcException {
        assertEquals(18.0,               calc.eval("4 1.5 3 + * abs"),         delta);
        assertEquals( 7.018333135438927, calc.eval("12.143 neg 61.4 + sqrt"),  delta);
        assertEquals(15.0,               calc.eval("1 2 3 sum 1 2 3 avg 5 *"), delta);
    }

    @Test
    public void testEvaluatingExpressionIgnoresExtraneousWhitespace() throws RPNCalcException {
        assertEquals(1.0, calc.eval(" 1 1 *"),         delta); // Leading
        assertEquals(5.0, calc.eval("20 4 / "),        delta); // Trailing
        assertEquals(1.0, calc.eval(" 11 5 % "),       delta); // Surrounding
        assertEquals(4.0, calc.eval("1024   1020\t-"), delta); // Inbetween
    }

    @Test
    public void testEvaluatingExpressionStoresCorrectValueInRegister() throws RPNCalcException {
        /* Default register */
        assertEquals(1.0, calc.eval("1.0 a <="), delta);
        assertEquals(1.0, calc.getRegister("a"), delta);
        /* Manually added register */
        assertTrue(calc.addRegister("special", 0.0));
        assertEquals(3.5, calc.eval("2.0 1.5 + special <="), delta);
        assertEquals(3.5, calc.getRegister("special"), delta);
        /* Register chaining */
        assertEquals(1.0, calc.eval("a b <= c <= special <="), delta);
        assertEquals(1.0, calc.getRegister("a"), delta);
        assertEquals(1.0, calc.getRegister("b"), delta);
        assertEquals(1.0, calc.getRegister("c"), delta);
        assertEquals(1.0, calc.getRegister("special"), delta);
    }

    @Test
    public void testEvaluatingExpressionReturnsInfinityOnDivisionByZero() throws RPNCalcException {
        assertEquals(Double.POSITIVE_INFINITY, calc.eval("1 0 /"),             0.0);
        assertEquals(Double.NEGATIVE_INFINITY, calc.eval("1 neg 2 3 + 5 - /"), 0.0);
    }

    @Test
    public void testEvaluatingExpressionThrowsOnBlankOrEmpty() {
        assertThrows(RPNCalcException.class, () -> calc.eval(""));
        assertThrows(RPNCalcException.class, () -> calc.eval(" "));
        assertThrows(RPNCalcException.class, () -> calc.eval("\t"));
        assertThrows(RPNCalcException.class, () -> calc.eval("   \t  "));
    }

    @Test
    public void testEvaluatingExpressionThrowsOnStackUnderflow() {
        assertThrows(RPNCalcException.class, () -> calc.eval("neg"));
        assertThrows(RPNCalcException.class, () -> calc.eval("pow"));
        assertThrows(RPNCalcException.class, () -> calc.eval("1 +"));
        assertThrows(RPNCalcException.class, () -> calc.eval("2 3 * /"));
        assertThrows(RPNCalcException.class, () -> calc.eval("cnt"));
    }

    @Test
    public void testEvaluatingExpressionThrowsOnUnrecognizedSymbol() {
        assertThrows(RPNCalcException.class, () -> calc.eval("1 2 po"));
        assertThrows(RPNCalcException.class, () -> calc.eval("4 PI +"));
    }

    @Test
    public void testGettingResultsReturnsNullOnUnderlyingNull() {
        assertNull(calc.getLastResult());
        assertNull(calc.getLastStack());
    }

    @Test
    public void testGettingFormattedResultsReturnsEmptyStringOnUnderlyingNull() {
        assertEquals("", calc.getFormattedLastResult());
        assertEquals("", calc.getFormattedLastStack());
    }

    @Test
    public void testGettingResultsReturnsAllOnCompleteEvaluation() throws RPNCalcException {
        calc.eval("5 2 - 1 -");
        /* Formatted results */
        assertEquals("2.000", calc.getFormattedLastResult());
        assertEquals("2.000", calc.getFormattedLastStack());
        /* Plain results */
        assertEquals(2.0, calc.getLastResult(), delta);
        assertEquals(2.0, calc.getLastStack().pop(), delta);
        assertTrue(calc.getLastStack().empty());
    }

    @Test
    public void testGettingResultsReturnsStackOnPartialEvaulation() throws RPNCalcException {
        calc.setPrecision(2);
        calc.setRegister("a", 120.765);
        calc.eval("a 45 3.4 c <= 2 +");
        /* Formatted results */
        assertEquals("", calc.getFormattedLastResult());
        assertEquals("120.77 45.00 5.40", calc.getFormattedLastStack());
        /* Plain results */
        assertNull(calc.getLastResult());
        assertEquals(5.4,     calc.getLastStack().pop(), delta);
        assertEquals(45.0,    calc.getLastStack().pop(), delta);
        assertEquals(120.765, calc.getLastStack().pop(), delta);
        assertTrue(calc.getLastStack().empty());
    }

    @Test
    public void testGettingResultsReturnsInfinityCorrectly() throws RPNCalcException {
        calc.eval("-Infinity neg");
        /* Formatted results */
        assertEquals("Infinity", calc.getFormattedLastResult());
        assertEquals("Infinity", calc.getFormattedLastStack());
        /* Plain results */
        assertEquals(Double.POSITIVE_INFINITY, calc.getLastResult(), delta);
        assertEquals(Double.POSITIVE_INFINITY, calc.getLastStack().pop(), delta);
        assertTrue(calc.getLastStack().empty());
    }

    @Test
    public void testGettingResultsReturnsNaNCorrectly() throws RPNCalcException {
        calc.eval("0.0 0.0 /");
        /* Formatted results */
        assertEquals("NaN", calc.getFormattedLastResult());
        assertEquals("NaN", calc.getFormattedLastStack());
        /* Plain results */
        assertEquals(Double.NaN, calc.getLastResult(), delta);
        assertEquals(Double.NaN, calc.getLastStack().pop(), delta);
        assertTrue(calc.getLastStack().empty());
    }
}