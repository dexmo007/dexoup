package com.dexmohq.dexoup.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Henrik Drefs
 */
public class FileReader extends AbstractReader {

    private final BufferedReader br;
    private int pos;
    private int current;

    public FileReader(File file) throws FileNotFoundException {
        final java.io.FileReader reader = new java.io.FileReader(file);
        this.br = new BufferedReader(reader);
        this.pos = 0;
    }

    @Override
    public char current() {
        return (char) current;
    }

    @Override
    public int position() {
        return pos;
    }

    @Override
    public void advance() throws IOException {
        current = br.read();
        pos++;
    }

    @Override
    public boolean isEOF() {
        return current == -1;
    }
}
