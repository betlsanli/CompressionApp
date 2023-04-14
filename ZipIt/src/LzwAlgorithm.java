import java.util.HashMap;

public class LzwAlgorithm implements CompressAlgorithm{

    private static final int max = 32767;
    @Override
    public byte[] compress(byte[] bytes) {
        HashMap<String, Integer> dictionary  = initializeDictionary();
        String compStr = compressToString(bytes,dictionary);
        return toByteArr(compStr);
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
    private String compressToString(byte[] bytes, HashMap<String,Integer> dictionary){
        StringBuilder output = new StringBuilder();
        int a = 256;
        StringBuilder currStr = new StringBuilder();
        String bitStr;
        for(int i = 0; i < bytes.length;i++){
            bitStr = String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF)).replace(' ', '0');
            if(dictionary.containsKey(currStr.toString() + bitStr)){
                currStr.append(bitStr);
            }
            else{

                String code = Integer.toBinaryString(dictionary.get(currStr.toString()));
                code = addPadding(code);
                output.append(code);
                if(a < max){
                    dictionary.put(currStr.toString() + bitStr,a);
                    a++;
                }
                currStr = new StringBuilder(bitStr);
            }
        }
        String code = Integer.toBinaryString(dictionary.get(currStr.toString()));;
        code = addPadding(code);
        output.append(code);
        return output.toString();
    }
    private String addPadding(String str){
        if(str.length() == 8){
            str = "0".repeat(8) + str;
        }
        else if(str.length() < 8){
            str = "0".repeat( (8 + 8 - (str.length()%8)) ) + str;
        }
        else if( str.length() % 8 != 0){
            str = "0".repeat( (8 - (str.length()%8)) ) + str;
        }
        return str;
    }
    private HashMap<String,Integer> initializeDictionary(){
        HashMap<String,Integer> dictionary = new HashMap<>(256);
        for(int i = 0; i < 256; i++){
            String str = Integer.toBinaryString(i);
            if(str.length()% 8 != 0){
                str = "0".repeat(8 - (str.length()%8)) + str;
            }
            dictionary.put(str,i);
        }
        return dictionary;
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        HashMap<Integer,String> dictionary = initializeDecodeDictionary();
        String bitStr = decompToString(bytes,dictionary);
        byte[] decompBytes = toByteArr(bitStr);
        return decompBytes;
    }

    private HashMap<Integer,String> initializeDecodeDictionary(){
        HashMap<Integer,String> dictionary = new HashMap<>(256);
        for(int i = 0; i < 256; i++){
            String str = Integer.toBinaryString(i);
            if(str.length()% 8 != 0){
                str = "0".repeat(8 - (str.length()%8)) + str;
            }
            dictionary.put(i,str);
        }
        return dictionary;
    }
    private String decompToString(byte[] bytes, HashMap<Integer,String> dictionary){
        int[] codes = toIntArr(bytes);
        int a = 256;
        String bitStr = dictionary.get(codes[0]);
        StringBuilder decompStr = new StringBuilder(bitStr);

        for(int i = 1; i < codes.length; i++){
            String entry;
            if(dictionary.containsKey(codes[i])){
                entry = dictionary.get(codes[i]);
            }
            else{
                entry = bitStr + bitStr.substring(0,8);
            }
            decompStr.append(entry);
            if(a < max){
                dictionary.put(a,bitStr + entry.substring(0,8));
                a++;
            }
            bitStr = entry;
        }
        return decompStr.toString();
    }
    private int[] toIntArr(byte[] bytes){
        int[] arr = new int[bytes.length/2];
        int i = 0 ,j = 0;
        while(i < arr.length){
            arr[i] = ( ((int) (bytes[j]) << 8) & 0xFF00 ) | ((int) (bytes[j+1]) & 0xFF);
            i++;
            j += 2;
        }
        return arr;
    }


    @Override
    public String getFileFormat() {
        return ".lzw";
    }
}
