public class ByteNode implements Comparable<ByteNode>{
    private Byte data;
    private int freq;
    private ByteNode left;
    private ByteNode right;

    public ByteNode(byte data, int freq){
        this.data = data;
        this.freq = freq;
        left = null;
        right = null;
    }
    public ByteNode(ByteNode left, ByteNode right){
        data = null;
        freq = left.freq + right.freq;
        this.left = left;
        this.right = right;
    }

    public Byte getData() {
        return data;
    }

    public ByteNode getLeft() {
        return left;
    }

    public ByteNode getRight() {
        return right;
    }

    public boolean isLeaf(){
        return left == null && right == null;
//        return data != null;
    }

    @Override
    public int compareTo(ByteNode o) {
        return freq - o.freq;
    }
    public String toString(){
        return String.format("%8s", Integer.toBinaryString(data & 0xFF)).replace(' ', '0');
    }

}
