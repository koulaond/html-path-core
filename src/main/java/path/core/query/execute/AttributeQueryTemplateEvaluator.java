package path.core.query.execute;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import path.core.query.syntax.AttributeQueryTemplate;
import path.core.query.syntax.WebPage;

public class AttributeQueryTemplateEvaluator implements StatementEvaluator<AttributeQueryTemplate, ElementQueryResult> {

    @Override
    public AttributeQueryTreeResult evaluate(Element element, AttributeQueryTemplate statement, WebPage webPage) {
        Attributes attributes = element.attributes();
        AttributeQueryTreeResolver resolver = new AttributeQueryTreeResolver();
        for (Attribute attribute : attributes) {
            boolean resultForAttribute = resolver.resolveAttribute(attribute, statement);
            if (resultForAttribute) {
                return new AttributeQueryTreeResult(webPage, statement, null, true, element, attribute);
            }
        }
        return new AttributeQueryTreeResult(webPage, statement, null, false, element, null);
    }
}
