package ch.bztf;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Timothy R. Schmid
 * 
 * This is the test suite for the {@link RPNCalc} class.
 * The tests are executed using the standard JUnit framework.
 */
public class RPNCalcTest {
    
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
}