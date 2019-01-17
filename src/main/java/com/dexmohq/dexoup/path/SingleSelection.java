package com.dexmohq.dexoup.path;

import com.dexmohq.dexoup.dom.Element;
import lombok.RequiredArgsConstructor;

/**
 * @author Henrik Drefs
 */
@RequiredArgsConstructor
public class SingleSelection implements Selection {

    private final Element element;

    @Override
    public Selection findElements(String name) {
        if (element.getName().equals(name)) {
            return this;
        }
        return EmptySelection.INSTANCE;
    }
}
