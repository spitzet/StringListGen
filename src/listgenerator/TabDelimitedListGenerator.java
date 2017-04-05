package listgenerator;

import java.io.File;
import java.io.IOException;

/**
 * Created by Travis on 3/23/2017.
 */
public class TabDelimitedListGenerator extends ListGenerator {

    protected TabDelimitedListGenerator(File file) throws IOException {
        super(file);
    }

    /**
     * See {@link ListGenerator#getDelimiter()}
     */
    @Override
    protected String getDelimiter() {
        return "\t";
    }
}
