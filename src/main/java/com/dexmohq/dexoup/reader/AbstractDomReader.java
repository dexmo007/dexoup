package com.dexmohq.dexoup.reader;

import com.dexmohq.dexoup.reader.exception.UnexpectedTokenException;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

/**
 * @author Henrik Drefs
 */
@RequiredArgsConstructor
public class AbstractDomReader {

    protected final Reader r;

    protected String readName() throws IOException, UnexpectedTokenException {
        final StringBuilder sb = new StringBuilder();
        while (r.hasMore() && (Character.isAlphabetic(r.current()) || r.current() == '-' || r.current() == '_')) {
            sb.append(r.current());
            r.advance();
        }
        final String name = sb.toString();
        if (name.isEmpty()) {
            throw new UnexpectedTokenException("name cannot be empty", r.current(), r.position());
        }
        return name;
    }

}
