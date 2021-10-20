package path.core.query.execute;

import lombok.Getter;
import org.jsoup.nodes.Element;
import path.core.query.syntax.Statement;
import path.core.query.syntax.WebPage;

import java.util.List;

@Getter
public class ElementQueryResult  {
    protected final WebPage sourceWebPage;
    protected final Statement matchingStatement;
    protected final List<ElementQueryResult> subElementResults;
    protected final boolean success;
    protected final Element element;

    public ElementQueryResult(WebPage sourceWebPage, Statement matchingStatement, List<ElementQueryResult> subElementResults, boolean success, Element element) {
        this.sourceWebPage = sourceWebPage;
        this.matchingStatement = matchingStatement;
        this.subElementResults = subElementResults;
        this.success = success;
        this.element = element;
    }
}
