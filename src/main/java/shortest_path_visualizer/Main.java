package shortest_path_visualizer;

import java.io.File;
import java.io.FileNotFoundException;
import javafx.application.Application;
import shortest_path_visualizer.IO.BenchmarkFileReader;
import shortest_path_visualizer.IO.MapReaderIO;
import shortest_path_visualizer.performanceTesting.PerformanceTest;
import shortest_path_visualizer.ui.Ui;
import shortest_path_visualizer.utils.Node;

public class Main {
  public static void main(String[] args) throws FileNotFoundException {

    /*BenchmarkFileReader b = new BenchmarkFileReader(new MapReaderIO());
    Node[][] startAndGoal = b.getScenarioCoordinates(new File("src/main/resources/Berlin_0_256.scen.txt"));

    double totalTimeD = 0;
    double totalTimeA = 0;

    PerformanceTest test = new PerformanceTest(2);
    for (int i = 0; i < startAndGoal.length; i ++) {*/
      /*test.testDijkstra(new File("src/main/resources/Berlin_0_256.txt"), startAndGoal[i][0], startAndGoal[i][1]);
      totalTimeD += test.getAverage();
      int d = test.getVastausD();*/

      /*test.testAStar(new File("src/main/resources/Berlin_0_256.txt"), startAndGoal[i][0], startAndGoal[i][1]);
      totalTimeA += test.getAverage();
      double a = test.getVastausA();
      System.out.println(a);*/

      /*if (d != a) {
        System.out.println("Virhe!");
      }*/
    //}

    /*System.out.println("Dijkstra total time: " + totalTimeD);
    System.out.println("A* total time: " + totalTimeA);*/

    Application.launch(Ui.class);

    /*PerformanceTest test = new PerformanceTest();
    test.testAStar(new File("src/test/resources/kartat/Brushfire.txt"));
    test.getAverage();
    test.testDijkstra(new File("src/test/resources/kartat/Brushfire.txt"));
    test.getAverage();

    System.out.println(test.getVastausD());
    System.out.println(test.getVastausA());*/


  }
}
