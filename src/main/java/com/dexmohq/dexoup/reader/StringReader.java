package com.dexmohq.dexoup.reader;

/**
 * @author Henrik Drefs
 */
public class StringReader extends AbstractReader {

    private char[] source;
    private int pos;

    public StringReader(String source) {
        this.source = source.toCharArray();
        this.pos = 0;
    }

    @Override
    public char current() {
        return source[pos];
    }

    @Override
    public int position() {
        return pos;
    }

    @Override
    public void advance() {
        pos++;
    }

    @Override
    public boolean isEOF() {
        return pos >= source.length;
    }
}
