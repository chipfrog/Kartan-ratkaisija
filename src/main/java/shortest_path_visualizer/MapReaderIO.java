package shortest_path_visualizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MapReaderIO implements IO {
    private Scanner scanner;
    private File file;

    public MapReaderIO() {

    }

    @Override
    public void printChar(char s) {
        System.out.print(s);
    }

    @Override
    public void printString(String s) {
        System.out.println(s);
    }

    @Override
    public void setFile(File file) throws FileNotFoundException{
        this.scanner = new Scanner(file);
    }

    @Override
    public boolean hasNextLine() {
        return this.scanner.hasNextLine();
    }

    @Override
    public String getNextLine() {
        return this.scanner.nextLine();
    }
}
