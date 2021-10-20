package path.core.query.execute;

import org.jsoup.nodes.Attribute;
import path.core.query.syntax.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static path.core.query.syntax.LogicalStatementType.AND;
import static path.core.query.syntax.LogicalStatementType.OR;

public class AttributeQueryTreeResolver {

    public  boolean resolveAttribute(Attribute attribute, Statement statement) {
        return AttributeStatementResolverWrapper.resolve(attribute, statement);
    }

    private interface AttributeStatementResolver<S extends Statement> {

        boolean resolve(Attribute attribute, S statement);
    }

    private static class AttributeLogicalStatementResolver implements AttributeStatementResolver<LogicalStatement<Statement>> {

        @Override
        public boolean resolve(Attribute attribute, LogicalStatement<Statement> statement) {
            Set<Statement> subStatements = statement.getSubordinates();
            LogicalStatementType type = statement.getType();
            for (Statement subStatement : subStatements) {
                boolean result = AttributeStatementResolverWrapper.resolve(attribute, subStatement);
                if (AND.equals(type) && !result) {
                    return false;
                } else if (OR.equals(type) && result) {
                    return true;
                }
            }
            // On this place, type is either AND and all results were true, or type is OR and no result was true
            return AND.equals(type);
        }
    }

    private static class AttributeOperableStatementResolver implements AttributeStatementResolver<OperableStatement<?>> {

        @Override
        public boolean resolve(Attribute attribute, OperableStatement<?> statement) {
            StatementTarget target = statement.getTarget();
            Operator<?> operator = statement.getOperator();
            boolean result = false;
            switch (target) {
                case ATTRIBUTE_NAME:
                    result = operator.operate(attribute.getKey());
                    break;
                case ATTRIBUTE_VALUE:
                    result = operator.operate(attribute.getValue());
                    break;
            }
            return result;
        }
    }

    @SuppressWarnings("unchecked")
    private static class AttributeStatementResolverWrapper {

        private static final Map<Class<? extends Statement>, AttributeStatementResolver<? extends Statement>> statementResolvers = new HashMap<>();

        static {
            statementResolvers.put(LogicalStatement.class, new AttributeLogicalStatementResolver());
            statementResolvers.put(OperableStatement.class, new AttributeOperableStatementResolver());
            statementResolvers.put(AttributeQueryTemplate.class, new AttributeLogicalStatementResolver());
        }

        static boolean resolve(Attribute attribute, Statement statement) {
            AttributeStatementResolver<Statement> resolver = (AttributeStatementResolver<Statement>) statementResolvers.get(statement.getClass());
            return resolver.resolve(attribute, statement);
        }
    }
}
