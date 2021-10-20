package path.core.query.syntax;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PACKAGE)
@Builder
public class WebPage {
    private UUID id;
    private String pageTitle;
    private String html;
    private URL sourceUrl;

    private Set<URL> outcomeUrlsOnDomain;
    private Set<URL> outcomeUrlsOutOfDomain;

    private Set<String> scripts;
    private Set<String> embeddedStylesheets;
    private Set<String> stylesheetPaths;

    private Set<String> tables;
    private Set<String> imageSources;
}
