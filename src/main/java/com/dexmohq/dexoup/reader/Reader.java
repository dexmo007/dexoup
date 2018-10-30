package com.dexmohq.dexoup.reader;

import java.io.IOException;
import java.util.function.Predicate;

/**
 * @author Henrik Drefs
 */
public interface Reader {
    char current();

    int position();

    void advance() throws IOException;

    boolean hasMore();

    boolean isEOF();

    void checkNotEOF();

    String consumeUntil(char until) throws IOException;

    String consumeUntil(Predicate<Character> until) throws IOException;

    void squeeze() throws IOException;
}
