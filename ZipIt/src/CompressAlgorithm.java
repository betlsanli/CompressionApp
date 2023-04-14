public interface CompressAlgorithm {

    public byte[] compress(byte[] bytes);
    public byte[] decompress(byte[] bytes);
    public String getFileFormat();
}
