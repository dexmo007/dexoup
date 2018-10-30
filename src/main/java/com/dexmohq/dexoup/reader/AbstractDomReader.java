package com.dexmohq.dexoup.reader;

import lombok.RequiredArgsConstructor;

import java.io.IOException;

/**
 * @author Henrik Drefs
 */
@RequiredArgsConstructor
public class AbstractDomReader {

    protected final Reader r;

    protected String readName() throws IOException {
        final StringBuilder sb = new StringBuilder();
        while (r.hasMore() && (Character.isAlphabetic(r.current()) || r.current() == '-' || r.current() == '_')) {
            sb.append(r.current());
            r.advance();
        }
        return sb.toString();
    }

}
