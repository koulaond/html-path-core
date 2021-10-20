package path.core.query.execute;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import path.core.query.syntax.OperableStatement;
import path.core.query.syntax.WebPage;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static path.core.query.syntax.Operator.*;
import static path.core.query.syntax.StatementTarget.ELEMENT_NAME;
import static path.core.query.syntax.StatementTarget.ELEMENT_TEXT;

class OperableStatementEvaluatorTest {

    private static Element PARSED_ELEMENT;
    private static WebPage WEB_PAGE;

    @BeforeAll
    static void setup() throws IOException {
        String html = TestUtils.loadHtmlFromResource("OperableStatementEvaluatorTestHtml.html");
        Document parsedHtml = Jsoup.parse(html);
        PARSED_ELEMENT = parsedHtml.body().getElementsByTag("div").first();
        WEB_PAGE = WebPage.builder()
                .html(parsedHtml.html())
                .build();
    }

    @Test()
    void testElementNameStartsWith() {
        OperableStatement<String> statement = new OperableStatement<>(ELEMENT_NAME, startsWith("di"));
        OperableStatementEvaluator evaluator = new OperableStatementEvaluator();
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, statement, WEB_PAGE);
        boolean success = result.isSuccess();
        assertTrue(success);
    }

    @Test()
    void testElementNameEndsWith() {
        OperableStatement<String> statement = new OperableStatement<>(ELEMENT_NAME, endsWith("iv"));
        OperableStatementEvaluator evaluator = new OperableStatementEvaluator();
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, statement, WEB_PAGE);
        boolean success = result.isSuccess();
        assertTrue(success);
    }

    @Test()
    void testElementNameContains() {
        OperableStatement<String> statement = new OperableStatement<>(ELEMENT_NAME, contains("i"));
        OperableStatementEvaluator evaluator = new OperableStatementEvaluator();
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, statement, WEB_PAGE);
        boolean success = result.isSuccess();
        assertTrue(success);
    }

    @Test()
    void testElementNameContainsFull() {
        OperableStatement<String> statement = new OperableStatement<>(ELEMENT_NAME, contains("div"));
        OperableStatementEvaluator evaluator = new OperableStatementEvaluator();
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, statement, WEB_PAGE);
        boolean success = result.isSuccess();
        assertTrue(success);
    }

    @Test()
    void testElementNameExact() {
        OperableStatement<String> statement = new OperableStatement<>(ELEMENT_NAME, exact("div"));
        OperableStatementEvaluator evaluator = new OperableStatementEvaluator();
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, statement, WEB_PAGE);
        boolean success = result.isSuccess();
        assertTrue(success);
    }

    @Test()
    void testElementNameNotStartsWith() {
        OperableStatement<String> statement = new OperableStatement<>(ELEMENT_NAME, startsWith("did"));
        OperableStatementEvaluator evaluator = new OperableStatementEvaluator();
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, statement, WEB_PAGE);
        boolean success = result.isSuccess();
        assertFalse(success);
    }

    @Test()
    void testElementNameNotEndsWith() {
        OperableStatement<String> statement = new OperableStatement<>(ELEMENT_NAME, startsWith("id"));
        OperableStatementEvaluator evaluator = new OperableStatementEvaluator();
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, statement, WEB_PAGE);
        boolean success = result.isSuccess();
        assertFalse(success);
    }

    @Test()
    void testElementNameNotContains() {
        OperableStatement<String> statement = new OperableStatement<>(ELEMENT_NAME, startsWith("ii"));
        OperableStatementEvaluator evaluator = new OperableStatementEvaluator();
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, statement, WEB_PAGE);
        boolean success = result.isSuccess();
        assertFalse(success);
    }

    @Test()
    void testElementNameNotExact() {
        OperableStatement<String> statement = new OperableStatement<>(ELEMENT_NAME, startsWith("vid"));
        OperableStatementEvaluator evaluator = new OperableStatementEvaluator();
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, statement, WEB_PAGE);
        boolean success = result.isSuccess();
        assertFalse(success);
    }

    @Test()
    void testElementTextStartsWith() {
        OperableStatement<String> statement = new OperableStatement<>(ELEMENT_TEXT, startsWith("This is the first inner "));
        OperableStatementEvaluator evaluator = new OperableStatementEvaluator();
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, statement, WEB_PAGE);
        boolean success = result.isSuccess();
        assertTrue(success);
    }

    @Test()
    void testElementTextEndsWith() {
        OperableStatement<String> statement = new OperableStatement<>(ELEMENT_TEXT, endsWith("second inner text block"));
        OperableStatementEvaluator evaluator = new OperableStatementEvaluator();
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, statement, WEB_PAGE);
        boolean success = result.isSuccess();
        assertTrue(success);
    }

    @Test()
    void testElementTextContains() {
        OperableStatement<String> statement = new OperableStatement<>(ELEMENT_TEXT, contains(" text block This is the "));
        OperableStatementEvaluator evaluator = new OperableStatementEvaluator();
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, statement, WEB_PAGE);
        boolean success = result.isSuccess();
        assertTrue(success);
    }

    @Test()
    void testElementTextContainsFull() {
        OperableStatement<String> statement = new OperableStatement<>(ELEMENT_TEXT, contains("This is the first inner text block This is the second inner text block"));
        OperableStatementEvaluator evaluator = new OperableStatementEvaluator();
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, statement, WEB_PAGE);
        boolean success = result.isSuccess();
        assertTrue(success);
    }

    @Test()
    void testElementTextExact() {
        OperableStatement<String> statement = new OperableStatement<>(ELEMENT_TEXT, contains("This is the first inner text block This is the second inner text block"));
        OperableStatementEvaluator evaluator = new OperableStatementEvaluator();
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, statement, WEB_PAGE);
        boolean success = result.isSuccess();
        assertTrue(success);
    }

    @Test()
    void testElementTextNotStartsWith() {
        OperableStatement<String> statement = new OperableStatement<>(ELEMENT_TEXT, startsWith("This is the first inner_"));
        OperableStatementEvaluator evaluator = new OperableStatementEvaluator();
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, statement, WEB_PAGE);
        boolean success = result.isSuccess();
        assertFalse(success);
    }

    @Test()
    void testElementTextNotEndsWith() {
        OperableStatement<String> statement = new OperableStatement<>(ELEMENT_TEXT, endsWith("_second inner text block"));
        OperableStatementEvaluator evaluator = new OperableStatementEvaluator();
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, statement, WEB_PAGE);
        boolean success = result.isSuccess();
        assertFalse(success);
    }

    @Test()
    void testElementTextNotContains() {
        OperableStatement<String> statement = new OperableStatement<>(ELEMENT_TEXT, contains(" text block_This is the "));
        OperableStatementEvaluator evaluator = new OperableStatementEvaluator();
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, statement, WEB_PAGE);
        boolean success = result.isSuccess();
        assertFalse(success);
    }

    @Test()
    void testElementTextNotExact() {
        OperableStatement<String> statement = new OperableStatement<>(ELEMENT_TEXT, contains("This is the first inner text block_This is the second inner text block"));
        OperableStatementEvaluator evaluator = new OperableStatementEvaluator();
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, statement, WEB_PAGE);
        boolean success = result.isSuccess();
        assertFalse(success);
    }
}