package utils.filesystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.spark.api.java.JavaPairRDD;

import java.io.IOException;
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

        // For an explanation of what the files of the output are see
        // https://stackoverflow.com/questions/23898098/what-are-the-files-generated-by-spark-when-using-saveastextfile#23902886
        results.saveAsTextFile(resultsDirectory);
    }
}
