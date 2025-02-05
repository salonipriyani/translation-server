/**

 Custom exception class for handling invalid inputs.
 This exception extends the built-in Exception class.
 */
package ds;

//Name: Saloni Priyani
// Andrew ID: spriyani
public class InvalidInputException extends Exception {
    /**
     * Constructor that takes in a message as a parameter and
     * passes it to the superclass constructor using the "super" keyword.
     * @param message - the error message to be displayed
     */
    public InvalidInputException(String message) {
        super(message);
    }
}