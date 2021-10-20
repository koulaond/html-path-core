package path.core.query.execute;

import path.core.query.syntax.*;

import java.util.HashMap;
import java.util.Map;

public class EvaluatorProvider {

    private static final Map<Class<? extends Statement>, StatementEvaluator> evaluators = new HashMap<>();

    static {
        evaluators.put(OperableStatement.class, new OperableStatementEvaluator());
        evaluators.put(LogicalStatement.class, new LogicalStatementEvaluator());
        evaluators.put(ElementQueryTemplate.class, new ElementQueryTemplateEvaluator());
        evaluators.put(AttributeQueryTemplate.class, new AttributeQueryTemplateEvaluator());
    }

    static <S extends Statement> StatementEvaluator getEvaluator(S statement) {
        return getEvaluator(statement.getClass());
    }

    static StatementEvaluator getEvaluator(Class<? extends Statement> clazz) {
        return evaluators.get(clazz);
    }
}
