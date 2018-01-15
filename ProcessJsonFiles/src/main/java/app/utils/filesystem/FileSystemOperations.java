package app.utils.filesystem;

import app.internal.Reducible;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.spark.api.java.JavaPairRDD;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemOperations {

    private static final Logger LOGGER = LogManager.getLogger(FileSystemOperations.class);

    public static void deleteFiles(String parentDirectory) {
        Path start = Paths.get(parentDirectory);

        if (!Files.exists(start)) {
            LOGGER.info("Directory {} does not exist. Nothing to delete", start.toAbsolutePath().toString());
            return;
        }

        try {
            Files.walkFileTree(start, new DeleteFileTreeVisitor());
        } catch (IOException e) {
            LOGGER.info("While deleting {} directory recursively the following exception occurred {}",
                    start.toAbsolutePath().toString(), e.getCause());
        }
    }

    public static void saveResults(JavaPairRDD<?, ?> results, String resultsDirectory) {
        FileSystemOperations.deleteFiles(resultsDirectory);

        LOGGER.info("Processing results will be output in {} directory", resultsDirectory);
        results.saveAsTextFile(resultsDirectory);
    }

    /**
     * Creates a file containing the value the {@link Reducible} object provides via its {@link Reducible#getValueToPersist()}
     * I wrote this because I couldn't find a way to store the value of Spark's reduce operations using "standard" Spark's
     * methods such as "saveAsTextFile"
     *
     * @param reducible
     * @param resultsDirectory
     * @throws IOException in case results' directory or results' file can't be created or results can't be saved into
     *                     result's file.
     */
    public static void saveResults(Reducible reducible, String resultsDirectory) throws IOException {
        FileSystemOperations.deleteFiles(resultsDirectory);

        LOGGER.info("Processing results will be output in {} directory", resultsDirectory);

        Path dirPath = Paths.get(resultsDirectory);

        // The following directory/results file as tried/catched in sequence instead of in a single block to be able to
        // identify failure's culprit faster.
        try {
            Files.createDirectory(dirPath);
        } catch (IOException e) {
            LOGGER.info("Result's directory {} couldn't be created. Results will not be persisted.", resultsDirectory);
            throw new IOException(e);
        }

        Path filePath = Paths.get(resultsDirectory + "/" + reducible.getClass().getSimpleName() + "_results");
        try {
            Files.createFile(filePath);
        } catch (IOException e) {
            LOGGER.info("Result's file {} couldn't be created. Results will not be persisted.", resultsDirectory);
            throw new IOException(e);
        }

        try {
            Files.write(filePath, reducible.getValueToPersist().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOGGER.info("A exception occurred while saving results in {} file. Results will not be persisted.",
                    filePath.toString());
        }
    }
}
