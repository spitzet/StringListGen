package listgenerator;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Created by Travis on 3/13/2017.
 */
public class ListGeneratorTest {
    private static String randomName;
    private static Set<File> testFiles; // Set of test files to be deleted after running tests
    private static ListGeneratorFactory generatorFactory;

    // Generate a random name for the test files to use, and initialize the list of test files.
    @BeforeClass
    public static void start() {
        generatorFactory = new ListGeneratorFactory();
        randomName = UUID.randomUUID().toString();
        testFiles = new HashSet<>();
    }

    /**
     * Performs positive testing on the ListGenerator class for all supported delimiters returned by the class.
     * Creates a randomly named test file with each delimiter, then populates the file with test data.
     *
     * Positive tests:
     * 1. Test that the most basic case returns the expected result for each delimiter
     * 2. Test that an empty line returns a list with an empty string
     * 3. Test that leading delimiters produce empty string elements
     * 4. Test that null is read when there is no more data
     * 5. Test that null is continuously read after there is no more data
     *
     * @throws IOException
     */
    @Test
    public void positiveTest() throws IOException {
            final File TEST_FILE = createTestFile("tab");
            final String DELIMITER = "\t";

            // Write the test data to the test file
            PrintWriter writer = new PrintWriter(TEST_FILE, "UTF-8");
            writer.println("this" + DELIMITER + "is" + DELIMITER + "a" + DELIMITER + "test");
            writer.println();
            writer.println(DELIMITER + DELIMITER + "red" + DELIMITER + "green" + DELIMITER + "blue");
            writer.close();

            // Create expected output
            List<String> expectedLineOne = Arrays.asList("this", "is", "a", "test");
            List<String> expectedLineTwo = Arrays.asList("");
            List<String> expectedLineThree = Arrays.asList("", "", "red", "green", "blue");

            // Gather actual output from the generator
            ListGenerator generator = generatorFactory.getListGenerator(TEST_FILE);
            List<String> actualLineOne = generator.getNextLineTokens();
            List<String> actualLineTwo = generator.getNextLineTokens();
            List<String> actualLineThree = generator.getNextLineTokens();
            List<String> actualLineFour = generator.getNextLineTokens();
            List<String> actualLineFive = generator.getNextLineTokens();

            // Run test cases
            assertEquals("Actual tokens from line 1 did not match the expected tokens.",
                    expectedLineOne, actualLineOne);
            assertEquals("Actual tokens from line 2 did not match the expected tokens.",
                    expectedLineTwo, actualLineTwo);
            assertEquals("Actual tokens from line 3 did not match the expected tokens.",
                    expectedLineThree, actualLineThree);
            assertNull("Expected line 4 to be null.", actualLineFour);
            assertNull("Expected line 5 to be null.", actualLineFive);
    }

    /**
     * Negative test that an IllegalArgumentException is thrown with the correct error message when attempting to use
     * the ListGenerator with an unsupported file extension.
     *
     * @throws IOException
     */
    @Test
    public void testUnsupportedFileExtension() throws IOException {
        final File TEST_FILE = createTestFile("txt");

        try {
            generatorFactory.getListGenerator(TEST_FILE);
            fail("Expected IllegalArgumentException to be thrown for unsupported file extension.");
        } catch (IllegalArgumentException e) {
            String expectedMessage = "File extension not supported: txt";
            assertEquals("IllegalArgumentException did not have the expected error message for a bad file " +
                    "extension.", expectedMessage, e.getMessage());
        }
    }

    /**
     * Negative test that an IllegalArgumentException is thrown with the correct error message when attempting to use
     * the ListGenerator with a null file.
     *
     * Negative tests:
     * 1. Test that an exception is thrown when creating a ListGenerator with ListGeneratorFactory
     * 2. Test that an exception is thrown when creating a ListGenerator with a TabDelimitedListGenerator
     *
     * @throws IOException
     */
    @Test
    public void testNullFile() throws IOException {
        String expectedMessage = "File is null.";

        // Test case 1
        try {
            generatorFactory.getListGenerator(null);
            fail("Expected IllegalArgumentException to be thrown for null file argument.");
        } catch (IllegalArgumentException e) {
            assertEquals("IllegalArgumentException did not have the expected error message for a null argument.",
                    expectedMessage, e.getMessage());
        }

        // Test case 2
        try {
            new TabDelimitedListGenerator(null);
            fail("Expected IllegalArgumentException to be thrown for nonexistent file argument.");
        } catch (IllegalArgumentException e) {
            assertEquals("IllegalArgumentException did not have the expected error message for a nonexistent " +
                    "file argument.", expectedMessage, e.getMessage());
        }
    }

    /**
     * Negative test that an IllegalArgumentException is thrown with the correct error message when attempting to use
     * the ListGenerator with a bad argument.
     *
     * Negative tests:
     * 1. Test that an exception is thrown when creating a ListGenerator with ListGeneratorFactory
     * 2. Test that an exception is thrown when creating a ListGenerator with a TabDelimitedListGenerator
     *
     * @throws IOException
     */
    @Test
    public void testNonExistentFileArgument() throws IOException {
        // Test case 1
        final File NONEXISTENT_FILE = new File(randomName+1 + ".tab");
        String expectedMessage = "File does not exist.";

        try {
            generatorFactory.getListGenerator(NONEXISTENT_FILE);
            fail("Expected IllegalArgumentException to be thrown for nonexistent file argument.");
        } catch (IllegalArgumentException e) {
            assertEquals("IllegalArgumentException did not have the expected error message for a nonexistent " +
                    "file argument.", expectedMessage, e.getMessage());
        }

        // Test case 2
        try {
            new TabDelimitedListGenerator(NONEXISTENT_FILE);
            fail("Expected IllegalArgumentException to be thrown for nonexistent file argument.");
        } catch (IllegalArgumentException e) {
            assertEquals("IllegalArgumentException did not have the expected error message for a nonexistent " +
                    "file argument.", expectedMessage, e.getMessage());
        }
    }

    // Deletes all test files in the test file set
    @AfterClass
    public static void finish() throws IOException {
        for (File testFile : testFiles) {
            Files.delete(testFile.toPath());
        }
    }

    /**
     * Creates a new test file with a random name and the given file extension, then adds it to the set of test files.
     *
     * @param fileExtension     The file extension to use with the new file.
     * @return                  The newly created file
     * @throws IOException
     */
    private File createTestFile(String fileExtension) throws IOException {
        File testFile = new File(randomName + "." + fileExtension);
        testFile.createNewFile();
        testFiles.add(testFile);
        return testFile;
    }
}
