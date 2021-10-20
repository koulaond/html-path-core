package path.core.query.syntax.website;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import path.core.query.syntax.Query;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class WebsitePatternNode {
    private Query query;
}
