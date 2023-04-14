import java.util.HashMap;
import java.util.Stack;

public class HuffmanTree {
    private ByteNode root;

    public HuffmanTree(MinPQueue<ByteNode> minQ){
        while(minQ.size() > 1){
            ByteNode left = minQ.poll();
            ByteNode right = minQ.poll();
            ByteNode parent = new ByteNode(left,right);
            minQ.add(parent);
        }
        this.root = minQ.poll();
    }
    public HuffmanTree(ByteNode root){
        this.root = root;
    }
    public HashMap<Byte,String> getHuffmanTable(){
        HashMap<Byte, String> hfTable = new HashMap<>();
        getHuffmanTable(root, "", hfTable);
        return hfTable;
    }
    private void getHuffmanTable(ByteNode root, String code, HashMap<Byte,String> hfTable){
        if(root != null){
            if(root.isLeaf()){
                hfTable.put(root.getData(),code);
            }
            else{
                getHuffmanTable(root.getLeft(), code + "0", hfTable);
                getHuffmanTable(root.getRight(), code + "1", hfTable);
            }
        }
    }
    public HashMap<String,Byte> getReverseHuffmanTable(){
        HashMap<String, Byte> hfTable = new HashMap<>();
        getReverseHuffmanTable(root, "", hfTable);
        return hfTable;
    }
    private void getReverseHuffmanTable(ByteNode root, String code, HashMap<String,Byte> hfTable){
        if(root != null){
            if(root.isLeaf()){
                hfTable.put(code,root.getData());
            }
            else{
                getReverseHuffmanTable(root.getLeft(), code + "0", hfTable);
                getReverseHuffmanTable(root.getRight(), code + "1", hfTable);
            }
        }
    }
    public static HuffmanTree decodeTree(String code){
        Stack<ByteNode> s = new Stack<>();
        for(int i = 0; i < code.length(); i++){
            if(code.charAt(i) == '1'){
                byte b = (byte) Integer.parseInt(code.substring(i+1,i+9),2);
                s.push(new ByteNode(b,0));
                i += 8;
            }
            else{
                ByteNode right = s.pop();
                if(s.isEmpty()){
                    return new HuffmanTree(right);
                }
                ByteNode left = s.pop();
                ByteNode parent = new ByteNode(left,right);
                s.push(parent);
            }
        }
        return new HuffmanTree(s.pop());
    }
    public String postOrder(){
        return postOrder(root);
    }
    private String postOrder(ByteNode root){
        if(root == null) return "";
        String str = postOrder(root.getLeft()) + postOrder(root.getRight());
        if(root.isLeaf()) str += "1" + root;
        else str += "0";
        return str;
    }
}
