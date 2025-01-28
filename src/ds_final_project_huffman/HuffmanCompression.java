package ds_final_project_huffman;

import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.util.PriorityQueue;

class Node {
    char character;
    int frequency;
    Node left , right;
    
    Node(char character, int frequency){
        this.character = character;
        this.frequency = frequency;
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
    
}
