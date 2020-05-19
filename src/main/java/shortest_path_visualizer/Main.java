package shortest_path_visualizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        MapReader reader = new MapReader();

        reader.createMatrix(new File ("src/main/resources/testikartta.txt"));
        reader.printMap();
    }
}
