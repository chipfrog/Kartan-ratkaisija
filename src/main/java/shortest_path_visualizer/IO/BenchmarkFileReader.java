package shortest_path_visualizer.IO;

import java.io.File;
import java.io.FileNotFoundException;
import shortest_path_visualizer.utils.Node;

public class BenchmarkFileReader {
  private final IO io;
  private double[] optimalPath;

  public BenchmarkFileReader(IO io) {
    this.io = io;
  }

  public Node[][] getScenarioCoordinates(File file) throws FileNotFoundException {
    io.setFile(file);
    int j = -1;
    while (io.hasNextLine()) {
      io.getNextLine();
      j ++;
    }
    this.optimalPath = new double[j];
    Node[][] coordinates = new Node[j][2];
    io.setFile(file);
    io.getNextLine();
    j = 0;

    try {
      while (io.hasNextLine()) {
        String s = io.getNextLine();
        Node start = new Node(0, Integer.parseInt(s.split("\\s+")[4]), Integer.parseInt(s.split("\\s+")[5]));
        Node goal = new Node(0, Integer.parseInt(s.split("\\s+")[6]), Integer.parseInt(s.split("\\s+")[7]));
        optimalPath[j] = Double.parseDouble(s.split("\\s+")[8]);
        coordinates[j][0] = start;
        coordinates[j][1] = goal;
        j ++;
      }
    } catch (Exception e) {
    }
    return coordinates;
  }

  public double[] getOptimalPath() {
    return this.optimalPath;
  }



}
