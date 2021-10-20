package path.core.query.execute;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import path.core.query.syntax.ElementQueryTemplate;
import path.core.query.syntax.Statement;
import path.core.query.syntax.WebPage;

import java.util.*;

import static java.util.Collections.singletonList;

public class ElementQueryTemplateEvaluator implements StatementEvaluator<ElementQueryTemplate, ElementQueryResult> {
    @Override
    public ElementQueryResult evaluate(Element element, ElementQueryTemplate statement, WebPage webPage) {
        Set<Statement> subordinates = statement.getSubordinates();
        ElementQueryResult resultForAnyElement = null;
        Statement lastSubStatement = null;
        List<ElementQueryResult> subElementResults = new ArrayList<>();
        if (statement.isProcessSubElements()) {
            List<ElementQueryResult> resultsPerElement = new ArrayList<>();
            Elements subElements = element.children();
            LOOP_ELEMENT:
            for (Element subElement : subElements) {
                for (Statement subStatement : subordinates) {
                    lastSubStatement = subStatement;
                    StatementEvaluator subEvaluator = EvaluatorProvider.getEvaluator(subStatement);
                    ElementQueryResult subElementResult = subEvaluator.evaluate(subElement, subStatement, webPage);
                    if (!subElementResult.isSuccess()) {
                        subElementResults.clear();
                        continue LOOP_ELEMENT;
                    } else {
                        subElementResults.add(subElementResult);
                    }
                }
                // All sub-element results are not empty (were not cleared), sub-element found
                if (!subElementResults.isEmpty()) {
                    resultsPerElement.add(new ElementQueryResult(webPage, lastSubStatement, subElementResults, true, subElement));
                }
            }
            if (!resultsPerElement.isEmpty()) {
                resultForAnyElement = new ElementQueryResult(webPage, lastSubStatement, resultsPerElement, true, null);
            } else {
                resultForAnyElement = new ElementQueryResult(webPage, lastSubStatement, resultsPerElement, false, null);
            }
        } else {
            for (Statement subStatement : subordinates) {
                StatementEvaluator subEvaluator = EvaluatorProvider.getEvaluator(subStatement);
                ElementQueryResult elementResult = subEvaluator.evaluate(element, subStatement, webPage);
                if (!elementResult.isSuccess()) {
                    resultForAnyElement = new ElementQueryResult(webPage, statement, singletonList(elementResult), false, null);
                    break;
                } else {
                    subElementResults.add(elementResult);
                }
            }
            if (!subElementResults.isEmpty()) {
                resultForAnyElement = new ElementQueryResult(webPage, statement, subElementResults, true, null);
            }
        }
        return resultForAnyElement;
    }
}
