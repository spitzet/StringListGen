package listgenerator;

import java.io.*;
import java.util.*;

/**
 * The ListGenerator class reads the next line from a text file and return a List of Strings that represent each
 * delimited token that it reads from the file. Determining which parser type to use is based on the extension of the
 * filename. For example, with files of type “*.tab”, given the following file contents:
 *
 * This<tab>is<tab>a<tab>test
 * red<tab>green<tab>blue
 *
 * The first call to a getNextLineTokens method returns a List of Strings that includes “This”, “is”, “a” and “test”.
 * The second call returns “red”, “green” and “blue”. A third call will return null.
 *
 * Created by Travis on 3/14/2017.
 */
public abstract class ListGenerator {

    private BufferedReader bufferedReader; // File reader

    /**
     * Creates the ListGenerator with the given file to be read.
     * @param file  File to be parsed by the ListGenerator
     */
    public ListGenerator(File file) throws IOException {
        checkFileExists(file);

        FileReader fileReader = new FileReader(file);
        bufferedReader = new BufferedReader(fileReader);
    }

    /**
     * Gets the delimiter to be used based on the type of object created by {@link ListGeneratorFactory}.
     * @return  The delimiter to be used
     */
    protected abstract String getDelimiter();

    /**
     * Checks if the file being used is not null and exists. If one of these conditions is not met, the method will
     * throw an IllegalArgumentException.
     */
    private void checkFileExists(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File is null.");
        }

        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist.");
        }
    }

    /**
     * Checks if the buffered reader is ready, then either reads the next line and parses it into a list of Strings
     * if the buffered reader is ready, or closes the reader if it is not.
     * @return  Parsed tokens from the next line of the file.
     * @throws IOException
     */
    public List<String> getNextLineTokens() throws IOException {
        String nextLine = checkBufferedReaderReady() ? bufferedReader.readLine() : null;
        List<String> stringList = null;

        if (nextLine != null) {
            String[] stringArray = nextLine.split(getDelimiter());
            stringList = Arrays.asList(stringArray);
        } else {
            bufferedReader.close();
        }

        return stringList;
    }

    /**
     * Checks if the BufferedReader is ready to be read.
     * @return  true if the bufferedReader is ready, false if not
     * @throws FileNotFoundException
     */
    private boolean checkBufferedReaderReady() throws FileNotFoundException {
        try {
            bufferedReader.ready();
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    /**
     * Closes the buffered reader if it is not already closed.
     */
    public void closeReader() throws IOException {
        if (checkBufferedReaderReady()) {
            bufferedReader.close();
        }
    }
}
