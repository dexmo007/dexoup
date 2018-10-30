package com.dexmohq.dexoup.reader;

import com.dexmohq.dexoup.dom.Document;
import com.dexmohq.dexoup.dom.Element;
import com.dexmohq.dexoup.dom.TextElement;

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

    private void handleOpeningTag() throws IOException {
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

    private void handleClosingTag() throws IOException {
        final String en = readName();
        if (!currentElement.getName().equals(en)) {
            throw new IllegalStateException("unexpected closing tag");
        }
        r.checkNotEOF();
        if (r.current() != '>') {
            throw new UnexpectedTokenException(r.current(), r.position());
        }
        r.advance();
        if (tags.isEmpty()) {
            throw new IllegalStateException("closing unopened tag");
        }
        tags.pop();
        currentElement = currentElement.getParent();
    }

    public synchronized Document parse() throws IOException {

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
                currentElement.addChild(new TextElement(text));
            }
        }

        if (tags.isEmpty()) {
            return new Document(rootElement.getChildren());
        }
        throw new IllegalStateException("unclosed tag");
    }

    public static void main(String[] args) throws IOException {

//        System.out.println(new AttributeReader("class=\"container d-flex\" aria=\"hidden\"").read());
//
        System.out.println(new DocumentReader(new StringReader("<div-custom class=\"container\">" +
                "<void />" +
                "some text" +
                "<ul>" +
                "<li>First</li>" +
                "<li>Second</li>" +
                "</ul>" +
                "</div-custom>")).parse());
    }

    public static class UnexpectedTokenException extends RuntimeException {

        public UnexpectedTokenException(char token, int pos) {
            super(token + " at " + pos);
        }
    }


}
