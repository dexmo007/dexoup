package com.dexmohq.dexoup.writer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.*;

/**
 * @author Henrik Drefs
 */
@RequiredArgsConstructor
public class IndentableWriter {

    private String indentation = "";

    @NonNull
    private final Writer downStream;

    public IndentableWriter(@NonNull OutputStream downStream) {
        this.downStream = new OutputStreamWriter(downStream);
    }

    public void setIndentation(int indentation) {
        if (indentation < 0) {
            throw new IllegalArgumentException("indentation must be non-negative");
        }
        this.indentation = "\t".repeat(indentation);
    }

    public IndentableWriter pushIndentation() {
        indentation += "\t";
        return this;
    }

    public IndentableWriter popIndentation() {
        if (indentation.length() > 0) {
            indentation = indentation.substring(1);
        }
        return this;
    }

    public void writeLine(String line) throws IOException {
        downStream.write(indentation + line + "\n");
        downStream.flush();
    }

}
