package ds_final_project_huffman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map; 

public class UserInterface {
    public UserInterface(){
        
        JFrame [] jFrames = new JFrame[3]; 
        
        JFrame MainFrame = new JFrame("Main Frame"); 
        jFrames[0] = MainFrame;
        
        JFrame EncodeFrame = new JFrame("Encode");
        jFrames[1] = EncodeFrame;
        
        JFrame DecodeFrame = new JFrame("Encode");
        jFrames[2] = DecodeFrame;
        
        
        
        
        JButton EncodeButton = new JButton("Encode selected file");
        EncodeButton.setBounds(275,170,250,45);
        EncodeButton.setBackground(new Color(128, 0, 128));
        EncodeButton.setForeground(Color.WHITE);
        
        
        JButton DecodeButton = new JButton("Decode selected file");  
        DecodeButton.setBounds(275,230,250,45);
        DecodeButton.setBackground(new Color(128, 0, 128));
        DecodeButton.setForeground(Color.WHITE);
        

        JButton openFileToEncodeItButton = new JButton("Open File"); 
        openFileToEncodeItButton.setBounds(280,200,250,45);
        openFileToEncodeItButton.setBackground(new Color(128, 0, 128));
        openFileToEncodeItButton.setForeground(Color.WHITE);
        
        JButton openFileToDecodeItButton = new JButton("Open File"); 
        openFileToDecodeItButton.setBounds(280,200,250,45);
        openFileToDecodeItButton.setBackground(new Color(128, 0, 128));
        openFileToDecodeItButton.setForeground(Color.WHITE);
        

        
        
        JLabel L1 = new JLabel("Please choose the file you want to Encode");         
        L1.setBounds(270,145,440,30);
        L1.setForeground(Color.WHITE);
        L1.setFont(new Font("Serif", Font.BOLD, 15));
        
        JLabel L2 = new JLabel("Please choose the file you want to Decode");         
        L2.setBounds(270,145,440,30);
        L2.setForeground(Color.WHITE);
        L2.setFont(new Font("Serif", Font.BOLD, 15));
   
        
        for(int i = 0 ; i < jFrames.length ; i++){
            JLabel BackGroundPic = new JLabel(new ImageIcon("MainPic.jpg"));
            BackGroundPic.setBounds(0, 0,850,478); 
            
            if(i == 0){  //MainFrame
                jFrames[i].setVisible(true);
                jFrames[i].add(EncodeButton);
                jFrames[i].add(DecodeButton);
            }
            
            if(i == 1){  //EncodeFrame
                jFrames[i].setVisible(false);
                jFrames[i].add(L1);
                jFrames[i].add(openFileToEncodeItButton); 
                
            }
            
            if(i == 2){  //DecodeFrame
                jFrames[i].setVisible(false);
                jFrames[i].add(L2);
                jFrames[i].add(openFileToDecodeItButton); 
            }
            
                jFrames[i].setSize(850,478);
                jFrames[i].setResizable(false);
                jFrames[i].getContentPane().setBackground(Color.BLACK);
                jFrames[i].setLayout(null);          
                jFrames[i].setLocationRelativeTo(null);  
                jFrames[i].add(BackGroundPic);          
        }
        
        
        
        
        JButton OK = new JButton("OK"); 
        OK.setBounds(140,140,150,40);
        OK.setBackground(new Color(28, 0, 58));
        OK.setForeground(Color.WHITE);
        
        JLabel L3 = new JLabel("- The Action was performed Successfully -");         
        L3.setBounds(80,75,300,30);
        L3.setForeground(Color.GREEN);
        L3.setFont(new Font("Serif", Font.BOLD, 15));
        
        JLabel OKBackGroundPic = new JLabel(new ImageIcon("ActionResultPic.jpg"));
        OKBackGroundPic.setBounds(0, 0,450,253); 
        
        JFrame ActionResult = new JFrame();
        ActionResult.setSize(450,253);
        ActionResult.setResizable(false);
        ActionResult.getContentPane().setBackground(Color.BLACK);
        ActionResult.setLayout(null);          
        ActionResult.setLocationRelativeTo(null);
        ActionResult.setVisible(false);
        ActionResult.add(L3);
        ActionResult.add(OK);
        ActionResult.add(OKBackGroundPic);

                  
        
// ########################## BUTTONs' actions ############################################################### 

    EncodeButton.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
            MainFrame.setVisible(false);
            EncodeFrame.setVisible(true);
 
        }
        });
// -----------------------------------------------------------------------------------------------------------   

    DecodeButton.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
            MainFrame.setVisible(false);
            DecodeFrame.setVisible(true);

        }
        });
    
// -----------------------------------------------------------------------------------------------------------   

    OK.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
            System.exit(0);
        }
        });    
    
// -----------------------------------------------------------------------------------------------------------  

    openFileToEncodeItButton.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
            
            try{
                
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if(returnValue == JFileChooser.APPROVE_OPTION){
                    File selectedFile = fileChooser.getSelectedFile();     
                    String FilePath = selectedFile.getAbsolutePath(); 

                    String NewFilePath = "";
                    char temp = FilePath.charAt(0);
                    int j = 1;
                    while(temp != '.'){
                        NewFilePath += Character.toString(temp);
                        temp = FilePath.charAt(j);
                        j++;
                    }
                    NewFilePath += "_Compressed.txt";


                    System.out.println(NewFilePath);
                    Map<Character , Integer> charFreq = HuffmanCompression.Calculate_Frequency(FilePath);
                    Node node = HuffmanCompression.buildHuffmanTree(charFreq);

                    Map<Character , String> BinaryCodes = new HashMap<>();
                    HuffmanCompression.EncodeCharacters(node, "", BinaryCodes);

                    HuffmanCompression.WriteCodedFile(FilePath, NewFilePath, BinaryCodes);

                    System.out.println("Encoding was safe and sound!");
                    
                    EncodeFrame.setVisible(false);
                    ActionResult.setVisible(true);

            }
            }
            
            catch(IOException P){
                System.err.print(P.getMessage());
            }
        }
        });
    
    
    
// -----------------------------------------------------------------------------------------------------------   
    
        openFileToDecodeItButton.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
            
            try{
                
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if(returnValue == JFileChooser.APPROVE_OPTION){
                    File selectedFile = fileChooser.getSelectedFile();     
                    String FilePath = selectedFile.getAbsolutePath(); 

                    String NewFilePath = "";
                    char temp = FilePath.charAt(0);
                    int j = 1;
                    while(temp != '.'){
                        NewFilePath += Character.toString(temp);
                        temp = FilePath.charAt(j);
                        j++;
                    }
                    NewFilePath += "_DeCompressed.txt";
                    
                    Map<String , Character> bn = HuffmanCompression.readCodeTable(FilePath);
                    HuffmanCompression.DecodeCodedFile(FilePath, NewFilePath, bn);
                    
                    System.out.println("Decoding was safe and sound!");
                    
                    DecodeFrame.setVisible(false);
                    ActionResult.setVisible(true);

            }
            }
            
            catch(IOException P){
                System.err.print(P.getMessage());
            }
        }
        });

    }
}
