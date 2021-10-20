package path.core.query.execute;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import path.core.query.syntax.LogicalStatement;
import path.core.query.syntax.OperableStatement;
import path.core.query.syntax.Statement;
import path.core.query.syntax.WebPage;

import java.io.IOException;
import java.util.HashSet;

import static java.util.Arrays.asList;
import static org.assertj.core.util.Sets.newHashSet;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static path.core.query.syntax.LogicalStatementType.AND;
import static path.core.query.syntax.LogicalStatementType.OR;
import static path.core.query.syntax.Operator.endsWith;
import static path.core.query.syntax.Operator.startsWith;
import static path.core.query.syntax.StatementTarget.*;

class LogicalStatementEvaluatorTest {

    private static Element PARSED_ELEMENT;
    private static WebPage WEB_PAGE;

    @BeforeAll
    static void setup() throws IOException {
        String webPageHtml = TestUtils.loadHtmlFromResource("LogicalStatementEvaluatorTestHtml.html");
        Document parsed = Jsoup.parse(webPageHtml);
        PARSED_ELEMENT = parsed.getElementsByTag("header").first();
        WEB_PAGE = WebPage.builder().html(webPageHtml).build();
    }

    @Test
    void test_two_operable_statements__all_true() {
        HashSet<OperableStatement<String>> statements = newHashSet(asList(
                new OperableStatement<>(ELEMENT_NAME, startsWith("head")),
                new OperableStatement<>(ELEMENT_NAME, endsWith("eader")))
        );
        LogicalStatement statement = new LogicalStatement(ELEMENT_SUBELEMENTS, AND, statements);
        LogicalStatementEvaluator evaluator = new LogicalStatementEvaluator();
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, statement, WEB_PAGE);
        boolean success = result.isSuccess();
        assertTrue(success);
    }

    @Test
    void test_two_operable_statements__one_is_false() {
        HashSet<OperableStatement<String>> statements = newHashSet(asList(
                new OperableStatement<>(ELEMENT_NAME, startsWith("head")),
                new OperableStatement<>(ELEMENT_NAME, endsWith("eade")))
        );
        LogicalStatement statement = new LogicalStatement(ELEMENT_SUBELEMENTS, AND, statements);
        LogicalStatementEvaluator evaluator = new LogicalStatementEvaluator();
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, statement, WEB_PAGE);
        boolean success = result.isSuccess();
        assertFalse(success);
    }

    @Test
    void test_nested_or() {
        HashSet<OperableStatement<String>> orStatements = newHashSet(asList(
                new OperableStatement<>(ELEMENT_NAME, startsWith("head")),
                new OperableStatement<>(ELEMENT_NAME, endsWith("eade")))
        );
        LogicalStatement<OperableStatement<String>> orStatement = new LogicalStatement<>(ELEMENT_SUBELEMENTS, OR, orStatements);

        HashSet<Statement> statements = newHashSet(asList(
                new OperableStatement<>(ELEMENT_NAME, startsWith("head")),
                new OperableStatement<>(ELEMENT_NAME, endsWith("eader")),
                orStatement
        ));
        LogicalStatement statement = new LogicalStatement(ELEMENT_SUBELEMENTS, AND, statements);
        LogicalStatementEvaluator evaluator = new LogicalStatementEvaluator();
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, statement, WEB_PAGE);
        boolean success = result.isSuccess();
        assertTrue(success);
    }

    @Test
    void test_nested_or__false() {
        HashSet<OperableStatement<String>> orStatements = newHashSet(asList(
                new OperableStatement<>(ELEMENT_NAME, startsWith("ead")),   // false
                new OperableStatement<>(ELEMENT_NAME, endsWith("eade"))     // false
        ));
        LogicalStatement<OperableStatement<String>> orStatement = new LogicalStatement<>(ELEMENT_SUBELEMENTS, OR, orStatements);

        HashSet<Statement> statements = newHashSet(asList(
                new OperableStatement<>(ELEMENT_NAME, startsWith("head")),  // true
                new OperableStatement<>(ELEMENT_NAME, endsWith("eader")),   // true
                orStatement
        ));
        LogicalStatement statement = new LogicalStatement(ELEMENT_SUBELEMENTS, AND, statements);
        LogicalStatementEvaluator evaluator = new LogicalStatementEvaluator();
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, statement, WEB_PAGE);
        boolean success = result.isSuccess();
        assertFalse(success);
    }

    @Test
    void test_nested_and__or_is_true() {
        HashSet<OperableStatement<String>> orStatements = newHashSet(asList(
                new OperableStatement<>(ELEMENT_NAME, startsWith("ead")),   // false
                new OperableStatement<>(ELEMENT_NAME, endsWith("eader"))    // true
        ));
        LogicalStatement<OperableStatement<String>> orStatement = new LogicalStatement<>(ELEMENT_SUBELEMENTS, AND, orStatements);   // true

        HashSet<Statement> statements = newHashSet(asList(
                new OperableStatement<>(ELEMENT_NAME, startsWith("head")),  // true
                new OperableStatement<>(ELEMENT_NAME, endsWith("eader")),   // true
                orStatement
        ));
        LogicalStatement statement = new LogicalStatement(ELEMENT_SUBELEMENTS, OR, statements);     // true
        LogicalStatementEvaluator evaluator = new LogicalStatementEvaluator();
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, statement, WEB_PAGE);
        boolean success = result.isSuccess();
        assertTrue(success);
    }

    @Test
    void test_nested_and__all_is_false() {
        HashSet<OperableStatement<String>> orStatements = newHashSet(asList(
                new OperableStatement<>(ELEMENT_NAME, startsWith("ead")),   // false
                new OperableStatement<>(ELEMENT_NAME, endsWith("eade"))    // false
        ));
        LogicalStatement<OperableStatement<String>> orStatement = new LogicalStatement<>(ELEMENT_SUBELEMENTS, AND, orStatements);   // false

        HashSet<Statement> statements = newHashSet(asList(
                new OperableStatement<>(ELEMENT_NAME, startsWith("ead")),  // true
                new OperableStatement<>(ELEMENT_NAME, endsWith("eade")),   // true
                orStatement
        ));
        LogicalStatement statement = new LogicalStatement(ELEMENT_SUBELEMENTS, OR, statements); // false
        LogicalStatementEvaluator evaluator = new LogicalStatementEvaluator();
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, statement, WEB_PAGE);
        boolean success = result.isSuccess();
        assertFalse(success);
    }

    @Test
    void test_with_text_block() {
        HashSet<OperableStatement<String>> orStatements = newHashSet(asList(
                new OperableStatement<>(ELEMENT_NAME, startsWith("ead")),   // false
                new OperableStatement<>(ELEMENT_NAME, endsWith("eade"))    // false
        ));
        LogicalStatement<OperableStatement<String>> orStatement = new LogicalStatement<>(ELEMENT_SUBELEMENTS, AND, orStatements);   // false

        HashSet<Statement> statements = newHashSet(asList(
                new OperableStatement<>(ELEMENT_TEXT, startsWith("This is the first")),  // true
                new OperableStatement<>(ELEMENT_TEXT, endsWith("second text block")),   // true
                orStatement
        ));
        LogicalStatement statement = new LogicalStatement(ELEMENT_SUBELEMENTS, OR, statements); // true
        LogicalStatementEvaluator evaluator = new LogicalStatementEvaluator();
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, statement, WEB_PAGE);
        boolean success = result.isSuccess();
        assertTrue(success);
    }

    @Test
    void test_with_text_block__false() {
        HashSet<OperableStatement<String>> orStatements = newHashSet(asList(
                new OperableStatement<>(ELEMENT_NAME, startsWith("ead")),   // false
                new OperableStatement<>(ELEMENT_NAME, endsWith("eade"))    // false
        ));
        LogicalStatement<OperableStatement<String>> orStatement = new LogicalStatement<>(ELEMENT_SUBELEMENTS, AND, orStatements);   // false

        HashSet<Statement> statements = newHashSet(asList(
                new OperableStatement<>(ELEMENT_TEXT, startsWith("Th9is is the first")),  // true
                new OperableStatement<>(ELEMENT_TEXT, endsWith("second text bl0ock")),   // true
                orStatement
        ));
        LogicalStatement statement = new LogicalStatement(ELEMENT_SUBELEMENTS, OR, statements); // true
        LogicalStatementEvaluator evaluator = new LogicalStatementEvaluator();
        ElementQueryResult result = evaluator.evaluate(PARSED_ELEMENT, statement, WEB_PAGE);
        boolean success = result.isSuccess();
        assertFalse(success);
    }
}