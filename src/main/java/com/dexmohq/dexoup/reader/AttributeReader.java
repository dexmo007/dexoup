package com.dexmohq.dexoup.reader;

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

    public Map<String, String> read() throws IOException {
        final HashMap<String, String> attrs = new HashMap<>();
        while (r.hasMore()) {
            if (Character.isWhitespace(r.current())) {
                r.advance();
                continue;
            }
            final String name = readName();
            if (r.hasMore() && r.current() == '=') {
                r.advance();
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
