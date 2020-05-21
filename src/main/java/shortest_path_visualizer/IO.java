package shortest_path_visualizer;

import java.io.File;
import java.io.FileNotFoundException;

public interface IO {
    void printChar(char s);
    void printString(String s);
    void setFile(File file) throws FileNotFoundException;
    boolean hasNextLine();
    String getNextLine();

}
