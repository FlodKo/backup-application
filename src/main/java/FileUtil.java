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

    public static BigInteger generateHash(File file) {
        byte[] hash = null;
        try {
            byte[] data = Files.readAllBytes(file.toPath());
            hash = MessageDigest.getInstance("MD5").digest(data);

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Algorithm problems.");
        } catch (IOException e) {
            System.out.println("sdfkjsödlkfölsdkfö");
            System.err.println("Hash generation not possible");
            return null;

        }
        return new BigInteger(1, hash);
    }

}
