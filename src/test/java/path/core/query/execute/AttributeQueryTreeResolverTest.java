package path.core.query.execute;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import path.core.query.syntax.Statement;
import path.core.query.syntax.WebPage;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static path.core.query.syntax.Attributes.*;
import static path.core.query.syntax.Operator.*;

class AttributeQueryTreeResolverTest {
    private static Attribute ATTRIBUTE;
    private static WebPage WEB_PAGE;

    @BeforeAll
    static void setup() throws IOException {
        String html = TestUtils.loadHtmlFromResource("AttributeQueryTreeResolverTestHtml.html");
        Document parsedHtml = Jsoup.parse(html);
        Element parsedElement = parsedHtml.body().getElementsByTag("a").first();
        ATTRIBUTE = parsedElement.attributes().iterator().next();
        WEB_PAGE = WebPage.builder()
                .html(parsedHtml.html())
                .build();
    }

    @Test
    public void resolveAttributeWithNameStartsWithOperableStatement() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = hasName(startsWith("hre"));
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertTrue(result);
    }

    @Test
    public void resolveAttributeWithNameEndsWithOperableStatement() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = hasName(endsWith("ref"));
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertTrue(result);
    }

    @Test
    public void resolveAttributeWithNameContainsOperableStatement() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = hasName(contains("re"));
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertTrue(result);
    }

    @Test
    public void resolveAttributeWithNameExactOperableStatement() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = hasName(exact("href"));
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertTrue(result);
        statement = hasName("href");
        result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertTrue(result);
    }


    @Test
    public void resolveAttributeWithValueStartsWithOperableStatement() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = hasValue(startsWith("assets/images/"));
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertTrue(result);
    }

    @Test
    public void resolveAttributeWithValueEndsWithOperableStatement() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = hasValue(endsWith("mothman_cover.jpg"));
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertTrue(result);
    }

    @Test
    public void resolveAttributeWithValueContainsOperableStatement() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = hasValue(contains("mothman_cover"));
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertTrue(result);
    }

    @Test
    public void resolveAttributeWithValueExactOperableStatement() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = hasValue(exact("assets/images/bigfootbill_shadow_mothman_cover.jpg"));
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertTrue(result);
        statement = hasName("href");
        result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertTrue(result);
    }

    @Test
    public void resolveAttributeWithNameNotStartsWithOperableStatement() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = hasName(startsWith("hree"));
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertFalse(result);
    }

    @Test
    public void resolveAttributeWithNameNotEndsWithOperableStatement() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = hasName(endsWith("reff"));
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertFalse(result);
    }

    @Test
    public void resolveAttributeWithNameNotContainsOperableStatement() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = hasName(contains("ree"));
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertFalse(result);
    }

    @Test
    public void resolveAttributeWithNameNotExactOperableStatement() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = hasName(exact("href"));
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertTrue(result);
        statement = hasName("href");
        result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertTrue(result);
    }


    @Test
    public void resolveAttributeWithValueNotStartsWithOperableStatement() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = hasValue(startsWith("aassets/images/"));
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertFalse(result);
    }

    @Test
    public void resolveAttributeWithValueNotEndsWithOperableStatement() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = hasValue(endsWith("mothman_cover.jpgg"));
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertFalse(result);
    }

    @Test
    public void resolveAttributeWithValueNotContainsOperableStatement() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = hasValue(contains("mothmann_cover"));
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertFalse(result);
    }

    @Test
    public void resolveAttributeWithValueNotExactOperableStatement() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = hasValue(exact("assets/images//bigfootbill_shadow_mothman_cover.jpg"));
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertFalse(result);
        statement = hasValue("assets/images//bigfootbill_shadow_mothman_cover.jpg");
        result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertFalse(result);
    }

    @Test
    public void resolveAttributeComposableLogicalStatementAllTrue() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = hasValue(startsWith("assets"))
                .hasValue(endsWith("mothman_cover.jpg"))
                .hasName("href");
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertTrue(result);
    }

    @Test
    public void resolveAttributeComposableLogicalStatementOneFalse() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = hasValue(startsWith("assets"))
                .hasValue(endsWith("mothman_cover.jpg"))
                .hasName("hrf"); // false
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertFalse(result);
    }

    @Test
    public void resolveAttributeComposableLogicalStatementWithAnd() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = hasValue(startsWith("assets"))
                .and(hasValue(endsWith("mothman_cover.jpg")), hasName("href"));
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertTrue(result);
    }

    @Test
    public void resolveAttributeComposableLogicalStatementWithAndWithOneFalse() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = hasValue(startsWith("assets"))
                .and(hasValue(endsWith("mothman_cover.jpg")), hasName("hrf"));
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertFalse(result);
    }

    @Test
    public void resolveAttributeComposableLogicalStatementWithOr() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = hasValue(startsWith("assets"))
                .or(hasValue(endsWith("mothman_cover.jpg")), hasName("href"));
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertTrue(result);
    }

    @Test
    public void resolveAttributeComposableLogicalStatementWithOrWithOneFalse() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = hasValue(startsWith("assets"))
                .or(hasValue(endsWith("mothman_cover.jpg")), hasName("hrf"));
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertTrue(result);
    }

    @Test
    public void resolveAttributeComposableLogicalStatementWithOrWithAllFalse() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = hasValue(startsWith("assets"))
                .or(hasValue(endsWith("mothman_coveer.jpg")), hasName("hrf"));
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertFalse(result);
    }

    @Test
    public void resolveAttributeComposableLogicalStatementAllTrueWith__AND_Of_ORs() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = and(      // True
                or(     // true
                        hasValue(startsWith("assets")),
                        hasValue(endsWith("cover.jpg"))
                ),
                or(     // true
                        hasValue(endsWith("man_cover.jpg")),
                        hasName("href")
                )
        );
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertTrue(result);
    }

    @Test
    public void resolveAttributeComposableLogicalStatementWith__One_True_In_OR() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = and(      // true
                or(     // true
                        hasValue(startsWith("ssets")),    // false
                        hasValue(endsWith("cover.jpg"))   // true
                ),
                or(     // true
                        hasValue(endsWith("man_cover.jpg")),    // true
                        hasName("href")                         // true
                )
        );
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertTrue(result);
    }

    @Test
    public void resolveAttributeComposableLogicalStatementWith__All_False_In_One_OR() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = and(      // false
                or(     // false
                        hasValue(startsWith("ssets")),    // false
                        hasValue(endsWith("cover.jp"))   // false
                ),
                or(     // true
                        hasValue(endsWith("man_cover.jpg")),    // true
                        hasName("href")                         // true
                )
        );
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertFalse(result);
    }

    @Test
    public void resolveAttributeComposableLogicalStatementWith__All_False_ORs_In_AND() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = and(      // false
                or(     // false
                        hasValue(startsWith("ssets")),    // false
                        hasValue(endsWith("cover.jp"))   // false
                ),
                or(     // false
                        hasValue(endsWith("man_cover.jp")),      // false
                        hasName("hreef")                         // false
                )
        );
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertFalse(result);
    }

    @Test
    public void resolveAttributeComposableLogicalStatementWith__OR_Of_ANDs() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = or(      // true
                and(     // true
                        hasValue(startsWith("assets")),    // true
                        hasValue(endsWith("cover.jpg"))   // true
                ),
                and(     // true
                        hasValue(endsWith("man_cover.jpg")),      // true
                        hasName("href")                         // true
                )
        );
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertTrue(result);
    }

    @Test
    public void resolveAttributeComposableLogicalStatementWith__OR_Of_ANDs__One_AND_Is_False() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = or(      // true
                and(     // false
                        hasValue(startsWith("ssets")),    // false
                        hasValue(endsWith("cover.jpg"))   // true
                ),
                and(     // true
                        hasValue(endsWith("man_cover.jpg")),      // true
                        hasName("href")                         // true
                )
        );
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertTrue(result);
    }

    @Test
    public void resolveAttributeComposableLogicalStatementWith__OR_Of_ANDs__Both_ANDs_Are_False() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = or(      // false
                and(     // false
                        hasValue(startsWith("ssets")),    // false
                        hasValue(endsWith("cover.jpg"))   // true
                ),
                and(     // false
                        hasValue(endsWith("man_cover.jp")),      // false
                        hasName("href")                         // true
                )
        );
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertFalse(result);
    }

    @Test
    public void resolveAttributeComposableLogicalStatementWith__Multilevel_ANDs_ORs() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = or(      // true
                and(     // false
                        or(     // true
                                hasValue(endsWith(".jpg")),     // true
                                hasName("href")                 // true
                        ),
                        and(    // true
                                hasValue(endsWith("cover.jpg")),    // true
                                hasValue(endsWith("over.jpg")),     // true
                                hasValue(endsWith("ver.jpg")),      // true
                                hasValue(endsWith("er.jpg")),       // true
                                hasValue(endsWith("r.jpg")),
                                or(    // true
                                        hasName(contains("hr")),    // true
                                        hasName(contains("re")),    // true
                                        hasName(contains("ef")),    // true
                                        hasName(contains("href"))   // true
                                )
                        )
                ),
                and(     // false
                        hasValue(endsWith("man_cover.jpg")),      // true
                        hasName("href")                         // true
                )
        );
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertTrue(result);
    }

    @Test
    public void resolveAttributeComposableLogicalStatementWith__Multilevel_ANDs_ORs_where_nested_AND_fails() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = and(      // false
                and(     // false
                        or(     // true
                                hasValue(endsWith(".jpg")),     // true
                                hasName("href")                 // true
                        ),
                        and(    // false
                                hasValue(endsWith("cover.jpg")),    // true
                                hasValue(endsWith("over.jpg")),     // true
                                hasValue(endsWith("ver.jpg")),      // true
                                hasValue(endsWith("er.jpg")),       // true
                                hasValue(endsWith("r.jpg")),
                                and(   // false
                                        hasName(contains("he")),    // false
                                        hasName(contains("re")),    // true
                                        hasName(contains("ef")),    // true
                                        hasName(contains("href"))   // true
                                )
                        )
                ),
                and(     // false
                        hasValue(endsWith("man_cover.jpg")),      // true
                        hasName("href")                         // true
                )
        );
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertFalse(result);
    }

    @Test
    public void resolveAttributeComposableLogicalStatementWith__Multilevel_ANDs_ORs_many_siblings() {
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        Statement statement = and(      // false
                or(     // false
                        hasValue(endsWith(".j")),     // false
                        hasName("hre")                 // false
                ),
                hasValue(endsWith("cover.jpg")),    // true
                hasValue(endsWith("over.jpg")),     // true
                hasValue(endsWith("ver.jpg")),      // true
                hasValue(endsWith("er.jpg")),       // true
                hasValue(endsWith("r.jpg"))
        );
        boolean result = resolver.resolveAttribute(ATTRIBUTE, statement);
        assertFalse(result);
    }
}