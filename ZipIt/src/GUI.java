import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class GUI implements ActionListener {
    private static final String title = "File Compressor";
    private JFileChooser fileChooser = new JFileChooser();
    private static final FileNameExtensionFilter lzwFilter = new FileNameExtensionFilter(".lzw files","lzw");
    private static final FileNameExtensionFilter hfFilter = new FileNameExtensionFilter(".hf files","hf");
    JFrame frame;
    private JButton huffmanCompButton, lzwCompButton, huffmanDecompButton, lzwDecompButton;
    public GUI(){
        frame = new JFrame();
        frame.setTitle(title);
        frame.setSize(500,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
    }

    public void renderGUI(){

        JLabel label = new JLabel();
        label.setText("Choose A Compression Algorithm");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.TOP);
        label.setBounds(140,50,200,20);

        huffmanCompButton = new JButton();
        huffmanCompButton.setText("Compress Using Huffman");
        huffmanCompButton.setBounds(20,100,200,30);
        huffmanCompButton.addActionListener(this);
        huffmanCompButton.setFocusable(false);
        huffmanCompButton.setBackground(Color.PINK);

        lzwCompButton = new JButton();
        lzwCompButton.setText("Compress Using LZW");
        lzwCompButton.setBounds(20,150,200,30);
        lzwCompButton.addActionListener(this);
        lzwCompButton.setFocusable(false);
        lzwCompButton.setBackground(Color.PINK);

        huffmanDecompButton = new JButton();
        huffmanDecompButton.setText("Decompress .hf File");
        huffmanDecompButton.setBounds(260,100,200,30);
        huffmanDecompButton.addActionListener(this);
        huffmanDecompButton.setFocusable(false);
        huffmanDecompButton.setBackground(Color.PINK);

        lzwDecompButton = new JButton();
        lzwDecompButton.setText("Decompress .lzw File");
        lzwDecompButton.setBounds(260,150,200,30);
        lzwDecompButton.addActionListener(this);
        lzwDecompButton.setFocusable(false);
        lzwDecompButton.setBackground(Color.PINK);

        frame.add(label);
        frame.add(huffmanCompButton);
        frame.add(lzwCompButton);
        frame.add(huffmanDecompButton);
        frame.add(lzwDecompButton);
        frame.setVisible(true);
    }
    public void onAlgorithmSelect(CompressAlgorithm algo){
        Compressor.setAlgorithm(algo);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == huffmanCompButton){
            onAlgorithmSelect(new HuffmanAlgorithm());
            onCompressButtonClick();
        }
        else if (e.getSource() == lzwCompButton) {
            onAlgorithmSelect(new LzwAlgorithm());
            onCompressButtonClick();
        }
        else if (e.getSource() == huffmanDecompButton) {
            onAlgorithmSelect(new HuffmanAlgorithm());
            onDecompressButtonClick(hfFilter);
        }
        else if(e.getSource() == lzwDecompButton){
            onAlgorithmSelect(new LzwAlgorithm());
            onDecompressButtonClick(lzwFilter);
        }
    }
    public void onCompressButtonClick(){
        File file = chooseFile(null);
        File newFile;
        if(file != null){
            try {
                newFile = Compressor.compressFile(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            frame.getContentPane().removeAll();
            frame.repaint();
            displayFileInfo(file,newFile);
        }

    }
    public void onDecompressButtonClick(FileNameExtensionFilter filter){
        File file = chooseFile(filter);
        File newFile;
        if(file != null){
            try {
                newFile = Compressor.decompressFile(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            frame.getContentPane().removeAll();
            frame.repaint();
            displayFileInfo(file, newFile);
        }
    }
    public File chooseFile(FileNameExtensionFilter filter){
        fileChooser.setFileFilter(filter);
        int i = fileChooser.showOpenDialog(null);
        if(i == JFileChooser.APPROVE_OPTION){
            return new File(fileChooser.getSelectedFile().getAbsolutePath());
        }
        return null;
    }
    public void displayFileInfo(File oldFile, File newFile){
        JLabel msg = new JLabel();
        msg.setText("Compression/Decompression Complete");
        msg.setHorizontalAlignment(JLabel.CENTER);
        msg.setVerticalAlignment(JLabel.TOP);
        msg.setBounds(120,50,250,20);

        JLabel old = new JLabel();
        old.setText("Selected file: " + oldFile.getName() + "\t " + oldFile.length() + " bytes");
        old.setHorizontalAlignment(JLabel.CENTER);
        old.setVerticalAlignment(JLabel.TOP);
        old.setBounds(0,100,500,20);

        JLabel newLabel = new JLabel();
        newLabel.setText("New file: " + newFile.getName() + "\t " + newFile.length() + " bytes");
        newLabel.setHorizontalAlignment(JLabel.CENTER);
        newLabel.setVerticalAlignment(JLabel.TOP);
        newLabel.setBounds(0,130,500,20);

        frame.add(msg);
        frame.add(old);
        frame.add(newLabel);
    }
}
