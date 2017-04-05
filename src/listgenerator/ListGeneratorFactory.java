package listgenerator;

import java.io.File;
import java.io.IOException;

/**
 * Created by Travis on 3/23/2017.
 */
public class ListGeneratorFactory {

    /**
     * Creates a concrete {@link ListGenerator} object based on the file extension of the file argument. If the file
     * extension is not supported or null, the method will throw an IllegalArgumentException.
     *
     * @param file  File to be parsed by the ListGenerator
     * @return  The delimiter to be used
     */
    public ListGenerator getListGenerator(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File is null.");
        }
        String fileExtension = getExtension(file);

        if (fileExtension.equalsIgnoreCase("tab")) {
            return new TabDelimitedListGenerator(file);
        } else {
            throw new IllegalArgumentException("File extension not supported: " + fileExtension);
        }
    }



    /**
     * Extracts the file extension from file being used.
     * @return  the file extension
     */
    private String getExtension(File file){
        String path = file.getPath();
        int extensionIndex = file.getPath().lastIndexOf('.');
        String fileType = path.substring(extensionIndex + 1, path.length());
        return fileType;
    }
}
