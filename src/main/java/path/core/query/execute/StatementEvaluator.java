package path.core.query.execute;

import org.jsoup.nodes.Element;
import path.core.query.syntax.Statement;
import path.core.query.syntax.WebPage;

public interface StatementEvaluator<S extends Statement, R extends ElementQueryResult> {

    R evaluate(Element element, S statement, WebPage webPage);
}
