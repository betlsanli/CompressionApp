import java.io.*;

public class FileManager {

    public static byte[] read(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] bytes = fis.readAllBytes();
        fis.close();
        return bytes;
    }
    public static File write(byte[] bytes,File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        fos.close();
        return file;
    }
}
