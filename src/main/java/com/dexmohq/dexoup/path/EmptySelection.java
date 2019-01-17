package com.dexmohq.dexoup.path;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Henrik Drefs
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmptySelection implements Selection {

    public static final EmptySelection INSTANCE = new EmptySelection();

    @Override
    public Selection findElements(String name) {
        return INSTANCE;
    }
}
