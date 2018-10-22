package com.github.teastman.hibernate.exception;

/**
 * Exception for when a method has in incorrect number of parameters defined.
 *
 * @author Tyler Eastman
 */
public class InvalidParameterCount extends Exception {

    public InvalidParameterCount() {
    }

    public InvalidParameterCount(String message) {
        super(message);
    }
}
