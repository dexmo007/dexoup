package com.dexmohq.dexoup.dom;

import com.dexmohq.dexoup.writer.IndentableWriter;

import java.io.IOException;

/**
 * @author Henrik Drefs
 */
public interface Child {

    Element getParent();

    void setParent(Element parent);

    void writeTo(IndentableWriter writer) throws IOException;
}
