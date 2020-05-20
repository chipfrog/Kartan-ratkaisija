package shortest_path_visualizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MapReader {
    private char [][] array;
    private Scanner scanner;
    private int rows;
    private int columns;

    public MapReader() {

    }

    public void createMatrix(File file) throws FileNotFoundException {
        initArray(file);
        this.scanner = new Scanner(file);
        int currentRow = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            for (int i = 0; i < line.length(); i ++) {
                array[currentRow][i] = line.charAt(i);

            }
            currentRow ++;
        }
    }

    public void initArray(File file) throws FileNotFoundException {
        this.scanner = new Scanner(file);
        this.rows = 0;
        this.columns = 0;

        while (scanner.hasNextLine()) {
            columns = scanner.nextLine().length();
            rows ++;
        }
        this.array = new char[rows][columns];
    }

    public char[][] getMapArray() {
        return this.array;
    }

    public void printMap() {
    for (int i = 0; i < rows; i ++) {
        for (int j = 0; j < columns; j ++) {
            System.out.print(array[i][j]);
        }
        System.out.println();
    }
    System.out.println("rows: " + rows);
    System.out.println("columns: " + columns);
    }
}
