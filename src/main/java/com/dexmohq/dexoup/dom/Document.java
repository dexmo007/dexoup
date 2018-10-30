package com.dexmohq.dexoup.dom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Henrik Drefs
 */
@RequiredArgsConstructor
public class Document {

    @Getter
    private final List<Child> elements;

    @Override
    public String toString() {
        return elements.stream().map(Child::toString).collect(Collectors.joining("\n"));
    }

}
