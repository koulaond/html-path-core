package path.core.query.syntax;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashSet;

import static java.util.Arrays.asList;

@EqualsAndHashCode(callSuper = true)
@Getter
public class ElementQueryTemplate extends LogicalStatement<Statement> {

    private boolean processSubElements;

    ElementQueryTemplate() {
        super(StatementTarget.ELEMENT_SUBELEMENTS, LogicalStatementType.AND, new HashSet<>());
        processSubElements(true);
    }

    public ElementQueryTemplate withName(String name) {
        return withName(Operator.exact(name));
    }

    public ElementQueryTemplate withName(Operator<String> operator) {
        subordinates.add(new OperableStatement<>(StatementTarget.ELEMENT_NAME, operator));
        return this;
    }

    public ElementQueryTemplate hasText() {
        subordinates.add(new OperableStatement<>(StatementTarget.ELEMENT_NAME, Operator.any()));
        return this;
    }

    public ElementQueryTemplate hasText(Operator<String> operator) {
        subordinates.add(new OperableStatement<>(StatementTarget.ELEMENT_NAME, operator));
        return this;
    }

    public ElementQueryTemplate hasAttribute(String attrName) {
        return hasAttribute(Attributes.hasName(attrName));
    }

    public ElementQueryTemplate hasAttribute(AttributeQueryTemplate attributeQueryTemplate) {
        subordinates.add(attributeQueryTemplate);
        return this;
    }

    public ElementQueryTemplate and(ElementQueryTemplate... templates) {
        for (ElementQueryTemplate template : templates) {
            template.processSubElements(false);
        }
        subordinates.add(new LogicalStatement<>(StatementTarget.ELEMENT_SUBELEMENTS, LogicalStatementType.AND, new HashSet<>(asList(templates))));
        return this;
    }

    public ElementQueryTemplate or(ElementQueryTemplate... templates) {
        for (ElementQueryTemplate template : templates) {
            template.processSubElements(false);
        }
        subordinates.add(new LogicalStatement<>(StatementTarget.ELEMENT_SUBELEMENTS, LogicalStatementType.OR, new HashSet<>(asList(templates))));
        return this;
    }

    public ElementQueryTemplate containsSubElement(ElementQueryTemplate elementQueryTemplate) {
        subordinates.add(elementQueryTemplate);
        return this;
    }

    void processSubElements(boolean value) {
        this.processSubElements = value;
    }
}
