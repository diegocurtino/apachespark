package app.utils.filesystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class DeleteFileTreeVisitor extends SimpleFileVisitor<Path> {
    private static final Logger LOGGER = LogManager.getLogger(DeleteFileTreeVisitor.class);

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        super.preVisitDirectory(dir, attrs);

        // This check matters depending on whether the deletion is attempted at the beginning or at the end of the test.
        // At the beginning it's possible that the directory where the results will be stored does not exist.
        if (Files.exists(dir)) {
            LOGGER.info("Deleting the content of directory {} ", dir.toAbsolutePath().toString());
            return FileVisitResult.CONTINUE;
        }

        LOGGER.info("Directory {} does not exist. There's nothing to delete", dir.toAbsolutePath().toString());
        return FileVisitResult.SKIP_SUBTREE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        super.visitFile(file, attrs);

        if (attrs.isRegularFile()) {
            LOGGER.info("Deleting file {}", file.toAbsolutePath().toString());
            Files.delete(file);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        super.postVisitDirectory(dir, exc);

        // If the deletion is attempted at the beginning of the unit test, it is possible that the directory where the
        // results will be stored does not exist.
        try {
            Files.delete(dir);
        } catch (NoSuchFileException e) {
            LOGGER.info("Directory {} does not exist. There is nothing to delete", dir.toAbsolutePath().toString());
            return FileVisitResult.SKIP_SUBTREE;
        }

        LOGGER.info("Deleting directory {}", dir.toAbsolutePath().toString());
        return FileVisitResult.CONTINUE;
    }
}