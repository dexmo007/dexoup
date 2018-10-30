package com.dexmohq.dexoup.reader;

import com.dexmohq.dexoup.dom.Document;
import com.dexmohq.dexoup.dom.Element;
import com.dexmohq.dexoup.dom.TextElement;
import com.dexmohq.dexoup.reader.exception.ParseException;
import com.dexmohq.dexoup.reader.exception.UnclosedTagException;
import com.dexmohq.dexoup.reader.exception.UnexpectedEndOfFileException;
import com.dexmohq.dexoup.reader.exception.UnexpectedTokenException;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Henrik Drefs
 */
public class DocumentReader extends AbstractDomReader {

    private Deque<String> tags = new LinkedList<>();

    private Element rootElement = new Element("root");
    private Element currentElement = rootElement;

    public DocumentReader(Reader reader) {
        super(reader);
    }

    private void handleOpeningTag() throws IOException, UnexpectedTokenException, UnexpectedEndOfFileException {
        final String elementName = readName();
        tags.push(elementName);
        r.squeeze();
        r.checkNotEOF();

        final StringBuilder c = new StringBuilder();
        while (r.hasMore() && r.current() != '/' && r.current() != '>') {
            c.append(r.current());
            r.advance();
        }
        final Map<String, String> attrs = new AttributeReader(new StringReader(c.toString())).read();

        if (r.current() == '/') {
            r.advance();
            r.checkNotEOF();
            if (r.current() == '>') {
                r.advance();
                final String en = tags.pop();
                currentElement.addChild(new Element(en, attrs));
            } else {
                throw new UnexpectedTokenException(r.current(), r.position());
            }
        } else if (r.current() == '>') {
            final Element parent = currentElement;
            currentElement = new Element(tags.peek(), attrs);
            parent.addChild(currentElement);
            currentElement.setParent(parent);
            r.advance();
        }
    }

    private void handleClosingTag() throws IOException, UnexpectedTokenException, UnexpectedEndOfFileException {
        final String en = readName();
        if (!currentElement.getName().equals(en)) {
            throw new IllegalStateException("unexpected closing tag");
        }
        r.squeeze();
        r.checkNotEOF();
        if (r.current() != '>') {
            throw new UnexpectedTokenException(r.current(), r.position());
        }
        r.advance();
        if (tags.isEmpty()) {
            throw new UnexpectedTokenException("closing unopened tag", r.current(), r.position());
        }
        tags.pop();
        currentElement = currentElement.getParent();
    }

    public synchronized Document parse() throws IOException, ParseException {

        while (r.hasMore()) {
            if (Character.isWhitespace(r.current())) {
                r.advance();
                continue;
            }
            if (r.current() == '<') {
                r.advance();
                r.checkNotEOF();

                if (r.current() == '/') {
                    r.advance();
                    r.squeeze();
                    r.checkNotEOF();
                    handleClosingTag();
                } else {
                    handleOpeningTag();
                }

            } else {
                final String text = r.consumeUntil('<');
                currentElement.addChild(new TextElement(text, currentElement));
            }
        }

        if (tags.isEmpty()) {
            rootElement.getChildren().forEach(c -> c.setParent(null));
            return new Document(rootElement.getChildren());
        }
        throw new UnclosedTagException(tags.peek());
    }

}
