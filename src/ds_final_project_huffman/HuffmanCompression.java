package ds_final_project_huffman;

import java.util.Scanner;
import java.util.HashMap;
import java.util.Map; 
import java.util.PriorityQueue; 
import java.io.*;

class Node {
    char character;
    int frequency;
    String Code;
    Node left , right;
    
    Node(char character, int frequency){
        this.character = character;
        this.frequency = frequency;
        Code = "";
        left = right = null;
    }
    
    @Override
    public String toString(){
        return "character :" + this.character + "  frequency :" + this.frequency + "\n";
    }
 
}

public class HuffmanCompression {
     
    // Character -> key -> character| Interger -> data -> frequency
    public static Map<Character , Integer> Calculate_Frequency(String fileName) throws IOException{
        Map<Character , Integer> charFreq = new HashMap<>();
        
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        
        while(scanner.hasNext()){
            String str = scanner.next();
            int i = 0;
            while(i != str.length()){
                char ch = str.charAt(i);
                
                // getOrDefault -> if the key was there it would return the data , otherwise it would return a dafault value!
                charFreq.put(ch, charFreq.getOrDefault(ch,0) + 1);
                
                i++;
            }       
        }
        
        scanner.close();
        
        return charFreq;
    }
    
// #########################################################################################################################    
    public static Node buildHuffmanTree(Map<Character, Integer> charFreq){
        // we want the Nodes with smaller Frequency to be extracted from the Priority queue sooner ...
        PriorityQueue<Node> minHeap = new PriorityQueue<>((n1,n2) -> n1.frequency - n2.frequency);
        
        for(Map.Entry<Character, Integer> entry : charFreq.entrySet()){
            minHeap.add(new Node(entry.getKey(), entry.getValue()));
        }
        
        while(minHeap.size() > 1){
            Node left = minHeap.poll();
            Node right = minHeap.poll();
            Node internalNode = new Node('\u0000', left.frequency + right.frequency);
            internalNode.left = left;
            internalNode.right = right;
            minHeap.add(internalNode);
        }
        
        return minHeap.poll();
    }

// #########################################################################################################################
    
    // this recursive function turns letters into binary code using given tree!
    public static void Encode(Node root , String binary , Map<Character , String> BinaryCodes){ 
        
        if(root.left == null && root.right == null){
            root.Code = binary;
            BinaryCodes.put(root.character , root.Code);
            return;
        }
        else{
            Encode(root.left , binary+"0" , BinaryCodes);
            Encode(root.right , binary+"1" , BinaryCodes);
        }
    }
    
// #########################################################################################################################   
  
    public static void WriteCodedFile(String InputPath , String OutputPath , Map<Character , String> BinaryCodes) throws IOException{
        
        try( BufferedReader reader = new BufferedReader(new FileReader(InputPath)) ; 
            BufferedWriter writer = new BufferedWriter(new FileWriter(OutputPath)) ){
            
            for(Map.Entry<Character,String> entry : BinaryCodes.entrySet()){       
                writer.write(entry.getKey() + " " + entry.getValue() + "\n");
            }
            // the following line marks the end of the code table!
            writer.write("end\n");
            
            int character;
            while((character = reader.read()) != -1){ 
                char c = (char) character;
                String code = BinaryCodes.get(c);
                if(code != null){
                    writer.write(code);
                }
                else{
                    writer.write(c);
                }

            }   
        }
    }  
    
// #########################################################################################################################    

    public static Map<String , Character> readCodeTable(String CodedFilePath)throws IOException{
        // in the following hashmap , 
        Map<String , Character> binaryCodes = new HashMap<>();
        
        File file = new File(CodedFilePath);
        Scanner scanner = new Scanner(file);
        
        while(scanner.hasNext()){
            String Char = scanner.next();
            if(Char.equals("end")){
                break;
            }
            String Code = scanner.next();
            binaryCodes.put(Code, Char.charAt(0));   
        }    
        return binaryCodes;
    }
    
// #########################################################################################################################
    
    public static void EncodeCodedFile(String CodedFilePath , String NewFilePath , Map<String , Character> binaryCodes)throws IOException{
        
        File file = new File(CodedFilePath);
        Scanner scanner = new Scanner(file);
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(NewFilePath));
        
        //for skipping the code table ...
        while(!scanner.next().equals("end")) scanner.next();
        
        while(scanner.hasNext()){
            String str = scanner.next();
            String charCode = "";
            
            for(int i = 0 ; i < str.length() ; i++){
                charCode += str.charAt(i);
                if(binaryCodes.get(charCode) != null){
                    writer.write(binaryCodes.get(charCode));
                    charCode = "";
                }
            }
            
            writer.write(" ");         
        }
        writer.close();
                  
    }
}
