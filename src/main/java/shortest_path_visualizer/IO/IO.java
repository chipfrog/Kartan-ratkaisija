package shortest_path_visualizer.IO;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Rajapinta, jonka kautta tiedostojen käsittely ja konsoliin tulostaminen hoidetaan.
 */

public interface IO {
  void printChar(char s);

  void printString(String s);

  void printStringWithoutNewLine(String s);

  void setFile(File file) throws FileNotFoundException;

  boolean hasNextLine();

  String getNextLine();

}
