package com.dexmohq.dexoup.path;

import com.dexmohq.dexoup.dom.Element;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Henrik Drefs
 */
@RequiredArgsConstructor
public class ListSelection implements Selection {

    private final List<Element> elements;

    @Override
    public Selection findElements(String name) {
        return new ListSelection(
                elements.stream().filter(e -> e.getName().equals(name))
                        .collect(Collectors.toList())
        );
    }
}
