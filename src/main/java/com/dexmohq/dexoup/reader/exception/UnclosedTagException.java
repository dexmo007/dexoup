package com.dexmohq.dexoup.reader.exception;

/**
 * @author Henrik Drefs
 */
public class UnclosedTagException extends ParseException {
    public UnclosedTagException(String tag) {
        super("Tag " + tag + " is not closed");
    }
}
