package shortest_path_visualizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MapReader {
    private IO io;
    private char [][] array;
    private int rows;
    private int columns;

    public MapReader(IO io) {
        this.io = io;
    }

    public void createMatrix(File file) throws FileNotFoundException {
        initArray(file);
        io.setFile(file);
        int currentRow = 0;

        while (io.hasNextLine()) {
            String line = io.getNextLine();
            for (int i = 0; i < line.length(); i ++) {
                array[currentRow][i] = line.charAt(i);

            }
            currentRow ++;
        }
    }

    private void initArray(File file) throws FileNotFoundException {
        io.setFile(file);
        this.rows = 0;
        this.columns = 0;

        while (io.hasNextLine()) {
            columns = io.getNextLine().length();
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
            io.printChar(array[i][j]);
        }
        System.out.println();
    }
    io.printString("rows: " + rows);
    io.printString("columns: " + columns);
    }
}
