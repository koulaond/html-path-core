package path.core.query.execute;

import lombok.Getter;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import path.core.query.syntax.Statement;
import path.core.query.syntax.WebPage;

import java.util.List;

@Getter
public class AttributeQueryTreeResult extends ElementQueryResult {

    private final Attribute foundAttribute;

    public AttributeQueryTreeResult(WebPage sourceWebPage,
                                    Statement matchingStatement,
                                    List<ElementQueryResult> subElementResults,
                                    boolean success,
                                    Element element,
                                    Attribute foundAttribute) {
        super(sourceWebPage, matchingStatement, subElementResults, success, element);
        this.foundAttribute = foundAttribute;
    }
}
