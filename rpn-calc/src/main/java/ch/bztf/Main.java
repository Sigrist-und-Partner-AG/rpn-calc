package ch.bztf;

/**
 * Wrapper for {@code main} that does not {@code extend Application}.
 * This is necessary in order to successfully bundle the {@code .jar}
 * file for distribution using the shade plugin. 
 * 
 * @author Timothy R. Schmid
 */
public class Main {
    
    public static void main(String[] args) {
        App.main(args);
    }
}