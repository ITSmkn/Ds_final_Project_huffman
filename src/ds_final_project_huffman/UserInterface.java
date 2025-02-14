package ds_final_project_huffman;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserInterface {

    // پنل سفارشی با پس‌زمینه گرادیان بنفش
    static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int w = getWidth();
            int h = getHeight();
            Color color1 = new Color(28, 0, 58);
            Color color2 = new Color(128, 0, 128);
            GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }

    // حاشیه گرد برای دکمه‌ها
    static class RoundedBorder implements Border {
        private int radius;
        public RoundedBorder(int radius) {
            this.radius = radius;
        }
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius+1, radius+1, radius+2, radius);
        }
        @Override
        public boolean isBorderOpaque() {
            return true;
        }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(new Color(128, 0, 128));
            g.drawRoundRect(x, y, width-1, height-1, radius, radius);
        }
    }

    public UserInterface() {
        // استفاده از Nimbus Look & Feel در صورت موجود بودن
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
                if ("Nimbus".equals(info.getName())){
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch(Exception e) {
            // در صورت بروز مشکل، از ظاهر پیش‌فرض استفاده می‌شود.
        }
        
        // تعریف فریم‌ها و پنل‌های دارای پس‌زمینه گرادیان و لایه‌بندی با GridBagLayout
        JFrame mainFrame = new JFrame("Main Frame");
        JFrame encodeFrame = new JFrame("Encode");
        JFrame decodeFrame = new JFrame("Decode");
        JFrame actionResultFrame = new JFrame("Result");
        
        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new GridBagLayout());
        GradientPanel encodePanel = new GradientPanel();
        encodePanel.setLayout(new GridBagLayout());
        GradientPanel decodePanel = new GradientPanel();
        decodePanel.setLayout(new GridBagLayout());
        GradientPanel resultPanel = new GradientPanel();
        resultPanel.setLayout(new GridBagLayout());
        
        mainFrame.setContentPane(mainPanel);
        encodeFrame.setContentPane(encodePanel);
        decodeFrame.setContentPane(decodePanel);
        actionResultFrame.setContentPane(resultPanel);
        
        // تنظیمات عمومی فریم‌ها
        JFrame[] frames = {mainFrame, encodeFrame, decodeFrame};
        for (JFrame frame : frames) {
            frame.setSize(850, 478);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        actionResultFrame.setSize(450, 253);
        actionResultFrame.setResizable(false);
        actionResultFrame.setLocationRelativeTo(null);
        actionResultFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // تعریف فونت‌های سفارشی
        Font labelFont = new Font("Segoe UI", Font.BOLD, 18);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 16);
        
        // ------------------------ اجزای MainFrame ------------------------
        JButton encodeButton = new JButton("Encode selected file");
        encodeButton.setBackground(new Color(28, 0, 58));
        encodeButton.setForeground(Color.WHITE);
        encodeButton.setFont(buttonFont);
        encodeButton.setBorder(new RoundedBorder(10));
        encodeButton.setFocusPainted(false);
        encodeButton.setToolTipText("Select a file to encode");

        JButton decodeButton = new JButton("Decode selected file");
        decodeButton.setBackground(new Color(28, 0, 58));
        decodeButton.setForeground(Color.WHITE);
        decodeButton.setFont(buttonFont);
        decodeButton.setBorder(new RoundedBorder(10));
        decodeButton.setFocusPainted(false);
        decodeButton.setToolTipText("Select a file to decode");
        
        // ------------------------ اجزای EncodeFrame ------------------------
        JLabel encodeLabel = new JLabel("Please choose the file you want to Encode");
        encodeLabel.setForeground(Color.WHITE);
        encodeLabel.setFont(labelFont);
        encodeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JButton openFileToEncodeButton = new JButton("Open File");
        openFileToEncodeButton.setBackground(new Color(28, 0, 58));
        openFileToEncodeButton.setForeground(Color.WHITE);
        openFileToEncodeButton.setFont(buttonFont);
        openFileToEncodeButton.setBorder(new RoundedBorder(10));
        openFileToEncodeButton.setFocusPainted(false);
        openFileToEncodeButton.setToolTipText("Open a file to encode");
        
        // ------------------------ اجزای DecodeFrame ------------------------
        JLabel decodeLabel = new JLabel("Please choose the file you want to Decode");
        decodeLabel.setForeground(Color.WHITE);
        decodeLabel.setFont(labelFont);
        decodeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JButton openFileToDecodeButton = new JButton("Open File");
        openFileToDecodeButton.setBackground(new Color(28, 0, 58));
        openFileToDecodeButton.setForeground(Color.WHITE);
        openFileToDecodeButton.setFont(buttonFont);
        openFileToDecodeButton.setBorder(new RoundedBorder(10));
        openFileToDecodeButton.setFocusPainted(false);
        openFileToDecodeButton.setToolTipText("Open a file to decode");
        
        // ------------------------ اجزای ActionResultFrame ------------------------
        JLabel resultLabel = new JLabel("- The Action was performed Successfully -");
        resultLabel.setForeground(Color.GREEN);
        resultLabel.setFont(labelFont);
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JButton okButton = new JButton("OK");
        okButton.setBackground(new Color(28, 0, 58));
        okButton.setForeground(Color.WHITE);
        okButton.setFont(buttonFont);
        okButton.setBorder(new RoundedBorder(10));
        okButton.setFocusPainted(false);
        okButton.setToolTipText("Close application");
        
        // ------------------------ قرار دادن اجزا با استفاده از GridBagLayout ------------------------
        // برای صفحه اصلی (MainFrame) دکمه‌ها بزرگتر شوند
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.insets = new Insets(15, 15, 15, 15);
        gbcMain.fill = GridBagConstraints.HORIZONTAL;
        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        // افزایش فضای داخلی دکمه‌ها
        gbcMain.ipadx = 50;
        gbcMain.ipady = 20;
        mainPanel.add(encodeButton, gbcMain);
        gbcMain.gridy = 1;
        mainPanel.add(decodeButton, gbcMain);
        
        // EncodeFrame: قرار دادن JLabel در ردیف اول و دکمه انتخاب فایل در ردیف دوم
        GridBagConstraints gbcEncode = new GridBagConstraints();
        gbcEncode.insets = new Insets(15, 15, 15, 15);
        gbcEncode.fill = GridBagConstraints.HORIZONTAL;
        gbcEncode.gridx = 0;
        gbcEncode.gridy = 0;
        encodePanel.add(encodeLabel, gbcEncode);
        gbcEncode.gridy = 1;
        encodePanel.add(openFileToEncodeButton, gbcEncode);
        
        // DecodeFrame: قرار دادن JLabel در ردیف اول و دکمه انتخاب فایل در ردیف دوم
        GridBagConstraints gbcDecode = new GridBagConstraints();
        gbcDecode.insets = new Insets(15, 15, 15, 15);
        gbcDecode.fill = GridBagConstraints.HORIZONTAL;
        gbcDecode.gridx = 0;
        gbcDecode.gridy = 0;
        decodePanel.add(decodeLabel, gbcDecode);
        gbcDecode.gridy = 1;
        decodePanel.add(openFileToDecodeButton, gbcDecode);
        
        // ActionResultFrame: قرار دادن JLabel در ردیف اول و دکمه OK در ردیف دوم
        GridBagConstraints gbcResultLabel = new GridBagConstraints();
        gbcResultLabel.insets = new Insets(15, 15, 5, 15);
        gbcResultLabel.fill = GridBagConstraints.HORIZONTAL;
        gbcResultLabel.gridx = 0;
        gbcResultLabel.gridy = 0;
        resultPanel.add(resultLabel, gbcResultLabel);
        
        GridBagConstraints gbcResultButton = new GridBagConstraints();
        gbcResultButton.insets = new Insets(5, 15, 15, 15);
        gbcResultButton.fill = GridBagConstraints.NONE;
        gbcResultButton.gridx = 0;
        gbcResultButton.gridy = 1;
        // کاهش فضای داخلی دکمه OK برای اندازه مناسب‌تر
        gbcResultButton.ipadx = 20;
        gbcResultButton.ipady = 5;
        resultPanel.add(okButton, gbcResultButton);
        
        // ------------------------ اکشن‌های دکمه‌ها ------------------------
        encodeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                mainFrame.setVisible(false);
                encodeFrame.setVisible(true);
            }
        });
        
        decodeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                mainFrame.setVisible(false);
                decodeFrame.setVisible(true);
            }
        });
        
        okButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });
        
        openFileToEncodeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    JFileChooser fileChooser = new JFileChooser();
                    int returnValue = fileChooser.showOpenDialog(null);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        String filePath = selectedFile.getAbsolutePath();

                        String newFilePath = "";
                        int j = 0;
                        while (j < filePath.length() && filePath.charAt(j) != '.') {
                            newFilePath += filePath.charAt(j);
                            j++;
                        }
                        newFilePath += "_Compressed.hff";

                        Map<Character, Integer> freqMap = HuffmanCompression.Calculate_Frequency(filePath);
                        Node root = HuffmanCompression.buildHuffmanTree(freqMap);

                        Map<Character, String> binaryCodes = new HashMap<>();
                        HuffmanCompression.EncodeCharacters(root, "", binaryCodes);

                        HuffmanCompression.writeCompressedFile(filePath, newFilePath, binaryCodes, root);

                        System.out.println("Encoding was safe and sound!");

                        encodeFrame.setVisible(false);
                        actionResultFrame.setVisible(true);
                    }
                } catch(IOException ex) {
                    System.err.println("Encoding was not successful!");
                }
            }
        });
        
        openFileToDecodeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    JFileChooser fileChooser = new JFileChooser();
                    int returnValue = fileChooser.showOpenDialog(null);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        String filePath = selectedFile.getAbsolutePath();

                        String newFilePath = "";
                        int j = 0;
                        while (j < filePath.length() && filePath.charAt(j) != '.') {
                            newFilePath += filePath.charAt(j);
                            j++;
                        }
                        newFilePath += "_DeCompressed.txt";

                        HuffmanCompression.decodeCompressedFile(filePath, newFilePath);

                        System.out.println("Decoding was safe and sound!");

                        decodeFrame.setVisible(false);
                        actionResultFrame.setVisible(true);
                    }
                } catch(IOException ex) {
                    System.err.println("Decoding was not successful!");
                }
            }
        });
        
        // نمایش فریم اصلی
        mainFrame.setVisible(true);
    }
}
