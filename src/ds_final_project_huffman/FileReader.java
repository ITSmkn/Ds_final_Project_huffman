package ds_final_project_huffman;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileReader {
    
    public static String readFile(String path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path));
        return String.join("\n", lines);
    }  
}
