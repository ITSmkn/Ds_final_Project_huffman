package ds_final_project_huffman;
 
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Ds_final_Project_huffman {

 
    public static void main(String[] args) { 
                
//        int b1 = 0b1011101;
//        String s = Integer.toBinaryString(b1);
        
        try{
            String str = "D:\\File.txt";
            Map<Character, Integer> charFreq = HuffmanCompression.Calculate_Frequency(str);
            
            Node node = HuffmanCompression.buildHuffmanTree(charFreq);
            
            System.out.print(node);
            
            for(Map.Entry<Character , Integer> entry : charFreq.entrySet()){
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
            
            // the following hashMap comtains allotted Binary Codes to each character.
            Map<Character , String> BinaryCodes = new HashMap<>();
            HuffmanCompression.Encode(node, "", BinaryCodes);
            
            for(Map.Entry<Character , String> entry : BinaryCodes.entrySet()){
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
            
            HuffmanCompression.WriteCodedFile("D:\\File.txt", "D:\\compressed.txt", BinaryCodes);
            
            Map<String , Character> bn = HuffmanCompression.readCodeTable("D:\\compressed.txt");
            for(Map.Entry<String , Character> entry : bn.entrySet()){
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
          
        }
        
        catch(IOException e){
            System.err.print("There was a problem while running!!!!");
        }
        
    }   
}
