package path.core.query.execute;

import org.jsoup.nodes.Element;
import path.core.query.syntax.OperableStatement;
import path.core.query.syntax.Operator;
import path.core.query.syntax.StatementTarget;
import path.core.query.syntax.WebPage;

import static java.util.Collections.emptyList;

public class OperableStatementEvaluator implements StatementEvaluator<OperableStatement, ElementQueryResult> {

    @Override
    public ElementQueryResult evaluate(Element element, OperableStatement statement, WebPage webPage) {
        Operator operator = statement.getOperator();
        StatementTarget target = statement.getTarget();
        ElementQueryResult result;
        switch (target) {
            case ELEMENT_NAME:
                boolean nameMatches = operator.operate(element.tag().getName());
                result = new ElementQueryResult(webPage, statement, emptyList(), nameMatches, element);
                break;
            case ELEMENT_TEXT:
                boolean textMatches = operator.operate(element.text());
                result = new ElementQueryResult(webPage, statement, emptyList(), textMatches, element);
                break;
            default:
                result = new ElementQueryResult(webPage, statement, emptyList(), false, element);
        }
        return result;
    }
}
