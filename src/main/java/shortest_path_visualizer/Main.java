package shortest_path_visualizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javafx.application.Application;
import shortest_path_visualizer.IO.BenchmarkFileReader;
import shortest_path_visualizer.IO.MapReaderIO;
import shortest_path_visualizer.performanceTesting.PerformanceTest;
import shortest_path_visualizer.ui.Ui;
import shortest_path_visualizer.utils.Node;

public class Main {
  public static void main(String[] args) throws FileNotFoundException {

    BenchmarkFileReader b = new BenchmarkFileReader(new MapReaderIO());
    Node[][] startAndGoal = b.getScenarioCoordinates(new File("src/main/resources/Berlin_0_256.scen.txt"));
    double[] optimalPath = b.getOptimalPath();

    double totalTimeD = 0;
    double totalTimeA = 0;

    PerformanceTest test = new PerformanceTest(5);
    for (int i = 0; i < startAndGoal.length; i++) {

      test.testDijkstra(new File("src/main/resources/Berlin_0_256.txt"), startAndGoal[i][0], startAndGoal[i][1]);
      totalTimeD += test.getAverage();
      double d = test.getVastausD();
      if (d - optimalPath[i] > 0.001) {
        System.out.println(i);
        System.out.println(d + " vs. " + optimalPath[i]);
      }

      test.testAStar(new File("src/main/resources/Berlin_0_256.txt"), startAndGoal[i][0], startAndGoal[i][1]);
      totalTimeA += test.getAverage();
      double a = test.getVastausA();
      if (a - optimalPath[i] > 0.001) {
        System.out.println(i);
        System.out.println(a + " vs. " + optimalPath[i]);
      }

      if (a - d > 0.001) {
        System.out.println("D: " + d + " A: " + a);
      }
    }

    System.out.println("Dijkstra total time: " + totalTimeD);
    System.out.println("A* total time: " + totalTimeA);

    //Application.launch(Ui.class);
  }
}
