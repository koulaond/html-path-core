package path.core.query.syntax;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public abstract class Statement {

    protected StatementTarget target;

    public Statement(StatementTarget target) {
        this.target = target;
    }
}
