import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

public class CleanupFileVisitor implements FileVisitor<Path> {

    private Path sourceDirectory;
    private Path targetDirectory;

    CleanupFileVisitor(Path sourceDirectory, Path targetDirectory) {
        this.sourceDirectory = sourceDirectory;
        this.targetDirectory = targetDirectory;
    }


    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {

        // get the relative file name from path "expected"
        Path relativize = expected.toPath().relativize(dir);
        // construct the path for the counterpart file in "generated"
        File otherDir = generated.toPath().resolve(relativize).toFile();
        log.debug("=== preVisitDirectory === compare " + dir + " to " + otherDir);
        assertEquals("Folders doesn't contain same file!?!?",
                Arrays.toString(dir.toFile().list()),
                Arrays.toString(otherDir.list()));
        return result;
        return FileVisitResult.CONTINUE;
    }
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        Path relativize = sourceDirectory.relativize(file);
        Path fileInOther = targetDirectory.resolve(relativize);

        if (file != fileInOther) {
            fileInOther.toFile().delete();
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        return FileVisitResult.CONTINUE;
    }
}
