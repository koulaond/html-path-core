package path.core.query.execute;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import path.core.query.syntax.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static path.core.query.syntax.Attributes.hasName;
import static path.core.query.syntax.Elements.withName;
import static path.core.query.syntax.Operator.*;

class ElementQueryTemplateEvaluatorTest {
    private static Element PARSED_ELEMENT;
    private static WebPage WEB_PAGE;

    @BeforeAll
    static void setup() throws IOException {
        String html = TestUtils.loadHtmlFromResource("ElementQueryTemplateEvaluatorTestHtml.html");
        Document parsedHtml = Jsoup.parse(html);
        PARSED_ELEMENT = parsedHtml.body().getElementsByTag("div").first();
        WEB_PAGE = WebPage.builder()
                .html(parsedHtml.html())
                .build();
    }

    @Test
    void test_element_found() {
        ElementQueryTemplateEvaluator evaluator = new ElementQueryTemplateEvaluator();
        ElementQueryTemplate template =
               withName("form")
                        .hasAttribute(hasName("action").hasValue(Operator.startsWith("https://earthworm")))
                        .containsSubElement(withName("div").hasAttribute(hasName("class").hasValue(startsWith("input-gr"))));
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, template, WEB_PAGE);
        boolean success = result.isSuccess();
        assertTrue(success);
    }

    @Test
    void test_element_not_found__subelement_form_has_bad_name() {
        ElementQueryTemplateEvaluator evaluator = new ElementQueryTemplateEvaluator();
        ElementQueryTemplate template =
                        withName("formm")
                                .hasAttribute(hasName("action").hasValue(startsWith("https://earthworm")))
                                .containsSubElement(withName("div").hasAttribute(hasName("class").hasValue(startsWith("input-gr"))));
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, template, WEB_PAGE);
        boolean success = result.isSuccess();
        assertFalse(success);
    }

    @Test
    void test_element_not_found__subelement_form_has_bad_attribute() {
        ElementQueryTemplateEvaluator evaluator = new ElementQueryTemplateEvaluator();
        ElementQueryTemplate template =
                        withName("form")
                                .hasAttribute(hasName("actionz").hasValue(startsWith("https://earthworm")))
                                .containsSubElement(withName("div").hasAttribute(hasName("class").hasValue(startsWith("input-gr"))));
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, template, WEB_PAGE);
        boolean success = result.isSuccess();
        assertFalse(success);
    }

    @Test
    void test_element_not_found__subsubelement_div_has_bad_attribute_value() {
        ElementQueryTemplateEvaluator evaluator = new ElementQueryTemplateEvaluator();
        ElementQueryTemplate template = withName("form")
                                .hasAttribute(hasName("action").hasValue(startsWith("https://earthworm")))
                                .containsSubElement(withName("div").hasAttribute(hasName("class").hasValue(endsWith("input-gr"))))
                ;
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, template, WEB_PAGE);
        boolean success = result.isSuccess();
        assertFalse(success);
    }

    @Test
    void test_big_OR_one_true() {
        ElementQueryTemplateEvaluator evaluator = new ElementQueryTemplateEvaluator();
        ElementQueryTemplate template = Elements.or(
                withName("f"),
                withName("o"),
                withName("r"),
                withName("m"),
                withName("fo"),
                withName("form")
        );
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, template, WEB_PAGE);
        boolean success = result.isSuccess();
        assertTrue(success);
    }

    @Test
    void test_big_OR_all_false() {
        ElementQueryTemplateEvaluator evaluator = new ElementQueryTemplateEvaluator();
        ElementQueryTemplate template = Elements.or(
                withName("f"),
                withName("o"),
                withName("r"),
                withName("m"),
                withName("fo"),
                withName("for")
        );
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, template, WEB_PAGE);
        boolean success = result.isSuccess();
        assertFalse(success);
    }

    @Test
    void test_big_AND_one_true() {
        ElementQueryTemplateEvaluator evaluator = new ElementQueryTemplateEvaluator();
        ElementQueryTemplate template = Elements.and(
                withName("f"),
                withName("o"),
                withName("r"),
                withName("fo"),
                withName("rm"),
                withName("form")     // true
        );
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, template, WEB_PAGE);
        boolean success = result.isSuccess();
        assertFalse(success);
    }

    @Test
    void test_big_AND_all_true() {
        ElementQueryTemplateEvaluator evaluator = new ElementQueryTemplateEvaluator();
        ElementQueryTemplate template = Elements.and(
                withName(startsWith("f")),
                withName(startsWith("fo")),
                withName(startsWith("for")),
                withName(contains("or")),
                withName(contains("rm")),
                withName(contains("fo")),
                withName(endsWith("orm")),
                withName(endsWith("rm")),
                withName(exact("form"))
        );
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, template, WEB_PAGE);
        boolean success = result.isSuccess();
        assertTrue(success);
    }

    @Test
    void test_big_AND_one_false() {
        ElementQueryTemplateEvaluator evaluator = new ElementQueryTemplateEvaluator();
        ElementQueryTemplate template = Elements.and(
                withName(startsWith("f")),
                withName(startsWith("fo")),
                withName(startsWith("for")),
                withName(contains("f")),
                withName(contains("o")),
                withName(contains("r")),
                withName(endsWith("rm")),
                withName(endsWith("om")),   // false
                withName(exact("form"))
        );
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, template, WEB_PAGE);
        boolean success = result.isSuccess();
        assertFalse(success);
    }

    @Test
    void test_big_AND_oll_false() {
        ElementQueryTemplateEvaluator evaluator = new ElementQueryTemplateEvaluator();
        ElementQueryTemplate template = Elements.and(
                withName(startsWith("X")),
                withName(startsWith("X")),
                withName(startsWith("X")),
                withName(contains("X")),
                withName(contains("X")),
                withName(contains("X")),
                withName(endsWith("X")),
                withName(endsWith("X")),
                withName(exact("X"))
        );
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, template, WEB_PAGE);
        boolean success = result.isSuccess();
        assertFalse(success);
    }

    @Test
    void test_contains_sub_elements_deep__allFound() {
        ElementQueryTemplateEvaluator evaluator = new ElementQueryTemplateEvaluator();
        ElementQueryTemplate template =
                withName("form").containsSubElement(
                        withName("div").containsSubElement(
                                withName("input")
                        ).containsSubElement(
                                withName("span")
                                        .hasAttribute(
                                                hasName("class").hasValue(startsWith("input-group-"))
                                        )
                                        .containsSubElement(
                                                withName("button").containsSubElement(
                                                        withName("i")
                                                )
                                        )
                        )
                );
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, template, WEB_PAGE);
        boolean success = result.isSuccess();
        assertTrue(success);
    }

    @Test
    void test_contains_sub_elements_deep__attribute_not_match() {
        ElementQueryTemplateEvaluator evaluator = new ElementQueryTemplateEvaluator();
        ElementQueryTemplate template =
                withName("form").containsSubElement(
                        withName("div").containsSubElement(
                                withName("input")
                        ).containsSubElement(
                                withName("span")
                                        .hasAttribute(
                                                hasName("class").hasValue(startsWith("iInput-group-"))
                                        )
                                        .containsSubElement(
                                                withName("button").containsSubElement(
                                                        withName("i")
                                                )
                                        )
                        )
                );
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, template, WEB_PAGE);
        boolean success = result.isSuccess();
        assertFalse(success);
    }

    @Test
    void test_contains_sub_elements_deep__wrong_nested_query() {
        ElementQueryTemplateEvaluator evaluator = new ElementQueryTemplateEvaluator();
        ElementQueryTemplate template =
                withName("form").containsSubElement(
                        withName("div").containsSubElement(
                                withName("span")
                                        .hasAttribute(
                                                hasName("class").hasValue(startsWith("iInput-group-"))
                                        )
                                        .containsSubElement(
                                                withName("button").containsSubElement(
                                                        withName("i")
                                                )
                                        )
                        )
                );
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, template, WEB_PAGE);
        boolean success = result.isSuccess();
        assertFalse(success);
    }

    @Test
    void test_contains_sub_elements_deep__subelement_span_name_not_match() {
        ElementQueryTemplateEvaluator evaluator = new ElementQueryTemplateEvaluator();
        ElementQueryTemplate template =
                withName("form").containsSubElement(
                        withName("div").containsSubElement(
                                withName("input")
                        ).containsSubElement(
                                withName("spann")
                                        .hasAttribute(
                                                hasName("class").hasValue(startsWith("input-group-"))
                                        )
                                        .containsSubElement(
                                                withName("button").containsSubElement(
                                                        withName("i")
                                                )
                                        )
                        )
                );
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, template, WEB_PAGE);
        boolean success = result.isSuccess();
        assertFalse(success);
    }


    @Test
    void test_contains_sub_elements_deep__more_criteria_for_nonexisting_elements() {
        ElementQueryTemplateEvaluator evaluator = new ElementQueryTemplateEvaluator();
        ElementQueryTemplate template =
                withName("form").containsSubElement(
                        withName("div").containsSubElement(
                                withName("input")
                        ).containsSubElement(
                                withName("span")
                                        .hasAttribute(
                                                hasName("class").hasValue(startsWith("input-group-"))
                                        )
                                        .containsSubElement(
                                                withName("button").containsSubElement(
                                                        withName("i")
                                                )
                                        )
                        ).containsSubElement(
                                withName("table")
                        )
                );
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, template, WEB_PAGE);
        boolean success = result.isSuccess();
        assertFalse(success);
    }

    @Test
    void test_contains_sub_elements_deep__complex_attribute_queries() {
        ElementQueryTemplateEvaluator evaluator = new ElementQueryTemplateEvaluator();
        ElementQueryTemplate template =
                withName("form")
                        .hasAttribute(
                                hasName(startsWith("actio"))
                                        .or(
                                                Attributes.hasValue(startsWith("https://earth")),
                                                Attributes.hasValue(endsWith("b148"))
                                        )
                        )
                        .hasAttribute(
                                hasName("method")
                        )
                        .hasAttribute(
                                hasName("name").hasValue(startsWith("mc-embedded"))
                        )
                        .containsSubElement(
                                withName("div")
                                        .hasAttribute(
                                                hasName("class").hasValue("input-group")
                                        )
                                        .containsSubElement(
                                                withName("input")
                                        )
                        );
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, template, WEB_PAGE);
        boolean success = result.isSuccess();
        assertTrue(success);
    }


    @Test
    void test_contains_sub_elements_deep__complex_attribute_queries__one_attribute_not_matches() {
        ElementQueryTemplateEvaluator evaluator = new ElementQueryTemplateEvaluator();
        ElementQueryTemplate template =
                withName("form")
                        .hasAttribute(
                                hasName(startsWith("actio"))
                                        .or(
                                                Attributes.hasValue(startsWith("https://earth")),
                                                Attributes.hasValue(endsWith("b148"))
                                        )
                        )
                        .hasAttribute(
                                hasName("method")
                        )
                        .hasAttribute(
                                hasName("name").hasValue(startsWith("mc-embedded"))
                        )
                        .containsSubElement(
                                withName("div")
                                        .hasAttribute(
                                                hasName("clazz").hasValue("input-group")
                                        )
                                        .containsSubElement(
                                                withName("input")
                                        )
                        );
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, template, WEB_PAGE);
        boolean success = result.isSuccess();
        assertFalse(success);
    }
}