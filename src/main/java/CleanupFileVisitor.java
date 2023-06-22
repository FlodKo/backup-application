import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class CleanupFileVisitor implements FileVisitor<Path> {

    private Path sourceDirectory;
    private Path targetDirectory;

    CleanupFileVisitor(Path sourceDirectory, Path targetDirectory) {
        this.sourceDirectory = sourceDirectory;
        this.targetDirectory = targetDirectory;
    }


    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
/*
        // get the relative file name from path "expected"
        Path relativize = sourceDirectory.relativize(dir);
        // construct the path for the counterpart file in "generated"
        File otherDir = targetDirectory.resolve(relativize).toFile();
        if (!(dir.toFile() == otherDir)){
            otherDir.delete();
        };
        */
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

        Path relativize = sourceDirectory.relativize(file);
        Path fileInOther = targetDirectory.resolve(relativize);

        if (fileInOther.compareTo(file) != 0) {
            fileInOther.toFile().delete();
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return null;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        return FileVisitResult.CONTINUE;
    }
}
