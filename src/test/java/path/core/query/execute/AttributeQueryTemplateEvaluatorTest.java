package path.core.query.execute;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import path.core.query.syntax.AttributeQueryTemplate;
import path.core.query.syntax.WebPage;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static path.core.query.syntax.Attributes.*;
import static path.core.query.syntax.Operator.*;

class AttributeQueryTemplateEvaluatorTest {

    private static Element PARSED_ELEMENT;
    private static WebPage WEB_PAGE;

    @BeforeAll
    static void setup() throws IOException {
        String webPageHtml = TestUtils.loadHtmlFromResource("AttributeQueryTemplateEvaluatorTestHtml.html");
        Document parsed = Jsoup.parse(webPageHtml);
        PARSED_ELEMENT = parsed.getElementsByTag("header").first();
        WEB_PAGE = WebPage.builder().html(webPageHtml).build();
    }

    @Test
    void test__class_attribute_found() {
        AttributeQueryTemplate template = or(
                hasName(startsWith("cla")),
                hasName(endsWith("ass")),
                hasName(contains("las"))
        ).hasValue(startsWith("com.ondrejkoula"));
        AttributeQueryTemplateEvaluator evaluator = new AttributeQueryTemplateEvaluator();
        AttributeQueryTreeResult result = evaluator.evaluate(PARSED_ELEMENT, template, WEB_PAGE);
        Attribute foundAttribute = result.getFoundAttribute();
        boolean success = result.isSuccess();
        assertTrue(success);
        assertEquals("class", foundAttribute.getKey());
        assertEquals("com.ondrejkoula.netspectr", foundAttribute.getValue());
    }

    @Test
    void test__attribute_not_found() {
        AttributeQueryTemplate template = or(
                hasName(startsWith("cla")),
                hasName(endsWith("ass")),
                hasName(contains("las"))
        ).hasValue(startsWith("om.ondrejkoula"));
        AttributeQueryTemplateEvaluator evaluator = new AttributeQueryTemplateEvaluator();
        AttributeQueryTreeResult result = evaluator.evaluate(PARSED_ELEMENT, template, WEB_PAGE);
        boolean success = result.isSuccess();
        assertFalse(success);
    }

    @Test
    void test__composite_statement() {
        AttributeQueryTemplate template = and(
                or(
                        hasName(startsWith("nam")),
                        hasName(endsWith("ame")),
                        hasName(contains("am"))
                ),
                or(
                        hasValue(startsWith("test-html-")),
                        hasValue(endsWith("html-header")),
                        hasValue(contains("-html-head"))
                )
        );
        AttributeQueryTemplateEvaluator evaluator = new AttributeQueryTemplateEvaluator();
        AttributeQueryTreeResult result = evaluator.evaluate(PARSED_ELEMENT, template, WEB_PAGE);
        boolean success = result.isSuccess();
        assertTrue(success);
    }


    @Test
    void test__composite_statement_2() {
        AttributeQueryTemplate template = and(
                or(     // true
                        hasName(startsWith("nam")),    // true
                        hasName(endsWith("ame")),      // true
                        hasName(contains("am"))        // true
                ),
                or(    // false
                        hasValue(startsWith("1test-html-")),    // false
                        hasValue(endsWith("html-header1")),     // false
                        hasValue(contains("-html-he1ad")),      // false
                        and(
                                hasName("nam"),             // false
                                hasValue("test-html-")      // false
                        )
                )
        );
        AttributeQueryTemplateEvaluator evaluator = new AttributeQueryTemplateEvaluator();
        AttributeQueryTreeResult result = evaluator.evaluate(PARSED_ELEMENT, template, WEB_PAGE);
        boolean success = result.isSuccess();
        assertFalse(success);
    }

    @Test
    void test__composite_statement_3() {
        AttributeQueryTemplate template = and(
                or(     // true
                        hasName(startsWith("nam")),    // true
                        hasName(endsWith("ame")),      // true
                        hasName(contains("am"))        // true
                ),
                or(    // false
                        hasValue(startsWith("1test-html-")),    // false
                        hasValue(endsWith("html-header1")),     // false
                        hasValue(contains("-html-he1ad")),      // false
                        and(
                                hasName("name"),             // false
                                hasValue(startsWith("test-html-"))      // false
                        )
                )
        );
        AttributeQueryTemplateEvaluator evaluator = new AttributeQueryTemplateEvaluator();
        AttributeQueryTreeResult result = evaluator.evaluate(PARSED_ELEMENT, template, WEB_PAGE);
        boolean success = result.isSuccess();
        assertTrue(success);
    }

    @Test
    void test__composite_statement__super_nested_OR_true() {
        AttributeQueryTemplate template = or(
                hasName(startsWith("m")),
                or(
                        hasName(startsWith("m")),
                        or(
                                hasName(startsWith("m")),
                                or(
                                        hasName(startsWith("m")),
                                        or(
                                                hasName(startsWith("m")),
                                                or(
                                                        hasName(startsWith("m")),
                                                        or(
                                                                hasName(startsWith("nam"))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        AttributeQueryTemplateEvaluator evaluator = new AttributeQueryTemplateEvaluator();
        AttributeQueryTreeResult result = evaluator.evaluate(PARSED_ELEMENT, template, WEB_PAGE);
        boolean success = result.isSuccess();
        assertTrue(success);
    }

    @Test
    void test__composite_statement___super_nested_OR_false() {
        AttributeQueryTemplate template = or(
                hasName(startsWith("m")),
                or(
                        hasName(startsWith("m")),
                        or(
                                hasName(startsWith("m")),
                                or(
                                        hasName(startsWith("m")),
                                        or(
                                                hasName(startsWith("m")),
                                                or(
                                                        hasName(startsWith("m")),
                                                        or(
                                                                hasName(endsWith("nam"))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        AttributeQueryTemplateEvaluator evaluator = new AttributeQueryTemplateEvaluator();
        AttributeQueryTreeResult result = evaluator.evaluate(PARSED_ELEMENT, template, WEB_PAGE);
        boolean success = result.isSuccess();
        assertFalse(success);
    }
}