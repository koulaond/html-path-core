package path.core.query.execute;


import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class TestUtils {
    public static String loadHtmlFromResource(String resourceFile) throws IOException {
        ClassLoader classLoader = TestUtils.class.getClassLoader();
        URL resource = classLoader.getResource(resourceFile);
        File webPageFile = new File(resource.getFile());
        return IOUtils.toString(new FileReader(webPageFile));
    }
}
