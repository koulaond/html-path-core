package path.core.query.syntax;

public final class Elements {

    public static ElementQueryTemplate withName(String name) {
        return new ElementQueryTemplate().withName(name);
    }

    public static ElementQueryTemplate withName(Operator<String> operator) {
        return new ElementQueryTemplate().withName(operator);
    }

    public static ElementQueryTemplate hasText() {
        return new ElementQueryTemplate().hasText();
    }

    public static ElementQueryTemplate hasText(Operator<String> operator) {
        return new ElementQueryTemplate().hasText(operator);
    }

    public static ElementQueryTemplate hasAttribute(String attrName) {
        return hasAttribute(Attributes.hasName(attrName));
    }

    public static ElementQueryTemplate hasAttribute(AttributeQueryTemplate attributeQueryTemplate) {
        return new ElementQueryTemplate().hasAttribute(attributeQueryTemplate);
    }

    public static ElementQueryTemplate and(ElementQueryTemplate... templates) {
        return new ElementQueryTemplate().and(templates);
    }

    public static ElementQueryTemplate or(ElementQueryTemplate... templates) {
        return new ElementQueryTemplate().or(templates);
    }

    public static ElementQueryTemplate containsSubElement(ElementQueryTemplate elementQueryTemplate) {
        return new ElementQueryTemplate().containsSubElement(elementQueryTemplate);
    }
}
