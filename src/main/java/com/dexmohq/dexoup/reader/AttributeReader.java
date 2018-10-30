package com.dexmohq.dexoup.reader;

import com.dexmohq.dexoup.reader.exception.UnexpectedEndOfFileException;
import com.dexmohq.dexoup.reader.exception.UnexpectedTokenException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Henrik Drefs
 */
public class AttributeReader extends AbstractDomReader {

    public AttributeReader(Reader r) {
        super(r);
    }

    public Map<String, String> read() throws IOException, UnexpectedEndOfFileException, UnexpectedTokenException {
        final HashMap<String, String> attrs = new HashMap<>();
        while (r.hasMore()) {
            if (Character.isWhitespace(r.current())) {
                r.advance();
                continue;
            }
            final String name = readName();
            r.squeeze();
            if (r.hasMore() && r.current() == '=') {
                r.advance();
                r.squeeze();
                r.checkNotEOF();
                if (r.current() == '\"') {
                    r.advance();
                    r.checkNotEOF();
                    final String value = r.consumeUntil('\"');
                    r.advance();
                    attrs.put(name, value);
                } else {
                    throw new IllegalStateException("expected opening quotes");
                }
            }
        }
        return attrs;
    }
}
