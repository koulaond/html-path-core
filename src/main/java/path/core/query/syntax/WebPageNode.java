package path.core.query.syntax;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter(AccessLevel.PACKAGE)
public class WebPageNode {
    private WebPage webPage;
    private URL sourceUrl;
    private Set<WebPageNode> outcomeWebPageNodes = new HashSet<>();
    private Set<WebPageNode> incomeWebPageNodes = new HashSet<>();
}
