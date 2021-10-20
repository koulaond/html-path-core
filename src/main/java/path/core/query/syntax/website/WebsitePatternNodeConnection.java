package path.core.query.syntax.website;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class WebsitePatternNodeConnection {

    private WebsitePatternNode source;

    private WebsitePatternNode target;
}
