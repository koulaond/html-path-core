package path.core.query.syntax;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static lombok.AccessLevel.PRIVATE;

@EqualsAndHashCode
@AllArgsConstructor(access = PRIVATE)
@Getter
public class Query {

    private ElementQueryTemplate template;

    public static Query query(ElementQueryTemplate template) {
        return new Query(template);
    }
}
