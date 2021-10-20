package path.core.query.syntax;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class OperableStatement<T> extends Statement {
    protected Operator<T> operator;

    public OperableStatement(StatementTarget target, Operator<T> operator) {
        super(target);
        this.operator = operator;
    }
}
