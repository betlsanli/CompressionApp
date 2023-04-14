import java.util.HashMap; 

public class HuffmanAlgorithm implements CompressAlgorithm{

    @Override
    public byte[] compress(byte[] bytes) {
        MinPQueue<ByteNode> minQ = getMinPQueue(bytes);
        HuffmanTree hfTree = new HuffmanTree(minQ);
        HashMap<Byte, String> table = hfTree.getHuffmanTable();
        String bitStr = compressToString(bytes,table);
        byte[] compBytes = toByteArr(bitStr);
        byte[] code = encodeTree(hfTree);
        int[] sizes = {bytes.length, compBytes.length,code.length}; // size of original file -- size of compressed file -- size of huffman code
        return addHeader(compBytes,sizes,code);
    }
    private byte[] addHeader(byte[] bytes, int [] sizes, byte[] code){ // adds header info to the given bytes. returns a new byte array
        byte[] newBytes = new byte[bytes.length + sizes.length*4 + code.length];

        int i = 0;
        int j = 0;
        while(i < sizes.length){
            int a = sizes[i];
            newBytes[j++] = (byte)((a & 0xFF000000) >> 24);
            newBytes[j++] = (byte)((a & 0xFF0000) >> 16);
            newBytes[j++] = (byte)((a & 0xFF00) >> 8);
            newBytes[j++] = (byte)((a & 0xFF));
            i++;
        }
        i = 0;
        while(i < code.length){
            newBytes[j] = code[i];
            i++;
            j++;
        }
        i = 0;
        while(i < bytes.length){
            newBytes[j] = bytes[i];
            i++;
            j++;
        }
        return newBytes;
    }
    private byte[] encodeTree(HuffmanTree hfTree){
        String codeStr = hfTree.postOrder();
        codeStr = addPadding(codeStr);
        return toByteArr(codeStr);
    }
    private byte[] toByteArr(String str){
        byte[] bytes = new byte[str.length()/8];
        int i = 0;
        int j = 0;
        while(i < str.length()){
            bytes[j] = (byte) Integer.parseInt(str.substring(i,i+8),2);
            i += 8;
            j++;
        }
        return bytes;
    }
    private String addPadding(String str){
        if(str.length() % 8 == 0) return str;
        return str + "0".repeat(8 - str.length() % 8);
    }
    private String compressToString(byte[] bytes, HashMap<Byte,String> hfTable){
        StringBuilder compStr = new StringBuilder();
        for(int i = 0; i < bytes.length; i++){
            compStr.append(hfTable.get(bytes[i]));
        }
        return addPadding(compStr.toString());
    }
    private MinPQueue<ByteNode> getMinPQueue(byte[] bytes){
        HashMap<Byte,Integer> freqTable = getFreqTable(bytes);
        MinPQueue<ByteNode> minQ = new MinPQueue<>(freqTable.size());
        freqTable.forEach(
                (key,value) -> minQ.add(new ByteNode(key,value))
        );
        return minQ;
    }
    private HashMap<Byte, Integer> getFreqTable(byte[] bytes){
        HashMap<Byte,Integer> freqTable = new HashMap<>();
        for(int i = 0; i< bytes.length; i++){
            if(freqTable.containsKey(bytes[i])){
                int value = freqTable.get(bytes[i]);
                freqTable.put(bytes[i],value + 1);
            }
            else
                freqTable.put(bytes[i], 1);
        }
        return freqTable;
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        int[] sizes = getSizes(bytes);
        byte[] code = getCode(bytes,sizes[2]);//sizes[2] length of the huffman code
        bytes = getCompBytes(bytes, sizes[1]);// sizes[1] length of the compressed bytes
        HuffmanTree hfTree = HuffmanTree.decodeTree(toString(code));
        HashMap<String,Byte> table = hfTree.getReverseHuffmanTable();
        String bitStr = toString(bytes);

        byte[] decompBytes = new byte[sizes[0]]; // sizes[0] length of the original bytes
        int j = 0;
        for(int i = 0; i < decompBytes.length; i++){
            StringBuilder key = new StringBuilder();
            while(j < bitStr.length()){
                key.append(bitStr.charAt(j));
                if(table.containsKey(key.toString())){
                    decompBytes[i] = table.get(key.toString());
                    j++;
                    break;
                }
                j++;
            }
        }

        return decompBytes;
    }
    private String toString(byte[] bytes){
        StringBuilder str = new StringBuilder(bytes.length);
        for(int i = 0; i < bytes.length; i++){
            str.append(String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF)).replace(' ', '0'));
        }
        return str.toString();
    }
    private byte[] getCompBytes(byte[] bytes, int size){
        byte[] compBytes = new byte[size];
        int i = size -1;
        int j = bytes.length -1;
        while(i >= 0){
            compBytes[i] = bytes[j];
            i--;
            j--;
        }
        return compBytes;
    }
    private byte[] getCode(byte[] bytes, int size){
        byte[] code = new byte[size];
        int i = 0;
        int j = 12;
        while(i < size){
            code[i] = bytes[j];
            i++;
            j++;
        }
        return code;
    }
    private int[] getSizes(byte[] bytes) {
        int[] arr = new int[3];
        for (int i = 0; i < arr.length; i++){
            arr[i] = (((int) (bytes[i * 4]) << 24) & 0xFF000000) |
                    (((int) (bytes[i * 4 + 1]) << 16) & 0xFF0000) |
                    (((int) (bytes[i * 4 + 2]) << 8) & 0xFF00) |
                    ((int) (bytes[i * 4 + 3]) & 0xFF);
        }
        return arr;
    }

    @Override
    public String getFileFormat() {
        return ".hf";
    }

}
