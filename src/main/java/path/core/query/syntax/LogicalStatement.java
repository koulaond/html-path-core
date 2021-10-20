package path.core.query.syntax;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Set;

@Getter
@EqualsAndHashCode(callSuper = true)
public class LogicalStatement<ST extends Statement> extends Statement {
    protected LogicalStatementType type;
    protected Set<ST> subordinates;

    public LogicalStatement(StatementTarget target, LogicalStatementType type, Set<ST> subordinates) {
        super(target);
        this.type = type;
        this.subordinates = subordinates;
    }
}
