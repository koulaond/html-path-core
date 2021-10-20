package path.core.query.execute;

import lombok.AllArgsConstructor;
import lombok.Data;
import path.core.query.syntax.WebPage;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class BatchQueryResult {
    private int numberOfInputPages;
    private int numberOfFoundPages;
    private Map<WebPage, List<ElementQueryResult>> results;
}
