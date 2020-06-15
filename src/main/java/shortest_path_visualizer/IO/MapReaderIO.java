package shortest_path_visualizer.IO;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * IO-rajapinnan toteuttava luokka, joka vastaa tulostamisesta ja Scanner-olioiden luomisesta
 * ja käsittelystä.
 */

public class MapReaderIO implements IO {
  private Scanner scanner;

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
  public void printStringWithoutNewLine(String s) {
    System.out.print(s);
  }

  @Override
  public void setFile(File file) throws FileNotFoundException {
    this.scanner = new Scanner(file);
  }

  /**
   * Tarkistaa onko Scannerille annetussa tiedostossa tarkastelemattomia rivejä jäljellä.
   * @return boolean, onko rivejä jäljellä
   */

  @Override
  public boolean hasNextLine() {
    return this.scanner.hasNextLine();
  }

  /**
   * Hakee Scanner-oliolle annetusta tiedostosta seuraavan rivin.
   * @return .txt-tiedoston seuraava rivi
   */

  @Override
  public String getNextLine() {
    return this.scanner.nextLine();
  }
}
