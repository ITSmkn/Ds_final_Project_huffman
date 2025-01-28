package ds_final_project_huffman;
 
import java.io.IOException;
import java.util.Map;

public class Ds_final_Project_huffman {

 
    public static void main(String[] args) { 
        
        try{
            String str = "D:\\File.txt";
            Map<Character, Integer> charFreq = HuffmanCompression.Calculate_Frequency(str);
            
            for(Map.Entry<Character , Integer> entry : charFreq.entrySet()){
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
          
        }
        
        catch(IOException e){
            System.err.print("Error!!!!");
        }
 
            
//        try{
//        String res = FileReader.readFile("D:\\File.txt");
//        System.out.println(res);
//        }
//        catch(IOException e){
//            System.err.print("error");
//        }
            
    }  
}
