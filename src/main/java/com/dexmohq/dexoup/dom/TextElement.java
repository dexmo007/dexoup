package com.dexmohq.dexoup.dom;

import com.dexmohq.dexoup.writer.IndentableWriter;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;

/**
 * @author Henrik Drefs
 */
@Data
@AllArgsConstructor
public class TextElement implements Child {

    private String text;

    private Element parent;

    @Override
    public String toString() {
        return text;
    }

    @Override
    public void writeTo(IndentableWriter writer) throws IOException {
        writer.writeLine(text);
    }
}
