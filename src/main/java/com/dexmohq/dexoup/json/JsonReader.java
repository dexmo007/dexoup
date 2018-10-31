package com.dexmohq.dexoup.json;

import com.dexmohq.dexoup.reader.Reader;
import com.dexmohq.dexoup.reader.StringReader;
import com.dexmohq.dexoup.reader.exception.ParseException;
import com.dexmohq.dexoup.reader.exception.UnexpectedEndOfFileException;
import com.dexmohq.dexoup.reader.exception.UnexpectedTokenException;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Henrik Drefs
 */
@RequiredArgsConstructor
public class JsonReader {

    private final Reader r;

    public Map<String, Object> readObject() throws IOException, ParseException {
        final Map<String, Object> json = internalReadObject();
        r.squeeze();
        r.expectEOF();
        return json;
    }

    private Map.Entry<String, Object> readProperty() throws IOException, ParseException {
        r.expectAndAdvance('\"');
        final String name = r.consumeUntil('\"');
        if (name.isEmpty()) {
            throw new UnexpectedTokenException("name cannot be empty", r.current(), r.position());
        }
        r.advance();
        r.squeeze();
        r.expectAndAdvance(':');
        r.squeeze();
        final Object value = readValue();
        return Map.entry(name, value);
    }

    private Map<String, Object> internalReadObject() throws IOException, ParseException {
        final Map<String, Object> json = new LinkedHashMap<>();

        r.squeeze();
        r.expectAndAdvance('{');
        r.squeeze();
        r.checkNotEOF();
        boolean first = true;
        while (r.hasMore()) {
            if (r.current() == '}') {
                r.advance();
                break;
            }
            if (r.current() == ',') {
                if (first) {
                    throw new UnexpectedTokenException(r.current(), r.position());
                }
                r.advance();
                r.squeeze();
            } else {
                if (!first) {
                    throw new UnexpectedTokenException("expected ,", r.current(), r.position());
                }
            }
            first = false;
            r.checkNotEOF();
            final Map.Entry<String, Object> prop = readProperty();
            json.put(prop.getKey(), prop.getValue());
            r.squeeze();
        }

        return json;
    }

    private List<Object> readArray() throws ParseException, IOException {
        final ArrayList<Object> list = new ArrayList<>();
        r.expectAndAdvance('[');
        r.squeeze();
        boolean first = true;
        while (r.hasMore() && r.current() != ']') {
            if (!first) {
                r.expectAndAdvance(',');
            }
            first = false;
            final Object element = readValue();
            list.add(element);
        }
        r.expectAndAdvance(']');
        return list;
    }

    private Object readValue() throws ParseException, IOException {
        r.checkNotEOF();
        final char current = r.current();
        switch (current) {
            case '\"':
                return readString();
            case '{':
                return internalReadObject();
            case '[':
                return readArray();
            default:
                return readNumber();
        }
    }

    private Number readNumber() throws UnexpectedTokenException, IOException {
        final StringBuilder sb = new StringBuilder();
        boolean decimalPoint = false;
        while (r.hasMore()) {
            if (Character.isDigit(r.current())) {
                sb.append(r.current());
            } else if (r.current() == '.') {
                if (decimalPoint) {
                    throw new UnexpectedTokenException("multiple decimal points in number", r.current(), r.position());
                }
                decimalPoint = true;
                sb.append(r.current());
            } else {
                break;
            }
            r.advance();
        }
        final String numberString = sb.toString();
        if (numberString.isEmpty()) {
            throw new UnexpectedTokenException("number cannot be empty", r.current(), r.position());
        }
        if (decimalPoint) {
            return new BigDecimal(numberString);
        }
        return Integer.parseInt(numberString);
    }

    private String readString() throws IOException, UnexpectedEndOfFileException, UnexpectedTokenException {
        r.expectAndAdvance('\"');
        final String s = r.consumeUntil('\"');
        r.advance();
        return s;
    }

    public static void main(String[] args) throws IOException, ParseException {
//        System.out.println(new JsonReader(new StringReader("{\"foo\":\"bar\",\"n\":67,\"o\":{\"foo\":\"child\"}}")).readObject());
        System.out.println(new JsonReader(new StringReader("{\"foo\":[1,2,\"12\"]}")).readObject());

    }

}
