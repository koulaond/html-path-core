package path.core.query.syntax;

public final class Attributes {

    public static AttributeQueryTemplate hasName(String name) {
        return new AttributeQueryTemplate().hasName(name);
    }

    public static AttributeQueryTemplate hasName(Operator<String> operator) {
        return new AttributeQueryTemplate().hasName(operator);
    }

    public static AttributeQueryTemplate hasValue(String value) {
        return new AttributeQueryTemplate().hasValue(value);
    }

    public static AttributeQueryTemplate hasValue(Operator<String> operator) {
        return new AttributeQueryTemplate().hasValue(operator);
    }

    public static AttributeQueryTemplate or(AttributeQueryTemplate... definitions) {
        return new AttributeQueryTemplate().or(definitions);
    }

    public static AttributeQueryTemplate and(AttributeQueryTemplate... definitions) {
        return new AttributeQueryTemplate().and(definitions);
    }
}
