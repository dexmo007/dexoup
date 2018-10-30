package com.dexmohq.dexoup.reader;

import java.io.IOException;
import java.util.function.Predicate;

/**
 * @author Henrik Drefs
 */
public abstract class AbstractReader implements Reader {

    @Override
    public boolean hasMore() {
        return !isEOF();
    }

    @Override
    public void checkNotEOF() {
        if (isEOF()) {
            throw new IllegalStateException("unexpected EOF");
        }
    }

    @Override
    public String consumeUntil(char until) throws IOException {
        final StringBuilder sb = new StringBuilder();
        while (hasMore() && current() != until) {
            sb.append(current());
            advance();
        }
        return sb.toString();
    }

    @Override
    public String consumeUntil(Predicate<Character> until) throws IOException {
        final StringBuilder sb = new StringBuilder();
        while (hasMore() && until.test(current())) {
            sb.append(current());
            advance();
        }
        return sb.toString();
    }

    @Override
    public void squeeze() throws IOException {
        while (hasMore() && Character.isWhitespace(current())) {
            advance();
        }
    }

}
