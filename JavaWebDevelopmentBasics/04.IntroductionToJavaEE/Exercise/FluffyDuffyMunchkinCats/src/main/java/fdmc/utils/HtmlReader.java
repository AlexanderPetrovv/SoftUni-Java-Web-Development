package fdmc.utils;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface HtmlReader {

    String readHtmlFile(String htmlFilePath) throws IOException;
}
