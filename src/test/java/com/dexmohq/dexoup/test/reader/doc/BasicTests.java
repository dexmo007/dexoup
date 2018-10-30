package com.dexmohq.dexoup.test.reader.doc;

import com.dexmohq.dexoup.dom.Document;
import com.dexmohq.dexoup.dom.Element;
import com.dexmohq.dexoup.dom.TextElement;
import com.dexmohq.dexoup.reader.DocumentReader;
import com.dexmohq.dexoup.reader.StringReader;
import com.dexmohq.dexoup.reader.exception.ParseException;
import com.dexmohq.dexoup.reader.exception.UnclosedTagException;
import com.dexmohq.dexoup.reader.exception.UnexpectedEndOfFileException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * @author Henrik Drefs
 */
public class BasicTests {

    @ParameterizedTest
    @ValueSource(strings = {
            "<div/>",
            "<div />",
            "<div  />",
            "<div></div>",
            "<div>\n</div>",
            "<div>\t</div>",
            "<div ></div>",
            "<div  ></div>"
    })
    void testSingleDiv(String source) throws IOException, ParseException {
        final Document doc = new DocumentReader(new StringReader(source)).parse();
        assertThat(doc.getElements()).hasSize(1);
        assertThat(doc.getElements()).first()
                .isInstanceOf(Element.class)
                .satisfies(c -> {
                    final Element e = (Element) c;
                    assertThat(e.getName()).isEqualTo("div");
                    assertThat(e.getParent()).isNull();
                    assertThat(e.getChildren()).isEmpty();
                });
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "<div>",
            "<div><div></div>",
    })
    void testSingleDivUnclosed(String source) {
        assertThatThrownBy(() -> new DocumentReader(new StringReader(source)).parse())
                .isInstanceOf(UnclosedTagException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "<div><",
    })
    void testSingleDivUnexpectedEOF(String source) {
        assertThatThrownBy(() -> new DocumentReader(new StringReader(source)).parse())
                .isInstanceOf(UnexpectedEndOfFileException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "<div class=\"container\" />",
            "<div class =\"container\" />",
            "<div class= \"container\" />",
            "<div class = \"container\" />",
            "<div class=\"container\"></div>",
            "<div class=\"container\">\t</div>",
            "<div class=\"container\">\n</div>",
            "<div class=\"container\" ></div>",
    })
    void testSingleTagWithAttribute(String source) throws IOException, ParseException {
        final Document doc = new DocumentReader(new StringReader(source)).parse();
        assertThat(doc.getElements()).hasSize(1);
        assertThat(doc.getElements()).first()
                .isInstanceOf(Element.class)
                .satisfies(c -> {
                    final Element e = (Element) c;
                    assertThat(e.getName()).isEqualTo("div");
                    assertThat(e.getParent()).isNull();
                    assertThat(e.getChildren()).isEmpty();
                    assertThat(e.getAttributes()).isEqualTo(Map.of("class", "container"));
                });
    }

    static Stream<Arguments> singleTagWithText() {
        return Stream.of(
                arguments("<div>A</div>", "A"),
                arguments("<div>A B</div>", "A B")
        );
    }

    @ParameterizedTest
    @MethodSource("singleTagWithText")
    void singleTagWithText(String source, String expectedText) throws IOException, ParseException {
        final Document doc = new DocumentReader(new StringReader(source)).parse();
        assertThat(doc.getElements()).hasSize(1);
        assertThat(doc.getElements()).first()
                .isInstanceOf(Element.class)
                .satisfies(c -> {
                    final Element e = (Element) c;
                    assertThat(e.getName()).isEqualTo("div");
                    assertThat(e.getParent()).isNull();
                    assertThat(e.getChildren()).hasSize(1);
                    assertThat(e.getChildren()).first()
                            .isInstanceOf(TextElement.class)
                            .satisfies(te -> assertThat(((TextElement) te).getText()).isEqualTo(expectedText));
                });
    }
}
