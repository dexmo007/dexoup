package com.dexmohq.dexoup.reader.exception;

/**
 * @author Henrik Drefs
 */
public class UnexpectedTokenException extends ParseException {

    public UnexpectedTokenException(char token, int pos) {
        super(token + " at " + pos);
    }

    public UnexpectedTokenException(String message, char token, int pos) {
        super(message + ": " + token + " at " + pos);
    }
}
