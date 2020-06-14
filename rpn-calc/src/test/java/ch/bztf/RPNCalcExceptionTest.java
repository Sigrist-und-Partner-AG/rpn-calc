package ch.bztf;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * This is the test suite for the {@link RPNCalcException} class.
 * The tests are executed using the standard JUnit framework.
 * 
 * @author Timothy R. Schmid
 */
public class RPNCalcExceptionTest {
    
    @Test
    public void testPlainExceptionProvidesCorrectMessage() {
        assertEquals("Test message", new RPNCalcException("Test message").getMessage());
    }

    @Test
    public void testDetailedExceptionProvidesCorrectMessage() {
        assertEquals(
            "Stack underflow ('<-' at index 0)", 
            new RPNCalcException("Stack underflow", "<-", 0).getMessage()
        );
    }
}