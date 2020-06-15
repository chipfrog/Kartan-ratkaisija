package shortest_path_visualizer.IO;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Luokka .txt -muodossa olevien karttojen muuntamiseen matriiseiksi.
 */

public class MapReader {
  private final IO io;
  private char[][] array;
  private int rows;
  private int columns;

  public MapReader(IO io) {
    this.io = io;
  }

  /**
   * Metodi saa konstruktorissa karttatiedoston ja muuntaa sen matriisiksi.
   *
   * @param file matriisiksi muunnettava kartta
   * @throws FileNotFoundException
   */

  public void createMatrix(File file) throws FileNotFoundException {
    initArray(file);
    io.setFile(file);
    int currentRow = 0;

    while (io.hasNextLine()) {
      String line = io.getNextLine();
      for (int i = 0; i < line.length(); i++) {
        array[currentRow][i] = line.charAt(i);
      }
      currentRow++;
    }
  }

  /**
   * Metodi matriisin alustamiseen oikean kokoiseksi. Metodi laskee .txt-tiedoston
   * rivit ja sarakkeet ja alustaa näiden avulla matriisin.
   *
   * @param file .txt -muotoinen karttatiedosto
   * @throws FileNotFoundException
   */

  private void initArray(File file) throws FileNotFoundException {
    io.setFile(file);
    this.rows = 0;
    this.columns = 0;

    while (io.hasNextLine()) {
      columns = io.getNextLine().length();
      rows++;
    }
    this.array = new char[rows][columns];
  }

  /**
   * Metodi palauttaa mariisimuotoisen kartan.
   * @return kartta matriisimuodossa
   */
  public char[][] getMapArray() {
    return this.array;
  }

  /**
   * Metodi tulostaa kartan konsoliin.
   */
  public void printMap() {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        io.printChar(array[i][j]);
      }
      io.printString("");
    }
    io.printString("rows: " + rows);
    io.printString("columns: " + columns);
  }
}
