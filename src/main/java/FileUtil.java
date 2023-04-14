import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtil {

    public static boolean compareFiles(File a, File b) throws IOException, NoSuchAlgorithmException {
        return generateHash(a).equals(generateHash(b));
    }

    private static BigInteger generateHash(File file) throws IOException, NoSuchAlgorithmException {
        byte[] data = Files.readAllBytes(file.toPath());
        byte[] hash = MessageDigest.getInstance("MD5").digest(data);
        return new BigInteger(1, hash);
    }

}
