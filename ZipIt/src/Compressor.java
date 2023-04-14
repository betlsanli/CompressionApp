import java.io.File;
import java.io.IOException;

public class Compressor {
    private static CompressAlgorithm algorithm;

    public static void setAlgorithm(CompressAlgorithm algo){
        algorithm = algo;
    }
    public static File compressFile(File file) throws IOException {
        byte[] bytes = algorithm.compress(FileManager.read(file));
        String name = file.getAbsolutePath() + algorithm.getFileFormat();
        File newFile = new File(name);
        FileManager.write(bytes,newFile);
        return newFile;
    }
    public static File decompressFile(File file) throws IOException {
        byte[] bytes = algorithm.decompress(FileManager.read(file));
        String name = file.getAbsolutePath().replace(algorithm.getFileFormat(), "");
        File newFile = new File(name);
        FileManager.write(bytes,newFile);
        return newFile;
    }
}
