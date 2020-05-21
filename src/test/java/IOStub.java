import shortest_path_visualizer.IO;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class IOStub implements IO {
  Scanner scanner;
  ArrayList<String> stringOutputs;
  ArrayList<Character> charOutputs;

  public IOStub() {
    this.stringOutputs = new ArrayList<String>();
    this.charOutputs = new ArrayList<Character>();
  }

  @Override
  public void printChar(char c) {
    charOutputs.add(c);
  }

  @Override
  public void printString(String s) {
    stringOutputs.add(s);
  }

  @Override
  public void printStringWithoutNewLine(String s) {
    stringOutputs.add(s);
  }

  @Override
  public void setFile(File file) throws FileNotFoundException {
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
