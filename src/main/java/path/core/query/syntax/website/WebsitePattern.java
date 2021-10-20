package path.core.query.syntax.website;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
@EqualsAndHashCode
public class WebsitePattern {

    private final Set<WebsitePatternNode> nodes = new HashSet<>();

    private final Set<WebsitePatternNodeConnection> connections = new HashSet<>();

    public WebsitePattern addConnection(WebsitePatternNode source, WebsitePatternNode successor, WebsitePatternNode... nextSuccessors) {
        nodes.add(source);
        nodes.add(successor);
        WebsitePatternNodeConnection connection = new WebsitePatternNodeConnection(source, successor);
        connections.add(connection);
        for (WebsitePatternNode nextSuccessor : nextSuccessors) {
            nodes.add(nextSuccessor);
            WebsitePatternNodeConnection nextConnection = new WebsitePatternNodeConnection(source, nextSuccessor);
            connections.add(nextConnection);
        }
        return this;
    }
}
