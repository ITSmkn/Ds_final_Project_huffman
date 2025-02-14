package ds_final_project_huffman;

import java.util.HashMap;
import java.util.Map; 
import java.util.PriorityQueue; 
import java.io.*;

class Node {
    char character;
    int frequency; 
    Node left, right;
    
    Node(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
        left = right = null;
    }
    
    @Override
    public String toString() {
        return "character: " + this.character + "  frequency: " + this.frequency;
    }
}

public class HuffmanCompression {
    
    
    public static Map<Character, Integer> Calculate_Frequency(String fileName) throws IOException {
        Map<Character, Integer> charFreq = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        int ch;
        while ((ch = reader.read()) != -1) {
            char c = (char) ch;
            charFreq.put(c, charFreq.getOrDefault(c, 0) + 1);
        }
        reader.close();
        return charFreq;
    }
    
// ##########################################################################################################################
    
     
    public static Node buildHuffmanTree(Map<Character, Integer> charFreq) {
        // in case that (n1.frequency - n2.frequency) was negative , n1 will have a higher priority .
        PriorityQueue<Node> minHeap = new PriorityQueue<>((n1, n2) -> n1.frequency - n2.frequency);
        for (Map.Entry<Character, Integer> entry : charFreq.entrySet()) {
            minHeap.add(new Node(entry.getKey(), entry.getValue()));
        }
        while (minHeap.size() > 1) {
            Node left = minHeap.poll();
            Node right = minHeap.poll();
            Node internal = new Node('\0', left.frequency + right.frequency);
            internal.left = left;
            internal.right = right;
            minHeap.add(internal);
        }
        return minHeap.poll();
    }
    
// ##########################################################################################################################   
     
    public static void EncodeCharacters(Node root, String binary, Map<Character, String> BinaryCodes) {
        if (root.left == null && root.right == null) {
            BinaryCodes.put(root.character, binary);
            return;
        }
        if (root.left != null)
            EncodeCharacters(root.left, binary + "0", BinaryCodes);
        if (root.right != null)
            EncodeCharacters(root.right, binary + "1", BinaryCodes);
    }
    
// ########################################################################################################################## 
    
    // turning the tree into binary String ...
    private static String serializeTree(Node root) {
        // in case we reach a leaf ...
        if (root.left == null && root.right == null){     
            // Unicode char in 16-bits in java so ...
            return "1" + String.format("%16s", Integer.toBinaryString(root.character)).replace(' ', '0');
        } 
        else {          
            return "0" + serializeTree(root.left) + serializeTree(root.right);
        }
    }
 
// ##########################################################################################################################
    
    // < index = current location >
    private static Node deserializeTree(String bitString, int[] index) {
        //end of process
        if (index[0] >= bitString.length()) return null;
        
        char flag = bitString.charAt(index[0]);
        index[0]++;
        
        // a sign of reaching a leaf Node -> (flag = 1) ...
        // determining the character ...
        if (flag == '1') {
            String charBits = bitString.substring(index[0], index[0] + 16);
            index[0] += 16;
            int charCode = Integer.parseInt(charBits, 2);
            return new Node((char) charCode, 0);
        } 
        
        // in case we are not dealing with a leaf ...
        else { // flag == '0'
            Node left = deserializeTree(bitString, index);
            Node right = deserializeTree(bitString, index);
            Node node = new Node('\0', 0);
            node.left = left;
            node.right = right;
            return node;
        }
    }
    
// ##########################################################################################################################

    // it turns a bit string to byte array (this will be used when compressing a file) ...
    private static byte[] bitStringToByteArray(String bitString) {
        int len = bitString.length();
        final int byteCount = (len + 7) / 8;
        byte[] result = new byte[byteCount];
        for (int i = 0; i < byteCount; i++) {
            int start = i * 8;
            int end = Math.min(start + 8, len);
            String byteStr = bitString.substring(start, end);
            while (byteStr.length() < 8) {
                byteStr += "0";
            }
            int b = Integer.parseInt(byteStr, 2);
            result[i] = (byte) b;
        }
        return result;
    }                                                 
    
// ##########################################################################################################################    
     
    // this will be used in decode function ...
    private static String byteArrayToBitString(byte[] bytes, int bitLength) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            int b = bytes[i] & 0xFF;
            String s = String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0');
            sb.append(s);
        }
        return sb.substring(0, bitLength);
    }
// ########################################################################################################################## 
    
    // it turns a bit string to byte array (7-bits this time)
    private static byte[] pack7Bit(String input) {
        int len = input.length();
        int totalBits = len * 7;
        int byteCount = (totalBits + 7) / 8;
        byte[] result = new byte[byteCount];
        int bitPos = 0;
        for (int i = 0; i < len; i++) {
            int value = input.charAt(i) & 0x7F; 
            for (int j = 6; j >= 0; j--) {
                int bit = (value >> j) & 1;
                int byteIndex = bitPos / 8;
                int bitIndex = 7 - (bitPos % 8);
                result[byteIndex] |= bit << bitIndex;
                bitPos++;
            }
        }
        return result;
    }
    
// ########################################################################################################################## 
    
    public static void writeCompressedFile(String inputPath, String outputPath, 
                                           Map<Character, String> BinaryCodes,
                                           Node root) throws IOException {
         
        StringBuilder fileContent = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(inputPath));
        int ch;
        while ((ch = reader.read()) != -1) {
            fileContent.append((char) ch);
        }
        reader.close();
        
        String originalContent = fileContent.toString();
        int originalLength = originalContent.length();
        
        // creating encoded string using the related hash map ...
        StringBuilder encodedStr = new StringBuilder();
        for (int i = 0; i < originalContent.length(); i++){
            char c = originalContent.charAt(i);
            encodedStr.append(BinaryCodes.get(c));
        }
        
        // serialazing the tree for compressed file ...
        String treeBitString = serializeTree(root);
        
        // turn the byte code table and the file's content to byte array ...
        byte[] treeBytes = bitStringToByteArray(treeBitString);
        byte[] encodedBytes = bitStringToByteArray(encodedStr.toString());
        
        int huffmanCompressedSize = 17 + treeBytes.length + encodedBytes.length;
        
        // if all the chars are 7 bits ...
        boolean all7Bit = true;
        for (int i = 0; i < originalContent.length(); i++) {
            if (originalContent.charAt(i) >= 128) {
                all7Bit = false;
                break;
            }
        }
        
        int sevenBitCompressedSize = Integer.MAX_VALUE;
        byte[] sevenBitBytes = null;
        if (all7Bit) {
            sevenBitBytes = pack7Bit(originalContent);
            sevenBitCompressedSize = 1 + 4 + 4 + sevenBitBytes.length;
        }
        
        File inputFile = new File(inputPath);
        int originalSize = (int) inputFile.length();
        
        // choosing the best way to compress ...
        int bestMethod = 0; 
        int bestSize = originalSize;  
        if (huffmanCompressedSize < bestSize) {
            bestMethod = 1;
            bestSize = huffmanCompressedSize;
        }
        if (sevenBitCompressedSize < bestSize) {
            bestMethod = 2;
            bestSize = sevenBitCompressedSize;
        }
        
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputPath));
        if (bestMethod == 0) {
            // simple compressing ...
            dos.writeByte(0); // flag = 0
            dos.write(originalContent.getBytes());
        } 
        else if (bestMethod == 1) {
             
            dos.writeByte(1); // flag = 1
            dos.writeInt(treeBitString.length());
            dos.writeInt(treeBytes.length);
            dos.write(treeBytes);
            dos.writeInt(encodedStr.length());
            dos.writeInt(encodedBytes.length);
            dos.write(encodedBytes);
        } 
        
        else if (bestMethod == 2) {
             
            dos.writeByte(2); // flag = 2
            dos.writeInt(originalLength);  
            dos.writeInt(sevenBitBytes.length);  
            dos.write(sevenBitBytes);
        }
        dos.close();
    }
    
// ########################################################################################################################## 
    
    public static void decodeCompressedFile(String inputPath, String outputPath) throws IOException {
        DataInputStream dis = new DataInputStream(new FileInputStream(inputPath));
        int flag = dis.readByte();
        
        if (flag == 0) {
            
            FileOutputStream fos = new FileOutputStream(outputPath);
            byte[] buffer = new byte[4096];
            int len;
            while ((len = dis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            dis.close();
            return;
        } 
        
        else if (flag == 2) {
             
            int numChars = dis.readInt();
            int packedLength = dis.readInt();
            byte[] packedBytes = new byte[packedLength];
            dis.readFully(packedBytes);
            dis.close();
            StringBuilder sb = new StringBuilder();
            int bitPos = 0;
            for (int i = 0; i < numChars; i++) {
                int value = 0;
                for (int j = 0; j < 7; j++) {
                    int byteIndex = bitPos / 8;
                    int bitIndex = 7 - (bitPos % 8);
                    int bit = (packedBytes[byteIndex] >> bitIndex) & 1;
                    value = (value << 1) | bit;
                    bitPos++;
                }
                sb.append((char) value);
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));
            writer.write(sb.toString());
            writer.close();
            return;
        }
         
        int treeBitLength = dis.readInt();
        int treeBytesLength = dis.readInt();
        byte[] treeBytes = new byte[treeBytesLength];
        dis.readFully(treeBytes);
        String treeBitString = byteArrayToBitString(treeBytes, treeBitLength);
        int[] index = new int[]{0};
        Node root = deserializeTree(treeBitString, index);
        
        int encodedBitLength = dis.readInt();
        int encodedBytesLength = dis.readInt();
        byte[] encodedBytes = new byte[encodedBytesLength];
        dis.readFully(encodedBytes);
        String encodedBitString = byteArrayToBitString(encodedBytes, encodedBitLength);
        dis.close();
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));
        Node current = root;
        for (int i = 0; i < encodedBitString.length(); i++) {
            char bit = encodedBitString.charAt(i);
            if (bit == '0') {
                current = current.left;
            } else {
                current = current.right;
            }
            if (current.left == null && current.right == null) {
                writer.write(current.character);
                current = root;
            }
        }
        writer.close();
    }
    

}
