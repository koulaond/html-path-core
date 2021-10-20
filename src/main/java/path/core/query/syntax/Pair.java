package path.core.query.syntax;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Pair<T> {

    private T first;

    private T second;
}
