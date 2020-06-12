package shortest_path_visualizer.IO;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MapFileCreator {
  private final IO io;

  public MapFileCreator(IO io) {
    this.io = io;
  }

  public void WriteMapToFile(char[][] kartta, String fileName) {
    String dir = "src/main/resources";
    try {
      FileWriter fileWriter = new FileWriter(new File(dir, fileName));
      for (int i = 0; i < kartta.length; i ++) {
        for (int j = 0; j < kartta[0].length; j++) {
          fileWriter.write(kartta[i][j]);
        }
        fileWriter.write("\n");
      }
      fileWriter.close();
      io.printString("Tallennus onnistui!");
    } catch (IOException e) {
      io.printString("Error while writing a file!");
    }
  }
}
