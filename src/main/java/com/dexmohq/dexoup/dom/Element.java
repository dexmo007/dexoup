package com.dexmohq.dexoup.dom;

import com.dexmohq.dexoup.writer.IndentableWriter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

/**
 * @author Henrik Drefs
 */
@RequiredArgsConstructor
public class Element implements Child {

    @Getter
    private final String name;

    @Getter
    private Map<String, String> attributes = new HashMap<>();

    @Getter
    private List<Child> children = new LinkedList<>();

    @Getter
    @Setter
    private Element parent;

    public Element(String name, Map<String, String> attributes) {
        this.name = name;
        this.attributes = attributes;
    }

    public Element putAttribute(String name, String value) {
        attributes.put(name, value);
        return this;
    }

    public Element addChild(Child child) {
        children.add(child);
        return this;
    }

    private String stringifyAttributes() {
        return attributes.entrySet().stream()
                .map(e -> e.getKey() + "=\"" + e.getValue() + "\"")
                .collect(joining(" "));
    }

    @Override
    public void writeTo(IndentableWriter writer) throws IOException {
        if (children.isEmpty()) {
            writer.writeLine("<" + name + " " + stringifyAttributes() + " />");
            return;
        }
        writer.writeLine("<" + name + " " + stringifyAttributes() + ">");
        writer.pushIndentation();
        for (final Child child : children) {
            child.writeTo(writer);
        }
        writer.popIndentation();
        writer.writeLine("</" + name + ">");
    }

    @Override
    public String toString() {
        final StringWriter sw = new StringWriter();
        try {
            writeTo(new IndentableWriter(sw));
        } catch (IOException e) {
            // cannot happen with in memory StringWriter
        }
        return sw.toString();
    }
}
