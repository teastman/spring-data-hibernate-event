package io.github.teastman.hibernate.exception;

/**
 * Exception for when a method parameter does not inherit from a defined type.
 *
 * @author Tyler Eastman
 */
public class AssignableParameterException extends Exception {

    public AssignableParameterException() {
    }

    public AssignableParameterException(String message) {
        super(message);
    }
}
