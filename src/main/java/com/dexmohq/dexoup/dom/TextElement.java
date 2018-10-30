package com.dexmohq.dexoup.dom;

import com.dexmohq.dexoup.writer.IndentableWriter;
import lombok.Value;

import java.io.IOException;

/**
 * @author Henrik Drefs
 */
@Value
public class TextElement implements Child {

    String text;

    @Override
    public String toString() {
        return text;
    }

    @Override
    public void writeTo(IndentableWriter writer) throws IOException {
        writer.writeLine(text);
    }
}
